package com.github.wensimin.boliboli_android.ui.voice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.github.wensimin.boliboli_android.databinding.FragmentVoiceBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class VoiceFragment : Fragment() {

    private val viewModel: VoiceListViewModel by activityViewModels()
    private lateinit var voiceAdapter: VoiceAdapter
    private lateinit var binding: FragmentVoiceBinding
    private var searchJob: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoiceBinding.inflate(inflater)
        initAdapter()
        //初始搜索
        search("")
        initSearch()
        return binding.root
    }


    /**
     * 目前只处理输入法go
     */
    private fun initSearch() {
        binding.searchText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search(view.text.trim().toString())
                true
            } else {
                false
            }
        }
    }

    /**
     * 进行搜索
     */
    private fun search(keyword: String) {
        searchJob?.cancel()
        clearIME()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.search(keyword).collectLatest {
                voiceAdapter.submitData(it)
            }
        }
    }

    /**
     * 关闭输入法和焦点
     */
    private fun clearIME() {
        binding.searchText.clearFocus()
        getSystemService(
            requireContext(),
            InputMethodManager::class.java
        )?.hideSoftInputFromWindow(binding.searchText.windowToken, 0)
    }

    /**
     * 初始化分页adapter
     */
    private fun initAdapter() {
        voiceAdapter = VoiceAdapter()
        lifecycleScope.launchWhenCreated {
            voiceAdapter.loadStateFlow.collectLatest {
                binding.swipeRefresh.isRefreshing = it.refresh is LoadState.Loading
            }
        }
        binding.apply {
            swipeRefresh.setOnRefreshListener {
                voiceAdapter.refresh()
            }
            list.adapter = voiceAdapter
        }
        // FIXME 数据乱序问题 https://github.com/googlecodelabs/android-paging/issues/127
        // FIXME page往上加载数据,滚动条的位置调整在前,见以下问题 https://github.com/googlecodelabs/android-paging/issues/149
//        lifecycleScope.launchWhenCreated {
//            voiceAdapter.loadStateFlow
//                .distinctUntilChangedBy { it.refresh }
//                .filter {
//                    it.refresh is LoadState.NotLoading
//                }
//                .collect {
//                    binding.list.scrollToPosition(0)
//                }
//        }
    }
}
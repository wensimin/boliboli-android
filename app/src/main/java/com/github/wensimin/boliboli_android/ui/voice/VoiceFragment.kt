package com.github.wensimin.boliboli_android.ui.voice

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.github.wensimin.boliboli_android.databinding.FragmentVoiceBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

class VoiceFragment : Fragment() {

    private val voiceListViewModel: VoiceListViewModel by activityViewModels()
    private lateinit var binding: FragmentVoiceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoiceBinding.inflate(inflater)
        initAdapter()
        return binding.root
    }

    /**
     * 初始化分页adapter
     */
    private fun initAdapter() {
        val voiceAdapter = VoiceAdapter()
        lifecycleScope.launchWhenCreated {
            voiceListViewModel.voices.collectLatest {
                voiceAdapter.submitData(it)
            }
        }
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
        // fixme page往上加载数据,滚动条的位置调整在前,见以下问题
        // https://github.com/googlecodelabs/android-paging/issues/149
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("scroll", binding.list.scrollY)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getInt("scroll")?.let {
            binding.list.scrollToPosition(it)
        }
    }
}
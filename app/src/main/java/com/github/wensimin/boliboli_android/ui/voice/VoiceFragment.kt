package com.github.wensimin.boliboli_android.ui.voice

import android.os.Bundle
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val voiceAdapter = VoiceAdapter()
        val binding = FragmentVoiceBinding.inflate(inflater).apply {
            swipeRefresh.setOnRefreshListener {
                //FIXME refresh后不触发下拉刷新  refresh&invalidate均不可行
                voiceAdapter.refresh()
//                voiceListViewModel.pageInvalidate()
            }
            list.adapter = voiceAdapter
        }
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
        //TODO scroll 归零,目前好像没啥作用
        lifecycleScope.launchWhenCreated {
            voiceAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.list.scrollToPosition(0) }
        }
        return binding.root
    }
}
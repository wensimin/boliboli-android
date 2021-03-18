package com.github.wensimin.boliboli_android.ui.voice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.wensimin.boliboli_android.R
import com.github.wensimin.boliboli_android.databinding.FragmentVoiceBinding

class VoiceFragment : Fragment() {

    private lateinit var voiceListViewModel: VoiceListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        voiceListViewModel = activityViewModels<VoiceListViewModel>().value
        val binding = FragmentVoiceBinding.inflate(inflater)
        voiceListViewModel.initData()
        binding.list.adapter = VoiceAdapter(voiceListViewModel)
        return binding.root
    }
}
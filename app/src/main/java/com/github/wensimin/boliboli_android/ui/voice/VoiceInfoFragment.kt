package com.github.wensimin.boliboli_android.ui.voice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.wensimin.boliboli_android.databinding.FragmentVoiceInfoBinding
import com.github.wensimin.boliboli_android.utils.logD
import kotlinx.coroutines.launch

class VoiceInfoFragment : Fragment() {
    private val voiceInfoViewModel: VoiceInfoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //初始化
        voiceInfoViewModel.loadInfo(arguments?.getString("id")!!)
        return FragmentVoiceInfoBinding.inflate(inflater).apply {
            this.model = voiceInfoViewModel
            lifecycleOwner = this@VoiceInfoFragment
        }.root
    }

}
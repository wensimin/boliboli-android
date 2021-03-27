package com.github.wensimin.boliboli_android.ui.voice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.wensimin.boliboli_android.databinding.FragmentVoiceInfoBinding
import com.github.wensimin.boliboli_android.utils.logD

class VoiceInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logD("start info id: ${savedInstanceState?.getString("id")}")
        return FragmentVoiceInfoBinding.inflate(inflater).root
    }

}
package com.github.wensimin.boliboli_android.ui.voice

import com.github.wensimin.boliboli_android.R
import com.github.wensimin.boliboli_android.databinding.VoiceMediasItemBinding
import com.github.wensimin.boliboli_android.pojo.SimpleVoiceMedia
import com.xwray.groupie.databinding.BindableItem

class VoiceMediaItem(private val media: SimpleVoiceMedia) : BindableItem<VoiceMediasItemBinding>() {
    override fun bind(viewBinding: VoiceMediasItemBinding, position: Int) {
        viewBinding.item = media
    }

    override fun getLayout(): Int = R.layout.voice_medias_item
}
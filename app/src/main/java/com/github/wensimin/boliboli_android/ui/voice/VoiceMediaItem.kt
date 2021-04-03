package com.github.wensimin.boliboli_android.ui.voice

import androidx.navigation.findNavController
import com.github.wensimin.boliboli_android.R
import com.github.wensimin.boliboli_android.databinding.VoiceMediasItemBinding
import com.github.wensimin.boliboli_android.pojo.SimpleVoiceMedia
import com.xwray.groupie.databinding.BindableItem

class VoiceMediaItem(private val media: SimpleVoiceMedia) : BindableItem<VoiceMediasItemBinding>() {
    override fun bind(viewBinding: VoiceMediasItemBinding, position: Int) {
        viewBinding.apply {
            item = this@VoiceMediaItem.media
        }.also {
            it.root.setOnClickListener { view ->
                view.findNavController().navigate(
                    VoiceInfoFragmentDirections.actionVoiceInfoFragmentToMediaPlayerFragment(
                        media.id,
                        media.type
                    )
                )
            }
        }
    }

    override fun getLayout(): Int = R.layout.voice_medias_item
}
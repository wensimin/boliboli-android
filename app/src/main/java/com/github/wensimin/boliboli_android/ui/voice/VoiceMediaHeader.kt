package com.github.wensimin.boliboli_android.ui.voice

import com.github.wensimin.boliboli_android.R
import com.github.wensimin.boliboli_android.databinding.VoiceMediasItemFolderBinding
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.databinding.BindableItem

class VoiceMediaHeader(private val title: String) : BindableItem<VoiceMediasItemFolderBinding>(), ExpandableItem {

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        //TODO 点击切换
    }

    override fun bind(viewBinding: VoiceMediasItemFolderBinding, position: Int) {
        viewBinding.title = title
    }

    override fun getLayout(): Int = R.layout.voice_medias_item_folder


}
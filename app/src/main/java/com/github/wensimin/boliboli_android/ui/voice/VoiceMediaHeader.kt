package com.github.wensimin.boliboli_android.ui.voice

import com.github.wensimin.boliboli_android.R
import com.github.wensimin.boliboli_android.databinding.VoiceMediasItemFolderBinding
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.databinding.BindableItem

class VoiceMediaHeader(private val title: String) : BindableItem<VoiceMediasItemFolderBinding>(), ExpandableItem {
    private lateinit var expandableGroup: ExpandableGroup

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    override fun bind(viewBinding: VoiceMediasItemFolderBinding, position: Int) {
        viewBinding.title = title
        //TODO 图标
        viewBinding.titleView.setOnClickListener {
            expandableGroup.onToggleExpanded()
        }

    }

    override fun getLayout(): Int = R.layout.voice_medias_item_folder


}
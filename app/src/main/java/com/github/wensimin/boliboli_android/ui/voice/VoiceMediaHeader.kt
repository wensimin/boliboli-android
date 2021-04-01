package com.github.wensimin.boliboli_android.ui.voice

import android.graphics.drawable.Animatable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
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
        viewBinding.apply {
            title = this@VoiceMediaHeader.title
            icon.visibility = View.VISIBLE
            icon.setIcon(expandableGroup.isExpanded)
        }.root.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewBinding.icon.apply {
                setIcon(expandableGroup.isExpanded)
            }.also {
                (it.drawable as Animatable).start()
            }
        }

    }

    /**
     * 设置当前图标
     */
    private fun AppCompatImageView.setIcon(expanded: Boolean) {
        setImageResource(if (expanded) R.drawable.collapse_animated else R.drawable.expand_animated)
    }

    override fun getLayout(): Int = R.layout.voice_medias_item_folder


}
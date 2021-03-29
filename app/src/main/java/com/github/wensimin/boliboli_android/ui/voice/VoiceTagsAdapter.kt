package com.github.wensimin.boliboli_android.ui.voice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.wensimin.boliboli_android.databinding.VoiceTagsItemBinding
import com.github.wensimin.boliboli_android.pojo.SimpleVoiceTag

class VoiceTagsAdapter :
    ListAdapter<SimpleVoiceTag, VoiceTagsAdapter.VoiceTagViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SimpleVoiceTag>() {
            override fun areItemsTheSame(oldItem: SimpleVoiceTag, newItem: SimpleVoiceTag) =
                oldItem.key == newItem.key

            override fun areContentsTheSame(oldItem: SimpleVoiceTag, newItem: SimpleVoiceTag) = oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceTagViewHolder {
        return VoiceTagViewHolder(VoiceTagsItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: VoiceTagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VoiceTagViewHolder(private val binding: VoiceTagsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: SimpleVoiceTag?) {
            tag.let {
                binding.item = tag
                binding.executePendingBindings()
            }
        }
    }

}
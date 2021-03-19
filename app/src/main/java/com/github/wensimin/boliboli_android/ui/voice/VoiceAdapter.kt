package com.github.wensimin.boliboli_android.ui.voice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.wensimin.boliboli_android.databinding.VoiceListItemBinding
import com.github.wensimin.boliboli_android.rest.dto.Voice

/**
 * voice adapter
 */
class VoiceAdapter :
    PagedListAdapter<Voice, VoiceAdapter.VoiceViewHolder>(DIFF_CALLBACK) {

    /**
     * 获取新的holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceViewHolder {
        return VoiceViewHolder(VoiceListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * 绑定指定位置的holder数据
     */
    override fun onBindViewHolder(holder: VoiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * holder 目前仅绑定voice
     * TODO 后续应该有事件
     */
    class VoiceViewHolder(private val binding: VoiceListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(voice: Voice?) {
            voice?.let {
                binding.voice = it
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Voice>() {
            override fun areItemsTheSame(oldItem: Voice, newItem: Voice) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Voice, newItem: Voice) = oldItem == newItem
        }
    }


}
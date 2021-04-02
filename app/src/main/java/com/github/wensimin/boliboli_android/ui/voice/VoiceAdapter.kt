package com.github.wensimin.boliboli_android.ui.voice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.wensimin.boliboli_android.databinding.VoiceListItemBinding
import com.github.wensimin.boliboli_android.pojo.SimpleVoice

/**
 * voice adapter
 */
class VoiceAdapter :
    PagingDataAdapter<SimpleVoice, VoiceAdapter.VoiceViewHolder>(DIFF_CALLBACK) {
    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SimpleVoice>() {
            override fun areItemsTheSame(oldItem: SimpleVoice, newItem: SimpleVoice) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SimpleVoice, newItem: SimpleVoice) = oldItem == newItem
        }
    }

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
     */
    inner class VoiceViewHolder(private val binding: VoiceListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(voice: SimpleVoice?) {
            voice?.let {
                binding.run {
                    this.voice = it
                    this.root.setOnClickListener {
                        it.findNavController()
                            .navigate(VoiceFragmentDirections.actionNavigationVoiceToVoiceInfoFragment(voice.id))
                    }
                }
            }
        }
    }


}
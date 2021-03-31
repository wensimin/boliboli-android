package com.github.wensimin.boliboli_android.ui.voice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.wensimin.boliboli_android.databinding.VoiceMediasItemBinding
import com.github.wensimin.boliboli_android.pojo.SimpleVoiceMedia

class VoiceMediasAdapter :
    ListAdapter<SimpleVoiceMedia, VoiceMediasAdapter.VoiceMediaViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SimpleVoiceMedia>() {
            override fun areItemsTheSame(oldItem: SimpleVoiceMedia, newItem: SimpleVoiceMedia) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SimpleVoiceMedia, newItem: SimpleVoiceMedia) = oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceMediaViewHolder {
        return VoiceMediaViewHolder(VoiceMediasItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: VoiceMediaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VoiceMediaViewHolder(private val binding: VoiceMediasItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: SimpleVoiceMedia?) {
            tag.let {
                binding.item = tag
                binding.executePendingBindings()
            }
        }
    }

}
package com.github.wensimin.boliboli_android.ui.voice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.wensimin.boliboli_android.databinding.VoiceListItemBinding
import com.github.wensimin.boliboli_android.rest.dto.Voice
import com.github.wensimin.boliboli_android.utils.logI

class VoiceAdapter(private val voiceListViewModel: VoiceListViewModel) :
    PagedListAdapter<Voice, VoiceAdapter.VoiceViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceViewHolder {
        // view holder居然为抽象类 TODO 待研究
        return VoiceViewHolder(VoiceListItemBinding.inflate(LayoutInflater.from(parent.context)).root)
    }

    override fun onBindViewHolder(holder: VoiceViewHolder, position: Int) {
//        holder.(getItem(position))
    }

    class VoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Voice>() {
            override fun areItemsTheSame(oldItem: Voice, newItem: Voice) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Voice, newItem: Voice
            ) = oldItem == newItem
        }
    }


}
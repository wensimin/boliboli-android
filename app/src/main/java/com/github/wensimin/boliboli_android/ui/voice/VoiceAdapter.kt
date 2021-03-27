package com.github.wensimin.boliboli_android.ui.voice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.github.wensimin.boliboli_android.MainActivity
import com.github.wensimin.boliboli_android.R
import com.github.wensimin.boliboli_android.databinding.VoiceListItemBinding
import com.github.wensimin.boliboli_android.rest.dto.SimpleVoice

/**
 * voice adapter
 */
class VoiceAdapter :
    PagingDataAdapter<SimpleVoice, VoiceAdapter.VoiceViewHolder>(DIFF_CALLBACK) {


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

        companion object {
            private val options = RequestOptions().apply {
                override(Target.SIZE_ORIGINAL)
            }
        }

        fun bind(voice: SimpleVoice?) {
            voice?.let {
                binding.apply {
                    this.voice = it
                    mainImg.apply {
                        Glide.with(this).setDefaultRequestOptions(options)
                            .load(it.mainImg)
                            .into(this)
                    }
                    this.root.setOnClickListener {
                        // FIXME 是否使用fragment 存疑
                        (it.context as MainActivity)
                            .supportFragmentManager.beginTransaction().apply {
                                replace(
                                    R.id.container,
                                    VoiceInfoFragment::class.java,
                                    Bundle().apply {
                                        //FIXME 无效
                                        putString("id", voice.id)
                                    })
                                addToBackStack(null)
                                commit()
                            }
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SimpleVoice>() {
            override fun areItemsTheSame(oldItem: SimpleVoice, newItem: SimpleVoice) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SimpleVoice, newItem: SimpleVoice) = oldItem == newItem
        }
    }


}
package com.github.wensimin.boliboli_android.ui.voice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.wensimin.boliboli_android.databinding.VoiceListItemBinding

class VoiceAdapter(private val voiceListViewModel: VoiceListViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // view holder居然为抽象类 TODO 待研究
        return object :
            RecyclerView.ViewHolder(VoiceListItemBinding.inflate(LayoutInflater.from(parent.context)).root) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //TODO
    }

    override fun getItemCount(): Int = voiceListViewModel.count

}
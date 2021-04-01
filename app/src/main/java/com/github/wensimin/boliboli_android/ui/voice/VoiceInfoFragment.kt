package com.github.wensimin.boliboli_android.ui.voice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.wensimin.boliboli_android.databinding.FragmentVoiceInfoBinding
import com.github.wensimin.boliboli_android.pojo.SimpleVoiceMedia
import com.github.wensimin.boliboli_android.pojo.SimpleVoiceTag
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

@BindingAdapter("voiceTagItems")
fun setTagsItems(view: RecyclerView, items: List<SimpleVoiceTag>?) {
    items?.let {
        (view.adapter as VoiceTagsAdapter).submitList(it)
    }
}

//TODO test delete
@BindingAdapter("voiceMediaItems")
fun setMediasItems(view: RecyclerView, items: List<SimpleVoiceMedia>?) {
    items?.let {
        //TODO 处理数据
        val group = ExpandableGroup(VoiceMediaHeader("test group")).apply {
            it.map(::VoiceMediaItem).forEach(this::add)
        }
        (view.adapter as GroupAdapter<*>).add(group)
//        (view.adapter as GroupAdapter<*>).addAll(it.map { item ->
//            ExpandableGroup(VoiceMediaHeader(item.filename)).apply {
//                addAll())
//            }
//        })
    }
}

class VoiceInfoFragment : Fragment() {
    private val voiceInfoViewModel: VoiceInfoViewModel by viewModels()
    private lateinit var binding: FragmentVoiceInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //初始化
        voiceInfoViewModel.loadInfo(arguments?.getString("id")!!)
        return FragmentVoiceInfoBinding.inflate(inflater).apply {
            this.model = voiceInfoViewModel
            lifecycleOwner = this@VoiceInfoFragment
        }.also {
            it.tags.adapter = VoiceTagsAdapter()
            it.medias.apply {
                adapter = GroupAdapter<GroupieViewHolder>()
                layoutManager = LinearLayoutManager(requireContext())
            }
            binding = it
        }.root
    }

}
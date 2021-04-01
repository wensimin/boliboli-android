package com.github.wensimin.boliboli_android.ui.voice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.wensimin.boliboli_android.manager.RestApi
import com.github.wensimin.boliboli_android.pojo.SimpleVoiceMedia
import com.github.wensimin.boliboli_android.pojo.VoiceInfo
import com.github.wensimin.boliboli_android.pojo.base.MediaType
import kotlinx.coroutines.launch

class VoiceInfoViewModel : ViewModel() {
    val voiceInfo: MutableLiveData<VoiceInfo> = MutableLiveData()
    val mediaMap: MutableLiveData<Map<String, List<SimpleVoiceMedia>>> = MutableLiveData()
    fun loadInfo(id: String) {
        viewModelScope.launch {
            RestApi.request("voice/$id", VoiceInfo::class.java).let { response ->
                response.data?.let {
                    voiceInfo.value = it
                    toVoiceMediaMap(it.medias)
                }
            }
        }
    }

    /**
     * 转换media map
     */
    private fun toVoiceMediaMap(medias: List<SimpleVoiceMedia>) {
        val map: MutableMap<String, MutableList<SimpleVoiceMedia>> = LinkedHashMap()
        medias.forEach {
            // VIDEO & AUDIO
            if (!listOf(MediaType.AUDIO, MediaType.VIDEO).contains(it.type)) {
                return@forEach
            }
            val keyString = if (it.folder.isEmpty()) "ROOT" else it.folder
            var key = map[keyString]
            if (key == null) {
                key = ArrayList()
                map[keyString] = key
            }
            key.add(it)
        }
        mediaMap.value = map
    }

}
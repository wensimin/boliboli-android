package com.github.wensimin.boliboli_android.ui.voice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.wensimin.boliboli_android.manager.RestApi
import com.github.wensimin.boliboli_android.pojo.VoiceInfo
import kotlinx.coroutines.launch

class VoiceInfoViewModel : ViewModel() {
    val voiceInfo: MutableLiveData<VoiceInfo> = MutableLiveData()

    fun loadInfo(id: String) {
        viewModelScope.launch {
            RestApi.request("voice/$id", VoiceInfo::class.java).let {
                voiceInfo.value = (it.data)
            }
        }
    }

}
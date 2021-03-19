package com.github.wensimin.boliboli_android.ui.voice

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.github.wensimin.boliboli_android.manager.RestManager
import com.github.wensimin.boliboli_android.rest.dto.Voice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class VoiceListViewModel(application: Application) : AndroidViewModel(application) {
    val voices: MutableLiveData<List<Voice>> = MutableLiveData()
    private val restManager = RestManager(application.baseContext)
    var nextPage = 0
    var count = 0
    fun initData() {
        viewModelScope.launch {
            val page = this.async(Dispatchers.IO) {
                restManager.getPage("voice", Voice::class.java, mapOf("page.number" to nextPage++))
            }
            page.await()?.let {
                count = it.totalElements
                voices.value = it.content
            }
        }
    }

}
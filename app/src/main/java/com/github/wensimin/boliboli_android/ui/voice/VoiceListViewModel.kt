package com.github.wensimin.boliboli_android.ui.voice

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.*

class VoiceListViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var voiceDataSource: VoiceDataSource
    val voices = Pager(
        config = PagingConfig(
            pageSize = VoiceDataSource.NETWORK_PAGE_SIZE
        ),
        pagingSourceFactory = {
            voiceDataSource = VoiceDataSource()
            return@Pager voiceDataSource
        }
    ).flow.cachedIn(viewModelScope)

    fun pageInvalidate() {
        voiceDataSource.invalidate()
    }
}
package com.github.wensimin.boliboli_android.ui.voice

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.*

class VoiceListViewModel(application: Application) : AndroidViewModel(application) {
    val voices = Pager(
        config = PagingConfig(
            pageSize = VoiceDataSource.NETWORK_PAGE_SIZE
        ),
        pagingSourceFactory = { VoiceDataSource() }
    ).flow.cachedIn(viewModelScope)
}
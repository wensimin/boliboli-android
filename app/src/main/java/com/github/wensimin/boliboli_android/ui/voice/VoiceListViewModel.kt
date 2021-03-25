package com.github.wensimin.boliboli_android.ui.voice

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.*
import com.github.wensimin.boliboli_android.rest.dto.SimpleVoice
import kotlinx.coroutines.flow.Flow

class VoiceListViewModel(application: Application) : AndroidViewModel(application) {
    //初始搜索无关键字
    private var lastKeyword: String? = null
    private var voices = search("")

    fun search(keyword: String): Flow<PagingData<SimpleVoice>> {
        if (keyword == lastKeyword) return voices
        lastKeyword = keyword
        voices = Pager(
            config = PagingConfig(
                pageSize = VoiceDataSource.NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = {
                VoiceDataSource(keyword)
            }
        ).flow.cachedIn(viewModelScope)
        return voices
    }
}
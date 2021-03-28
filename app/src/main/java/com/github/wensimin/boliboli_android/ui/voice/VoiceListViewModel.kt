package com.github.wensimin.boliboli_android.ui.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.wensimin.boliboli_android.pojo.SimpleVoice
import kotlinx.coroutines.flow.Flow

class VoiceListViewModel : ViewModel() {
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
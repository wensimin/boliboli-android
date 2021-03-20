package com.github.wensimin.boliboli_android.ui.voice

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.wensimin.boliboli_android.manager.RestManager
import com.github.wensimin.boliboli_android.rest.dto.Voice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class VoiceListViewModel(application: Application) : AndroidViewModel(application) {
    //TODO test pageSize
    private val pagedListConfig = PagedList.Config.Builder()
        .setPageSize(1)
        .setPrefetchDistance(90)
        .build()
    val voices =
        LivePagedListBuilder(VoiceDataSource.VoiceDataSourceFactory(application.baseContext), pagedListConfig).build()

}
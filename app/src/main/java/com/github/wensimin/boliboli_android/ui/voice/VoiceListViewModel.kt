package com.github.wensimin.boliboli_android.ui.voice

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

class VoiceListViewModel(application: Application) : AndroidViewModel(application) {
    //TODO test pageSize
    private val pagedListConfig = PagedList.Config.Builder()
        .setPageSize(1)
        .setPrefetchDistance(1)
        .build()
    val voices =
        LivePagedListBuilder(VoiceDataSource.VoiceDataSourceFactory(application.baseContext), pagedListConfig).build()

}
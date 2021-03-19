package com.github.wensimin.boliboli_android.ui.voice

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.github.wensimin.boliboli_android.rest.dto.Voice

class VoiceDataSource : PageKeyedDataSource<Int, Voice>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Voice>) {
        TODO("Not yet implemented")
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Voice>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Voice>) {
        TODO("Not yet implemented")
    }

    class VoiceDataSourceFactory : DataSource.Factory<Int, Voice>() {
        override fun create(): DataSource<Int, Voice> {
            return VoiceDataSource()
        }

    }

}
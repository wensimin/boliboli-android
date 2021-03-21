package com.github.wensimin.boliboli_android.ui.voice

import android.content.Context
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.github.wensimin.boliboli_android.manager.RestApi
import com.github.wensimin.boliboli_android.rest.dto.Voice

//TODO rest manager to repository
//FIXME 第一优先级,目前仅为测试代码
class VoiceDataSource : PositionalDataSource<Voice>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Voice>) {
        val page = RestApi.getPage(
            "voice", Voice::class.java, mapOf(
                "page.number" to 0,
                "page.size" to params.pageSize
            )
        )
        page?.let {
            callback.onResult(it.content, 0, it.totalElements)
        }
    }


    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Voice>) {
        val page = RestApi.getPage(
            "voice", Voice::class.java, mapOf(
                "page.number" to params.startPosition / params.loadSize,
                "page.size" to params.loadSize
            )
        )
        page?.let {
            callback.onResult(it.content)
        }
    }

    //FIXME 注释部分效果不明 并没有作用
    class VoiceDataSourceFactory(private val context: Context) : DataSource.Factory<Int, Voice>() {
//        private val voiceLiveData = MutableLiveData<VoiceDataSource>()
//        private lateinit var voiceDataSource: VoiceDataSource

        override fun create(): DataSource<Int, Voice> {
//            voiceDataSource = VoiceDataSource(context)
//            voiceLiveData.postValue(voiceDataSource)
            return VoiceDataSource()
        }
    }

}
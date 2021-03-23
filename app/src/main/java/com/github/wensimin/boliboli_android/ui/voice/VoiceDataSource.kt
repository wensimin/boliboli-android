package com.github.wensimin.boliboli_android.ui.voice

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.wensimin.boliboli_android.manager.RestApi
import com.github.wensimin.boliboli_android.rest.dto.Voice
import com.github.wensimin.boliboli_android.utils.logD
import com.github.wensimin.boliboli_android.utils.logI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

//TODO rest 存储库模式
class VoiceDataSource : PagingSource<Int, Voice>() {
    override fun getRefreshKey(state: PagingState<Int, Voice>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.let { page ->
                // 起始位置未必是0,需要判断前后key
                page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
            }
        }.also {
            logD("init key :$it")
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Voice> {
        val loadSize = params.loadSize
        logI("load voice page ${params.key ?: 0}")
        val requestWork = GlobalScope.async(Dispatchers.IO) {
            RestApi.getPage(
                "voice", Voice::class.java, mapOf(
                    "page.number" to (params.key ?: 0),
                    "page.size" to loadSize,
                    "page.properties" to "rjId", //rjId 倒序
                    "page.direction" to "DESC"
                )
            )
        }
        val res = requestWork.await()
        res.data?.let {
            val addPage = loadSize / NETWORK_PAGE_SIZE
            return LoadResult.Page(
                data = it.content,
                prevKey = if (params.key == 0) null else params.key?.minus(1),
                nextKey = if (it.last) null else (it.number + addPage)
            )
        }
        return LoadResult.Error(res.error!!)
    }

    companion object {
        // 页数 TODO 测试定值1
        const val NETWORK_PAGE_SIZE = 1
    }

}
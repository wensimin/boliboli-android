package com.github.wensimin.boliboli_android.ui.voice

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.wensimin.boliboli_android.manager.RestApi
import com.github.wensimin.boliboli_android.rest.dto.Voice
import com.github.wensimin.boliboli_android.utils.logI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

//TODO rest 请求改同步&存储库模式
class VoiceDataSource : PagingSource<Int, Voice>() {
    override fun getRefreshKey(state: PagingState<Int, Voice>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Voice> {
        val loadSize = params.loadSize
        logI("load voice page ${params.key ?: 0}")
        val res = GlobalScope.async(Dispatchers.IO) {
            RestApi.getPage(
                "voice", Voice::class.java, mapOf(
                    "page.number" to (params.key ?: 0),
                    "page.size" to loadSize
                )
            )
        }
        val page = res.await()
        if (page != null) {
            val addPage = loadSize / NETWORK_PAGE_SIZE
            return LoadResult.Page(
                data = page.content,
                prevKey = params.key,
                nextKey = if (page.last) null else (page.number + addPage)
            )
        }
        // TODO api可能改同步
        return LoadResult.Error(RuntimeException("请求错误"))
    }

    companion object {
        // 页数 TODO 测试定值1
        const val NETWORK_PAGE_SIZE = 1
    }

}
package com.github.wensimin.boliboli_android.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.wensimin.boliboli_android.manager.RestApi
import com.github.wensimin.boliboli_android.rest.dto.AuthToken
import com.github.wensimin.boliboli_android.rest.dto.Voice
import com.github.wensimin.boliboli_android.utils.logD
import com.github.wensimin.boliboli_android.utils.toastShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    //FIXME 不应暴露可变liveData
    val text: MutableLiveData<String> = MutableLiveData<String>()

    fun changeText() {
        viewModelScope.launch {
            val res = async(Dispatchers.IO) {
                RestApi.request("user", AuthToken::class.java).also {
                    this@DashboardViewModel.logD("token ${it.data?.name}")
                }
                RestApi.getPage("voice", Voice::class.java, mapOf("page.number" to 2, "page.size" to 1))
            }
            this@DashboardViewModel.logD("async request")
            //TODO viewModel 不应该调用toastShow
            res.await().data?.let { voices ->
                getApplication<Application>().baseContext.toastShow("all voice ${voices.totalElements}, current page :${voices.number}")
                text.value = voices.content.first().title
            }
        }

    }

}
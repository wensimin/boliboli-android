package com.github.wensimin.boliboli_android.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.wensimin.boliboli_android.manager.RestApi
import com.github.wensimin.boliboli_android.rest.pojo.AuthToken
import com.github.wensimin.boliboli_android.pojo.SimpleVoice
import com.github.wensimin.boliboli_android.utils.logD
import com.github.wensimin.boliboli_android.utils.toastShow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    //FIXME 不应暴露可变liveData
    val text: MutableLiveData<String> = MutableLiveData<String>()

    fun changeText() {
        viewModelScope.launch {
            RestApi.request("user", AuthToken::class.java).also {
                this@DashboardViewModel.logD("token ${it.data?.name}")
            }
            val res = RestApi.getPage("voice", SimpleVoice::class.java, mapOf("page.number" to 2, "page.size" to 1))
            //TODO viewModel 不应该调用toastShow
            res.data?.let { voices ->
                getApplication<Application>().baseContext.toastShow("all voice ${voices.totalElements}, current page :${voices.number}")
                text.value = voices.content.first().title
            }
        }

    }

}
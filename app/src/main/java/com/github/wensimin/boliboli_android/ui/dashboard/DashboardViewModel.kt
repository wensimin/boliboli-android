package com.github.wensimin.boliboli_android.ui.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.wensimin.boliboli_android.manager.RestManager
import com.github.wensimin.boliboli_android.rest.dto.AuthToken
import com.github.wensimin.boliboli_android.rest.dto.Voice
import com.github.wensimin.boliboli_android.utils.logD
import com.github.wensimin.boliboli_android.utils.toastShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    private val restManager = RestManager(application.baseContext)

    fun changeText() {
        viewModelScope.launch {
            val res = async(Dispatchers.IO) {
                restManager.request("user", AuthToken::class.java).also {
                    logD("token ${it?.name}")
                }
                restManager.getPage("voice", Voice::class.java, mapOf("page.number" to 2, "page.size" to 1))
            }
            logD("async request")
            //TODO viewModel 不应该调用toastShow
            res.await()?.let { voices ->
                getApplication<Application>().baseContext.toastShow("all voice ${voices.totalElements}, current page :${voices.number}")
                _text.value = voices.content.first().title
            }
        }

    }

}
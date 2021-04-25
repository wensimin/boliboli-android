package com.github.wensimin.boliboli_android.ui.mine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MineViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "TODO"
    }
    val text: LiveData<String> = _text
}
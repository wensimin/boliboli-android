package com.github.wensimin.boliboli_android.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * 使用主线程makeText
 */
fun Context.toastShow(message: String, time: Int = Toast.LENGTH_LONG) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(this, message, time).show()
    }
}
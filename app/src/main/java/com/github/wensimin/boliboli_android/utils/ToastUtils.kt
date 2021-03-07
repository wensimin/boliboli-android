package com.github.wensimin.boliboli_android.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * 使用主线程makeText
 */
fun toastShow(context: Context, message: String, time: Int = Toast.LENGTH_LONG) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(context, message, time).show()
    }
}
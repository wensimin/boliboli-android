package com.github.wensimin.boliboli_android

import android.app.Application
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        QMUISwipeBackActivityManager.init(this)
    }
}
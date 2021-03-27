package com.github.wensimin.boliboli_android.ui.base

import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.wensimin.boliboli_android.utils.toastShow

open class BaseActivity : AppCompatActivity() {
    private var count: Int = 0

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            onBack()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    private fun onBack() {
        Handler(mainLooper).postDelayed({ count = 0 }, 3000)
        if (++count > 1) {
            this.finishAffinity()
        } else {
            this.toastShow("再次按下返回键退出", Toast.LENGTH_SHORT)
        }
    }
}
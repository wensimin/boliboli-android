package com.github.wensimin.boliboli_android

import android.os.Bundle
import android.view.View
import com.github.wensimin.boliboli_android.manager.TokenManager
import com.github.wensimin.boliboli_android.utils.logD
import com.qmuiteam.qmui.arch.QMUIFragmentActivity


class LoginActivity : QMUIFragmentActivity() {
    private lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(this)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        tokenManager.login({
            logD("login ok")
            this.finish()
        })
    }

    // 禁用返回
    override fun onBackPressed() {}

}
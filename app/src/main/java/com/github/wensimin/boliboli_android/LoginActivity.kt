package com.github.wensimin.boliboli_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.github.wensimin.boliboli_android.manager.TokenManager

private const val TAG = "login activity"

class LoginActivity : AppCompatActivity() {
    private lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(this)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        tokenManager.login(
            Runnable {
                Log.d(TAG, "login ok")
                this.finish()
            })
    }

    // 禁用返回
    override fun onBackPressed() {}

}
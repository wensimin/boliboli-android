package com.github.wensimin.boliboli_android

import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.wensimin.boliboli_android.manager.RestManager
import com.github.wensimin.boliboli_android.rest.dto.AuthToken
import com.github.wensimin.boliboli_android.rest.dto.Voice
import com.github.wensimin.boliboli_android.utils.toastShow
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.qmuiteam.qmui.arch.QMUIFragmentActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG: String = "main activity"

class MainActivity : QMUIFragmentActivity() {

    private lateinit var restManager: RestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restManager = RestManager(this)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            val res = GlobalScope.async {
                restManager.request("user", AuthToken::class.java).also {
                    Log.d(TAG, "token ${it?.name}")
                }
                restManager.getPage("voice", Voice::class.java, mapOf("page.number" to 2, "page.size" to 1))
            }
            Log.d(TAG, "async request")
            res.await()?.let { voices ->
                this@MainActivity.toastShow("all voice ${voices.totalElements}, current page :${voices.number}")
            }
        }
    }


}
package com.github.wensimin.boliboli_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.wensimin.boliboli_android.manager.RestManager
import com.github.wensimin.boliboli_android.manager.TokenManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.net.HttpURLConnection
import java.net.URL
import java.util.function.Consumer

private const val TAG: String = "main activity"

class MainActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var restManager: RestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restManager = RestManager(this)
        tokenManager = TokenManager(this, this)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        restManager.testRequest(
            success = Consumer { Log.d(TAG, "test request ok") },
            error = Consumer {
                //error to login
                tokenManager.login(
                    Runnable {
                        Log.d(TAG, "login ok")
                    })
            })
    }


}
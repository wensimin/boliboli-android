package com.github.wensimin.boliboli_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.wensimin.boliboli_android.manager.TokenManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.net.HttpURLConnection
import java.net.URL
import java.util.function.Consumer

private const val TAG: String = "MAIN ACTIVITY"

class MainActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(this, this)
        tokenManager.login(
            Runnable {
                Log.d(TAG, "login ok")
                tokenManager.testRequest(
                    success = Consumer { s -> Log.d(TAG, "onResume: $s") },
                    error = Consumer {
                        //TODO move to login activity
                    })
            })
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


}
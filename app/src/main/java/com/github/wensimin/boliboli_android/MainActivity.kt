package com.github.wensimin.boliboli_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.wensimin.boliboli_android.manager.RestManager
import com.github.wensimin.boliboli_android.rest.dto.AuthToken
import com.github.wensimin.boliboli_android.utils.toastShow
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG: String = "main activity"

class MainActivity : AppCompatActivity() {

    private lateinit var restManager: RestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restManager = RestManager(this)
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
        GlobalScope.launch {
            val res = GlobalScope.async { restManager.request("user", AuthToken::class.java) }
            Log.d(TAG, "async request")
            this@MainActivity.toastShow("${res.await()}")
        }
    }


}
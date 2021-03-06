package com.github.wensimin.boliboli_android

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.wensimin.boliboli_android.manager.RestManager
import com.github.wensimin.boliboli_android.rest.dto.AuthToken
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.function.Consumer

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
        restManager.request("public/test",AuthToken::class.java, Consumer { t -> Toast.makeText(this,"$t",Toast.LENGTH_LONG).show() })
    }


}
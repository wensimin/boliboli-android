package com.github.wensimin.boliboli_android

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.wensimin.boliboli_android.ui.base.BaseActivity
import com.github.wensimin.boliboli_android.ui.dashboard.DashboardViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity() {
    private lateinit var mode: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mode = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

}
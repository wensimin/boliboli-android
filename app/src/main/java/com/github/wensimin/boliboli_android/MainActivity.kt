package com.github.wensimin.boliboli_android

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.wensimin.boliboli_android.ui.dashboard.DashboardViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.qmuiteam.qmui.arch.QMUIFragmentActivity


class MainActivity : QMUIFragmentActivity() {
    private var mode: ViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mode = mode ?: DashboardViewModel(application)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

}
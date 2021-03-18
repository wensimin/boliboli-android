package com.github.wensimin.boliboli_android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.wensimin.boliboli_android.ui.base.BaseActivity
import com.github.wensimin.boliboli_android.ui.dashboard.DashboardViewModel
import com.github.wensimin.boliboli_android.ui.voice.VoiceListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity() {
    private val mode: DashboardViewModel by viewModels()
    // 音声list vm
    private val voiceListViewModel: VoiceListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

}
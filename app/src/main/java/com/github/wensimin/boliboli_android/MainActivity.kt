package com.github.wensimin.boliboli_android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.wensimin.boliboli_android.databinding.ActivityMainBinding
import com.github.wensimin.boliboli_android.ui.dashboard.DashboardViewModel
import com.github.wensimin.boliboli_android.ui.getNavVisibility
import com.github.wensimin.boliboli_android.ui.isFullScreen
import com.github.wensimin.boliboli_android.ui.voice.VoiceListViewModel
import com.github.wensimin.boliboli_android.utils.logD

//警告 AS的静态检查对以下models无效,实际有使用
class MainActivity : AppCompatActivity() {
    //TEST TODO DELETE
    val mode: DashboardViewModel by viewModels()

    // 音声list vm
    val voiceListViewModel: VoiceListViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.navView.visibility = destination.getNavVisibility()
            if (destination.isFullScreen()) {
                //TODO to full screen
                logD("is full")
            }
        }
    }

}
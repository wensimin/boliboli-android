package com.github.wensimin.boliboli_android.ui

import android.view.View
import androidx.navigation.NavDestination
import com.github.wensimin.boliboli_android.R

/**
 * 全屏的destination id集合
 */
private val fullScreenDestination = listOf(R.id.mediaPlayerFragment)

/**
 * 不需要导航栏的destination id集合 包含全屏destination
 */
private val noNavDestination = listOf(R.id.voiceInfoFragment) + fullScreenDestination


/**
 * 是否需要导航栏
 */
fun NavDestination.needNav() = !noNavDestination.contains(this.id)

/**
 * 获取导航栏可见性
 */
fun NavDestination.getNavVisibility() = if (this.needNav()) View.VISIBLE else View.GONE


fun NavDestination.isFullScreen() = fullScreenDestination.contains(this.id)
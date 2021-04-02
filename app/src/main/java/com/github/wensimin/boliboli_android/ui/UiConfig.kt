package com.github.wensimin.boliboli_android.ui

import android.view.View
import androidx.navigation.NavDestination
import com.github.wensimin.boliboli_android.R

/**
 * 不需要导航栏的fragment id集合
 */
private val noNavDestination = listOf(R.id.voiceInfoFragment)

/**
 * 是否需要导航栏
 */
fun NavDestination.needNav() = !noNavDestination.contains(this.id)

/**
 * 获取可见性
 */
fun NavDestination.getVisibility() = if (this.needNav()) View.VISIBLE else View.GONE

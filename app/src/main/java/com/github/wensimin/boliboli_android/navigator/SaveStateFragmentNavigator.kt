package com.github.wensimin.boliboli_android.navigator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

@Navigator.Name("fragment")
class SaveStateFragmentNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {
    private val cacheMap: MutableMap<String, Fragment> = HashMap()

    override fun instantiateFragment(
        context: Context,
        fragmentManager: FragmentManager,
        className: String,
        args: Bundle?
    ): Fragment {
        //重用已有的fragment
        return cacheMap[className] ?: super.instantiateFragment(
            context,
            fragmentManager,
            className,
            args
        )
    }

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        // 导航之后cache map
        manager.fragments.addAll(cacheMap.values)
        return super.navigate(destination, args, navOptions, navigatorExtras).also {
            manager.fragments.forEach { cacheMap[it::class.qualifiedName!!] = it }
        }

    }

}
package com.github.wensimin.boliboli_android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.wensimin.boliboli_android.R
import com.github.wensimin.boliboli_android.utils.inflateNoParent
import com.qmuiteam.qmui.arch.QMUIFragment

class HomeFragment : QMUIFragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = layoutInflater.inflateNoParent(R.layout.fragment_home)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
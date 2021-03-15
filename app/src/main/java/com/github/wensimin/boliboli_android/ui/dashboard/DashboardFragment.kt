package com.github.wensimin.boliboli_android.ui.dashboard

import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.github.wensimin.boliboli_android.R
import com.github.wensimin.boliboli_android.utils.inflateNoParent
import com.qmuiteam.qmui.arch.QMUIFragment

class DashboardFragment : QMUIFragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(): View {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = layoutInflater.inflateNoParent(R.layout.fragment_dashboard)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        textView.setOnClickListener {
            dashboardViewModel.changeText()
        }
        dashboardViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
}
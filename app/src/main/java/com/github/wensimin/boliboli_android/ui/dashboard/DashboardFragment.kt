package com.github.wensimin.boliboli_android.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.wensimin.boliboli_android.R
import com.github.wensimin.boliboli_android.utils.inflateNoParent

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dashboardViewModel = activityViewModels<DashboardViewModel>().value
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
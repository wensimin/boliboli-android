package com.github.wensimin.boliboli_android.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.wensimin.boliboli_android.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dashboardViewModel = activityViewModels<DashboardViewModel>().value
        return FragmentDashboardBinding.inflate(inflater).apply {
            model = dashboardViewModel
            lifecycleOwner = this@DashboardFragment
        }.root
    }

}
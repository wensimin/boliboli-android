package com.github.wensimin.boliboli_android.ui.notifications

import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.wensimin.boliboli_android.R
import com.github.wensimin.boliboli_android.utils.inflateNoParent
import com.qmuiteam.qmui.arch.QMUIFragment

class NotificationsFragment : QMUIFragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel


    override fun onCreateView(): View {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = layoutInflater.inflateNoParent(R.layout.fragment_notifications)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
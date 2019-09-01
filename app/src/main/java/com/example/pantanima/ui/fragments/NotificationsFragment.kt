package com.example.pantanima.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pantanima.R
import com.example.pantanima.ui.ViewModelUtils
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.viewmodels.NotificationsViewModel
import java.lang.ref.WeakReference

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModel = NotificationsViewModel(WeakReference(activity as NavActivity))
        val factory = ViewModelUtils.createFor(viewModel)
        notificationsViewModel = ViewModelProviders.of(this, factory).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}
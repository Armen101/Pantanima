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
import com.example.pantanima.ui.viewmodels.HomeViewModel
import java.lang.ref.WeakReference

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = HomeViewModel(WeakReference(activity as NavActivity))
        val factory = ViewModelUtils.createFor(viewModel)
        homeViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}
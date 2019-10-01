package com.example.pantanima.ui.fragments

import android.os.Bundle
import android.view.View
import com.example.pantanima.BR
import com.example.pantanima.databinding.FragmentStartScreenBinding
import com.example.pantanima.ui.viewmodels.StartScreenViewModel
import com.example.pantanima.R
import com.example.pantanima.ui.activities.NavActivity
import java.lang.ref.WeakReference

class StartScreenFragment : BaseFragment<FragmentStartScreenBinding, StartScreenViewModel>() {

    private lateinit var startScreenBinding: FragmentStartScreenBinding
    private lateinit var viewModel: StartScreenViewModel

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_start_screen
    }

    override fun getNavHostId(): Int {
        return R.id.nav_host_fragment
    }

    override fun getViewModel(): StartScreenViewModel {
        viewModel = StartScreenViewModel(WeakReference(activity as NavActivity))
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startScreenBinding = getViewDataBinding()
    }
}
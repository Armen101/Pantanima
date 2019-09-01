package com.example.pantanima.ui.fragments

import android.os.Bundle
import android.view.View
import com.example.pantanima.BR
import com.example.pantanima.databinding.FragmentDashboardBinding
import com.example.pantanima.ui.viewmodels.DashboardViewModel
import com.example.pantanima.R
import com.example.pantanima.ui.activities.NavActivity
import java.lang.ref.WeakReference

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    private lateinit var fragmentDashboardBinding: FragmentDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun getNavHostId(): Int {
        return R.id.nav_host_fragment
    }

    override fun getViewModel(): DashboardViewModel {
        viewModel = DashboardViewModel(WeakReference(activity as NavActivity))
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDashboardBinding = getViewDataBinding()
    }
}
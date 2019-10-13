package com.example.pantanima.ui.fragments

import android.os.Bundle
import android.view.View
import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.FragmentSettingsBinding
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.viewmodels.SettingsVM
import java.lang.ref.WeakReference


class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsVM>() {

    private lateinit var fragmentSettingsBinding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsVM

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_settings
    }

    override fun getNavHostId(): Int {
        return R.id.nav_host_fragment
    }

    override fun getViewModel(): SettingsVM {
        viewModel = SettingsVM(WeakReference(activity as NavActivity))
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSettingsBinding = getViewDataBinding()
    }
}
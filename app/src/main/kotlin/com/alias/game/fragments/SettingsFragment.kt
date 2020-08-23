package com.example.pantanima.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.FragmentSettingsBinding
import com.example.pantanima.ui.viewmodels.SettingsVM
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsVM>() {

    private val vm: SettingsVM by viewModel { parametersOf(this) }

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_settings

    override fun getNavHostId() = R.id.nav_host_fragment

    override fun getViewModel() = vm

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.backPressed.observe(this as LifecycleOwner, Observer {
            activity?.onBackPressed()
        })
    }

}
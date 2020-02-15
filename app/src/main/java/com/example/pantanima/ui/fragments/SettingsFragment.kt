package com.example.pantanima.ui.fragments

import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.FragmentSettingsBinding
import com.example.pantanima.ui.viewmodels.SettingsVM

interface SettingsFragmentCallback {
    fun onBackPressed()
}

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsVM>(),
    SettingsFragmentCallback {

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_settings

    override fun getNavHostId() = R.id.nav_host_fragment

    override fun getViewModel() = SettingsVM(this)

    override fun onBackPressed() {
        activity?.onBackPressed()
    }

}
package com.example.pantanima.ui.fragments

import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.FragmentSettingsBinding
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.viewmodels.SettingsVM
import java.lang.ref.WeakReference

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsVM>() {

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_settings

    override fun getNavHostId() = R.id.nav_host_fragment

    override fun getViewModel() = SettingsVM(WeakReference(activity as NavActivity))

}
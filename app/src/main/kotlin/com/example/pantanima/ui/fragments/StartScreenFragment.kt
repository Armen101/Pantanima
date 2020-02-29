package com.example.pantanima.ui.fragments

import com.example.pantanima.BR
import com.example.pantanima.databinding.FragmentStartScreenBinding
import com.example.pantanima.ui.viewmodels.StartScreenVM
import com.example.pantanima.R
import org.koin.android.viewmodel.ext.android.viewModel

class StartScreenFragment : BaseFragment<FragmentStartScreenBinding, StartScreenVM>() {

    private val vm by viewModel<StartScreenVM>()

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_start_screen

    override fun getNavHostId() = R.id.nav_host_fragment

    override fun getViewModel() = vm

}
package com.example.pantanima.ui.fragments

import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.FragmentPlayBinding
import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.viewmodels.PlayVM
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayFragment : BaseFragment<FragmentPlayBinding, PlayVM>() {

    private val vm: PlayVM by viewModel {
        parametersOf(arguments?.getStringArrayList(Constants.BUNDLE_GROUPS))
    }

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_play

    override fun getNavHostId() = R.id.nav_host_fragment

    override fun getViewModel() = vm
}
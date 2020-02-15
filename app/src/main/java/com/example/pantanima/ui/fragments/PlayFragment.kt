package com.example.pantanima.ui.fragments

import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.FragmentPlayBinding
import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.viewmodels.PlayVM

class PlayFragment : BaseFragment<FragmentPlayBinding, PlayVM>() {

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_play

    override fun getNavHostId() = R.id.nav_host_fragment

    override fun getViewModel(): PlayVM {
        val groups = arguments?.getStringArrayList(Constants.BUNDLE_GROUPS)
        return PlayVM(groups)
    }

}
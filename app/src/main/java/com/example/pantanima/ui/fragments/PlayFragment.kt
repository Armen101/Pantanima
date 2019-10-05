package com.example.pantanima.ui.fragments

import android.os.Bundle
import android.view.View
import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.FragmentPlayBinding
import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.viewmodels.PlayViewModel
import java.lang.ref.WeakReference

class PlayFragment : BaseFragment<FragmentPlayBinding, PlayViewModel>() {

    private lateinit var fragmentHomeBinding: FragmentPlayBinding
    private lateinit var viewModel: PlayViewModel

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_play
    }

    override fun getNavHostId(): Int {
        return R.id.nav_host_fragment
    }

    override fun getViewModel(): PlayViewModel {
        val groups = arguments?.getStringArrayList(Constants.BUNDLE_GROUPS)
        viewModel = PlayViewModel(WeakReference(activity as NavActivity), groups!!)
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding = getViewDataBinding()
    }
}
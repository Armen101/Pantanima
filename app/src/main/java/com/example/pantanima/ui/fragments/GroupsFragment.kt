package com.example.pantanima.ui.fragments

import android.os.Bundle
import android.view.View
import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.FragmentGroupsBinding
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.viewmodels.GroupsViewModel
import java.lang.ref.WeakReference

class GroupsFragment : BaseFragment<FragmentGroupsBinding, GroupsViewModel>() {

    private lateinit var fragmentGroupsBinding: FragmentGroupsBinding
    private lateinit var viewModel: GroupsViewModel

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_groups
    }

    override fun getNavHostId(): Int {
        return R.id.nav_host_fragment
    }

    override fun getViewModel(): GroupsViewModel {
        viewModel = GroupsViewModel(WeakReference(activity as NavActivity))
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentGroupsBinding = getViewDataBinding()
    }
}
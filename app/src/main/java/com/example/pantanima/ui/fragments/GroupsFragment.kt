package com.example.pantanima.ui.fragments

import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.FragmentGroupsBinding
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.viewmodels.GroupsVM
import java.lang.ref.WeakReference

class GroupsFragment : BaseFragment<FragmentGroupsBinding, GroupsVM>() {

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_groups

    override fun getNavHostId() = R.id.nav_host_fragment

    override fun getViewModel() = GroupsVM(WeakReference(activity as NavActivity))

}
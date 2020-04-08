package com.example.pantanima.ui.fragments

import androidx.lifecycle.Lifecycle
import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.FragmentGroupsBinding
import com.example.pantanima.ui.viewmodels.GroupsVM
import org.koin.android.viewmodel.ext.android.viewModel

class GroupsFragment : BaseFragment<FragmentGroupsBinding, GroupsVM>() {

    private val vm by viewModel<GroupsVM>()

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_groups

    override fun getNavHostId() = R.id.nav_host_fragment

    override fun getViewModel() = vm

    override fun onResume() {
        super.onResume()
        vm.lifecycleLiveData.value = Lifecycle.Event.ON_RESUME
    }
}
package com.example.pantanima.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.pantanima.ui.viewmodels.BaseVM
import androidx.navigation.NavController
import com.example.pantanima.ui.EventObserver
import com.example.pantanima.ui.activities.NavActivity


abstract class BaseFragment<T : ViewDataBinding, V : BaseVM> : Fragment() {

    private lateinit var viewDataBinding: T

    /**
     * Override for set binding variable
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * Override for set view model
     * @return view model instance
     */
    abstract fun getViewModel(): V

    @IdRes
    abstract fun getNavHostId(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun getViewDataBinding() = viewDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(getBindingVariable(), getViewModel())
        viewDataBinding.executePendingBindings()
        getViewModel().getNewDestination().observe(viewLifecycleOwner, EventObserver {
            getNavController()?.navigate(it.first, it.second)
        })
    }

    private fun getNavController(): NavController? {
        return (activity as NavActivity).navController
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getViewModel().onActivityResult(requestCode, resultCode, data)
    }
}
package com.example.pantanima.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pantanima.ui.viewmodels.BaseVM
import androidx.navigation.NavController
import com.example.pantanima.ui.EventObserver
import com.example.pantanima.ui.helpers.ViewModelUtils
import com.example.pantanima.ui.activities.NavActivity


abstract class BaseFragment<T : ViewDataBinding, V : BaseVM> : Fragment() {

    private lateinit var binding: Unit
    private lateinit var viewDataBinding: T
    private lateinit var viewModel: V

    /**
     * Override for set binding variable
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        viewModel.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return viewDataBinding.root
    }

    /**
     * Override for set view model
     * @return view model instance
     */
    abstract fun getViewModel(): V

    abstract fun getNavHostId(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun getViewDataBinding(): T {
        return viewDataBinding
    }

    private fun createViewModel(): V {
        val vm = getViewModel()
        val factory = ViewModelUtils.createFor(vm)
        viewModel = ViewModelProviders.of(this, factory).get(vm::class.java)
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(getBindingVariable(), viewModel)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.executePendingBindings()
        viewModel.getNewDestination().observe(this, EventObserver {
            getNavController()?.navigate(it.first, it.second)
        })
    }

    private fun getNavController(): NavController? {
        return (activity as NavActivity).navController
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onActivityResult(requestCode, resultCode, data)
    }
}
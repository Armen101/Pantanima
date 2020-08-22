package com.example.pantanima.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.FragmentStartScreenBinding
import com.example.pantanima.ui.viewmodels.StartScreenVM
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Exception


class StartScreenFragment : BaseFragment<FragmentStartScreenBinding, StartScreenVM>() {

    private val vm by viewModel<StartScreenVM>()

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_start_screen

    override fun getNavHostId() = R.id.nav_host_fragment

    override fun getViewModel() = vm

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.openTutorial.observe(viewLifecycleOwner, Observer {
            try {
                val url = "https://en.wikipedia.org/wiki/Alias_(board_game)"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } catch (e: Exception) {

            }
        })
    }

}
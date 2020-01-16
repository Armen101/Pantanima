package com.example.pantanima.ui.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.pantanima.R
import com.example.pantanima.ui.asynchronous.CompositeJob

abstract class NavActivity : AppCompatActivity() {

    val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    protected var compositeJob = CompositeJob()

    override fun onDestroy() {
        super.onDestroy()
        compositeJob.cancel()
    }

}
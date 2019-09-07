package com.example.pantanima.ui.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.pantanima.ui.asynchronous.CompositeJob

interface NavControllerContract {
    fun getNavController(): NavController?
}

abstract class NavActivity : AppCompatActivity(), NavControllerContract {

    protected var compositeJob = CompositeJob()

    override fun onDestroy() {
        super.onDestroy()
        compositeJob.cancel()
    }

}
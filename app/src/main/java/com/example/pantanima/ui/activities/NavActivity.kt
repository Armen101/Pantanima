package com.example.pantanima.ui.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController

interface NavControllerContract {
    fun getNavController(): NavController?
}

abstract class NavActivity : AppCompatActivity(), NavControllerContract
package com.example.pantanima.ui.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pantanima.R
import com.example.pantanima.ui.PantanimaApplication
import com.example.pantanima.ui.database.repository.NounRepo
import com.example.pantanima.ui.database.preference.PrefConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : NavActivity() {

    private lateinit var navController: NavController

    override fun getNavController(): NavController = navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)

        setupUI()
        setupData()
    }

    private fun setupUI(){
        supportActionBar?.hide()
    }


    private fun setupData(){
        val app = application as PantanimaApplication
        val isFirstRunning = app.prefs.getBoolean(PrefConstants.FIRST_RUNNING, true)
        if (isFirstRunning) {
            compositeJob.add(GlobalScope.launch(Dispatchers.IO) {
                NounRepo.insertNouns()
                app.prefs.save(PrefConstants.FIRST_RUNNING, false)
            })
        }
    }
}

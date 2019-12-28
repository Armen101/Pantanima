package com.example.pantanima.ui.activities

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.pantanima.R
import com.example.pantanima.ui.database.repository.NounRepo
import com.example.pantanima.ui.database.preference.PrefConstants
import com.example.pantanima.ui.database.preference.Preferences
import com.example.pantanima.ui.database.repository.GroupRepo
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
        val isFirstRunning = Preferences.getBoolean(PrefConstants.FIRST_RUNNING, true)
        if (isFirstRunning) {
            compositeJob.add(GlobalScope.launch(Dispatchers.IO) {
                NounRepo.insertInitialNouns()
                GroupRepo.insertInitialGroups()
                Preferences.save(PrefConstants.FIRST_RUNNING, false)
            })
        }
    }
}

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
import androidx.core.content.ContextCompat
import android.view.WindowManager

class MainActivity : NavActivity() {

    private lateinit var navController: NavController

    override fun getNavController(): NavController = navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)

        setupUI()
        setupData()
        supportActionBar?.hide();


// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent))
    }

    private fun setupUI(){
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)
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

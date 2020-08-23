package com.example.pantanima.ui.activities

import android.os.Bundle
import com.example.pantanima.R

class MainActivity : NavActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        setupUI()
    }

    private fun setupUI() {
        supportActionBar?.hide()
    }

}

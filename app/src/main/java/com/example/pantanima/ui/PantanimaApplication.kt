package com.example.pantanima.ui

import android.app.Application
import androidx.room.Room
import com.example.pantanima.ui.database.AppDatabase
import com.example.pantanima.ui.database.DbConstants
import com.example.pantanima.ui.database.repository.NounRepo
import com.example.pantanima.ui.database.preference.Preferences
import com.example.pantanima.ui.di.components.AppComponent
import com.example.pantanima.ui.di.components.DaggerAppComponent

class PantanimaApplication : Application() {

    private lateinit var database: AppDatabase
    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.create();
        database = Room.databaseBuilder(this, AppDatabase::class.java, DbConstants.DB_NAME)
            .build()

        Preferences.init(applicationContext)
        NounRepo.injectDao(database.nounDao())
    }

    fun getComponent() = component

    fun getDatabase() = database
}
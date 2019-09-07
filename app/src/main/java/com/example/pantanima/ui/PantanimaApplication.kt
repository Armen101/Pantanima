package com.example.pantanima.ui

import android.app.Application
import androidx.room.Room
import com.example.pantanima.ui.database.AppDatabase
import com.example.pantanima.ui.database.DbConstants
import com.example.pantanima.ui.database.repository.NounRepo
import com.example.pantanima.ui.database.preference.Preferences

class PantanimaApplication : Application() {

    private lateinit var database: AppDatabase
    lateinit var prefs : Preferences
    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, AppDatabase::class.java, DbConstants.DB_NAME)
            .build()

        prefs = Preferences(applicationContext)
        NounRepo.injectDao(database.nounDao())
    }

    fun getDatabase() = database
}
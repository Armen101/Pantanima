package com.example.pantanima.ui

import android.app.Application
import androidx.room.Room
import com.example.pantanima.BuildConfig
import com.example.pantanima.ui.database.AppDatabase
import com.example.pantanima.ui.database.DbConstants
import com.example.pantanima.ui.database.repository.NounRepo
import com.example.pantanima.ui.database.preference.Preferences
import com.example.pantanima.ui.database.repository.GroupRepo
import com.example.pantanima.ui.di.moduls.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class PantanimaApplication : Application() {

    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@PantanimaApplication)
            modules(appModule)
        }

        initTimber()

        database = Room.databaseBuilder(this, AppDatabase::class.java, DbConstants.DB_NAME)
            .build()

        Preferences.init(applicationContext)
        NounRepo.injectDao(database.nounDao())
        GroupRepo.injectDao(database.groupDao())
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format("(%s:%s) %s()",
                        element.fileName,
                        element.lineNumber,
                        element.methodName)
                }
            })
        }
        //todo, in else case init log tree for Fabric
    }

    companion object {
        lateinit var instance: PantanimaApplication
            private set
    }
}
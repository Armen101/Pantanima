package com.example.pantanima.ui

import android.app.Application
import com.example.pantanima.BuildConfig
import com.example.pantanima.ui.di.moduls.appModule
import com.example.pantanima.ui.di.moduls.repoModule
import com.example.pantanima.ui.di.moduls.roomModule
import com.example.pantanima.ui.di.moduls.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class PantanimaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@PantanimaApplication)
            modules(appModule, viewModelModule, roomModule, repoModule)
        }

        initTimber()
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

}
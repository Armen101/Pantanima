package com.example.pantanima.ui.di.moduls

import androidx.room.Room
import com.example.pantanima.ui.GroupManager
import com.example.pantanima.ui.database.AppDatabase
import com.example.pantanima.ui.database.repository.GroupRepo
import com.example.pantanima.ui.database.repository.impl.GroupRepoImpl
import com.example.pantanima.ui.database.repository.NounRepo
import com.example.pantanima.ui.database.repository.impl.NounRepoImpl
import com.example.pantanima.ui.fragments.SettingsFragmentCallback
import com.example.pantanima.ui.viewmodels.GroupsVM
import com.example.pantanima.ui.viewmodels.PlayVM
import com.example.pantanima.ui.viewmodels.SettingsVM
import com.example.pantanima.ui.viewmodels.StartScreenVM
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val appModule = module {
    factory { (gNames: ArrayList<String>?) -> GroupManager(gNames) }
}

val viewModelModule = module {

    viewModel { GroupsVM(get()) }

    viewModel { StartScreenVM() }

    viewModel { (gNames: ArrayList<String>?) -> PlayVM(get { parametersOf(gNames) }, get()) }

    viewModel { (cb: SettingsFragmentCallback) -> SettingsVM(cb) }

}

val roomModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "appDb"
        ).build()
    }

    single { get<AppDatabase>().groupDao() }

    single { get<AppDatabase>().nounDao() }
}

val repoModule = module {

    single<GroupRepo> { GroupRepoImpl(get()) }

    single<NounRepo> { NounRepoImpl(get()) }

}
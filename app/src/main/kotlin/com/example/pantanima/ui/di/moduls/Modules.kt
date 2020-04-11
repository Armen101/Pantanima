package com.example.pantanima.ui.di.moduls

import androidx.room.Room
import com.example.pantanima.ui.GroupManager
import com.example.pantanima.ui.database.AppDatabase
import com.example.pantanima.ui.database.repository.GroupRepo
import com.example.pantanima.ui.database.repository.impl.GroupRepoImpl
import com.example.pantanima.ui.database.repository.NounRepo
import com.example.pantanima.ui.database.repository.impl.NounRepoImpl
import com.example.pantanima.ui.models.Group
import com.example.pantanima.ui.viewmodels.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GroupsVM(get(), get()) }
    viewModel { StartScreenVM(get()) }
    viewModel { (gNames: ArrayList<String>?) -> PlayVM(get(), get { parametersOf(gNames) }, get()) }
    viewModel { SettingsVM(get()) }
    viewModel { (groups: ArrayList<Group>) -> WinVM(get(), groups) }
    factory { (gNames: ArrayList<String>?) -> GroupManager(gNames) }
}

val roomModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "appDb").build() }
    single { get<AppDatabase>().groupDao() }
    single { get<AppDatabase>().nounDao() }
}

val repoModule = module {
    single<GroupRepo> { GroupRepoImpl(get()) }
    single<NounRepo> { NounRepoImpl(get()) }
}
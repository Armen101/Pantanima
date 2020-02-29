package com.example.pantanima.ui.di.moduls

import com.example.pantanima.ui.GroupManager
import com.example.pantanima.ui.fragments.SettingsFragmentCallback
import com.example.pantanima.ui.viewmodels.GroupsVM
import com.example.pantanima.ui.viewmodels.PlayVM
import com.example.pantanima.ui.viewmodels.SettingsVM
import com.example.pantanima.ui.viewmodels.StartScreenVM
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { GroupsVM() }

    viewModel { StartScreenVM() }

    viewModel { (groupNames: ArrayList<String>?) -> PlayVM(get(), groupNames) }

    viewModel { (cb: SettingsFragmentCallback) -> SettingsVM(cb) }

    single { GroupManager() }

}
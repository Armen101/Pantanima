package com.example.pantanima.ui.di.components

import com.example.pantanima.ui.di.moduls.GroupModule
import com.example.pantanima.ui.viewmodels.PlayViewModel
import dagger.Component

@Component(modules = [GroupModule::class])
interface AppComponent {

    fun injectHomeViewModel(homeViewModel: PlayViewModel)
}

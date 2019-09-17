package com.example.pantanima.ui.di.moduls

import com.example.pantanima.ui.GroupManager
import dagger.Module
import dagger.Provides

@Module
class GroupModule {

    @Provides
    fun getGroupManager(): GroupManager = GroupManager()

}
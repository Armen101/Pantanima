package com.example.pantanima.ui.viewmodels

import com.example.pantanima.R
import com.example.pantanima.ui.activities.NavActivity
import java.lang.ref.WeakReference

class StartScreenViewModel(activity: WeakReference<NavActivity>) : BaseViewModel(activity) {

    fun goToGroups() {
        setNewDestination(R.id.navigateToGroups)
    }
}
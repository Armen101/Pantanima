package com.example.pantanima.ui.viewmodels

import androidx.databinding.ObservableField
import com.example.pantanima.R
import com.example.pantanima.ui.activities.NavActivity
import java.lang.ref.WeakReference

class GroupsViewModel(activity: WeakReference<NavActivity>) : BaseViewModel(activity) {

    var group1 : ObservableField<String?> = ObservableField(getString(R.string.first_def_group_name))
    var group2 : ObservableField<String?> = ObservableField(getString(R.string.second_def_group_name))
    var group3 : ObservableField<String?> = ObservableField("")
    var group4 : ObservableField<String?> = ObservableField("")

    fun goToPlay() {
        setNewDestination(R.id.navigateToPlay)
    }
}
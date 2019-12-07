package com.example.pantanima.ui.viewmodels

import android.os.Bundle
import androidx.databinding.ObservableField
import com.example.pantanima.R
import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.activities.NavActivity
import java.lang.ref.WeakReference

class GroupsVM(activity: WeakReference<NavActivity>) : BaseVM(activity) {

    var group1: ObservableField<String?> = ObservableField(getString(R.string.first_def_group_name))
    var group2: ObservableField<String?> = ObservableField(getString(R.string.second_def_group_name))
    var group3: ObservableField<String?> = ObservableField("")
    var group4: ObservableField<String?> = ObservableField("")

    fun onStartClick() {
        if(group1.get().isNullOrEmpty() || group2.get().isNullOrEmpty()){
            //todo open error dialog
        } else {
            val bundle = Bundle()
            bundle.putStringArrayList(Constants.BUNDLE_GROUPS, getGroups())
            setNewDestination(R.id.navigateToPlay, bundle)
        }
    }

    fun goToSettings() {
        setNewDestination(R.id.navigateToSettings)
    }

    private fun getGroups(): ArrayList<String> {
        val resultList = ArrayList<String>()
        val list = listOfNotNull(group1.get(), group2.get(), group3.get(), group4.get())
        for (i in list) {
            if (i.isNotEmpty()) {
                resultList.add(i)
            }
        }
        return resultList
    }
}
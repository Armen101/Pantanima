package com.example.pantanima.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pantanima.ui.activities.NavActivity
import java.lang.ref.WeakReference

class NotificationsViewModel(activity: WeakReference<NavActivity>) : BaseViewModel(activity) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}
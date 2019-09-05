package com.example.pantanima.ui.viewmodels

import com.example.pantanima.R
import com.example.pantanima.ui.activities.NavActivity
import java.lang.ref.WeakReference

class DashboardViewModel(activity: WeakReference<NavActivity>) : BaseViewModel(activity) {

     val title: String = "This is dashboard Fragment"

     fun goToNotifications() {
          setNewDestination(R.id.navigateToNotification)
     }

}
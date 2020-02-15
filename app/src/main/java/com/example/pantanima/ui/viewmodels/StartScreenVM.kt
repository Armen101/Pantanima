package com.example.pantanima.ui.viewmodels

import androidx.databinding.ObservableField
import com.example.pantanima.R
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.fragments.ReentrantLockDemonstration
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicInteger

class StartScreenVM(activity: WeakReference<NavActivity>) : BaseVM(activity) {

    private var languageList = activity.get()?.resources?.getStringArray(R.array.languages_list)!!

    var newGame = ObservableField<String>(getString(R.string.new_game))
    var tutorial = ObservableField<String>(getString(R.string.tutorial))
    var language = ObservableField<String>(languageList[0])

    fun goToGroups() {
        setNewDestination(R.id.navigateToGroups)
    }

    fun onLanguageClick() {
        ReentrantLockDemonstration().demonstrate()
    }

    private fun getNextLanguage(): String {
        val curIndex = languageList.indexOf(language.get())
        val futureLanguageIndex = if (curIndex < languageList.size - 1) {
            curIndex.inc()
        } else {
            0
        }
        return languageList[futureLanguageIndex]
    }

}
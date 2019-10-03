package com.example.pantanima.ui.viewmodels

import androidx.databinding.ObservableField
import com.example.pantanima.R
import com.example.pantanima.ui.activities.NavActivity
import java.lang.ref.WeakReference
import java.util.*

class StartScreenViewModel(activity: WeakReference<NavActivity>) : BaseViewModel(activity) {

    private var languageList = activity.get()?.resources?.getStringArray(R.array.languages_list)!!

    var newGame = ObservableField<String>(getString(R.string.new_game))
    var tutorial = ObservableField<String>(getString(R.string.tutorial))
    var language = ObservableField<String>(languageList[0])

    fun goToGroups() {
        setNewDestination(R.id.navigateToGroups)
    }

    fun onLanguageClick() {
        val curIndex = languageList.indexOf(language.get())
        val futureLanguageIndex = if (curIndex < languageList.size - 1) {
            curIndex.inc()
        } else {
            0
        }
        language.set(languageList[futureLanguageIndex])
        changeLanguage(getLanguageCode(language.get()))
        newGame.set(getString(R.string.new_game))
        tutorial.set(getString(R.string.tutorial))
    }

    private fun getLanguageCode(language: String?): String {
        return when (language) {
            getString(R.string.armenian) -> "hy"
            getString(R.string.russian) -> "ru"
            else -> "en"
        }
    }

    @Suppress("DEPRECATION")
    private fun changeLanguage(languageCode: String) {
        val res = activity.get()?.resources
        val dm = res?.displayMetrics
        val conf = res?.configuration
        conf?.setLocale(Locale(languageCode.toLowerCase(Locale.ENGLISH)))
        res?.updateConfiguration(conf, dm)
    }
}
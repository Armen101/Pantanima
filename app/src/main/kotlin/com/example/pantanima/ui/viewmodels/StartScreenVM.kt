package com.example.pantanima.ui.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import com.example.pantanima.R
import com.example.pantanima.ui.helpers.LocaleHelper

class StartScreenVM(app: Application) : BaseVM(app) {

    private var languageList = resources.value.getStringArray(R.array.languages_list)

    var newGame = ObservableField<String>(getString(R.string.new_game))
    var tutorial = ObservableField<String>(getString(R.string.tutorial))
    var language = ObservableField<String>(languageList[0])

    fun goToGroups() {
        setNewDestination(R.id.navigateToGroups)
    }

    fun onLanguageClick() {
        val newLanguage = getNextLanguage()
        LocaleHelper.changeLanguage(resources.value, newLanguage)

        language.set(newLanguage)
        newGame.set(getString(R.string.new_game))
        tutorial.set(getString(R.string.tutorial))
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
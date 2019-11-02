package com.example.pantanima.ui.helpers

import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.database.preference.Preferences

object GamePrefs {

    //Immutable prefs
    const val ASSORTMENT_WORDS_COUNT = 30
    const val WORDS_COUNT = 5
    var SOUND_TIME = 5L

    //Mutable prefs
    var LANGUAGE = Constants.LANGUAGE_AM
        get() {
            return Preferences.getString(Constants.PREF_LANGUAGE, field)
        }
    var ROUND_TIME = 40
        get() {
            return Preferences.getInt(Constants.PREF_ROUND_TIME, field)
        }
    var GOL_POINTS: Int = 80
        get() {
            return Preferences.getInt(Constants.PREF_GOL_POINTS, field)
        }

    var MODE = "medium" //todo
        get() {
            return Preferences.getString(Constants.PREF_MODE, field)
        }
}
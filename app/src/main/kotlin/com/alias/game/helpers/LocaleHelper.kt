package com.example.pantanima.ui.helpers

import android.content.res.Resources
import com.example.pantanima.R
import com.example.pantanima.ui.Constants
import java.util.*

object LocaleHelper {

    private fun toLanguageCode(res: Resources?, language: String?): String {
        return when (language) {
            res?.getString(R.string.armenian) -> Constants.LANGUAGE_AM
            res?.getString(R.string.russian) -> Constants.LANGUAGE_RU
            else -> Constants.LANGUAGE_EN
        }
    }

    fun toLanguageWord(res: Resources?, language: String?): String? {
        return when (language) {
            Constants.LANGUAGE_AM -> res?.getString(R.string.armenian)
            Constants.LANGUAGE_RU -> res?.getString(R.string.russian)
            else -> Constants.LANGUAGE_EN
        }
    }

    @Suppress("DEPRECATION")
    fun changeLanguage(res: Resources?, language: String?) {
        val languageCode: String = toLanguageCode(res, language)
        val dm = res?.displayMetrics
        val conf = res?.configuration
        conf?.setLocale(Locale(languageCode.toLowerCase(Locale.ENGLISH)))
        res?.updateConfiguration(conf, dm)
    }
}
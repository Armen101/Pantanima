package com.example.pantanima.ui.database.preference

import android.content.Context
import android.content.SharedPreferences

object Preferences {

    private lateinit var sharedPref: SharedPreferences

    fun init(context: Context) {
        sharedPref = context.getSharedPreferences(PrefConstants.PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun save(KEY_NAME: String, value: String?, async: Boolean = true) {
        val editor = sharedPref.edit()
        editor.putString(KEY_NAME, value)
        completeSaving(editor, async)
    }

    fun save(KEY_NAME: String, value: Int, async: Boolean = true) {
        val editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        completeSaving(editor, async)
    }

    fun save(KEY_NAME: String, value: Boolean, async: Boolean = true) {
        val editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, value)
        completeSaving(editor, async)
    }

    fun save(KEY_NAME: String, value: Long, async: Boolean = true) {
        val editor = sharedPref.edit()
        editor.putLong(KEY_NAME, value)
        completeSaving(editor, async)
    }

    fun getString(KEY_NAME: String, default: String? = null): String? {
        return sharedPref.getString(KEY_NAME, default)
    }

    fun getInt(KEY_NAME: String, default: Int = 0): Int {
        return sharedPref.getInt(KEY_NAME, default)
    }

    fun getLong(KEY_NAME: String, default: Long = 0): Long {
        return sharedPref.getLong(KEY_NAME, default)
    }

    fun getBoolean(KEY_NAME: String, default: Boolean = false): Boolean {
        return sharedPref.getBoolean(KEY_NAME, default)
    }

    private fun completeSaving(editor: SharedPreferences.Editor, async: Boolean) {
        if (async) {
            editor.apply()
        } else {
            editor.commit()
        }
    }
}
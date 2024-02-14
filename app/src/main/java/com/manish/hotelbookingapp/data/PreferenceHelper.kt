package com.manish.hotelbookingapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceHelper {
    private lateinit var main: SharedPreferences
    private const val KEY_SESSION_COUNT = "key_session_count"

    fun initialize(context: Context) {
        main = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun increaseSession() {
        main.edit().putInt(KEY_SESSION_COUNT, getCurrSession() + 1).apply()
    }

    fun getCurrSession() = main.getInt(KEY_SESSION_COUNT, 0)

}
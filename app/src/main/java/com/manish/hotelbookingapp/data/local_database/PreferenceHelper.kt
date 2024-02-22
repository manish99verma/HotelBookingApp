package com.manish.hotelbookingapp.data.local_database

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceHelper {
    private lateinit var main: SharedPreferences
    private const val KEY_SESSION_COUNT = "key_session_count"
    private const val KEY_USERNAME = "key_username"
    private const val KEY_FAVORITES = "favorites"

    fun initialize(context: Context) {
        main = PreferenceManager.getDefaultSharedPreferences(context)
        increaseSession()
    }

    fun increaseSession() {
        main.edit().putInt(KEY_SESSION_COUNT, getCurrSession() + 1).apply()
    }

    fun getCurrSession() = main.getInt(KEY_SESSION_COUNT, 0)

    fun saveUserName(name: String) {
        main.edit().putString(KEY_USERNAME, name).apply()
    }

    fun getUserName(): String? {
        return main.getString(KEY_USERNAME, null)
    }

    fun updateFavorites(favorites: Set<String>) {
        main.edit().putStringSet(KEY_FAVORITES, favorites).apply()
    }

    fun getFavorites(): MutableSet<String>? = main.getStringSet(KEY_FAVORITES, null)

}
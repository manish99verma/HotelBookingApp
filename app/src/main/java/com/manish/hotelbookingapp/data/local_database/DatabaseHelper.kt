package com.manish.hotelbookingapp.data.local_database

import androidx.lifecycle.LiveData
import com.manish.hotelbookingapp.data.model.hotel_search.Property

class DatabaseHelper(daoHotelsDao: HotelsDao) {
    private var database: HotelsDao = daoHotelsDao

    init {
        curr = this
    }

    suspend fun getFavoritesList(): List<Property> {
        return database.getFavoritesList()
    }

    suspend fun addToFavorites(property: Property) {
        database.addToFavorites(property)
    }

    suspend fun removeFromFavorites(property: Property) {
        database.deleteFromFavorites(property)
    }

    companion object {
        private var curr: DatabaseHelper? = null
        fun getInstance(): DatabaseHelper {
            return curr!!
        }
    }
}
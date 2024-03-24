package com.manish.hotelbookingapp.data.local_database

import androidx.lifecycle.LiveData
import com.manish.hotelbookingapp.data.model.BookedHotel
import com.manish.hotelbookingapp.data.model.hotel_search.Property

class DatabaseHelper(daoHotelsDao: HotelsDao) {
    private var database: HotelsDao = daoHotelsDao

    init {
        curr = this
    }

    suspend fun getFavoritesList(): List<Property> {
        return database.getFavoritesList()
    }

    fun getFavoritesListAsync(): LiveData<List<Property>> {
        return database.getFavoritesListAsync()
    }

    suspend fun addToFavorites(property: Property) {
        database.addToFavorites(property)
    }

    suspend fun removeFromFavorites(property: Property) {
        database.deleteFromFavorites(property)
    }

    suspend fun addToBookedHotels(hotel: BookedHotel) {
        database.addToBookedHotelsList(hotel)
    }

    fun getBookedHotelsList(): LiveData<List<BookedHotel>> {
        return database.getAllBookedHotels()
    }

    companion object {
        private var curr: DatabaseHelper? = null
        fun getInstance(): DatabaseHelper {
            return curr!!
        }
    }
}
package com.manish.hotelbookingapp.data.local_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manish.hotelbookingapp.data.model.hotel_search.Property

@Dao
interface HotelsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(property: Property)

    @Delete
    suspend fun deleteFromFavorites(property: Property)

    @Query("SELECT * FROM hotel_search_result_property")
    suspend fun getFavoritesList(): List<Property>

    @Query("SELECT * FROM hotel_search_result_property")
    fun getFavoritesListAsync(): LiveData<List<Property>>
}
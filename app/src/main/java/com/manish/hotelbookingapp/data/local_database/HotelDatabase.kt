package com.manish.hotelbookingapp.data.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.manish.hotelbookingapp.data.model.BookedHotel
import com.manish.hotelbookingapp.data.model.hotel_search.Property

@Database(entities = [Property::class, BookedHotel::class], exportSchema = false, version = 1)
@TypeConverters(MyConverter::class)
abstract class HotelDatabase : RoomDatabase() {
    abstract fun getHotelsDao(): HotelsDao
}
package com.manish.hotelbookingapp

import android.app.Application
import androidx.room.Room
import com.manish.hotelbookingapp.data.local_database.DatabaseHelper
import com.manish.hotelbookingapp.data.local_database.HotelDatabase
import com.manish.hotelbookingapp.data.local_database.HotelsDao
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HotelBookingApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val database = Room.databaseBuilder(
            applicationContext,
            HotelDatabase::class.java,
            "hotel_database"
        ).build()
        DatabaseHelper(database.getHotelsDao())
    }
}
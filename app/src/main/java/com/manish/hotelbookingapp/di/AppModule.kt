package com.manish.hotelbookingapp.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.manish.hotelbookingapp.HotelBookingApp
import com.manish.hotelbookingapp.data.HotelRepository
import com.manish.hotelbookingapp.data.HotelRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideHotelRepository(): HotelRepositoryInterface {
        return HotelRepository()
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun providesMainApplicationInstance(@ApplicationContext context: Context): HotelBookingApp {
        return context as HotelBookingApp
    }
}
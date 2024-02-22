package com.manish.hotelbookingapp.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.manish.hotelbookingapp.BuildConfig
import com.manish.hotelbookingapp.HotelBookingApp
import com.manish.hotelbookingapp.data.web_server.DefaultHeader
import com.manish.hotelbookingapp.data.web_server.HotelRepository
import com.manish.hotelbookingapp.data.web_server.HotelRepositoryInterface
import com.manish.hotelbookingapp.data.web_server.HotelsApiService
import com.manish.hotelbookingapp.ui.activities.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.HOTELS_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(DefaultHeader())
        }.build()
    }

    @Provides
    @Singleton
    fun providesHotelApiService(retrofit: Retrofit): HotelsApiService {
        return retrofit.create(HotelsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHotelRepository(hotelsApiService: HotelsApiService): HotelRepositoryInterface {
        return HotelRepository(hotelsApiService)
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
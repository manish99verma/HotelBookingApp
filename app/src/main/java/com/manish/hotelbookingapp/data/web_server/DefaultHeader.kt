package com.manish.hotelbookingapp.data.web_server

import com.manish.hotelbookingapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class DefaultHeader : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val requestBuilder: Request.Builder = originalRequest.newBuilder()
            .header("X-RapidAPI-Key", BuildConfig.X_API_KEY)
            .header("X-RapidAPI-Host", BuildConfig.X_API_HOST)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}
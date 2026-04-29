package com.airei.app.phc.attendance.api

import android.annotation.SuppressLint
import android.util.Log
import com.airei.app.phc.attendance.api.ApiDetails.PLANTATION_API
import com.airei.app.phc.attendance.common.AppPreferences
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TAG = "NetworkModule"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        // ✅ Compute baseUrl dynamically every time Retrofit is provided
        val apiType = AppPreferences.apiType

        val baseUrl = if (apiType == ApiDetails.PLANTATION_API)
            AppPreferences.apiClientList.find { it.isSelect }?.getFullLink()
                ?: ApiDetails.PLANTATION_BASE_URL
        else
            ApiDetails.MILL_BASE_URL

        // ✅ Log base URL for debugging
        Log.e(TAG, "Using Base URL: $baseUrl (API Type: $apiType)")

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}


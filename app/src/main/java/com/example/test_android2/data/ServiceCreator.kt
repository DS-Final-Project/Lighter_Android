package com.example.test_android2.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    //서버에서 준 URL 입력
    private const val BASE_URL = ""

    private val userRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            build()
        }

    val userService: UserService = userRetrofit.create(UserService::class.java)
}
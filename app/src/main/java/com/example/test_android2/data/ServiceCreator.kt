package com.example.test_android2.data

import com.example.test_android2.EmailInterceptor
import com.example.test_android2.LighterApplication
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.*

object ServiceCreator {
    //서버에서 준 URL 입력
    private const val BASE_URL = "http://34.22.107.140:8080"

    private val userRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100,TimeUnit.SECONDS)
        .writeTimeout(100,TimeUnit.SECONDS)
        .run {
            addInterceptor(EmailInterceptor())
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            build()
        }

    val tokenService: TokenService = userRetrofit.create(TokenService::class.java)
    val userService: UserService = userRetrofit.create(UserService::class.java)
    val chatService: ChatService = userRetrofit.create(ChatService::class.java)
    val testService: TestService = userRetrofit.create(TestService::class.java)
    val solutionService: SolutionService = userRetrofit.create(SolutionService::class.java)
    val solutionDetailService: SolutionDetailService = userRetrofit.create(SolutionDetailService::class.java)
}
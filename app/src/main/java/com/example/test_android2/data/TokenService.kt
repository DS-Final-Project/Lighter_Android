package com.example.test_android2.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {
    @POST("/google/login/redirect")
    fun tokenResult(@Body Token: TokenData): Call<ResponseToken>
}
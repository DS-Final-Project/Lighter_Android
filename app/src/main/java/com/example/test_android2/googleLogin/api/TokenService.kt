package com.example.test_android2.googleLogin.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface TokenService {
    @POST("/google/login/redirect")
    fun tokenResult(@Body Token: TokenData): Call<ResponseToken>

    // 회원 탈퇴
    @DELETE("/mypage/withdraw")
    fun deleteAccount(): Call<Void>
}
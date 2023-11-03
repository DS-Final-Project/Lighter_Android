package com.example.test_android2

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/signup")
    fun addUser(@Body userInfo: UserData): Call<ResponseData>

    @POST("/login")
    fun loginUser(@Body userInfo: UserData): Call<ResponseData>
}
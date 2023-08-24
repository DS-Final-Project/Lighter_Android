package com.example.test_android2.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatService {
    @POST("/chatupload/file")
    fun uploadChat(@Body chatInfo: ChatData): Call<ResponseChat>

    @POST("/chatupload/img")
    fun uploadChatImage(@Body chatInfo: ChatData): Call<ResponseChat>
}
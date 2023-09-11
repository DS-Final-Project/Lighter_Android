package com.example.test_android2.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatService {
    @POST("/chatupload/file")
    fun uploadChat(@Body chatInfo: ChatData): Call<ResponseChat>

    @POST("/chatupload/img")
    fun uploadChatImage(@Body chatInfo: ChatData): Call<ResponseChat>

    @GET("/mypage")
    fun getItems(): Call<ResponseItem>

    @GET("/mypage/chatResult/{chatId}")
    fun getChatResultData(@Path("chatId") chatId: String): Call<ResponseChat>
}
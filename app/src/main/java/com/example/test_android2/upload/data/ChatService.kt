package com.example.test_android2.upload.data

import com.example.test_android2.analysisresult.data.ResponseChat
import com.example.test_android2.mypage.data.ResponseItem
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ChatService {
    @Multipart
    @POST("/chatupload/file")
    fun uploadChat(
        @Part file: MultipartBody.Part, @Part("relation") relation: okhttp3.RequestBody
    ): Call<ResponseChat>

    @Multipart
    @POST("/chatupload/img")
    fun uploadChatImage(
        @Part image: MultipartBody.Part, @Part("relation") relation: okhttp3.RequestBody
    ): Call<ResponseChat>

    @GET("/mypage")
    fun getItems(): Call<ResponseItem>

    @GET("/mypage/chatResult/{chatId}")
    fun getChatResultData(@Path("chatId") chatId: String): Call<ResponseChat>
}
package com.example.test_android2.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TestService {
    @POST("/selftest/post")
    fun testResult(@Header("email") email: String, @Body testInfo: TestResultData): Call<ResponseTest>
}
package com.example.test_android2.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TestService {
    @POST("/selftest/post")
    fun testResult(@Body testInfo: TestResultData): Call<ResponseTest>
}
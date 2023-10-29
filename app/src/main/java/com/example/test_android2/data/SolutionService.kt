package com.example.test_android2.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SolutionService {
    @GET("/solution")
    fun getSolution(): Call<ResponseSolution>
}
package com.example.test_android2.info.data

import retrofit2.Call
import retrofit2.http.GET

interface SolutionService {
    @GET("/solution")
    fun getSolution(): Call<ResponseSolution>
}
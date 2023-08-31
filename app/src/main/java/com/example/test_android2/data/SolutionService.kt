package com.example.test_android2.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SolutionService {
    @POST("/solution")
    fun getSolution(@Body solutionInfo: SolutionData): Call<ResponseSolution>
}
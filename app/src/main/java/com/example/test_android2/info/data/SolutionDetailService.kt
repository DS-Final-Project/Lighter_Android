package com.example.test_android2.info.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SolutionDetailService {
    @GET("/solution/detail")
    fun getSolutionDetail(@Query("solutionId") solutionId: String): Call<ResponseSolutionDetail>
}
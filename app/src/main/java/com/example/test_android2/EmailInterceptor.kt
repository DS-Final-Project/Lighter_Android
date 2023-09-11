package com.example.test_android2

import okhttp3.Interceptor
import okhttp3.Response

class EmailInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val email = LighterApplication.getEmail()

        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("email", email)
            .build()
        return chain.proceed(newRequest)
    }
}
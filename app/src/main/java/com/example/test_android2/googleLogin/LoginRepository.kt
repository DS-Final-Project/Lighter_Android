package com.example.test_android2.googleLogin

import android.util.Log
import com.example.test_android2.googleLogin.model.LoginGoogleRequestModel
import com.example.test_android2.googleLogin.model.LoginGoogleResponseModel
import com.example.test_android2.googleLogin.model.SendAccessTokenModel
import com.example.test_android2.googleLogin.api.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {
    private val getAccessTokenBaseUrl = "https://www.googleapis.com"
    private val sendAccessTokenBaseUrl = "http://34.217.28.173:8080/google/login/redirect/"

    fun getAccessToken(authCode:String) {
        LoginService.loginRetrofit(getAccessTokenBaseUrl).getAccessToken(
            request = LoginGoogleRequestModel(
                grant_type = "authorization_code",
                client_id = "945444302382-7b29vcb2rdqga8cmrnat5r48i4ri7ndu.apps.googleusercontent.com",
                code = authCode.orEmpty()
            )
        ).enqueue(object : Callback<LoginGoogleResponseModel> {
            override fun onResponse(call: Call<LoginGoogleResponseModel>, response: Response<LoginGoogleResponseModel>) {
                if(response.isSuccessful) {
                    val accessToken = response.body()?.access_token.orEmpty()
                    Log.d(TAG, "accessToken: $accessToken")
                    sendAccessToken(accessToken)
                }
            }
            override fun onFailure(call: Call<LoginGoogleResponseModel>, t: Throwable) {
                Log.e(TAG, "getOnFailure: ",t.fillInStackTrace() )
            }
        })
    }

    fun sendAccessToken(accessToken:String){
        LoginService.loginRetrofit(sendAccessTokenBaseUrl).sendAccessToken(
            accessToken = SendAccessTokenModel(accessToken)
        ).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    Log.d(TAG, "sendOnResponse: ${response.body()}")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "sendOnFailure: ${t.fillInStackTrace()}", )
            }
        })
    }

    companion object {
        const val TAG = "LoginRepository"
    }
}
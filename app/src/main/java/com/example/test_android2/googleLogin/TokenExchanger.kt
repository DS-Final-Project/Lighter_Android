package com.example.test_android2.googleLogin

import android.util.Log
import com.example.test_android2.googleLogin.api.LoginService
import com.example.test_android2.googleLogin.model.LoginGoogleRequestModel
import com.example.test_android2.googleLogin.model.LoginGoogleResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenExchanger {
    private val getAccessTokenBaseUrl = "https://www.googleapis.com"

    fun getAccessToken(
        authCode: String,
        onSuccess: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        LoginService.loginRetrofit(getAccessTokenBaseUrl).getAccessToken(
            request = LoginGoogleRequestModel(
                grant_type = "authorization_code",
                client_id = "990677384481-pr02jubof69av8ponrt46eok0c7grmrg.apps.googleusercontent.com",
                code = authCode
            )
        ).enqueue(object : Callback<LoginGoogleResponseModel> {
            override fun onResponse(
                call: Call<LoginGoogleResponseModel>,
                response: Response<LoginGoogleResponseModel>
            ) {
                if (response.isSuccessful) {
                    val accessToken = response.body()?.access_token.orEmpty()
                    Log.d(TAG, "getOnResponse: $accessToken")
                    onSuccess(accessToken)
                } else {
                    onFailure(RuntimeException("액세스 토큰을 가지고 싶은 와중에 구글로부터 실패 응답이 왔습니다. 어서 이 문제를 해결해주세욧!!"))
                }
            }

            override fun onFailure(call: Call<LoginGoogleResponseModel>, t: Throwable) {
                Log.e(TAG, "getOnFailure: ", t.fillInStackTrace())
                onFailure(t)
            }
        })
    }

    companion object {
        const val TAG = "TokenExchanger"
    }
}
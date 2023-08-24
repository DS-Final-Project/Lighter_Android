package com.example.test_android2.googleLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.MainActivity
import com.example.test_android2.R
import com.example.test_android2.data.TokenData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.shobhitpuri.custombuttons.GoogleSignInButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class goo : AppCompatActivity() {

    private val RC_SIGN_IN = 1001
    private var idToken =""
    private val authCode=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_googlelogin)

        val googleSignInButton: GoogleSignInButton = findViewById(R.id.googlelogin)
        googleSignInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("945444302382-7b29vcb2rdqga8cmrnat5r48i4ri7ndu.apps.googleusercontent.com")
            .requestServerAuthCode("945444302382-7b29vcb2rdqga8cmrnat5r48i4ri7ndu.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleSignInResult(task: com.google.android.gms.tasks.Task<GoogleSignInAccount>?) {
        try {
            val account = task?.getResult(ApiException::class.java)
            if (account != null) {
                // 로그인 성공
                val email = account.email
                val displayName = account.displayName
                idToken = account.idToken.toString()

                // 여기서 서버로 idToken 등 필요한 정보를 전송하고 처리할 수 있습니다.
                Log.w(TAG, "email: $email")
                Log.w(TAG, "idToken: $idToken")
                //sendToServer(idToken.orEmpty())

                Toast.makeText(this@goo, "로그인 되었습니다.", Toast.LENGTH_LONG).show()
                val intent = Intent(this@goo, MainActivity::class.java)
                startActivity(intent)
            }
        } catch (e: ApiException) {
            // 로그인 실패
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, "구글 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    fun handleSignInResult2(completedTask: Task<GoogleSignInAccount>) {
        try {
            val authCode: String? =
                completedTask.getResult(ApiException::class.java)?.serverAuthCode
            LoginRepository().getAccessToken(authCode!!)
            if (authCode != null) {
                // 로그인 성공
                //completedTask.getResult(ApiException::class.java)?.serverAuthCode
                //LoginRepository().sendAccessToken(authCode);
                val Token = TokenData(idToken,authCode)
                sendToServer(Token)
                Log.w(TAG, "AccessToken: $authCode")
                Toast.makeText(this@goo, "로그인 되었습니다.", Toast.LENGTH_LONG).show()
                val intent = Intent(this@goo, MainActivity::class.java)
                startActivity(intent)
            }
        } catch (e: ApiException) {
            Log.w(LoginGoogle.TAG, "handleSignInResult: error" + e.statusCode)
        }
    }

    private fun sendToServer(Token: TokenData) {
        GlobalScope.launch(Dispatchers.IO) {
            val okHttpClient = OkHttpClient()

            val json = """{"Token": "$Token"}"""
            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)

            val request = Request.Builder()
                .url("http://34.217.28.173:8080/google/login/redirect")
                .post(requestBody)
                .build()

            try {
                val response = okHttpClient.newCall(request).execute()
                val responseBody = response.body?.string()
                Log.i(TAG, "Signed in as: $responseBody")
            } catch (e: IOException) {
                Log.e(TAG, "Error sending ID token to backend.", e)
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            //handleSignInResult(task)
            handleSignInResult2(task)
        }
    }

    companion object {
        const val TAG = "GoogleLoginActivity"
    }
}

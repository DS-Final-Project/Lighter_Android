package com.example.test_android2.googleLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.shobhitpuri.custombuttons.GoogleSignInButton

class goo : AppCompatActivity() {

    private val RC_SIGN_IN = 1001

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
        .requestIdToken("990677384481-pr02jubof69av8ponrt46eok0c7grmrg.apps.googleusercontent.com")
            .requestServerAuthCode("990677384481-pr02jubof69av8ponrt46eok0c7grmrg.apps.googleusercontent.com")
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
                val idToken = account.idToken
                // 여기서 서버로 idToken 등 필요한 정보를 전송하고 처리할 수 있습니다.
                sendToServer(idToken.orEmpty())
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
        } catch (e: ApiException) {
            Log.w(LoginGoogle.TAG, "handleSignInResult: error" + e.statusCode)
        }
    }

    private fun sendToServer(Token: TokenData) {
        val call: Call<ResponseToken> = ServiceCreator.tokenService.tokenResult(Token)

        call.enqueue(object : Callback<ResponseToken> {
            override fun onResponse(
                call: Call<ResponseToken>, response: Response<ResponseToken>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result?.let {
                        if (it.loginStatus == true) {
                            val intent = Intent(this@goo, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(this@goo, TestStartActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    Log.d("로그인 성공", "$result")
                    Toast.makeText(this@goo, "로그인 되었습니다.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseToken>, t: Throwable) {
                Log.i(TAG,"Network request failed: ${t.message}")
            }
        })
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

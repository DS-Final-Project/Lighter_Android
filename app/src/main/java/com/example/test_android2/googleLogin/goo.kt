package com.example.test_android2.googleLogin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.test_android2.main.LighterApplication
import com.example.test_android2.R
import com.example.test_android2.selftest.ui.TestStartActivity
import com.example.test_android2.googleLogin.api.ResponseToken
import com.example.test_android2.ServiceCreator
import com.example.test_android2.googleLogin.api.TokenData
import com.example.test_android2.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.shobhitpuri.custombuttons.GoogleSignInButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class goo : AppCompatActivity() {

    private val RC_SIGN_IN = 1001
    private var idToken =""

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_googlelogin)

        //라-이터 부분에 색상 적용
        textView = findViewById(R.id.tv_title)

        val tvTitle: String = textView.text.toString()
        val builder = SpannableStringBuilder(tvTitle)
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.highlight_darkest))
        builder.setSpan(colorSpan,19,23,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = builder

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

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask?.getResult(ApiException::class.java)

            val authCode: String? =
                completedTask.getResult(ApiException::class.java)?.serverAuthCode
            LoginRepository().getAccessToken(authCode!!)

            // 로그인 성공
            if (account != null) {
                idToken = account.idToken.toString()
                val email = account.email.toString()
                Log.w(TAG, "email: $email")

                LighterApplication.getInstance()?.userEmail = email
            }

            val Token = TokenData(idToken,authCode)
            sendToServer(Token)
            Log.w(TAG, "IdToken: $idToken")
            Log.w(TAG, "AccessToken: $authCode")
        } catch (e: ApiException) {
            Log.w(TAG, "handleSignInResult: error" + e.statusCode)
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

    private fun isSelfTestDone() {
        val sharedPreferences = getSharedPreferences("SelfTestFlag", Context.MODE_PRIVATE)
        val selfTestDone = sharedPreferences.getBoolean("selfTestDone", false)

            if(selfTestDone){
                val intent = Intent(this@goo, MainActivity::class.java)
                startActivity(intent)
            } else{
                val intent = Intent(this@goo, TestStartActivity::class.java)
                startActivity(intent)
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    companion object {
        const val TAG = "GoogleLoginActivity"
    }
}

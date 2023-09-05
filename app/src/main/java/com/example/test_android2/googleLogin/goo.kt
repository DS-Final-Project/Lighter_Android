package com.example.test_android2.googleLogin

import InfoFragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.MainActivity
import com.example.test_android2.R
import com.example.test_android2.TestStartActivity
import com.example.test_android2.data.ResponseToken
import com.example.test_android2.data.ServiceCreator
import com.example.test_android2.data.TokenData
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

    private lateinit var sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor by lazy { sharedPreferences.edit() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_googlelogin)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

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
                var email = account.email.toString()
                Log.w(TAG, "email: $email")

                //구글아이디 인포로 보내기
                editor.putString("email", email)
                editor.apply()

                //토큰아이디 테스트결과엑티비티로 보내기
                //editor.putString("idToken", idToken)
                //editor.apply()

//                val infoFragment = InfoFragment()
//                val bundle = Bundle()
//                bundle.putString("email", email)
//                infoFragment.arguments = bundle
            }

            if (authCode != null) {
                val Token = TokenData(idToken,authCode)
                sendToServer(Token)
                Log.w(TAG, "IdToken: $idToken")
                Log.w(TAG, "AccessToken: $authCode")
            }
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
                    Log.d("로그인 성공", "$result")
                    Toast.makeText(this@goo, "로그인 되었습니다.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@goo, TestStartActivity::class.java)
                    startActivity(intent)
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
            handleSignInResult(task)
        }
    }

    companion object {
        const val TAG = "GoogleLoginActivity"
    }
}

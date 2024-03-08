package com.example.test_android2.googleLogin

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.test_android2.main.LighterApplication
import com.example.test_android2.R
import com.example.test_android2.googleLogin.api.TokenData
import com.example.test_android2.main.MainActivity
import com.example.test_android2.selftest.ui.TestStartActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginGoogleActivity : AppCompatActivity() {
    private lateinit var loginGoogle: LoginGoogle // LoginGoogle 클래스 인스턴스
    private val RC_SIGN_IN = 1001
    private var idToken:String =""
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logingoogle)

        //라-이터 부분에 색상 적용
        textView = findViewById(R.id.tv_title)
        val tvTitle: String = textView.text.toString()
        val builder = SpannableStringBuilder(tvTitle)
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.highlight_darkest))
        builder.setSpan(colorSpan,19,23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = builder

        // LoginGoogle 인스턴스 생성
        loginGoogle = LoginGoogle(this)

        //val googleSignInButton: GoogleSignInButton = findViewById(R.id.googlelogin)
        val googleSignInButton: Button = findViewById(R.id.googlelogin)
        googleSignInButton.setOnClickListener {
            // signIn 메서드를 호출하여 Google 로그인을 시작합니다.
            loginGoogle.signIn(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
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
                Log.w(LoginGoogle.TAG, "email: $email")

                LighterApplication.getInstance()?.userEmail = email
            }

            val Token = TokenData(idToken,authCode)
            sendToServer(Token)
            Log.w(LoginGoogle.TAG, "IdToken: $idToken")
            Log.w(LoginGoogle.TAG, "AccessToken: $authCode")
        } catch (e: ApiException) {
            Log.w(LoginGoogle.TAG, "handleSignInResult: error" + e.statusCode)
        }
    }

    private fun sendToServer(token: TokenData) {
        LoginRepository().sendToServer(token) { response ->
            response?.let { res ->
                if (res.isSuccessful) {
                    val result = res.body()
                    result?.let {
                        when {
                            it.loginStatus == true -> {
                                val intent = Intent(this@LoginGoogleActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            it.loginStatus == false -> {
                                val intent = Intent(this@LoginGoogleActivity, TestStartActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                // 로그인 실패 처리
                                finish()
                            }
                        }
                    }
                    Log.d("로그인 성공", "$result")
                    Toast.makeText(this@LoginGoogleActivity, "로그인 되었습니다.", Toast.LENGTH_LONG).show()
                } else {
                    // 서버 응답이 실패한 경우 처리
                    Log.i(TAG, "Network request failed")
                }
            }
        }
    }

    companion object {
        const val TAG = "GoogleLoginActivity"
    }
}
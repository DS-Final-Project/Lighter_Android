package com.example.test_android2.googleLogin

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginGoogle(context: Context) {
    val RC_SIGN_IN = 1001

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("945444302382-7b29vcb2rdqga8cmrnat5r48i4ri7ndu.apps.googleusercontent.com")
        .requestServerAuthCode("945444302382-7b29vcb2rdqga8cmrnat5r48i4ri7ndu.apps.googleusercontent.com")
        .requestEmail()
        .build()

    private val googleSignInClient = GoogleSignIn.getClient(context, gso)

    fun signIn(activity: Activity) {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun signOut(context: Context) {
        googleSignInClient.signOut()
            .addOnCompleteListener {
                Toast.makeText(context, "로그아웃 되셨습니다!", Toast.LENGTH_SHORT).show()
            }
    }

    fun isLogin(context: Context): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return if (account == null) false else (true)
    }

    companion object {
        const val TAG = "GoogleLoginService"
    }
}
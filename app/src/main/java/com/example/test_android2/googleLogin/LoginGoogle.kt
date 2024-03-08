package com.example.test_android2.googleLogin

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginGoogle(context: Context) {
    private val RC_SIGN_IN = 1001

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
                Toast.makeText(context, "로그아웃 되었습니다!", Toast.LENGTH_SHORT).show()
            }
    }

    fun disconnect(context: Context, onCompleteListener: () -> Unit) {
        val googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
        googleSignInClient.revokeAccess()
            .addOnCompleteListener { task ->
            if (task.isSuccessful) { // 연동 해제가 성공적으로 완료된 경우
                onCompleteListener.invoke()
                Toast.makeText(context, "회원탈퇴가 완료되었습니다!", Toast.LENGTH_SHORT).show()
            } else { // 연동 해제가 실패한 경우
                Log.e(TAG, "disconnect: Failed to disconnect")
            }
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
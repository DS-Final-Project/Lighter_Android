package com.example.test_android2.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.R
import androidx.databinding.DataBindingUtil
import com.example.test_android2.MainActivity
import com.example.test_android2.data.ResponseData
import com.example.test_android2.data.ServiceCreator
import com.example.test_android2.data.UserData
import com.example.test_android2.databinding.ActivityLoginBinding
import com.example.test_android2.signup.SginUpActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.signUpBt.setOnClickListener(){
            val intent = Intent(this@LoginActivity, SginUpActivity::class.java)
            startActivity(intent)
        }

        loginButtonClickListener()
    }

    private fun loginButtonClickListener() = binding.lgnBt.setOnClickListener {
        val id = binding.lgnId.text.toString().trim()
        val pw = binding.lgnPw.text.toString().trim()
        val userData = UserData(id, pw)
        if (id == "" || pw == "") {
            Toast.makeText(this@LoginActivity, "로그인 정보를 모두 입력해주세요.", Toast.LENGTH_LONG).show()
        } else {
            loginNetwork(userData)
        }
    }

    private fun loginNetwork(userInfo: UserData) {
        val call: Call<ResponseData> = ServiceCreator.userService.loginUser(userInfo)

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(
                call: Call<ResponseData>, response: Response<ResponseData>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("로그인 성공", "$result")
                    Toast.makeText(this@LoginActivity, "로그인 되었습니다.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.d("로그인 실패", t.message.toString())
            }
        })
    }
}
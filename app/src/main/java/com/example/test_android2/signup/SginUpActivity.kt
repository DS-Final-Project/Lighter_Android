package com.example.test_android2.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.test_android2.R
import com.example.test_android2.data.ResponseData
import com.example.test_android2.data.ServiceCreator
import com.example.test_android2.data.UserData
import com.example.test_android2.databinding.ActivitySginUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySginUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sgin_up)

        signUpDoneButtonClickListener()
    }

    private fun signUpDoneButtonClickListener() = binding.btnSignUpDone.setOnClickListener {
        val id = binding.etId.text.toString()
        val pw = binding.etPw.text.toString()
        val userData = UserData(id, pw)
        if (id == "" || pw == "") {
            Toast.makeText(this@SignUpActivity, "회원정보를 모두 입력해주세요.", Toast.LENGTH_LONG).show()
        } else {
            userNetwork(userData)
        }
    }

    //기존에 등록된 id일 경우 조건 추가
    private fun userNetwork(userInfo: UserData) {
        val call: Call<ResponseData> = ServiceCreator.userService.addUser(userInfo)

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(
                call: Call<ResponseData>, response: Response<ResponseData>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("회원가입 성공", "$result")
                    Toast.makeText(this@SignUpActivity, "가입 완료 되었습니다.", Toast.LENGTH_LONG).show()
                    //val intent = Intent(this@SignUpActivity, LogInActivity::class.java)
                    //startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.d("회원가입 실패", t.message.toString())
            }
        })
    }

}
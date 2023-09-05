package com.example.test_android2

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.data.*
import com.example.test_android2.databinding.ActivityTestresultBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestresultBinding
    var avoidScore=0F
    var anxietyScore=0F
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestresultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.iv.setImageResource(R.drawable.ic_test_result)

        // Intent에서 점수 값을 받아옴
        avoidScore = intent.getFloatExtra("avoidScore", 0F)
        anxietyScore = intent.getFloatExtra("anxietyScore", 0F)

        // 회피점수와 불안점수 계산
        avoidScore = avoidScore/18
        anxietyScore = anxietyScore/18

        // 결과 계산
        val testType = when {
            avoidScore < 2.33 && anxietyScore < 2.61 -> 1
            avoidScore < 2.33 && anxietyScore >= 2.61 -> 2
            avoidScore >= 2.33 && anxietyScore < 2.61 -> 3
            avoidScore >= 2.33 && anxietyScore >= 2.61 -> 4
            else -> -1
        }

        val testResult = TestResultData(avoidScore,anxietyScore,testType)

        binding.btn.setOnClickListener {
            testNetwork(testResult)
            Log.i(TAG, "avoidScore: $avoidScore")
            Log.i(TAG, "anxietyScore: $anxietyScore")
            Log.i(TAG, "testType: $testType")
        }
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", "").toString()

    }

    private fun testNetwork(testInfo: TestResultData) {
        if (email != null) {
            val call: Call<ResponseTest> = ServiceCreator.testService.testResult(email, testInfo)

            call.enqueue(object : Callback<ResponseTest> {
                override fun onResponse(
                    call: Call<ResponseTest>, response: Response<ResponseTest>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d("자가진단 성공", "$result")
                        val intent = Intent(this@TestResultActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<ResponseTest>, t: Throwable) {
                    Log.i(TAG,"Network request failed: ${t.message}")
                }
            })
        }else {
            // 토큰이 null인 경우 처리
            Log.d("자가진단 결과 전송 실패/ email:", "$email")
        }
    }
}

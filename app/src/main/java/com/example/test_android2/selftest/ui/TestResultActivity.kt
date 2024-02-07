package com.example.test_android2.selftest.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.main.MainActivity
import com.example.test_android2.R.drawable
import com.example.test_android2.ServiceCreator
import com.example.test_android2.databinding.ActivityTestresultBinding
import com.example.test_android2.selftest.data.ResponseTest
import com.example.test_android2.selftest.data.TestResultData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestresultBinding
    var avoidScore=0F
    var anxietyScore=0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestresultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.iv.setImageResource(drawable.ic_test_result)

        // Intent에서 점수 값을 받아옴
        avoidScore = intent.getFloatExtra("avoidScore", 0F)
        anxietyScore = intent.getFloatExtra("anxietyScore", 0F)

        // 회피점수와 불안점수 계산
        avoidScore /= 18
        anxietyScore /= 18

        // 결과 계산
        val testType = when {
            avoidScore < 2.33 && anxietyScore < 2.61 -> 1 // 안정형
            avoidScore < 2.33 && anxietyScore >= 2.61 -> 2 //불안형
            avoidScore >= 2.33 && anxietyScore < 2.61 -> 3 // 거부회피형
            avoidScore >= 2.33 && anxietyScore >= 2.61 -> 4 //공포회피형
            else -> -1
        }

        val testResult = TestResultData(avoidScore,anxietyScore,testType)

        binding.btn.setOnClickListener {
            testNetwork(testResult)
            Log.i(TAG, "avoidScore: $avoidScore")
            Log.i(TAG, "anxietyScore: $anxietyScore")
            Log.i(TAG, "testType: $testType")
        }

    }

    private fun testNetwork(testInfo: TestResultData) {
        val call: Call<ResponseTest> = ServiceCreator.testService.testResult(testInfo)

        call.enqueue(object : Callback<ResponseTest> {
            override fun onResponse(
                call: Call<ResponseTest>, response: Response<ResponseTest>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("자가진단 성공", "$result")

                    updateSharedPreferences()

                    val intent = Intent(this@TestResultActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseTest>, t: Throwable) {
                Log.i(TAG, "Network request failed: ${t.message}")
            }
        })
    }

    private fun updateSharedPreferences() {
        val sharedPreferences = getSharedPreferences("SelfTestFlag", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("selfTestDone", true)
        editor.apply()
    }
}

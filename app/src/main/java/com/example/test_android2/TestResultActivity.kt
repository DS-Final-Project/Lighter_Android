package com.example.test_android2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.data.*
import com.example.test_android2.databinding.ActivityTestresultBinding
import com.example.test_android2.googleLogin.LoginGoogle.Companion.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class TestResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestresultBinding
    var avoidScore=0F
    var anxietyScore=0F
    lateinit var intentResult: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestresultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.iv.setImageResource(R.drawable.ic_test_result)

        intentResult = Intent(this, MainActivity::class.java)

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

    }

    private fun testNetwork(testInfo: TestResultData) {
        val call: Call<ResponseTest> = ServiceCreator.testService.testResult(testInfo)

        call.enqueue(object : Callback<ResponseTest> {
            override fun onResponse(
                call: Call<ResponseTest>, response: Response<ResponseTest>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        startActivity(intentResult)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseTest>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

}
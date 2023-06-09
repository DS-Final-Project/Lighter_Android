package com.example.test_android2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.test_android2.data.ResponseChat
import com.example.test_android2.data.ServiceCreator
import com.example.test_android2.databinding.ActivityResultAnalysisBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultAnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultAnalysisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val resultNum = intent.getStringExtra("resultNum")

        if (resultNum != null) {
            val percent = "$resultNum%"
            binding.tvPercent.text = percent
        }
        else{
            Toast.makeText(this@ResultAnalysisActivity, "결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
package com.example.test_android2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.databinding.ActivityTeststartBinding

class TestStartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeststartBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeststartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iv.setImageResource(R.drawable.ic_test_start)

        binding.skipBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btn.setOnClickListener {
            sharedPreferences = getSharedPreferences("SelfTestFlag", Context.MODE_PRIVATE)
            editor = sharedPreferences.edit()
            editor.putBoolean("selfTestDone", false); // 새로운 멤버 정보
            editor.apply();

            val intent = Intent(this, TestActivity0::class.java)
            startActivity(intent)
        }
    }
}
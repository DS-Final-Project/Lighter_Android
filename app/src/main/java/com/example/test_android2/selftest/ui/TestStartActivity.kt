package com.example.test_android2.selftest.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.main.MainActivity
import com.example.test_android2.R.drawable
import com.example.test_android2.databinding.ActivityTeststartBinding

class TestStartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeststartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeststartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iv.setImageResource(drawable.ic_test_start)

        binding.skipBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btn.setOnClickListener {
            val intent = Intent(this, TestActivity0::class.java)
            startActivity(intent)
        }
    }
}
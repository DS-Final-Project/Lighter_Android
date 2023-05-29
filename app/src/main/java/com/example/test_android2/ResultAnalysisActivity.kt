package com.example.test_android2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test_android2.databinding.ActivityResultAnalysisBinding

class ResultAnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultAnalysisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
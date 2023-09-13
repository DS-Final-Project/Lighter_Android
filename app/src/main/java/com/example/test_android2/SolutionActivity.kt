package com.example.test_android2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.data.Solution
import com.example.test_android2.databinding.ActivitySolutionBinding

class SolutionActivity : AppCompatActivity()  {

    private lateinit var binding: ActivitySolutionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySolutionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("mysolution")) {
            val mysolution = intent.getParcelableExtra<Solution>("mysolution")
            val solutionId = mysolution?.solutionId
            val relation = mysolution?.relation
            val keyword = mysolution?.keyword
            val solutionTitle = mysolution?.solutionTitle
            val solutionContent = mysolution?.solutionContent

            with(binding){
                tvTitle.text = solutionTitle

            }

        } else {
            Toast.makeText(this@SolutionActivity, "결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}

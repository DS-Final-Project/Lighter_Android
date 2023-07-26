package com.example.test_android2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.test_android2.R.color
import com.example.test_android2.data.Chat
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
        //val resultNum = "67"
        if (intent.hasExtra("mychat")) {
            val mychat = intent.getParcelableExtra<Chat>("mychat")
            val resultNum = mychat?.resultNum
            val avoidScore = mychat?.avoidScore
            val anxietyScore = mychat?.anxietyScore
            val doubtText1 = mychat?.doubtText1
            val doubtText2 = mychat?.doubtText2
            val doubtText3 = mychat?.doubtText3
            val doubtText4 = mychat?.doubtText4
            val doubtText5 = mychat?.doubtText5
            val testType = mychat?.testType

            //가스라이팅 퍼센트
            if (resultNum != null) {
                val percent = "$resultNum%" //val percent = "67%"
                with(binding) {
                    tvPercent.text = percent
                    progressbar.progress = resultNum
                }
                if (resultNum <= 50) {
                    with(binding) {
                        icNotice.setImageResource(R.drawable.ic_success)
                        tvNotice.text = "안전"
                        tvSubNotice.text = "정상적인 대화로 판단됩니다."
                        layoutNotice.background.setTint(getColor(color.support_success_light))
                        //결과가 정상일 경우 가스라이팅 의심 문장 안보이게
                        layoutSentence.visibility = View.GONE
                    }
                }
                if (resultNum in 51..80) {
                    with(binding) {
                        icNotice.setImageResource(R.drawable.ic_warning)
                        tvNotice.text = "주의"
                        tvSubNotice.text = "가스라이팅이 의심됩니다."
                        layoutNotice.background.setTint(getColor(color.support_warning_light))
                    }
                }
                if (resultNum > 80) {
                    with(binding) {
                        icNotice.setImageResource(R.drawable.ic_danger)
                        tvNotice.text = "위험"
                        tvSubNotice.text = "가스라이팅으로 판단됩니다."
                        layoutNotice.background.setTint(getColor(color.support_error_light))
                    }
                }
            } else {
                Toast.makeText(this@ResultAnalysisActivity, "결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }

            //나머지 결과
            with(binding){
                tvDoubt1.text = doubtText1
                tvDoubt2.text = doubtText2
                tvDoubt3.text = doubtText3
                tvDoubt4.text = doubtText4
                tvDoubt5.text = doubtText5
                tvAvoidScore.text = avoidScore.toString()
                tvAnxietyScore.text = anxietyScore.toString()
            }

            //자가진단 테스트 유형
            when (testType) {
                1 -> binding.tvType.text = "안정 유형"
                2 -> binding.tvType.text = "몰입 유형"
                3 -> binding.tvType.text = "거부적 회피 유형"
                4 -> binding.tvType.text = "공포형 회피 유형"
                else -> binding.tvType.text = "유형 없음"
            }
        }

        initButtonClickEvent()

    }

    //확인 버튼 클릭 시 이전 화면(채팅 등록)으로 이동
    private fun initButtonClickEvent() {
        binding.btnOk.setOnClickListener {
            finish()
        }
    }

}

package com.example.test_android2.analysisresult.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.test_android2.R.color
import com.example.test_android2.R.drawable
import com.example.test_android2.analysisresult.data.Chat
import com.example.test_android2.databinding.ActivityResultAnalysisBinding

class ResultAnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultAnalysisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("mychat")) {
            val mychat = intent.getParcelableExtra<Chat>("mychat")
            mychat?.let { fetchAnalysisReusltData(it) }
        } else {
            showErrorToast()
        }

        initButtonClickEvent()

    }

    private fun fetchAnalysisReusltData(mychat: Chat) {
        with(binding) {
            with(mychat) {
                val percent = "$resultNum%"
                tvPercent.text = percent
                progressbar.progress = resultNum ?: 0

                //가스라이팅 퍼센트
                resultNum?.let {
                    when {
                        it <= 50 -> showSafeResult()
                        it in 51..80 -> showWarningResult(relation)
                        it > 80 -> showDangerResult(relation)
                        else -> showErrorToast()
                    }
                }

                tvDoubt1.text = doubtText1
                tvDoubt2.text = doubtText2
                tvDoubt3.text = doubtText3
                tvDoubt4.text = doubtText4
                tvDoubt5.text = doubtText5
                tvAvoidScore.text = avoidScore.toString()
                tvAnxietyScore.text = anxietyScore.toString()

                showTestType(testType)
            }
        }
    }

    private fun showSafeResult() {
        with(binding) {
            icNotice.setImageResource(drawable.ic_success)
            tvNotice.text = "안전"
            tvSubNotice.text = "정상적인 대화로 판단됩니다."
            layoutNotice.background.setTint(getColor(color.support_success_light)) //결과가 정상일 경우 가스라이팅 의심 문장/솔루션 말풍선 안보이게
            layoutSentence.isVisible = false
            layoutSolution.isVisible = false
            tvSolution.isVisible = false
        }
    }

    private fun showWarningResult(relation: Int?) {
        with(binding) {
            icNotice.setImageResource(drawable.ic_warning)
            tvNotice.text = "주의"
            tvSubNotice.text = "가스라이팅이 의심됩니다."
            layoutNotice.background.setTint(getColor(color.support_warning_light))
            relation?.let { showSolution(it) }
        }
    }

    private fun showDangerResult(relation: Int?) {
        with(binding) {
            icNotice.setImageResource(drawable.ic_danger)
            tvNotice.text = "위험"
            tvSubNotice.text = "가스라이팅으로 판단됩니다."
            layoutNotice.background.setTint(getColor(color.support_error_light))
            relation?.let { showSolution(it) }
        }
    }

    //자가진단 테스트 유형
    private fun showTestType(testType: Int?) {
        binding.tvType.text = when (testType) {
            1 -> "안정 유형"
            2 -> "몰입 유형"
            3 -> "거부적 회피 유형"
            4 -> "공포형 회피 유형"
            else -> "유형 없음"
        }
    }

    //분석 결과 주의/위험일 경우 솔루션 말풍선 제공
    private fun showSolution(relation: Int) {
        val title: String
        val content: String

        when (relation) {
            1 -> {
                title = "연인 간 가스라이팅이 의심됩니다.\n"
                content =
                    "애인의 눈치를 보는 일이 많은가요? ‘내가 예민한가?” 생각하며 스스로를 검열하나요? 혼자 고민하지 말고 상대에게 내 생각, 감정을 표현해 보세요. 연인 간 소통은 가장 중요한 요소입니다."
            }
            2 -> {
                title = "친구 간 가스라이팅이 의심됩니다.\n"
                content = "친구와 대화할 때 사과하는 일이 많은가요? 상황을 객관적으로 보며 자신의 잘못이 아니라는 걸 스스로 상기해 보세요. 친구와 논쟁하지 말고 자신을 믿어야 해요!"
            }
            3 -> {
                title = "가족 간 가스라이팅이 의심됩니다.\n"
                content = "가족과 대화할 때 자존감이 떨어지고 불안한가요? 가족도 중요하지만 자신의 감정을 우선시하세요. 가족 간에도 건강한 경계를 설정하는 것이 중요합니다."
            }
            4 -> {
                title = "동료 간 가스라이팅이 의심됩니다.\n"
                content = "상사 혹은 동료에게 이유 없는 질책 또는 부정적인 피드백을 받나요? 반박하기 어려운 상대라면 피하는 것도 방법입니다. 상대에게 선을 확실히 그어보세요."
            }
            else -> return
        }

        binding.tvSolutionTitle.text = title
        binding.tvSolutionContent.text = content

    }

    private fun showErrorToast() {
        Toast.makeText(this, "결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
    }

    //확인 버튼 클릭 시 이전 화면(채팅 등록)으로 이동
    private fun initButtonClickEvent() {
        binding.btnOk.setOnClickListener {
            finish()
        }
    }
}

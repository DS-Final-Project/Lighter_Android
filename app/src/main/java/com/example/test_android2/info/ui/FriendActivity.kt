package com.example.test_android2.info.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.R.id
import com.example.test_android2.databinding.ActivitySolutionBinding
import com.example.test_android2.info.data.Solution
import com.example.test_android2.info.data.SolutionDetail

class FriendActivity : AppCompatActivity()  {

    private lateinit var binding: ActivitySolutionBinding
    lateinit var sDetail: SolutionDetail
    lateinit var solutionTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySolutionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val closeXImageView = findViewById<ImageView>(id.closeX)
        closeXImageView.setOnClickListener {
            finish() // 현재 엑티비티를 종료하여 전 엑티비티로 돌아감
        }

        sDetail = SolutionDetail(
            solutionTitle1 = "점진적 페이드아웃",
            solutionContent1 = "만남과 연락을 점차 줄이며 우정이 자연스럽게 끝나도록 합시다. 당신이 갈등회피 성향이거나 상대가 당신의 말을 수용하지 않는 편이라면 조금씩 관계를 마르게 하는 방법이 적절합니다. 전화 대신 문자를 보내고, 상대의 SNS 활동에 반응하지 말고, 연락 답장에 텀을 오래 두어 대화 자체를 줄이는 것이 좋습니다.",
            solutionTitle2 = "솔직한 대화",
            solutionContent2 = "점진적인 페이드아웃이 적절하지 않거나 전혀 효과가 없는 것 같다면 친구와 솔직하게 대화하는 것도 방법입니다. 대화는 빠른 손절도 가능하지만 예상 외로 친구가 자기 문제를 깨닫고 고쳐서 우정이 드라마틱하게 회복될지도 모릅니다.",
            solutionTitle3 = "휴식의 시간",
            solutionContent3 = "대화를 나누는 것도 싫고, 그렇다고 관계를 지속하면 스트레스가 심하고, 단번에 손절할 용기도 없다면 휴식의 시간을 가져보세요. 혼자만의 시간이 필요하다고 통보하고 연락을 하지 않고 지내는 겁니다.",
            solutionTitle4 = "단호한 절교 선언",
            solutionContent4 = "당신에게 스트레스를 주고 선을 넘는 유해한 친구가 있다면 즉시 절교 선언을 하는 것이 나을 수 있습니다. “서로 연락하지 않고 각자의 인생을 살아갔으면 좋겠어” 또는 “우리는 서로 맞지 않는 것 같아” 라고 단호히 말하며 관계를 끝내세요. 친구가 가스라이팅, 정서적학대, 비교하는 말 등으로 당신을 힘들게 한다면 왜 우정을 끊는지 설명할 필요 없이 즉시 절연하는게 좋습니다."
        )

        if (intent.hasExtra("clickSolution")) {
            val mysolution = intent.getParcelableExtra<Solution>("clickSolution")
            val solutionIdString = mysolution?.solutionId.toString()
            solutionTitle = mysolution?.solutionTitle.toString()

            with(binding) {
                tvTitle.text = solutionTitle // 또는 원하는 필드 사용

                title1.text = sDetail.solutionTitle1
                title2.text = sDetail.solutionTitle2
                title3.text = sDetail.solutionTitle3
                title4.text = sDetail.solutionTitle4

                body1.text = sDetail.solutionContent1
                body2.text = sDetail.solutionContent2
                body3.text = sDetail.solutionContent3
                body4.text = sDetail.solutionContent4
            }

        } else {
            Toast.makeText(this@FriendActivity, "결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}

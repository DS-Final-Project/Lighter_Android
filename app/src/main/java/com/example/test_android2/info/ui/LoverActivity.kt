package com.example.test_android2.info.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.R.id
import com.example.test_android2.databinding.ActivitySolutionBinding
import com.example.test_android2.info.data.Solution
import com.example.test_android2.info.data.SolutionDetail

class LoverActivity : AppCompatActivity()  {

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
            solutionTitle1 = "갈등을 받아들여라!",
            solutionContent1 = "갈등이 있는 것 자체에 문제가 있다고 생각하고 부정적으로 접근하는 경우가 많습니다. 갈등이 있는 것은 자연스러운 일이며 피할 수 없는 과정입니다. 중요한 것은 이 과정이 어떻게 지나가게하는가입니다. “올 것이 왔구나!” 생각하고, 더욱 관계가 깊어지고 성숙해지는 기회로 만드시기 바랍니다.",
            solutionTitle2 = "갈등을 직면하라!",
            solutionContent2 = "많은 커플들이 갈등이 있으면서 관계가 깨질까 두려워 문제를 꺼내지 않는 경우가 많습니다. 또한, 갈등을 힘들어하는 개인적인 취약함도 있을 수도 있습니다. 그러나 힘든 마음을 숨기고 회피나 억압하면 관계가 악화되고 더욱 큰 위기가 될 수 있습니다. 서로 관계 회복의 의지와 믿음을 가지고 솔직하고 진정성 있게 갈등을 해결하고자 노력하시기 바랍니다.",
            solutionTitle3 = "대화로 해결하라!",
            solutionContent3 = "어떤 관계든 갈등을 해결하는 가장 좋은 방법은 대화입니다. 대화시 마음가짐과 태도는, 상대방의 입장을 이해하려는 마음을 갖는것입니다. ",
            solutionTitle4 = "타협점을 찾아라!",
            solutionContent4 = "대화를 통해 서로의 입장을 충분히 들었다면, 타협점을 찾아야 합니다. 타협점이란, 한쪽 편의 요구와 생각에 일방적으로 따라가는 것이 아니라, 서로 원하는 바가 담겨있는 중간 지점을 말합니다. 서로 조금씩 양보하고 대화를 통해 중간지점을 찾아야합니다. 이때 때로는 서로의 잘못과 한계를 포용하고 용서가 필요할 수 있습니다."
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
            Toast.makeText(this@LoverActivity, "결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}

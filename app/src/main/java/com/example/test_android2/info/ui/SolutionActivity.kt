package com.example.test_android2.info.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.R.id
import com.example.test_android2.ServiceCreator
import com.example.test_android2.databinding.ActivitySolutionBinding
import com.example.test_android2.info.data.ResponseSolutionDetail
import com.example.test_android2.info.data.Solution
import com.example.test_android2.info.data.SolutionDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SolutionActivity : AppCompatActivity()  {

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
            solutionTitle1 = "A Title 1",
            solutionContent1 = "A Content 1",
            solutionTitle2 = "A Title 2",
            solutionContent2 = "A Content 2",
            solutionTitle3 = "A Title 3",
            solutionContent3 = "A Content 3",
            solutionTitle4 = "A Title 4",
            solutionContent4 = "A Content 4"
        )

        if (intent.hasExtra("clickSolution")) {
            val mysolution = intent.getParcelableExtra<Solution>("clickSolution")
            val solutionIdString = mysolution?.solutionId.toString()
            solutionTitle = mysolution?.solutionTitle.toString()

            if (solutionIdString != null) {
                getSolutionDetail(solutionIdString)
            }

        } else {
            Toast.makeText(this@SolutionActivity, "결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSolutionDetail(solutionId: String) {
        val call: Call<ResponseSolutionDetail> = ServiceCreator.solutionDetailService.getSolutionDetail(solutionId)

        call.enqueue(object : Callback<ResponseSolutionDetail> {
            override fun onResponse(
                call: Call<ResponseSolutionDetail>, response: Response<ResponseSolutionDetail>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { responseData ->
                        val solutionDetailList = responseData.data

                        if (solutionDetailList != null && solutionDetailList.isNotEmpty()) {
                            // 서버에서 반환된 JSON 배열의 첫 번째 요소를 가져옴
                            val solutionDetailObject = solutionDetailList[0]

                            // 필요한 데이터 추출
                            val solutionTitle1 = solutionDetailObject.solutionTitle1 ?: ""
                            val solutionContent1 = solutionDetailObject.solutionContent1 ?: ""
                            val solutionTitle2 = solutionDetailObject.solutionTitle2 ?: ""
                            val solutionContent2 = solutionDetailObject.solutionContent2 ?: ""
                            val solutionTitle3 = solutionDetailObject.solutionTitle3 ?: ""
                            val solutionContent3 = solutionDetailObject.solutionContent3 ?: ""
                            val solutionTitle4 = solutionDetailObject.solutionTitle4 ?: ""
                            val solutionContent4 = solutionDetailObject.solutionContent4 ?: ""

                            // 추출한 데이터를 사용하여 Solution 객체를 생성
                            val sDetail = SolutionDetail(
                                solutionTitle1,
                                solutionContent1,
                                solutionTitle2,
                                solutionContent2,
                                solutionTitle3,
                                solutionContent3,
                                solutionTitle4,
                                solutionContent4
                            )

                            with(binding) {
                                tvTitle.text = solutionTitle1 // 또는 원하는 필드 사용

                                title1.text = sDetail.solutionTitle1
                                title2.text = sDetail.solutionTitle2
                                title3.text = sDetail.solutionTitle3
                                title4.text = sDetail.solutionTitle4

                                body1.text = sDetail.solutionContent1
                                body2.text = sDetail.solutionContent2
                                body3.text = sDetail.solutionContent3
                                body4.text = sDetail.solutionContent4
                            }

                            // Solution 객체의 정보를 로그에 출력
                            Log.d("sDetail", sDetail.toString())
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseSolutionDetail>, t: Throwable) {
                Log.d("솔루션 세부 가져오기 실패", t.message.toString())
            }
        })
    }

}

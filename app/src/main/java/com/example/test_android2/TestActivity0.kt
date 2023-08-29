package com.example.test_android2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.data.TestData
import com.example.test_android2.data.TestModel
import com.example.test_android2.databinding.ActivityTest0Binding
import com.example.test_android2.googleLogin.LoginGoogle.Companion.TAG


class TestActivity0 : AppCompatActivity(){

    private lateinit var binding: ActivityTest0Binding
    private lateinit var questionList: ArrayList<TestModel>

    private var currentPosition: Int = 0 //질문 위치
    var isRadioGroupClickable = true

    var avoidScore = 0
    var anxietyScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTest0Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //질문 리스트 가져오기
        questionList = TestData.getTest()

        //화면 셋팅
        getQuestionData()
        val optionsGroup0: RadioGroup = findViewById(R.id.options_group0)
        optionsGroup0.setOnCheckedChangeListener(radioGroupClickListener)
        val optionsGroup1: RadioGroup = findViewById(R.id.options_group1)
        optionsGroup1.setOnCheckedChangeListener(radioGroupClickListener)
        val optionsGroup2: RadioGroup = findViewById(R.id.options_group2)
        optionsGroup2.setOnCheckedChangeListener(radioGroupClickListener)
        val optionsGroup3: RadioGroup = findViewById(R.id.options_group3)
        optionsGroup3.setOnCheckedChangeListener(radioGroupClickListener)
        val optionsGroup4: RadioGroup = findViewById(R.id.options_group4)
        optionsGroup4.setOnCheckedChangeListener(radioGroupClickListener)
        val optionsGroup5: RadioGroup = findViewById(R.id.options_group5)
        optionsGroup5.setOnCheckedChangeListener(radioGroupClickListener)

        binding.submitBtn.setOnClickListener {
            val avoidGroups = listOf(
                binding.optionsGroup0,
                binding.optionsGroup2,
                binding.optionsGroup4
            )

            val anxietyGroups = listOf(
                binding.optionsGroup1,
                binding.optionsGroup3,
                binding.optionsGroup5
            )

            for (radioGroup in avoidGroups) {
                var selectedRadioButtonId = radioGroup.checkedRadioButtonId

                if (selectedRadioButtonId != -1) {
                    val selectedRadioButton = radioGroup.findViewById<RadioButton>(selectedRadioButtonId)
                    val selectedText = selectedRadioButton.text.toString().trim()

                    when (selectedText) {
                        "전혀 그렇지 않다" -> avoidScore += 5
                        "그렇지 않다" -> avoidScore += 4
                        "보통 정도이다" -> avoidScore += 3
                        "대체로 그렇다" -> avoidScore += 2
                        "매우 그렇다" -> avoidScore += 1
                    }
                }
            }

            for (radioGroup in anxietyGroups) {
                val selectedRadioButtonId = radioGroup.checkedRadioButtonId

                if (selectedRadioButtonId != -1) {
                    val selectedRadioButton = radioGroup.findViewById<RadioButton>(selectedRadioButtonId)
                    val selectedText = selectedRadioButton.text.toString().trim()

                    when (selectedText) {
                        "전혀 그렇지 않다" -> anxietyScore += 5
                        "그렇지 않다" -> anxietyScore += 4
                        "보통 정도이다" -> anxietyScore += 3
                        "대체로 그렇다" -> anxietyScore += 2
                        "매우 그렇다" -> anxietyScore += 1
                    }
                }
            }

            // 각각의 점수 값 로그로 출력
            Log.d("ScoreLog", "Updated Avoid Score: $avoidScore")
            Log.d("ScoreLog", "Updated Anxiety Score: $anxietyScore")

            if (currentPosition == 6) {
                val intent = Intent(this, TestActivity1::class.java)
                intent.putExtra("avoidScore", avoidScore.toFloat())
                intent.putExtra("anxietyScore", anxietyScore.toFloat())
                startActivity(intent)
            } else {
                Log.i(TAG, "미답변")
                Toast.makeText(this, "모든 질문에 답변을 해주세요.", Toast.LENGTH_SHORT).show()
                avoidScore = 0
                anxietyScore = 0
            }
        }

    }


    private fun getQuestionData(){

        //질문 변수에 담기
        val question = questionList[currentPosition]
        val question1 = questionList[currentPosition+1]
        val question2 = questionList[currentPosition+2]
        val question3 = questionList[currentPosition+3]
        val question4 = questionList[currentPosition+4]
        val question5 = questionList[currentPosition+5]

        //상태바 위치
        binding.progressBar.progress = currentPosition

        //상태바 최대값
        binding.progressBar.max = questionList.size

        //현재 위치 표시
        binding.progressText.text =  getString(R.string.count_label, currentPosition, questionList.size)

        //질문 표시
        binding.questionNum0.text= "0"+question.id.toString()+"."
        binding.questionText0.text = question.question
        binding.questionNum1.text="0"+question1.id.toString()+"."
        binding.questionText1.text = question1.question
        binding.questionNum2.text= "0"+question2.id.toString()+"."
        binding.questionText2.text = question2.question
        binding.questionNum3.text="0"+question3.id.toString()+"."
        binding.questionText3.text = question3.question
        binding.questionNum4.text="0"+question4.id.toString()+"."
        binding.questionText4.text = question4.question
        binding.questionNum5.text="0"+question5.id.toString()+"."
        binding.questionText5.text = question5.question


        setSubmitBtn("다음")

    }

    //제출버튼 텍스트 설정
    private fun setSubmitBtn(name: String){

        binding.submitBtn.text = getString(R.string.submit, name)

    }

    val radioGroupClickListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        if (isRadioGroupClickable) {
            // 이미 선택된 라디오 버튼이 변경된 경우에만 currentPosition을 증가시킴
            // 프로그래바 표시를 위한 리스너임
            currentPosition++

            // 선택된 라디오 버튼을 다시 선택으로 변경
            group.setOnCheckedChangeListener(null)

            // currentPosition을 이용해서 현재 문항 번호 등을 업데이트
            binding.progressBar.progress = currentPosition
            binding.progressText.text = getString(R.string.count_label, currentPosition, questionList.size)
        }
    }

}
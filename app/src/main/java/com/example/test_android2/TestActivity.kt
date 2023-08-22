package com.example.test_android2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test_android2.data.TestData
import com.example.test_android2.data.TestModel
import com.example.test_android2.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity(){

    private lateinit var binding: ActivityTestBinding
    private lateinit var questionList: ArrayList<TestModel>

    private var currentPosition: Int = 1 //질문 위치
    private var selectedOption: Int = 0 // 선택 옵션
    private var score: Int = 0 //점수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //질문 리스트 가져오기
        questionList = TestData.getTest()

        //화면 셋팅
        getQuestionData()
    }


    private fun getQuestionData(){

        //질문 변수에 담기
        val question = questionList[currentPosition-1]
        val question1 = questionList[1]

        //상태바 위치
        binding.progressBar.progress = currentPosition

        //상태바 최대값
        binding.progressBar.max = questionList.size

        //현재 위치 표시
        binding.progressText.text =  getString(R.string.count_label, currentPosition, questionList.size)

        //질문 표시
        binding.questionNum.text= "0"+question.id.toString()+"."
        binding.questionText.text = question.question
        binding.questionNum1.text="0"+question1.id.toString()+"."
        binding.questionText1.text = question1.question

        //답변 표시
        /*binding.option1Radio.text = question.option_one
        binding.option2Radio.text = question.option_two
        binding.option3Radio.text = question.option_three
        binding.option4Radio.text = question.option_four
        binding.option4Radio.text = question.option_four*/

        setSubmitBtn("제출")

    }

    //제출버튼 텍스트 설정
    private fun setSubmitBtn(name: String){

        binding.submitBtn.text = getString(R.string.submit, name)

    }
}
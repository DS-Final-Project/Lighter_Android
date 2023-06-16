package com.example.test_android2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.test_android2.R.color
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
        val resultNum = intent.getStringExtra("resultNum")
        //val resultNum = "67"

        if (resultNum != null) {
            val percent = "$resultNum%"
            //val percent = "67%"
            with(binding) {
                tvPercent.text = percent
                progressbar.progress = resultNum.toInt()
            }
            if (resultNum.toInt() <=50){
                with(binding) {
                    icNotice.setImageResource(R.drawable.ic_success)
                    tvNotice.text = "안전"
                    tvSubNotice.text = "정상적인 대화로 판단됩니다."
                    layoutNotice.background.setTint(getColor(color.support_success_light))
                }
            }
            if (resultNum.toInt() in 51..80){
                with(binding) {
                    icNotice.setImageResource(R.drawable.ic_warning)
                    tvNotice.text = "주의"
                    tvSubNotice.text = "가스라이팅이 의심됩니다."
                    layoutNotice.background.setTint(getColor(color.support_warning_light))
                }
            }
            if (resultNum.toInt()>80){
                with(binding) {
                    icNotice.setImageResource(R.drawable.ic_danger)
                    tvNotice.text = "위험"
                    tvSubNotice.text = "가스라이팅으로 판단됩니다."
                    layoutNotice.background.setTint(getColor(color.support_error_light))
                }
            }
        }
        else{
            Toast.makeText(this@ResultAnalysisActivity, "결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
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
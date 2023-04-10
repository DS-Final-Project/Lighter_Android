package com.example.test_android2

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView

class MainActivity : Activity() {
    // TextView 변수
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. TextView 참조
        textView = findViewById(R.id.textViewSpannable)
        // 2. String 문자열 데이터 취득
        val textData: String = textView.text.toString()
        // 3. SpannableStringBuilder 타입으로 변환
        val builder = SpannableStringBuilder(textData)
        val colorBlueSpan = ForegroundColorSpan(Color.rgb(244,172,63))
        builder.setSpan(colorBlueSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // 5. TextView에 적용
        textView.text = builder
    }
}
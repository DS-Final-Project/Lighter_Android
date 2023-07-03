package com.example.test_android2

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2


class InfoFragment : Fragment() {
    lateinit var textView: TextView
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: cardviewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        viewPager = view.findViewById(R.id.view_pager2)

        val models: MutableList<String> = mutableListOf()
        models.add("친구 간 대화방식이\n고민인가요? ")
        models.add("갈등으로 치우지지\n않으려면?")
        models.add("나는 왜 관계가\n어려울까?")
        models.add("불안정 애착\n극복하기")

        textView = view.findViewById(R.id.tv_1)
        val textData: String = textView.text.toString()
        val builder = SpannableStringBuilder(textData)
        val colorBlueSpan = ForegroundColorSpan(Color.rgb(244,172,63))
        builder.setSpan(colorBlueSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = builder

        adapter = cardviewAdapter(models, requireContext())
        viewPager.adapter = adapter
        viewPager.setPadding(30, 0, 30, 0)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

        return view
    }
}
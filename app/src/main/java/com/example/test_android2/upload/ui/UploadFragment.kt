package com.example.test_android2.upload.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.test_android2.R
import com.example.test_android2.main.ViewPagerAdapter
import com.example.test_android2.databinding.FragmentUploadBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UploadFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout : TabLayout

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    // 최초 생성 여부를 나타내는 변수
    private var isInitialCreation = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadBinding.inflate(layoutInflater, container, false)
        viewPager = binding.vpUpload
        tabLayout = binding.tabUpload
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        val pagerAdapter = ViewPagerAdapter(this) // Fragment를 인자로 받도록 변경

        pagerAdapter.addFragment(UploadFileFragment())
        pagerAdapter.addFragment(UploadImageFragment())

        viewPager.adapter = pagerAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 최초 생성이 아닌 경우에만 로딩 바를 숨김
                if (!isInitialCreation) {
                    showProgress(false)
                }
            }
        })

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = "파일"
                1 -> tab.text = "사진"
            }
        }.attach()

        // 최초 생성 여부를 false로 설정합니다.
        isInitialCreation = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showProgress(isShow: Boolean) {
        val activity = activity as? AppCompatActivity
        activity?.findViewById<ProgressBar>(R.id.progressBar)?.visibility =
            if (isShow) View.VISIBLE else View.GONE
    }
}
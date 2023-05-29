package com.example.test_android2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.test_android2.databinding.FragmentUploadBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UploadFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout : TabLayout

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadBinding.inflate(layoutInflater, container, false)
        viewPager = binding.vpUpload
        tabLayout = binding.tabUpload
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val pagerAdapter = ViewPagerAdapter(requireActivity())

        pagerAdapter.addFragment(UploadFileFragment())
        pagerAdapter.addFragment(UploadImageFragment())

        viewPager.adapter = pagerAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = "파일"
                1 -> tab.text = "사진"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

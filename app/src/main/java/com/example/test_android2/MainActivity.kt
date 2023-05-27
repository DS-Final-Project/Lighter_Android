package com.example.test_android2


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.test_android2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initBottomNavi()

    }

    private fun initAdapter(){
        val fragmentList = listOf(UploadFragment(),InfoFragment(),MyPageFragment())
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.fragments.addAll(fragmentList)

        binding.vpMain.adapter = viewPagerAdapter
    }

    private fun initBottomNavi(){
        binding.bnvMain.itemIconTintList = null
        binding.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.bnvMain.menu.getItem(position).isChecked = true
            }
        })

        binding.bnvMain.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_upload -> {
                    binding.vpMain.currentItem = UPLOAD_FRAGMENT
                    true
                }
                R.id.menu_info -> {
                    binding.vpMain.currentItem = INFO_FRAGMENT
                    true
                }
                else -> {
                    binding.vpMain.currentItem = MY_PAGE_FRAGMENT
                    true
                }
            }
        }
    }

    companion object {
        const val UPLOAD_FRAGMENT = 0
        const val MY_PAGE_FRAGMENT = 1
        const val INFO_FRAGMENT = 2
    }
}

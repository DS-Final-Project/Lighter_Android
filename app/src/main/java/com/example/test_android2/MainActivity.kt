package com.example.test_android2


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.test_android2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavi()

    }

    private fun initBottomNavi(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.frm_main,UploadFragment())
            .commitAllowingStateLoss()

        binding.bnvMain.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_upload -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frm_main, UploadFragment())
                        .commitAllowingStateLoss()
                         true
                }
                R.id.menu_info -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frm_main, InfoFragment())
                        .commitAllowingStateLoss()
                    true
                }
                else -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frm_main, MyPageFragment())
                        .commitAllowingStateLoss()
                    true
                }
            }
        }
    }
}

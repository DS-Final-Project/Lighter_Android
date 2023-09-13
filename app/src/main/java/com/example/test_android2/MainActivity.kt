package com.example.test_android2

import InfoFragment
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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

    //화면 밖 클릭 시 키보드 내리기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        if (currentFocus is EditText) {
            currentFocus!!.clearFocus()
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun initBottomNavi() {

        supportFragmentManager.beginTransaction().replace(R.id.frm_main, UploadFragment()).commitAllowingStateLoss()

        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_upload -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frm_main, UploadFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.menu_info -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frm_main, InfoFragment())
                        .commitAllowingStateLoss()
                    true
                }
                else -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frm_main, MyPageFragment())
                        .commitAllowingStateLoss()
                    true
                }
            }
        }
    }
}

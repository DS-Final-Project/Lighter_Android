package com.example.test_android2.main

import android.content.Context
import com.example.test_android2.info.ui.InfoFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.test_android2.R.id
import com.example.test_android2.databinding.ActivityMainBinding
import com.example.test_android2.mypage.ui.MyPageFragment
import com.example.test_android2.upload.ui.UploadFragment

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

        supportFragmentManager.beginTransaction().replace(id.frm_main, UploadFragment()).commitAllowingStateLoss()

        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                id.menu_upload -> {
                    supportFragmentManager.beginTransaction().replace(id.frm_main, UploadFragment())
                        .commitAllowingStateLoss()
                    true
                }
                id.menu_info -> {
                    supportFragmentManager.beginTransaction().replace(id.frm_main, InfoFragment())
                        .commitAllowingStateLoss()
                    true
                }
                else -> {
                    supportFragmentManager.beginTransaction().replace(id.frm_main, MyPageFragment())
                        .commitAllowingStateLoss()
                    true
                }
            }
        }
    }
}

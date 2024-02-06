package com.example.test_android2.mypage.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.test_android2.databinding.DialogListDeleteBinding

class ListDeleteDialog(
    private val confirmDialogInterface: ConfirmDialogInterface, private val chatId: String
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogListDeleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogListDeleteBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 삭제 버튼 클릭
        binding.btnDelete.setOnClickListener { // 삭제 버튼을 클릭하면 ConfirmDialogInterface를 통해 해당 chatId를 전달
            confirmDialogInterface.onDeleteButtonClick(chatId) // 다이얼로그를 닫음
            dismiss()
        } // 취소 버튼 클릭
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        // Dialog의 너비를 화면 너비의 80%로 설정
        dialog?.window?.let {
            val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
            it.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface ConfirmDialogInterface {
    fun onDeleteButtonClick(chatId: String)
}
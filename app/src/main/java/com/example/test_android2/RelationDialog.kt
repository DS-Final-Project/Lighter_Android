package com.example.test_android2

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.example.test_android2.data.ChatData
import com.example.test_android2.databinding.DialogRelationBinding

class RelationDialog(
    private val confirmDialogInterface: ConfirmDialogInterface,
    private val chatData: ChatData
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogRelationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogRelationBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 완료 버튼 클릭
        binding.btnOk.setOnClickListener {
            val selectedRadioButtonId = binding.rgRelation.checkedRadioButtonId
            if(selectedRadioButtonId != -1) {
                val radioButton: RadioButton = view.findViewById(selectedRadioButtonId)

                //선택한 값에 따라 int 데이터 전달
                val dataToSend = when (radioButton.text.toString()) {
                    "연인" -> 1
                    "친구" -> 2
                    "동료" -> 3
                    "가족" -> 4
                    else -> 0 //예외 처리
                }
                chatData.relation = dataToSend
                confirmDialogInterface.onOkButtonClick(chatData)
            }
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface ConfirmDialogInterface {
    fun onOkButtonClick(chatData: ChatData)
}
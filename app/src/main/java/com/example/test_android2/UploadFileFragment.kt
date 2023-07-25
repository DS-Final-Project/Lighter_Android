package com.example.test_android2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test_android2.data.ChatData
import com.example.test_android2.data.ResponseChat
import com.example.test_android2.data.ServiceCreator
import com.example.test_android2.databinding.FragmentUploadFileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class UploadFileFragment : Fragment(), ConfirmDialogInterface {

    private var _binding: FragmentUploadFileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadFileBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initButtonClickEvent()

    }

    private fun initButtonClickEvent() = binding.btnAnalysis.setOnClickListener {
        val chatWords = binding.etChatWords.text.toString()
        val chatData = ChatData(chatWords)
        showCustomDialog(chatData)
    }

    private fun chatNetwork(chatInfo: ChatData) {
        val call: Call<ResponseChat> = ServiceCreator.chatService.uploadChat(chatInfo)

        call.enqueue(object : Callback<ResponseChat> {
            override fun onResponse(
                call: Call<ResponseChat>, response: Response<ResponseChat>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val resultNum = it.resultNum
                        val intent = Intent(context, ResultAnalysisActivity::class.java)
                        intent.putExtra("resultNum", resultNum.toString())
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseChat>, t: Throwable) {
                Log.d("문장 분석 실패", t.message.toString())
            }
        })
    }

    private fun showCustomDialog(chatData: ChatData) {
        val dialog = RelationDialog(this, chatData)
        dialog.show(childFragmentManager, "relation_dialog")
    }

    override fun onOkButtonClick(chatData: ChatData) {
        chatNetwork(chatData)

        showProgress(true)
        binding.progressBar.bringToFront()
        thread(start = true) {
            Thread.sleep(3000)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {
        showProgress(false)
    }

    private fun showProgress(isShow: Boolean) {
        if (isShow) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

}


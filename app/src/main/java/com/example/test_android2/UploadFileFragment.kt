package com.example.test_android2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test_android2.data.Chat
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

    //업로드 페이지에서 채팅 분석 버튼 클릭 시
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
                        val doubtText1 = it.doubtText1
                        val doubtText2 = it.doubtText2
                        val doubtText3 = it.doubtText3
                        val doubtText4 = it.doubtText4
                        val doubtText5 = it.doubtText5
                        val avoidScore = it.avoidScore
                        val anxietyScore = it.anxietyScore
                        val testType = it.testType

                        var mychat = Chat(resultNum,doubtText1,doubtText2,doubtText3,doubtText4,doubtText5,avoidScore,anxietyScore,testType)
                        val intent = Intent(context, ResultAnalysisActivity::class.java)
                        intent.putExtra("mychat", mychat)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseChat>, t: Throwable) {
                Log.d("문장 분석 실패", t.message.toString())
            }
        })
    }

    //로딩 바 초기 설정
    private fun init() {
        showProgress(false)
    }

    //로딩 바 보이기
    private fun showProgress(isShow: Boolean) {
        if (isShow) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    //다이얼로그 보이기
    private fun showCustomDialog(chatData: ChatData) {
        val dialog = RelationDialog(this, chatData)
        dialog.show(childFragmentManager, "relation_dialog")
    }

    //다이얼로그에서 완료 버튼 클릭 시
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

}


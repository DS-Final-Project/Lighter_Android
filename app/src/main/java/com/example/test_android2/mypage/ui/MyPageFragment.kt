package com.example.test_android2.mypage.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_android2.analysisresult.ui.ResultAnalysisActivity
import com.example.test_android2.analysisresult.data.Chat
import com.example.test_android2.mypage.data.ItemData
import com.example.test_android2.mypage.data.ItemDetailData
import com.example.test_android2.analysisresult.data.ResponseChat
import com.example.test_android2.mypage.data.ResponseItem
import com.example.test_android2.ServiceCreator
import com.example.test_android2.databinding.FragmentMyPageBinding
import com.example.test_android2.googleLogin.LoginGoogle
import com.example.test_android2.googleLogin.LoginGoogle.Companion.TAG
import com.example.test_android2.googleLogin.LoginGoogleActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment : Fragment(), ConfirmDialogInterface {

    private lateinit var expandableAdapter: ExpandableAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initButtonClickEvent()
        itemNetwork()
    }

    // 마이페이지 초기 설정
    private fun init() {
        showProgress(true)
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) ?: return
    }

    private fun initButtonClickEvent() {
        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnSignout.setOnClickListener {
            stateNetwork()
        }
    }

    private fun itemNetwork() {
        val call: Call<ResponseItem> = ServiceCreator.chatService.getItems()

        call.enqueue(object : Callback<ResponseItem> {
            override fun onResponse(
                call: Call<ResponseItem>, response: Response<ResponseItem>
            ) { // 로딩 바 숨기기
                showProgress(false)

                if (response.isSuccessful) {
                    val responseData = response.body()
                    responseData?.let {
                        binding.tvNickname.text = "반가워요,\n${it.name} 님"

                        if (it.data.isNullOrEmpty()) {
                            showErrorToast("불러올 리스트가 없습니다.")
                        } else {
                            setupAdapter(processData(it.data))
                        }
                    } ?: showErrorToast("불러올 리스트가 없습니다.")
                }
            }

            override fun onFailure(call: Call<ResponseItem>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
                showErrorToast("리스트를 불러오지 못했습니다.")
            }

        })
    }

    private fun stateNetwork() {
        val call: Call<Void> = ServiceCreator.tokenService.deleteAccount()

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    signout()
                } else {
                    Log.e(TAG, "Failed to send email to server")
                    showErrorToast("회원 탈퇴에 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
                showErrorToast("회원 탈퇴에 실패했습니다.")
            }
        })
    }

    private fun setupAdapter(itemList: MutableList<ItemData>) {
        expandableAdapter = ExpandableAdapter(requireContext(), itemList)
        binding.recyclerList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerList.adapter = expandableAdapter

        // 아이템 클릭 시 이벤트
        expandableAdapter.setOnItemClickListener { chatId ->
            fetchChatData(chatId)
        }

    }

    private fun fetchChatData(chatId: String?) {
        chatId?.let {
            val call: Call<ResponseChat> = ServiceCreator.chatService.getChatResultData(chatId)
            call.enqueue(object : Callback<ResponseChat> {
                override fun onResponse(call: Call<ResponseChat>, response: Response<ResponseChat>) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseData ->
                            val data = responseData.data
                            val mychat = Chat(
                                data.resultNum,
                                data.doubtText1,
                                data.doubtText2,
                                data.doubtText3,
                                data.doubtText4,
                                data.doubtText5,
                                data.avoidScore,
                                data.anxietyScore,
                                data.testType,
                                data.relation
                            )
                            val intent = Intent(context, ResultAnalysisActivity::class.java).apply {
                                putExtra("mychat", mychat)
                            }
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseChat>, t: Throwable) {
                    Log.d("결과 불러오기 실패", t.message.toString())
                }
            })
        }
    }

    // 데이터를 적절한 형태로 가공하는 함수
    private fun processData(dataList: List<ResponseItem.Data>): MutableList<ItemData> {
        val itemMap = mutableMapOf<String, ItemData>()

        // 년월을 키로 하는 ItemData 맵을 만듦
        for (data in dataList) {
            data.chatDate?.let { chatDate ->
                val yearMonth = chatDate.substring(0, 7) // yyyy-MM 형태의 년월 추출
                val item = itemMap.getOrPut(yearMonth) { ItemData(yearMonth) }

                val chatId = data.chatId
                val resultNum = data.resultNum ?: 0
                val noticeText = when {
                    resultNum <= 50 -> "안전"
                    resultNum in 51..80 -> "주의"
                    else -> "위험"
                }

                item.subList.add(ItemDetailData(chatId, chatDate, noticeText, resultNum)) // 년월에 해당하는 데이터 추가
            }
        }

        // 맵의 값들을 리스트로 변환하여 반환
        return itemMap.values.toMutableList()
    }

    // 로딩 바 보이기
    private fun showProgress(isShow: Boolean) {
        if (isShow) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // 로그아웃
    private fun logout() { // SharedPreferences에서 이메일 제거
        sharedPreferences.edit().remove("email").apply()

        val loginGoogle = LoginGoogle(requireContext())
        loginGoogle.signOut(requireContext())

        // 로그아웃 시 로그인 화면으로 이동
        val intent = Intent(context, LoginGoogleActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    // 회원 탈퇴
    private fun signout() {
        val loginGoogle = LoginGoogle(requireContext())
        loginGoogle.disconnect(requireContext()) { // 연동 해제가 성공한 경우에만 이후 작업 수행
            sharedPreferences.edit().remove("email").apply()

            // 회원탈퇴 시 로그인 화면으로 이동
            val intent = Intent(context, LoginGoogleActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDeleteButtonClick(chatId: String) {
        expandableAdapter.removeItem(chatId) // 리스트를 삭제한 후 리사이클러뷰 갱신
        expandableAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
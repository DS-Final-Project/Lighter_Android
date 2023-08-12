package com.example.test_android2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_android2.data.ItemData
import com.example.test_android2.data.ItemDetailData
import com.example.test_android2.data.ResponseItem
import com.example.test_android2.data.ServiceCreator
import com.example.test_android2.databinding.FragmentMyPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class MyPageFragment : Fragment() {

    private lateinit var expandableAdapter: ExpandableAdapter
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
        showProgress(true)
        binding.progressBar.bringToFront()
        thread(start = true) {
            Thread.sleep(3000)

        }
        itemNetwork()
    }

    private fun itemNetwork() {
        val call: Call<ResponseItem> = ServiceCreator.chatService.getItems()

        call.enqueue(object : Callback<ResponseItem> {
            override fun onResponse(
                call: Call<ResponseItem>, response: Response<ResponseItem>
            ) {
                // 로딩 바 숨기기
                showProgress(false)

                if (response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null){
                        binding.tvNickname.text = "반가워요,\n${responseData.name} 님"

                        responseData.data?.let { dataList ->
                            val processedData = processData(dataList)
                            setupAdapter(processedData)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseItem>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
                Toast.makeText(context, "리스트를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setupAdapter(itemList: MutableList<ItemData>) {
        expandableAdapter = ExpandableAdapter(requireContext(), itemList)
        binding.recyclerList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerList.adapter = expandableAdapter
    }


    // 데이터를 적절한 형태로 가공하는 함수
    private fun processData(dataList: List<ResponseItem.Data>): MutableList<ItemData> {
        val itemMap = mutableMapOf<String, ItemData>()

        // 년월을 키로 하는 ItemData 맵을 만듦
        for (data in dataList) {
            data.chatDate?.let { chatDate ->
                val yearMonth = chatDate.substring(0, 7) // yyyy-MM 형태의 년월 추출
                val item = itemMap.getOrPut(yearMonth) { ItemData(yearMonth) }

                val resultNum = data.resultNum ?: 0
                val noticeText = when {
                    resultNum <= 50 -> "안전"
                    resultNum in 51..80 -> "주의"
                    else -> "위험"
                }

                item.subList.add(ItemDetailData(chatDate, noticeText,resultNum)) // 년월에 해당하는 데이터 추가
            }
        }

        // 맵의 값들을 리스트로 변환하여 반환
        return itemMap.values.toMutableList()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
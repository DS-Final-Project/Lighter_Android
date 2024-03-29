package com.example.test_android2.mypage.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.test_android2.R.drawable
import com.example.test_android2.ServiceCreator
import com.example.test_android2.mypage.data.ItemData
import com.example.test_android2.mypage.data.ItemDetailData
import com.example.test_android2.databinding.ItemMypageBinding
import com.example.test_android2.databinding.ItemMypageDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

object Constants {
    const val PARENT = 0
    const val CHILD = 1
}

class ExpandableAdapter(
    private val mContext: Context, private var itemList: MutableList<ItemData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 아이템 클릭 리스너 설정 변수
    private var itemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.PARENT) {
            val binding = ItemMypageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            GroupViewHolder(binding)
        } else {
            val binding = ItemMypageDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChildViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataList = itemList[position]
        if (dataList.type == Constants.PARENT) {
            holder as GroupViewHolder
            holder.bind(dataList, position)
        } else {
            holder as ChildViewHolder
            holder.bind(dataList)
        }
    }

    private fun expandOrCollapseParentItem(singleBoarding: ItemData, position: Int) {
        if (singleBoarding.isExpanded) {
            collapseParentRow(position)
        } else {
            expandParentRow(position)
        }
    }

    private fun expandParentRow(position: Int) {
        val currentBoardingRow = itemList[position]
        val services = currentBoardingRow.subList
        currentBoardingRow.isExpanded = true
        var nextPosition = position
        if (currentBoardingRow.type == Constants.PARENT) {
            services.forEach { service ->
                val parentModel = ItemData(chatYearMonth = service.chatDay.substring(0, 7))
                parentModel.type = Constants.CHILD
                val subList: ArrayList<ItemDetailData> = ArrayList()
                subList.add(service)
                parentModel.subList = subList
                itemList.add(++nextPosition, parentModel)
            }
            notifyDataSetChanged()
        }
    }

    private fun collapseParentRow(position: Int) {
        val currentBoardingRow = itemList[position]
        val services = currentBoardingRow.subList
        itemList[position].isExpanded = false
        if (itemList[position].type == Constants.PARENT) {
            services.forEach { _ ->
                itemList.removeAt(position + 1)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int = itemList[position].type

    override fun getItemId(position: Int): Long = position.toLong()

    inner class GroupViewHolder(private val binding: ItemMypageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemData: ItemData, position: Int) {
            val yearMonthText = "${itemData.chatYearMonth.substring(0, 4)}년 ${itemData.chatYearMonth.substring(5, 7)}월"
            binding.tvDate.text = yearMonthText

            val rotationDegree = if (itemData.isExpanded) 180f else 0f
            binding.imgMore.rotation = rotationDegree
            binding.imgMore.setOnClickListener {
                expandOrCollapseParentItem(itemData, position) // 이미지 버튼 회전
                binding.imgMore.animate().setDuration(200).rotation(rotationDegree)
            }
        }
    }

    inner class ChildViewHolder(private val binding: ItemMypageDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemData: ItemData) {
            val singleService = itemData.subList.first()
            val chatId = singleService.chatId
            val dateParts = singleService.chatDay.split("-") // "-"로 분리하여 리스트로 변환
            if (dateParts.size == 3) {
                binding.tvDay.text = "${dateParts[2]}일"
            }
            binding.tvNotice.text = singleService.notice
            if (singleService.resultNum!! <= 50) {
                binding.imgNotice.setImageResource(drawable.ic_mypage_notice_success)
            } else {
                binding.imgNotice.setImageResource(drawable.ic_mypage_notice_danger)
            }

            binding.root.setOnClickListener {
                chatId?.let { itemClickListener?.invoke(it) }
            } // 리스트 삭제 버튼 클릭 시
            binding.imgListDelete.setOnClickListener {
                chatId?.let { showCustomDialog(it) }
            }
        }
    }

    //다이얼로그 보이기
    private fun showCustomDialog(chatId: String) {
        val fragmentManager = (mContext as FragmentActivity).supportFragmentManager
        val dialog = ListDeleteDialog(object : ConfirmDialogInterface {
            override fun onDeleteButtonClick(chatId: String) {
                deleteItem(chatId)
            }
        }, chatId)
        dialog.show(fragmentManager, "ListDeleteDialog")
    }

    private fun deleteItem(chatId: String) {
        val call: Call<Void> = ServiceCreator.chatService.deleteChatResult(chatId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    removeItem(chatId)
                } else {
                    Toast.makeText(mContext, "삭제에 실패했습니다.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("리스트 삭제 실패", t.message.toString())
            }
        })
    }

    fun removeItem(chatId: String) {
        var yearMonth: String? = null // 삭제할 자식 아이템의 날짜-월 초기화

        val iterator = itemList.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.type == Constants.CHILD && item.subList.isNotEmpty()) {
                val subList = item.subList
                var removeIndex: Int? = null // 삭제할 아이템의 인덱스
                for (i in subList.indices) {
                    val childItem = subList[i]
                    if (childItem.chatId == chatId) {
                        removeIndex = i
                        yearMonth = childItem.chatDay.substring(0, 7)
                        break
                    }
                }
                removeIndex?.let { it ->
                    subList.removeAt(it) // 삭제할 아이템 제거
                    if (subList.isEmpty()) {
                        iterator.remove() // sublist가 비어있는 경우에는 부모 아이템 제거
                    }

                    if (itemList.count { it.chatYearMonth == yearMonth } == 1) {
                        itemList.removeAll { it.chatYearMonth == yearMonth } // 동일한 날짜-월을 가진 ItemData 삭제
                    } else { // 부모 ItemData에서도 해당하는 chatId를 가진 ItemDetailData를 삭제
                        for (parentItem in itemList) {
                            if (parentItem.type == Constants.PARENT && parentItem.chatYearMonth == yearMonth) {
                                val parentDetail = parentItem.subList.find { it.chatId == chatId }
                                if (parentDetail != null) {
                                    parentItem.subList.remove(parentDetail)
                                    break
                                }
                            }
                        }
                    }
                    notifyDataSetChanged()
                    return
                }
            }
        }
    }
}
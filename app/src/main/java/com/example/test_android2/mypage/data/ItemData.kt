package com.example.test_android2.mypage.data

import com.example.test_android2.mypage.ui.Constants

data class ItemData(
    val chatYearMonth: String,
    var type:Int = Constants.PARENT,
    var isExpanded: Boolean = false,
    var subList : MutableList<ItemDetailData> = ArrayList()
)
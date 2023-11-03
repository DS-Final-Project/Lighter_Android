package com.example.test_android2.mypage.data

data class ResponseItem(
    val error: String,
    val name: String,
    val data: List<Data>
) {
    data class Data(
        val chatId: String?,
        val chatDate: String?,
        val resultNum: Int?
    )
}


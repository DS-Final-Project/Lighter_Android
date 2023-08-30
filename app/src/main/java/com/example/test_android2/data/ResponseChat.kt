package com.example.test_android2.data

data class ResponseChat(
    val error: String,
    val data: Data
)

data class Data(
    val resultNum: Int?,
    val doubtText1: String?,
    val doubtText2: String?,
    val doubtText3: String?,
    val doubtText4: String?,
    val doubtText5: String?,
    val avoidScore: Float?,
    val anxietyScore: Float?,
    val testType: Int?,
    val relation: Int?
)


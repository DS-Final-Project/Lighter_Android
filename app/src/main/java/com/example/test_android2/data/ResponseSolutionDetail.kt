package com.example.test_android2.data


data class ResponseSolutionDetail(
    val error: String?,
    val data: List<ResponseSolutionDetailData>?
)

data class ResponseSolutionDetailData(
    val solutionTitle1: String?,
    val solutionContent1: String?,
    val solutionTitle2: String?,
    val solutionContent2: String?,
    val solutionTitle3: String?,
    val solutionContent3: String?,
    val solutionTitle4: String?,
    val solutionContent4: String?
)

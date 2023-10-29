package com.example.test_android2.data

data class ResponseSolution(
    val error: String?,
    val data: List<ResponseSolutionData>?
)

data class ResponseSolutionData(
    val solutionId: String?, // solutionId를 String으로 변경
    val relation: Int?,
    val keyword: String?,
    val solutionTitle: String?,
    val solutionContent: String?
)

package com.example.test_android2.data

data class ResponseSolution(
    val error: String,
    val data: ResponseSolutionData
)

data class ResponseSolutionData(
    val solutionId: Int?,
    val relation: Int?,
    val keyword: String?,
    val solutionTitle: String?,
    val solutionContent: String?
)

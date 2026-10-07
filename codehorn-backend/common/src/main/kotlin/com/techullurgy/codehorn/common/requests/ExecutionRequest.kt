package com.techullurgy.codehorn.common.requests

import com.techullurgy.codehorn.common.models.ProblemTestcase

data class ExecutionRequest(
    val executionId: String,
    val fileContent: String,
    val testcases: List<ProblemTestcase>
)

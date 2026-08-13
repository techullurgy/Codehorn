package com.techullurgy.codehorn.python_execution.dto

import com.techullurgy.codehorn.common.models.ProblemTestcase

data class ExecutionRequestDto(
    val executionId: String,
    val fileContent: String,
    val testcases: List<ProblemTestcase>
)

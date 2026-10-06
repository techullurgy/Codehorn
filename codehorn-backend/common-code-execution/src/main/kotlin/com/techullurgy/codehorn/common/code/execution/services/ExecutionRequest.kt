package com.techullurgy.codehorn.common.code.execution.services

import com.techullurgy.codehorn.common.models.ParsedTestcase

data class ExecutionRequest(
    val executionId: String,
    val testcases: List<ParsedTestcase>
)
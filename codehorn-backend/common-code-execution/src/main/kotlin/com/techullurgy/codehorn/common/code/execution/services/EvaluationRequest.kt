package com.techullurgy.codehorn.common.code.execution.services

import com.techullurgy.codehorn.common.models.ParsedTestcase

data class EvaluationRequest(
    val evaluationId: String,
    val testcases: List<ParsedTestcase>
)
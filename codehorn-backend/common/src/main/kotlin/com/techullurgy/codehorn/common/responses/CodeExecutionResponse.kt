package com.techullurgy.codehorn.common.responses

import com.techullurgy.codehorn.common.models.ExecutionResult
import com.techullurgy.codehorn.common.models.Language
import kotlinx.serialization.Serializable

@Serializable
data class CodeExecutionResponse(
    val executionId: String,
    val problemId: String,
    val language: Language,
    val results: Set<ExecutionResult>
)

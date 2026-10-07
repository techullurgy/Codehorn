package com.techullurgy.codehorn.common.responses

import com.techullurgy.codehorn.common.dto.ProblemExecutionMetadataDto
import com.techullurgy.codehorn.common.models.Language
import com.techullurgy.codehorn.common.models.ProblemTestcase

data class ProblemForExecutionResponse(
    val problemId: String,
    val language: Language,
    val testcases: List<ProblemTestcase>,
    val metadata: ProblemExecutionMetadataDto
)
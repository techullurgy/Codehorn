package com.techullurgy.codehorn.common.dto

import com.techullurgy.codehorn.common.models.Language

data class ProblemExecutionMetadataDto(
    val language: Language,
    val imports: String,
    val utils: String,
    val main: String,
    val solution: String,
)

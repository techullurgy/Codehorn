package com.techullurgy.codehorn.problems.dto

import com.techullurgy.codehorn.common.models.Difficulty
import com.techullurgy.codehorn.problems.domain.model.ProblemExample
import com.techullurgy.codehorn.problems.domain.model.TestCase

data class CreateProblemRequest(
    val title: String,
    val slug: String? = null,
    val difficulty: Difficulty,
    val category: String,
    val acceptanceRate: Double = 0.0,
    val description: String,
    val examples: List<ProblemExample> = emptyList(),
    val constraints: List<String> = emptyList(),
    val hints: List<String> = emptyList(),
    val editorial: String = "",
    val solutionApproaches: List<String> = emptyList(),
    val starterCode: Map<String, String> = emptyMap(),
    val templates: Map<String, String> = emptyMap(),
    val solutions: Map<String, String> = emptyMap(),
    val testcases: List<TestCase> = emptyList()
)

data class UpdateProblemRequest(
    val title: String? = null,
    val difficulty: Difficulty? = null,
    val category: String? = null,
    val acceptanceRate: Double? = null,
    val description: String? = null,
    val examples: List<ProblemExample>? = null,
    val constraints: List<String>? = null,
    val hints: List<String>? = null,
    val editorial: String? = null,
    val solutionApproaches: List<String>? = null,
    val starterCode: Map<String, String>? = null,
    val templates: Map<String, String>? = null,
    val solutions: Map<String, String>? = null,
    val testcases: List<TestCase>? = null
)

data class ProblemSummaryDto(
    val id: String,
    val title: String,
    val slug: String,
    val difficulty: Difficulty,
    val category: String,
    val acceptanceRate: Double
)

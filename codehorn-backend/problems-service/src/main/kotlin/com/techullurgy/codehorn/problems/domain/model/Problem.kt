package com.techullurgy.codehorn.problems.domain.model

import com.techullurgy.codehorn.common.models.Difficulty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

data class ProblemExample(
    val id: Int,
    val input: String,
    val output: String,
    val explanation: String? = null
)

data class TestCase(
    val id: String,
    val input: String,
    val expectedOutput: String,
    val isSample: Boolean = false
)

@Document(collection = "problems")
data class Problem(
    @Id val id: String,
    val title: String,
    @Indexed(unique = true) val slug: String,
    val difficulty: Difficulty,
    @Indexed val category: String,
    val acceptanceRate: Double,
    val description: String,
    val examples: List<ProblemExample>,
    val constraints: List<String>,
    val hints: List<String>,
    val editorial: String,
    val solutionApproaches: List<String>,
    val starterCode: Map<String, String>,
    val templates: Map<String, String>,
    val solutions: Map<String, String>,
    val testcases: List<TestCase>
)

package com.techullurgy.codehorn.common.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface ExecutionResult {

    val executionId: String

    @Serializable
    data class CompilationError(
        override val executionId: String,
        val error: String,
    ): ExecutionResult

    @Serializable
    data class Verdict(
        override val executionId: String,
        val testcaseId: String,
        val expected: String,
        val actual: String,
        val stdout: String,
        val stderr: String,
        val verdict: ExecutionVerdict
    ): ExecutionResult {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Verdict) return false

            if (executionId != other.executionId) return false
            if (testcaseId != other.testcaseId) return false

            return true
        }

        override fun hashCode(): Int {
            var result = executionId.hashCode()
            result = 31 * result + testcaseId.hashCode()
            return result
        }
    }

    @Serializable
    data class NonExecutedVerdict(
        override val executionId: String,
        val testcaseId: String
    ): ExecutionResult
}

@Serializable
sealed interface ExecutionVerdict {
    @Serializable data class RuntimeError(val error: String): ExecutionVerdict
    @Serializable data object NotExecuted: ExecutionVerdict
    @Serializable data object Accepted: ExecutionVerdict
    @Serializable data object WrongAnswer: ExecutionVerdict
    @Serializable data object TimeLimitExceeded: ExecutionVerdict
    @Serializable data object MemoryLimitExceeded: ExecutionVerdict
}
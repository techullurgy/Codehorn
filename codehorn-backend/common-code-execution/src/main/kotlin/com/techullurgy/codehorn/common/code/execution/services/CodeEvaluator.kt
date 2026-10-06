package com.techullurgy.codehorn.common.code.execution.services

import java.io.File

abstract class CodeEvaluator(
    protected val rootDir: String,
): AutoCloseable {

    private val rootFolder = File(rootDir)

    val root: String get() = rootDir

    init {
        rootFolder.mkdirs()
    }

    abstract val srcFile: String

    open fun compile(request: ExecutionRequest): CompilationResult = CompilationResult.NotAppropriate
    abstract fun run(request: ExecutionRequest): Set<RunResult>

    override fun close() {
        rootFolder.deleteRecursively()
    }

    companion object {
        const val CODE_ACCEPTED = 0
        const val CODE_WRONG_ANSWER = 400
        const val CODE_TIME_LIMIT_EXCEEDED = 99999
        const val CODE_MEMORY_LIMIT_EXCEEDED = 55555

        const val ENV_CODE_ACCEPTED = "CODE_ACCEPTED"
        const val ENV_CODE_WRONG_ANSWER = "CODE_WRONG_ANSWER"
        const val ENV_CODE_TIME_LIMIT_EXCEEDED = "CODE_TIME_LIMIT_EXCEEDED"
        const val ENV_CODE_MEMORY_LIMIT_EXCEEDED = "CODE_MEMORY_LIMIT_EXCEEDED"
    }
}

sealed interface CompilationResult {
    data object NotAppropriate: CompilationResult

    data object Ok: CompilationResult
    data class Error(val stderr: String): CompilationResult
}

data class RunResult(
    val testcaseId: String,
    val verdict: RunVerdict,
    val expected: String,
    val actual: String,
    val stdout: String,
    val stderr: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RunResult) return false

        if (testcaseId != other.testcaseId) return false

        return true
    }

    override fun hashCode(): Int {
        return testcaseId.hashCode()
    }
}

sealed interface RunVerdict {
    data object None: RunVerdict
    data object Accepted: RunVerdict
    data object WrongAnswer: RunVerdict
    data object TimeLimitExceeded: RunVerdict
    data object MemoryLimitExceeded: RunVerdict
    data class Error(val stderr: String): RunVerdict
}
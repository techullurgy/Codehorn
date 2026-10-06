package com.techullurgy.codehorn.common.code.execution.services

import com.techullurgy.codehorn.common.models.ParsedTestcase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class NewCodeExecutionService(
    private val codeEvaluatorFactory: CodeEvaluatorFactory,
) {
    suspend fun executeFor(
        executionId: String,
        fileContent: String,
        testcases: List<ParsedTestcase>,
    ): Set<ExecutionResult> = withContext(Dispatchers.IO) {
        codeEvaluatorFactory.provideEvaluator(executionId)
            .use { evaluator ->
                FileService.writeFile(evaluator.srcFile, fileContent)

                testcases.forEach {
                    FileService.writeFile("${evaluator.root}/testcase_${it.id}.txt", it.testcase)
                }

                val request = ExecutionRequest(
                    executionId = executionId,
                    testcases = testcases
                )

                when (val compilationResult = evaluator.compile(request)) {
                    is CompilationResult.Error -> {
                        setOf(ExecutionResult.CompilationError(compilationResult.stderr))
                    }
                    CompilationResult.NotAppropriate,
                    CompilationResult.Ok -> {
                        val runResults = evaluator.run(request)

                        request.testcases.map {
                            val original = runResults.firstOrNull { r -> r.testcaseId == it.id }

                            original?.let { result ->
                                ExecutionResult.Verdict(
                                    testcaseId = result.testcaseId,
                                    expected = result.expected,
                                    actual = result.actual,
                                    stdout = result.stdout,
                                    stderr = result.stderr,
                                    verdict = when(val verdict = result.verdict) {
                                        RunVerdict.Accepted -> ExecutionVerdict.Accepted
                                        is RunVerdict.Error -> ExecutionVerdict.RuntimeError(verdict.stderr)
                                        RunVerdict.MemoryLimitExceeded -> ExecutionVerdict.MemoryLimitExceeded
                                        RunVerdict.None -> ExecutionVerdict.NotExecuted
                                        RunVerdict.TimeLimitExceeded -> ExecutionVerdict.TimeLimitExceeded
                                        RunVerdict.WrongAnswer -> ExecutionVerdict.WrongAnswer
                                    }
                                )
                            } ?: ExecutionResult.NonExecutedVerdict(it.id)
                        }.toSet()
                    }
                }
        }
    }
}

sealed interface ExecutionResult {
    data class CompilationError(
        val error: String,
    ): ExecutionResult

    data class Verdict(
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

            if (testcaseId != other.testcaseId) return false

            return true
        }

        override fun hashCode(): Int {
            return testcaseId.hashCode()
        }
    }

    data class NonExecutedVerdict(
        val testcaseId: String
    ): ExecutionResult
}

sealed interface ExecutionVerdict {
    data class RuntimeError(val error: String): ExecutionVerdict
    data object NotExecuted: ExecutionVerdict
    data object Accepted: ExecutionVerdict
    data object WrongAnswer: ExecutionVerdict
    data object TimeLimitExceeded: ExecutionVerdict
    data object MemoryLimitExceeded: ExecutionVerdict
}
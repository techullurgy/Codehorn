package com.techullurgy.codehorn.common.code.execution.services

import com.techullurgy.codehorn.common.models.ExecutionResult
import com.techullurgy.codehorn.common.models.ExecutionVerdict
import com.techullurgy.codehorn.common.models.ParsedTestcase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class CodeEvaluationService(
    private val codeEvaluatorFactory: CodeEvaluatorFactory,
) {
    suspend fun evaluateFor(
        evaluationId: String,
        fileContent: String,
        testcases: List<ParsedTestcase>,
    ): Set<ExecutionResult> = withContext(Dispatchers.IO) {
        codeEvaluatorFactory.provideEvaluator(evaluationId)
            .use { evaluator ->
                FileService.writeFile(evaluator.srcFile, fileContent)

                testcases.forEach {
                    FileService.writeFile("${evaluator.root}/testcase_${it.id}/testcase.txt", it.testcase)
                }

                val request = EvaluationRequest(
                    evaluationId = evaluationId,
                    testcases = testcases
                )

                when (val compilationResult = evaluator.compile(request)) {
                    is CompilationResult.Error -> {
                        setOf(
                            ExecutionResult.CompilationError(
                                executionId = evaluationId,
                                error = compilationResult.stderr
                            )
                        )
                    }
                    CompilationResult.NotAppropriate,
                    CompilationResult.Ok -> {
                        val runResults = evaluator.run(request)

                        request.testcases.map {
                            val original = runResults.firstOrNull { r -> r.testcaseId == it.id }

                            original?.let { result ->
                                ExecutionResult.Verdict(
                                    executionId = evaluationId,
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
                            } ?: ExecutionResult.NonExecutedVerdict(
                                executionId = evaluationId,
                                testcaseId = it.id
                            )
                        }.toSet()
                    }
                }
        }
    }
}
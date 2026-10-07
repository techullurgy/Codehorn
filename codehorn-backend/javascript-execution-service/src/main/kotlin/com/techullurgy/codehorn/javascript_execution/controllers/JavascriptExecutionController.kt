package com.techullurgy.codehorn.javascript_execution.controllers

import com.techullurgy.codehorn.common.code.execution.parsers.TestcaseParserStrategy
import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluationService
import com.techullurgy.codehorn.common.models.ExecutionResult
import com.techullurgy.codehorn.common.models.ParsedTestcase
import com.techullurgy.codehorn.common.requests.ExecutionRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/javascript")
class JavascriptExecutionController(
    private val codeEvaluationService: CodeEvaluationService,
    private val testcaseParserStrategy: TestcaseParserStrategy,
) {

    @PostMapping("/execute")
    suspend fun executeJavascriptCode(
        @RequestBody request: ExecutionRequest
    ): ResponseEntity<Set<ExecutionResult>> {
        val parsedTestcases = request.testcases.map { problemTestcase ->
            ParsedTestcase(
                id = problemTestcase.id,
                testcase = testcaseParserStrategy.parse(problemTestcase),
            )
        }

        val results = codeEvaluationService.evaluateFor(
            evaluationId = request.executionId,
            fileContent = request.fileContent,
            testcases = parsedTestcases
        )

        return ResponseEntity.ok(results)
    }
}

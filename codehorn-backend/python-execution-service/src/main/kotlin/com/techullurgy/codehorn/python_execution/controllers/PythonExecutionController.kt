package com.techullurgy.codehorn.python_execution.controllers

import com.techullurgy.codehorn.common.code.execution.parsers.TestcaseParserStrategy
import com.techullurgy.codehorn.common.code.execution.providers.VolumeMountPathProvider
import com.techullurgy.codehorn.common.code.execution.services.CodeExecutionService
import com.techullurgy.codehorn.common.code.execution.services.UserFolderCreator
import com.techullurgy.codehorn.common.models.ParsedTestcase
import com.techullurgy.codehorn.common.models.TestcaseResult
import com.techullurgy.codehorn.python_execution.dto.ExecutionRequestDto
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/python")
class PythonExecutionController(
    private val codeExecutionService: CodeExecutionService,
    private val testcaseParserStrategy: TestcaseParserStrategy,
    private val volumeMountPathProvider: ObjectProvider<VolumeMountPathProvider>
) {

    @PostMapping("/execute")
    suspend fun executePythonCode(
        @RequestBody request: ExecutionRequestDto
    ): ResponseEntity<List<TestcaseResult>> {
        val parsedTestcases = request.testcases.map { problemTestcase ->
            ParsedTestcase(
                id = problemTestcase.id,
                testcase = testcaseParserStrategy.parse(problemTestcase),
                isHidden = problemTestcase.isHidden
            )
        }

        val results = UserFolderCreator(request.executionId, volumeMountPathProvider).use { userFolderCreator ->
            codeExecutionService.executeFor(
                executionId = request.executionId,
                folder = userFolderCreator.folder,
                fileContent = request.fileContent,
                testcases = parsedTestcases
            )
        }

        return ResponseEntity.ok(results)
    }
}

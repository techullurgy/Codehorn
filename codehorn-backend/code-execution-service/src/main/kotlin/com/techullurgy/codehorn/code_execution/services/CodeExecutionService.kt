package com.techullurgy.codehorn.code_execution.services

import com.techullurgy.codehorn.code_execution.services.clients.JavaExecutionApiClient
import com.techullurgy.codehorn.code_execution.services.clients.ProblemsApiClient
import com.techullurgy.codehorn.common.models.Language
import com.techullurgy.codehorn.common.requests.ExecutionRequest
import com.techullurgy.codehorn.common.responses.CodeExecutionResponse
import org.springframework.stereotype.Service

@Service
class CodeExecutionService(
    private val problemClient: ProblemsApiClient,
    private val javaExecutionClient: JavaExecutionApiClient
) {
    suspend fun executeJavaCodeForSampleTestcases(
        executionId: String,
        problemId: String,
        userCode: String,
    ): CodeExecutionResponse {
        val problem = problemClient.getProblemForSampleExecution(
            problemId = problemId,
            language = Language.JAVA,
        )

        val fileContent = FileContentBuilder.build(
            imports = problem.metadata.imports,
            utils = problem.metadata.utils,
            solution = problem.metadata.solution,
            main = problem.metadata.main,
            userCode = userCode,
        )

        val results = javaExecutionClient.executeJavaCode(
            request = ExecutionRequest(
                executionId = executionId,
                fileContent = fileContent,
                testcases = problem.testcases,
            )
        )

        return CodeExecutionResponse(
            executionId = executionId,
            problemId = problemId,
            language = Language.JAVA,
            results = results,
        )
    }

    suspend fun executeJavaCodeForFullTestcases(
        executionId: String,
        problemId: String,
        userCode: String,
    ): CodeExecutionResponse {
        val problem = problemClient.getProblemForFullExecution(
            problemId = problemId,
            language = Language.JAVA,
        )

        val fileContent = FileContentBuilder.build(
            imports = problem.metadata.imports,
            utils = problem.metadata.utils,
            solution = problem.metadata.solution,
            main = problem.metadata.main,
            userCode = userCode,
        )

        val results = javaExecutionClient.executeJavaCode(
            request = ExecutionRequest(
                executionId = executionId,
                fileContent = fileContent,
                testcases = problem.testcases,
            )
        )

        return CodeExecutionResponse(
            executionId = executionId,
            problemId = problemId,
            language = Language.JAVA,
            results = results,
        )
    }
}

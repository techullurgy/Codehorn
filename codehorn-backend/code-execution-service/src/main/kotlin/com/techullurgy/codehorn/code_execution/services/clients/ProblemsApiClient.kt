package com.techullurgy.codehorn.code_execution.services.clients

import com.techullurgy.codehorn.common.models.Language
import com.techullurgy.codehorn.common.responses.ProblemForExecutionResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange


@HttpExchange("http://problems-service")
interface ProblemsApiClient {

    @GetExchange("/problems/execution/sample/{problemId}/{language}")
    suspend fun getProblemForSampleExecution(
        @PathVariable("problemId") problemId: String,
        @PathVariable("language") language: Language
    ): ProblemForExecutionResponse

    @GetExchange("/problems/execution/full/{problemId}/{language}")
    suspend fun getProblemForFullExecution(
        @PathVariable("problemId") problemId: String,
        @PathVariable("language") language: Language
    ): ProblemForExecutionResponse
}
package com.techullurgy.codehorn.code_execution.services.clients

import com.techullurgy.codehorn.common.models.ExecutionResult
import com.techullurgy.codehorn.common.requests.ExecutionRequest
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange(url = "http://java-execution-service/api/v1/java")
interface JavaExecutionApiClient {

    @PostExchange("execute")
    suspend fun executeJavaCode(@RequestBody request: ExecutionRequest): Set<ExecutionResult>
}
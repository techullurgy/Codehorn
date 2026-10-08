package com.techullurgy.codehorn.cpp_execution.services

import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluator
import com.techullurgy.codehorn.common.code.execution.services.CompilationResult
import com.techullurgy.codehorn.common.code.execution.services.EnvProvider
import com.techullurgy.codehorn.common.code.execution.services.EvaluationRequest
import com.techullurgy.codehorn.common.code.execution.services.RunResult
import com.techullurgy.codehorn.common.code.execution.services.client.CodeExecutorClient

internal class CppCodeEvaluator(
    evaluationId: String,
    private val envProvider: EnvProvider
): CodeEvaluator("evaluation/cpp/$evaluationId") {

    companion object {
        private const val BASE_IMAGE = "gcc:2.3.5"
        private const val WORKING_DIR_IN_CONTAINER = "/tmp"
    }

    private val codeClient by lazy {
        CodeExecutorClient(
            envProvider = envProvider,
            baseImage = BASE_IMAGE,
            workingDirInContainer = WORKING_DIR_IN_CONTAINER
        )
    }

    override val srcFile: String
        get() = "$rootDir/Main.cpp"

    override fun compile(request: EvaluationRequest): CompilationResult {
        return CompilationResult.Ok
    }

    override fun run(request: EvaluationRequest): Set<RunResult> {
        return emptySet()
    }
}
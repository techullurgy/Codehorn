package com.techullurgy.codehorn.python_execution.services


import com.techullurgy.codehorn.common.code.execution.services.*
import com.techullurgy.codehorn.common.code.execution.services.client.CodeExecutorClient

internal class PythonCodeEvaluator(
    evaluationId: String,
    private val envProvider: EnvProvider
) : CodeEvaluator("evaluation/python/$evaluationId") {
    companion object {
        private const val BASE_IMAGE = "python:3.12.15-slim"
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
        get() = "$rootDir/Main.py"

    override fun compile(request: EvaluationRequest): CompilationResult {
        return CompilationResult.Ok
    }

    override fun run(request: EvaluationRequest): Set<RunResult> {
        return emptySet()
    }
}
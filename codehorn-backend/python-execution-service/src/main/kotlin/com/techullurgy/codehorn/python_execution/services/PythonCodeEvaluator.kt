package com.techullurgy.codehorn.python_execution.services


import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluator
import com.techullurgy.codehorn.common.code.execution.services.CompilationResult
import com.techullurgy.codehorn.common.code.execution.services.EvaluationRequest
import com.techullurgy.codehorn.common.code.execution.services.RunResult
import com.techullurgy.codehorn.common.code.execution.services.client.CodeExecutorClient

class PythonCodeEvaluator(
    private val evaluationId: String
) : CodeEvaluator("evaluation/python/$evaluationId") {
    companion object {
        private const val BASE_IMAGE = "python:25"
        private const val WORKING_DIR_IN_CONTAINER = "/tmp"
    }

    private val codeClient by lazy {
        CodeExecutorClient(
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
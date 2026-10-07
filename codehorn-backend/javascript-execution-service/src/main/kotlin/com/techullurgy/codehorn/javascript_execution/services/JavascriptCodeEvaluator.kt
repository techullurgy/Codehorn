package com.techullurgy.codehorn.javascript_execution.services

import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluator
import com.techullurgy.codehorn.common.code.execution.services.CompilationResult
import com.techullurgy.codehorn.common.code.execution.services.EvaluationRequest
import com.techullurgy.codehorn.common.code.execution.services.RunResult
import com.techullurgy.codehorn.common.code.execution.services.client.CodeExecutorClient

class JavascriptCodeEvaluator(
    private val evaluationId: String
) : CodeEvaluator("evaluation/javascript/$evaluationId") {
    companion object {
        private const val BASE_IMAGE = "node:25"
        private const val WORKING_DIR_IN_CONTAINER = "/tmp"
    }

    private val codeClient by lazy {
        CodeExecutorClient(
            baseImage = BASE_IMAGE,
            workingDirInContainer = WORKING_DIR_IN_CONTAINER
        )
    }

    override val srcFile: String
        get() = "$rootDir/Main.js"

    override fun compile(request: EvaluationRequest): CompilationResult {
        return CompilationResult.Ok
    }

    override fun run(request: EvaluationRequest): Set<RunResult> {
        return emptySet()
    }
}
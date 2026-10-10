package com.techullurgy.codehorn.python_execution.services


import com.techullurgy.codehorn.common.code.execution.services.*
import com.techullurgy.codehorn.common.code.execution.services.client.CodeExecutorClient
import com.techullurgy.codehorn.common.code.execution.services.client.ContainerId
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

internal class PythonCodeEvaluator(
    private val evaluationId: String,
    private val envProvider: EnvProvider
) : CodeEvaluator("evaluation/python/$evaluationId") {
    companion object {
        const val BASE_IMAGE = "python:3.12.15-slim"
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
        // python3 -m py_compile Main.py


        var containerId: ContainerId? = null

        try {
            containerId = codeClient.createContainer(
                "python3", "-m", "py_compile", "Main.py"
            )

            codeClient.uploadFile(
                containerId = containerId,
                hostAbsolutePath = File(srcFile).absolutePath,
                containerPath = "$WORKING_DIR_IN_CONTAINER/"
            )

            codeClient.startContainer(containerId)

            val exitCode = codeClient.waitForContainer(containerId, 6)

            return if(exitCode == 0) {
                codeClient.downloadFile(
                    containerId = containerId,
                    hostAbsoluteDirPath = File("$rootDir/__pycache__").absolutePath,
                    containerPath = "$WORKING_DIR_IN_CONTAINER/__pycache__"
                )

                Files.walk(Path.of(File("$rootDir/__pycache__").absolutePath))
                    .forEach {
                        println("Download file: ${it.toRealPath().toUri()}")
                    }

                CompilationResult.Ok
            } else {
                val logStream = codeClient.extractLogStream(containerId)

                CompilationResult.Error(logStream.stderr)
            }
        } catch (e: Throwable) {
            System.err.println("Error encountered during judge pipeline execution: EVAL_ID[$evaluationId] -> " + e.message);
            e.printStackTrace()
            throw e
        } finally {
            if (containerId != null) {
                try {
                    println("Cleaning up: Removing container... -> $containerId")
                    codeClient.removeContainer(containerId)
                } catch (e: Exception) {
                    System.err.println("Failed to remove container -> $containerId: " + e.message)
                }
            }
        }
    }

    override fun run(request: EvaluationRequest): Set<RunResult> {
        // python3 __pycache__/Main_{py_version}.pyc
        return emptySet()
    }
}
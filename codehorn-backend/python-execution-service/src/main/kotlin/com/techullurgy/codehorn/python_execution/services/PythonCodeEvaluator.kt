package com.techullurgy.codehorn.python_execution.services


import com.techullurgy.codehorn.common.code.execution.services.*
import com.techullurgy.codehorn.common.code.execution.services.client.CodeExecutorClient
import com.techullurgy.codehorn.common.code.execution.services.client.ContainerId
import com.techullurgy.codehorn.common.models.ParsedTestcase
import java.io.File
import java.util.concurrent.TimeoutException

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

//                Files.walk(Path.of(File("$rootDir/__pycache__").absolutePath))
//                    .forEach {
//                        println("Download file: ${it.toRealPath().toUri()}")
//                    }

                CompilationResult.Ok
            } else {
                val logStream = codeClient.extractLogStream(containerId)

                CompilationResult.Error(logStream.stderr)
            }
        } catch (e: Throwable) {
            System.err.println("Error encountered during judge pipeline execution: EVAL_ID[$evaluationId] -> " + e.message)
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
        var containerId: ContainerId? = null

        val results = mutableSetOf<RunResult>()

        request.testcases.forEach { testcase ->
            try {
                containerId = createRunContainer()

                uploadPycFile(containerId)

                uploadTestcase(containerId, testcase)

                codeClient.startContainer(containerId)

                val exitCode = extractRunExitCode(containerId)

                val logStream = codeClient.extractLogStream(containerId)

                val verdict = when(exitCode) {
                    CODE_ACCEPTED -> RunVerdict.Accepted
                    CODE_WRONG_ANSWER -> RunVerdict.WrongAnswer
                    CODE_MEMORY_LIMIT_EXCEEDED -> RunVerdict.MemoryLimitExceeded
                    CODE_TIME_LIMIT_EXCEEDED -> RunVerdict.TimeLimitExceeded
                    else -> RunVerdict.Error(logStream.stderr)
                }

                val expected = codeClient.downloadSingleFileAsString(
                    containerId = containerId,
                    containerFilePath = "$WORKING_DIR_IN_CONTAINER/expected.txt"
                )

                val actual = try {
                    codeClient.downloadSingleFileAsString(
                        containerId = containerId,
                        containerFilePath = "$WORKING_DIR_IN_CONTAINER/actual.txt"
                    )
                } catch (e: NoSuchFileException) {
                    println(e.message)
                    ""
                }

                results.add(
                    RunResult(
                        testcaseId = testcase.id,
                        verdict = verdict,
                        expected = expected,
                        actual = actual,
                        stdout = logStream.stdout,
                        stderr = logStream.stderr,
                    )
                )
            } catch (e: Throwable) {
                System.err.println("Error encountered during judge pipeline execution: " + e.message)
                e.printStackTrace()
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

        return results
    }

    private fun createRunContainer(): ContainerId {
        return codeClient.createContainer(
            "timeout", "-s", "KILL", "6s", "sh", "-c", "python3 ./Main.cpython-312.pyc",
            envs = listOf(
                "$ENV_CODE_ACCEPTED=$CODE_ACCEPTED",
                "$ENV_CODE_WRONG_ANSWER=$CODE_WRONG_ANSWER",
                "$ENV_CODE_TIME_LIMIT_EXCEEDED=$CODE_TIME_LIMIT_EXCEEDED",
                "$ENV_CODE_MEMORY_LIMIT_EXCEEDED=$CODE_MEMORY_LIMIT_EXCEEDED",
            )
        )
    }

    private fun extractRunExitCode(containerId: ContainerId): Int {
        return try {
            codeClient.waitForContainer(containerId, 6)
        } catch (_: TimeoutException) { CODE_TIME_LIMIT_EXCEEDED }
    }

    private fun uploadPycFile(containerId: ContainerId) {
        val absPath = File("$rootDir/__pycache__/Main.cpython-312.pyc").absolutePath
        codeClient.uploadFile(
            containerId = containerId,
            hostAbsolutePath = absPath,
            containerPath = "$WORKING_DIR_IN_CONTAINER/"
        )
    }

    private fun uploadTestcase(containerId: ContainerId, testcase: ParsedTestcase) {
        codeClient.uploadFile(
            containerId = containerId,
            hostAbsolutePath = "$rootDir/testcase_${testcase.id}/testcase.txt",
            containerPath = "$WORKING_DIR_IN_CONTAINER/"
        )
    }
}
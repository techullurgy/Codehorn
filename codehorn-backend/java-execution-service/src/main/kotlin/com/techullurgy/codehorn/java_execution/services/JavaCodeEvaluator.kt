package com.techullurgy.codehorn.java_execution.services


import com.techullurgy.codehorn.common.code.execution.services.*
import com.techullurgy.codehorn.common.code.execution.services.client.CodeExecutorClient
import com.techullurgy.codehorn.common.code.execution.services.client.ContainerId
import com.techullurgy.codehorn.common.models.ParsedTestcase
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeoutException

internal class JavaCodeEvaluator(
    private val evaluationId: String,
    private val envProvider: EnvProvider
): CodeEvaluator("evaluation/java/$evaluationId") {

    companion object {
        const val BASE_IMAGE = "amazoncorretto:25"
        private const val WORKING_DIR_IN_CONTAINER = "/tmp"
    }

    private val codeClient by lazy {
        CodeExecutorClient(
            envProvider = envProvider,
            baseImage = BASE_IMAGE,
            workingDirInContainer = WORKING_DIR_IN_CONTAINER,
        )
    }

    override val srcFile: String
        get() = "$rootDir/Main.java"

    override fun compile(request: EvaluationRequest): CompilationResult {
        var containerId: ContainerId? = null

        try {
            containerId = codeClient.createContainer(
                "javac", "-d", "generated_classes", "Main.java"
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
                    hostAbsoluteDirPath = File("$rootDir/generated_classes").absolutePath,
                    containerPath = "$WORKING_DIR_IN_CONTAINER/generated_classes"
                )

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
        var containerId: ContainerId? = null

        val results = mutableSetOf<RunResult>()

        request.testcases.forEach { testcase ->
            try {
                containerId = createRunContainer()

                uploadGeneratedClasses(containerId)

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
                    e.printStackTrace()
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
                System.err.println("Error encountered during judge pipeline execution: " + e.message);
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
            "java", "Main",
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

    private fun uploadGeneratedClasses(containerId: ContainerId) {
        val path = Paths.get(File("$rootDir/generated_classes/").absolutePath)
        Files.walk(path).use {
            it
                .filter(Files::isRegularFile) // Excludes directories from output
                .forEach { p ->
                    val absPath = p.toAbsolutePath().toFile().absolutePath

                    codeClient.uploadFile(
                        containerId = containerId,
                        hostAbsolutePath = absPath,
                        containerPath = "$WORKING_DIR_IN_CONTAINER/"
                    )
                }
        }
    }

    private fun uploadTestcase(containerId: ContainerId, testcase: ParsedTestcase) {
        codeClient.uploadFile(
            containerId = containerId,
            hostAbsolutePath = "$rootDir/testcase_${testcase.id}/testcase.txt",
            containerPath = "$WORKING_DIR_IN_CONTAINER/"
        )
    }
}
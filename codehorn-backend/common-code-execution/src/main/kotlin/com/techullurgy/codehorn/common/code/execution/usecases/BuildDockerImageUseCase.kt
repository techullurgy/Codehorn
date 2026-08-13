package com.techullurgy.codehorn.common.code.execution.usecases

import com.techullurgy.codehorn.common.code.execution.utils.Compiler
import com.techullurgy.codehorn.common.code.execution.utils.errors
import com.techullurgy.codehorn.common.code.execution.utils.outputs
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Component
class BuildDockerImageUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend operator fun invoke(
        userFolderPath: String,
        executionId: String
    ): Boolean = withContext(Dispatchers.IO) {
        val imageName = "${Compiler.BASE_IMAGE_PREFIX}-$executionId".lowercase()

        val builder = ProcessBuilder("docker", "build", "-t", imageName, userFolderPath)
        val process = builder.start()

        val startTime = System.currentTimeMillis()
        val exitCode = process.waitFor()
        val endTime = System.currentTimeMillis()

        logger.info("Building docker image $imageName takes ${endTime - startTime} ms")

        if (exitCode == 0) {
            logger.info("Docker image $imageName created successfully")
            true
        } else {
            logger.info("Build logs: {}", process.outputs())
            logger.info("Build errors: {}", process.errors())
            false
        }
    }
}
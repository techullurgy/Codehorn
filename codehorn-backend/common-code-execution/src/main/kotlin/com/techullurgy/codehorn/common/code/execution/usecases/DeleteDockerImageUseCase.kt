package com.techullurgy.codehorn.common.code.execution.usecases

import com.techullurgy.codehorn.common.code.execution.utils.Compiler
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Component
class DeleteDockerImageUseCase {
    suspend operator fun invoke(executionId: String): Unit = withContext(Dispatchers.IO) {
        val imageName = "${Compiler.BASE_IMAGE_PREFIX}-$executionId".lowercase()
        val builder = ProcessBuilder("docker", "rmi", "$imageName:latest")
        val process = builder.start()
        val isNotAborted = process.waitFor(10, TimeUnit.SECONDS)

        if (isNotAborted) {
            if (process.exitValue() == 0) {
                println("$imageName successfully deleted from docker registry")
            } else {
                println("$imageName not able to delete from docker registry")
            }
        }
    }
}
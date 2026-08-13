package com.techullurgy.codehorn.common.code.execution.usecases

import com.techullurgy.codehorn.common.code.execution.providers.ExecutionParametersProvider
import com.techullurgy.codehorn.common.code.execution.providers.VolumeMountPathProvider
import com.techullurgy.codehorn.common.code.execution.services.FileService
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Component

@Component
class CreateDockerFileUseCase(
    private val executionParametersProvider: ExecutionParametersProvider,
    private val volumeMountPathProvider: ObjectProvider<VolumeMountPathProvider>
) {
    operator fun invoke(userFolderPath: String, executionId: String) {
        val compiler = executionParametersProvider.compiler
        val fileName = executionParametersProvider.codeFileName

        val destinationBasePath = volumeMountPathProvider.getObject(executionId).provideDestinationVolumeMountPath()

        val dockerFileContent = """
            |FROM $compiler
            |ADD ./$fileName $destinationBasePath/$fileName
            |COPY ./testcases $destinationBasePath/testcases
            |ADD ./entrypoint.sh $destinationBasePath/entrypoint.sh
            |ENV MOUNT_BASE_PATH $destinationBasePath
            |WORKDIR $destinationBasePath
            |RUN chmod +x entrypoint.sh
            |ENTRYPOINT ["./entrypoint.sh"]
        """.trimMargin()

        FileService.writeFile(filePath = "$userFolderPath/Dockerfile", value = dockerFileContent)
    }
}
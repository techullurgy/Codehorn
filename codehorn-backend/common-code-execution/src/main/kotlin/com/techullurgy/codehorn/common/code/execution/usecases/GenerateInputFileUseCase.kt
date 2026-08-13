package com.techullurgy.codehorn.common.code.execution.usecases

import com.techullurgy.codehorn.common.code.execution.providers.ExecutionParametersProvider
import com.techullurgy.codehorn.common.code.execution.services.FileService
import org.springframework.stereotype.Component

@Component
class GenerateInputFileUseCase(
    private val parametersProvider: ExecutionParametersProvider
) {
    operator fun invoke(
        userFolderPath: String,
        fileContent: String
    ) {
        FileService.writeFile("$userFolderPath/${parametersProvider.codeFileName}", fileContent)
    }
}
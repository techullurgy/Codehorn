package com.techullurgy.codehorn.common.code.execution.services

import com.techullurgy.codehorn.common.code.execution.usecases.BuildDockerImageUseCase
import com.techullurgy.codehorn.common.code.execution.usecases.CreateDockerFileUseCase
import com.techullurgy.codehorn.common.code.execution.usecases.CreateEntryPointFileUseCase
import com.techullurgy.codehorn.common.code.execution.usecases.CreateNecessaryTestcaseFilesUseCase
import com.techullurgy.codehorn.common.code.execution.usecases.DeleteDockerImageUseCase
import com.techullurgy.codehorn.common.code.execution.usecases.ExecuteForResultsUseCase
import com.techullurgy.codehorn.common.code.execution.usecases.GenerateInputFileUseCase
import com.techullurgy.codehorn.common.code.execution.usecases.GenerateTestcaseResultsUseCase
import com.techullurgy.codehorn.common.models.ParsedTestcase
import com.techullurgy.codehorn.common.models.TestcaseResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.io.File

@Component
class CodeExecutionService {
    @Autowired private lateinit var createEntryPointFileUseCase: CreateEntryPointFileUseCase
    @Autowired private lateinit var createDockerFileUseCase: CreateDockerFileUseCase
    @Autowired private lateinit var buildDockerImageUseCase: BuildDockerImageUseCase
    @Autowired private lateinit var createNecessaryTestcaseFilesUseCase: CreateNecessaryTestcaseFilesUseCase
    @Autowired private lateinit var executeForResults: ExecuteForResultsUseCase
    @Autowired private lateinit var generateInputFileUseCase: GenerateInputFileUseCase
    @Autowired private lateinit var deleteDockerImageUseCase: DeleteDockerImageUseCase
    @Autowired private lateinit var generateTestcaseResultsUseCase: GenerateTestcaseResultsUseCase

    suspend fun executeFor(
        executionId: String,
        folder: File,
        fileContent: String,
        testcases: List<ParsedTestcase>,
    ): List<TestcaseResult> {
        generateInputFileUseCase(folder.path, fileContent)

        createNecessaryTestcaseFilesUseCase(folder.path, testcases)

        createEntryPointFileUseCase(folder.path, testcases)

        createDockerFileUseCase(folder.path, executionId)

        val isCreated = buildDockerImageUseCase(folder.path, executionId)

        val results = executeForResults(executionId, testcases, isCreated)

        if(isCreated) {
            deleteDockerImageUseCase(executionId)
        }

        return generateTestcaseResultsUseCase(folder, testcases, results)
    }
}
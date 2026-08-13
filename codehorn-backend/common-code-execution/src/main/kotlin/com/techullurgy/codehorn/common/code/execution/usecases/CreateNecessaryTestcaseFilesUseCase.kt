package com.techullurgy.codehorn.common.code.execution.usecases

import com.techullurgy.codehorn.common.code.execution.services.FileService
import com.techullurgy.codehorn.common.models.ParsedTestcase
import org.springframework.stereotype.Component

@Component
class CreateNecessaryTestcaseFilesUseCase {
    operator fun invoke(userFolderPath: String, testcases: List<ParsedTestcase>) {
        testcases.filter { !it.isHidden }.forEach {
            val testcaseFilePath = "$userFolderPath/testcases/sample-input${it.id}.txt"
            FileService.writeFile(testcaseFilePath, it.testcase)
        }

        testcases.filter { it.isHidden }.forEach {
            val testcaseFilePath = "$userFolderPath/testcases/hidden-input${it.id}.txt"
            FileService.writeFile(testcaseFilePath, it.testcase)
        }
    }
}
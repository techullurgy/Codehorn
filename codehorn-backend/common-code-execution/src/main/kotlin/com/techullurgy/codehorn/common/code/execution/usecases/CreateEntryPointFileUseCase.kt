package com.techullurgy.codehorn.common.code.execution.usecases

import com.techullurgy.codehorn.common.code.execution.providers.DefaultTestcaseProvider
import com.techullurgy.codehorn.common.code.execution.providers.EntryPointProvider
import com.techullurgy.codehorn.common.code.execution.services.FileService
import com.techullurgy.codehorn.common.models.ParsedTestcase
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CreateEntryPointFileUseCase {

    @Autowired
    private lateinit var entryPointProvider: ObjectProvider<EntryPointProvider>

    operator fun invoke(userFolderPath: String, testcases: List<ParsedTestcase>) {
        val testcaseProvider = DefaultTestcaseProvider(
            sampleTestcases = testcases.filter { !it.isHidden }.map { it.id }.toSet(),
            hiddenTestcases = testcases.filter { it.isHidden }.map { it.id }.toSet()
        )
        val entryPointContent = entryPointProvider.getObject(testcaseProvider).provide()
        FileService.writeFile(filePath = "$userFolderPath/entrypoint.sh", value = entryPointContent)
    }
}
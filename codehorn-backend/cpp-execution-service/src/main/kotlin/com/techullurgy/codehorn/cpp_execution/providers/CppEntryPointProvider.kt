package com.techullurgy.codehorn.cpp_execution.providers

import com.techullurgy.codehorn.common.code.execution.providers.EntryPointProvider
import com.techullurgy.codehorn.common.code.execution.providers.TestcaseProvider
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class CppEntryPointProvider(
    testcaseProvider: TestcaseProvider
) : EntryPointProvider(testcaseProvider) {

    override fun compileCommand(): String = "g++ -o main Main.cpp"

    override fun runCommand(): String = "./main"
}

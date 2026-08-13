package com.techullurgy.codehorn.python_execution.providers

import com.techullurgy.codehorn.common.code.execution.providers.EntryPointProvider
import com.techullurgy.codehorn.common.code.execution.providers.TestcaseProvider
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class PythonEntryPointProvider(
    testcaseProvider: TestcaseProvider
) : EntryPointProvider(testcaseProvider) {

    override fun runCommand(): String = "python3 Main.py"
}

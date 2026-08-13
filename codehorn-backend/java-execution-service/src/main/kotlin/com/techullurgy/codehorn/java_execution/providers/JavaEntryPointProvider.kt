package com.techullurgy.codehorn.java_execution.providers

import com.techullurgy.codehorn.common.code.execution.providers.EntryPointProvider
import com.techullurgy.codehorn.common.code.execution.providers.TestcaseProvider
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class JavaEntryPointProvider(
    testcaseProvider: TestcaseProvider
) : EntryPointProvider(testcaseProvider) {

    override fun compileCommand(): String = "javac Main.java"

    override fun runCommand(): String = "java Main"
}

package com.techullurgy.codehorn.javascript_execution.providers

import com.techullurgy.codehorn.common.code.execution.providers.ExecutionParametersProvider
import com.techullurgy.codehorn.common.code.execution.utils.Compiler
import org.springframework.stereotype.Component

@Component
class JavascriptExecutionParametersProvider : ExecutionParametersProvider {
    override val codeFileName: String = Compiler.JAVASCRIPT_INPUT_FILE_NAME
    override val compiler: String = Compiler.FROM_DOCKER_IMAGE_FOR_JAVASCRIPT_COMPILER
}

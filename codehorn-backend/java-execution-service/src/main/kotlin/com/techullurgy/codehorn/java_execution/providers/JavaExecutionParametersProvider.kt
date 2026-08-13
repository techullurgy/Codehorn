package com.techullurgy.codehorn.java_execution.providers

import com.techullurgy.codehorn.common.code.execution.providers.ExecutionParametersProvider
import com.techullurgy.codehorn.common.code.execution.utils.Compiler
import org.springframework.stereotype.Component

@Component
class JavaExecutionParametersProvider : ExecutionParametersProvider {
    override val codeFileName: String = Compiler.JAVA_INPUT_FILE_NAME
    override val compiler: String = Compiler.FROM_DOCKER_IMAGE_FOR_JAVA_COMPILER
}

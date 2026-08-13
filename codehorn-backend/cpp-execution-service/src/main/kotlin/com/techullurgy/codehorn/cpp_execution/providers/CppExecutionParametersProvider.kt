package com.techullurgy.codehorn.cpp_execution.providers

import com.techullurgy.codehorn.common.code.execution.providers.ExecutionParametersProvider
import com.techullurgy.codehorn.common.code.execution.utils.Compiler
import org.springframework.stereotype.Component

@Component
class CppExecutionParametersProvider : ExecutionParametersProvider {
    override val codeFileName: String = Compiler.CPP_INPUT_FILE_NAME
    override val compiler: String = Compiler.FROM_DOCKER_IMAGE_FOR_CPP_COMPILER
}

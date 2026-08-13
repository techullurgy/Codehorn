package com.techullurgy.codehorn.python_execution.providers

import com.techullurgy.codehorn.common.code.execution.providers.ExecutionParametersProvider
import com.techullurgy.codehorn.common.code.execution.utils.Compiler
import org.springframework.stereotype.Component

@Component
class PythonExecutionParametersProvider : ExecutionParametersProvider {
    override val codeFileName: String = Compiler.PYTHON_INPUT_FILE_NAME
    override val compiler: String = Compiler.FROM_DOCKER_IMAGE_FOR_PYTHON3_COMPILER
}

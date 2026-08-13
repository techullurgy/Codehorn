package com.techullurgy.codehorn.python_execution.providers

import com.techullurgy.codehorn.common.code.execution.providers.VolumeMountPathProvider
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class PythonVolumeMountPathProvider(
    private val executionId: String
) : VolumeMountPathProvider {

    override fun provideSourceVolumeMountPath(): String = "/tmp/codehorn/executions/python/$executionId"

    override fun provideDestinationVolumeMountPath(): String = "/app"
}

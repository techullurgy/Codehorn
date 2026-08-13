package com.techullurgy.codehorn.javascript_execution.providers

import com.techullurgy.codehorn.common.code.execution.providers.VolumeMountPathProvider
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class JavascriptVolumeMountPathProvider(
    private val executionId: String
) : VolumeMountPathProvider {

    override fun provideSourceVolumeMountPath(): String = "/tmp/codehorn/executions/javascript/$executionId"

    override fun provideDestinationVolumeMountPath(): String = "/app"
}

package com.techullurgy.codehorn.common.code.execution.providers

interface VolumeMountPathProvider {
    fun provideSourceVolumeMountPath(): String
    fun provideDestinationVolumeMountPath(): String
}
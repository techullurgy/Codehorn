package com.techullurgy.codehorn.common.code.execution.services

import com.techullurgy.codehorn.common.code.execution.providers.VolumeMountPathProvider
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.io.Closeable
import java.io.File

class UserFolderCreator(
    executionId: String,
    volumeMountPathProvider: ObjectProvider<VolumeMountPathProvider>
): Closeable {
    val folder = File(
        volumeMountPathProvider.getObject(executionId).provideSourceVolumeMountPath(),
    ).apply { mkdirs() }

    init {
        File("${folder.path}/outputs").mkdir()
    }

    override fun close() {
//        folder.deleteRecursively()
    }
}
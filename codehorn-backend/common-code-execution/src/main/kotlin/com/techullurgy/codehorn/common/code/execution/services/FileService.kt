package com.techullurgy.codehorn.common.code.execution.services

import java.io.File
import java.io.FileWriter

internal object FileService {
    fun writeFile(filePath: String, value: String) {
        val file = File(filePath)
        file.createDirectoriesIfNotExists()
        file.createNewFile()
        FileWriter(file).use {
            it.write(value)
        }
    }

    fun deleteFile(filePath: String) {
        val file = File(filePath)
        file.delete()
    }

    fun getContentFromFile(filePath: String): String {
        val file = File(filePath)
        return file.readText()
    }

    private fun File.createDirectoriesIfNotExists() {
        val folder = path.substring(0, path.lastIndexOf('/'))
        val tempFile = File(folder)
        tempFile.mkdirs()
    }
}
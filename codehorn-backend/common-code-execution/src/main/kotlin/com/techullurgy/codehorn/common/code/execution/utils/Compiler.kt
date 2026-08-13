package com.techullurgy.codehorn.common.code.execution.utils

object Compiler {
    const val FROM_DOCKER_IMAGE_FOR_C_COMPILER = "gcc:9.5.0"
    const val FROM_DOCKER_IMAGE_FOR_JAVA_COMPILER = "amazoncorretto:21"
    const val FROM_DOCKER_IMAGE_FOR_JAVASCRIPT_COMPILER = "node:21"
    const val FROM_DOCKER_IMAGE_FOR_CPP_COMPILER = "gcc:9.5.0"
    const val FROM_DOCKER_IMAGE_FOR_PYTHON3_COMPILER = "gcc:9.5.0"

    const val C_INPUT_FILE_NAME = "Main.c"
    const val CPP_INPUT_FILE_NAME = "Main.cpp"
    const val JAVA_INPUT_FILE_NAME = "Main.java"
    const val PYTHON_INPUT_FILE_NAME = "Main.py"
    const val JAVASCRIPT_INPUT_FILE_NAME = "Main.js"

    const val BASE_IMAGE_PREFIX = "code-image"
}
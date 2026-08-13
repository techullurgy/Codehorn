package com.techullurgy.codehorn.common.code.execution.providers

interface ExecutionParametersProvider {
    val codeFileName: String
    val compiler: String
}
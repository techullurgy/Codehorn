package com.techullurgy.codehorn.common.code.execution.services

fun interface EnvProvider {
    fun get(name: String): String?

    companion object {
        val Default: EnvProvider = EnvProvider { System.getenv(it) }
    }
}
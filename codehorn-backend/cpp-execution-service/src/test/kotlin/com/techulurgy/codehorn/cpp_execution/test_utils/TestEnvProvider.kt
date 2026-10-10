package com.techulurgy.codehorn.cpp_execution.test_utils

import com.techullurgy.codehorn.common.code.execution.services.EnvProvider

internal class TestEnvProvider(
    private val envMap: Map<String, String>
): EnvProvider {
    override fun get(name: String): String? = envMap[name]
}
package com.techullurgy.codehorn.common.code.execution.providers

interface TestcaseProvider {
    fun sampleIds(): Set<String>
    fun hiddenIds(): Set<String>
}
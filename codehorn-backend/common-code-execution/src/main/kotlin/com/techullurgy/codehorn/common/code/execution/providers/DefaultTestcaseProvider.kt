package com.techullurgy.codehorn.common.code.execution.providers

class DefaultTestcaseProvider(
    private val sampleTestcases: Set<String>,
    private val hiddenTestcases: Set<String>,
): TestcaseProvider {
    override fun sampleIds(): Set<String> = sampleTestcases
    override fun hiddenIds(): Set<String> = hiddenTestcases
}
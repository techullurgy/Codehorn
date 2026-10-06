package com.techullurgy.codehorn.common.code.execution.services

interface CodeEvaluatorFactory {
    fun provideEvaluator(evaluationId: String): CodeEvaluator
}
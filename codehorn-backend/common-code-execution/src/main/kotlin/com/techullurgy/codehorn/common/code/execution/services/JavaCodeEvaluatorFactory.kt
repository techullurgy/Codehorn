package com.techullurgy.codehorn.common.code.execution.services

import org.springframework.stereotype.Component

@Component
class JavaCodeEvaluatorFactory: CodeEvaluatorFactory {
    override fun provideEvaluator(evaluationId: String): CodeEvaluator = JavaCodeEvaluator(evaluationId)
}
package com.techullurgy.codehorn.java_execution.services

import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluator
import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluatorFactory
import org.springframework.stereotype.Component

@Component
class JavaCodeEvaluatorFactory: CodeEvaluatorFactory {
    override fun provideEvaluator(evaluationId: String): CodeEvaluator = JavaCodeEvaluator(evaluationId)
}
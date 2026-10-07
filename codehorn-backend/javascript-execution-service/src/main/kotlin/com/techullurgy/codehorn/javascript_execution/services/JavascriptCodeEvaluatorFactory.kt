package com.techullurgy.codehorn.javascript_execution.services

import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluator
import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluatorFactory
import org.springframework.stereotype.Component

@Component
class JavascriptCodeEvaluatorFactory: CodeEvaluatorFactory {
    override fun provideEvaluator(evaluationId: String): CodeEvaluator = JavascriptCodeEvaluator(evaluationId)
}
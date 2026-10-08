package com.techullurgy.codehorn.javascript_execution.services

import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluator
import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluatorFactory
import com.techullurgy.codehorn.common.code.execution.services.EnvProvider
import org.springframework.stereotype.Component

@Component
class JavascriptCodeEvaluatorFactory(
    private val envProvider: EnvProvider
): CodeEvaluatorFactory {
    override fun provideEvaluator(evaluationId: String): CodeEvaluator = JavascriptCodeEvaluator(evaluationId, envProvider)
}
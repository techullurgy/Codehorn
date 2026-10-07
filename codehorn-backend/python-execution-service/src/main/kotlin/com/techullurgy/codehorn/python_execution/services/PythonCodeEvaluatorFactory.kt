package com.techullurgy.codehorn.python_execution.services

import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluator
import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluatorFactory
import org.springframework.stereotype.Component

@Component
class PythonCodeEvaluatorFactory: CodeEvaluatorFactory {
    override fun provideEvaluator(evaluationId: String): CodeEvaluator = PythonCodeEvaluator(evaluationId)
}
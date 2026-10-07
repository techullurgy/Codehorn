package com.techullurgy.codehorn.cpp_execution.services

import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluator
import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluatorFactory
import org.springframework.stereotype.Component

@Component
class CppCodeEvaluatorFactory: CodeEvaluatorFactory {
    override fun provideEvaluator(evaluationId: String): CodeEvaluator = CppCodeEvaluator(evaluationId)
}
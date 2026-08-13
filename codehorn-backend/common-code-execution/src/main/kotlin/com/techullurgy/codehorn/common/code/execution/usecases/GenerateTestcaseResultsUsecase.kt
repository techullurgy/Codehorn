package com.techullurgy.codehorn.common.code.execution.usecases

import com.techullurgy.codehorn.common.code.execution.services.FileService
import com.techullurgy.codehorn.common.models.CodeSubmissionResult
import com.techullurgy.codehorn.common.models.ParsedTestcase
import com.techullurgy.codehorn.common.models.ProblemTestcase
import com.techullurgy.codehorn.common.models.TestcaseResult
import org.springframework.stereotype.Component
import java.io.File

@Component
class GenerateTestcaseResultsUseCase {
    operator fun invoke(
        userFolder: File,
        testcases: List<ParsedTestcase>,
        results: Map<String, CodeSubmissionResult>
    ): List<TestcaseResult> {
        return results.map {
            val currentTestcase = testcases.find { t ->  t.id == it.key }!!

            val compilationError = if(it.value == CodeSubmissionResult.CompilationError) {
                FileService.getContentFromFile("${userFolder.canonicalPath.removeSuffix("/")}/outputs/compilation_err.log")
            } else ""

            val expectedResult = if(it.value.isResultExists()) {
                FileService.getContentFromFile("${userFolder.canonicalPath.removeSuffix("/")}/outputs/eResult${currentTestcase.id}.txt")
            } else ""

            val yourResult = if(it.value.isResultExists()) {
                FileService.getContentFromFile("${userFolder.canonicalPath.removeSuffix("/")}/outputs/result${currentTestcase.id}.txt")
            } else ""

            val stdout = if(it.value.isStandardOutExists()) {
                if(currentTestcase.isHidden) {
                    FileService.getContentFromFile("${userFolder.canonicalPath.removeSuffix("/")}/outputs/hidden-out${currentTestcase.id}.log")
                } else {
                    FileService.getContentFromFile("${userFolder.canonicalPath.removeSuffix("/")}/outputs/sample-out${currentTestcase.id}.log")
                }
            } else ""

            val stderr = if(it.value.isStandardOutExists()) {
                if(currentTestcase.isHidden) {
                    FileService.getContentFromFile("${userFolder.canonicalPath.removeSuffix("/")}/outputs/hidden-outerr${currentTestcase.id}.log")
                } else {
                    FileService.getContentFromFile("${userFolder.canonicalPath.removeSuffix("/")}/outputs/sample-outerr${currentTestcase.id}.log")
                }
            } else ""

            TestcaseResult(
                testcaseId = currentTestcase.id,
                expectedResult = expectedResult,
                yourResult = yourResult,
                stdout = stdout,
                stderr = stderr,
                compilationError = compilationError,
                result = it.value
            )
        }
    }
}
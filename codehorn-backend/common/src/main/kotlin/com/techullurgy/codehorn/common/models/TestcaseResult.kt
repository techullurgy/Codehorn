package com.techullurgy.codehorn.common.models

data class TestcaseResult(
    val testcaseId: String,
    val expectedResult: String,
    val yourResult: String,
    val stdout: String,
    val stderr: String,
    val compilationError: String,
    val result: CodeSubmissionResult
)
package com.techullurgy.codehorn.common.models

enum class CodeSubmissionResult {
    CompilationError, TimeLimitExceeded, WrongAnswer, Accepted, RuntimeError, NotExecuted;

    fun isResultExists() = this == Accepted || this == WrongAnswer

    fun isStandardOutExists() = this != CompilationError && this != NotExecuted
}
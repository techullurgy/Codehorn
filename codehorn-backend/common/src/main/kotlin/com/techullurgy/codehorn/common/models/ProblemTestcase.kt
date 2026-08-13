package com.techullurgy.codehorn.common.models

data class ProblemTestcase(
    val id: String,
    val isHidden: Boolean = true,
    val inputNames: List<String>,
    val inputs: List<String>,
    val masks: List<Long>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProblemTestcase

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
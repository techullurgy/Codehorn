package com.techullurgy.codehorn.common.code.execution.parsers

import com.techullurgy.codehorn.common.models.ProblemTestcase
import com.techullurgy.codehorn.common.models.TestcaseTypeMasks

abstract class TestcaseParserStrategy {
    abstract fun parse(testcase: ProblemTestcase): String

    protected fun isStringType(mask: Long): Boolean = (mask and TestcaseTypeMasks.STRING_TYPE) != 0L
    protected fun isIntType(mask: Long): Boolean = (mask and TestcaseTypeMasks.INT_TYPE) != 0L
    protected fun isLongType(mask: Long): Boolean = (mask and TestcaseTypeMasks.LONG_TYPE) != 0L
    protected fun isDoubleType(mask: Long): Boolean = (mask and TestcaseTypeMasks.DOUBLE_TYPE) != 0L

    protected fun isSingleType(mask: Long): Boolean {
        return ((mask and TestcaseTypeMasks.SINGLE_TYPE) != 0L) || ((mask and TestcaseTypeMasks.SINGLE_NULL_TYPE) != 0L)
    }

    protected fun is1DList(mask: Long): Boolean {
        return ((mask and TestcaseTypeMasks.LIST_TYPE) != 0L) || ((mask and TestcaseTypeMasks.LIST_NULL_TYPE) != 0L)
    }

    protected fun is2DList(mask: Long): Boolean {
        return ((mask and TestcaseTypeMasks.LIST_LIST_TYPE) != 0L) || ((mask and TestcaseTypeMasks.LIST_LIST_NULL_TYPE) != 0L)
    }

    protected fun isNullAllowed(mask: Long): Boolean {
        return ((mask and TestcaseTypeMasks.SINGLE_NULL_TYPE) != 0L) || ((mask and TestcaseTypeMasks.LIST_NULL_TYPE) != 0L) || ((mask and TestcaseTypeMasks.LIST_LIST_NULL_TYPE) != 0L)
    }
}
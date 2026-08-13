package com.techullurgy.codehorn.common.models

object TestcaseTypeMasks {
    const val INT_TYPE = 1L shl 1
    const val LONG_TYPE = 1L shl 2
    const val DOUBLE_TYPE = 1L shl 3
    const val STRING_TYPE = 1L shl 4

    const val SINGLE_TYPE = 1L shl 11
    const val LIST_TYPE = 1L shl 12
    const val LIST_LIST_TYPE = 1L shl 13
    const val SINGLE_NULL_TYPE = 1L shl 14
    const val LIST_NULL_TYPE = 1L shl 15
    const val LIST_LIST_NULL_TYPE = 1L shl 16
}

enum class TestcaseDataType {
    INT, LONG, DOUBLE, STRING
}

enum class TestcaseCollectionType {
    SINGLE, LIST, LIST_LIST, SINGLE_NULL, LIST_NULL, LIST_LIST_NULL
}

data class TestcaseType(
    private val dataType: TestcaseDataType,
    private val collectionType: TestcaseCollectionType
) {
    val mask: Long
        get() {
            return (
                    when(dataType) {
                        TestcaseDataType.INT -> TestcaseTypeMasks.INT_TYPE
                        TestcaseDataType.LONG -> TestcaseTypeMasks.LONG_TYPE
                        TestcaseDataType.DOUBLE -> TestcaseTypeMasks.DOUBLE_TYPE
                        TestcaseDataType.STRING -> TestcaseTypeMasks.STRING_TYPE
                    }
                    ) or (
                    when(collectionType) {
                        TestcaseCollectionType.SINGLE -> TestcaseTypeMasks.SINGLE_TYPE
                        TestcaseCollectionType.LIST -> TestcaseTypeMasks.LIST_TYPE
                        TestcaseCollectionType.LIST_LIST -> TestcaseTypeMasks.LIST_LIST_TYPE
                        TestcaseCollectionType.SINGLE_NULL -> TestcaseTypeMasks.SINGLE_NULL_TYPE
                        TestcaseCollectionType.LIST_NULL -> TestcaseTypeMasks.LIST_NULL_TYPE
                        TestcaseCollectionType.LIST_LIST_NULL -> TestcaseTypeMasks.LIST_LIST_NULL_TYPE
                    }
                    )
        }

    companion object {
        fun getFromMask(mask: Long): TestcaseType {
            val dataType = when {
                (mask and TestcaseTypeMasks.INT_TYPE) == 0L -> TestcaseDataType.INT
                (mask and TestcaseTypeMasks.LONG_TYPE) == 0L -> TestcaseDataType.LONG
                (mask and TestcaseTypeMasks.DOUBLE_TYPE) == 0L -> TestcaseDataType.DOUBLE
                (mask and TestcaseTypeMasks.STRING_TYPE) == 0L -> TestcaseDataType.STRING
                else -> TODO()
            }

            val collectionType = when {
                (mask and TestcaseTypeMasks.LIST_TYPE) == 0L -> TestcaseCollectionType.LIST
                (mask and TestcaseTypeMasks.LIST_LIST_TYPE) == 0L -> TestcaseCollectionType.LIST_LIST
                (mask and TestcaseTypeMasks.SINGLE_TYPE) == 0L -> TestcaseCollectionType.SINGLE
                (mask and TestcaseTypeMasks.LIST_NULL_TYPE) == 0L -> TestcaseCollectionType.LIST_NULL
                (mask and TestcaseTypeMasks.LIST_LIST_NULL_TYPE) == 0L -> TestcaseCollectionType.LIST_LIST_NULL
                (mask and TestcaseTypeMasks.SINGLE_NULL_TYPE) == 0L -> TestcaseCollectionType.SINGLE_NULL
                else -> TODO()
            }

            return TestcaseType(dataType, collectionType)
        }
    }
}
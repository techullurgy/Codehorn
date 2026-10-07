package com.techullurgy.codehorn.code_execution.services


object FileContentBuilder {
    fun build(
        imports: String,
        utils: String,
        solution: String,
        userCode: String,
        main: String,
    ): String {
        val filledFileContent = """
            $FILE_CONTENT_IMPORTS_REPLACE_CODE
            
            $FILE_CONTENT_UTILS_REPLACE_CODE
            
            $FILE_CONTENT_EXPECTED_SOLUTION_REPLACE_CODE
            
            $FILE_CONTENT_ACTUAL_SOLUTION_REPLACE_CODE
            
            $FILE_CONTENT_MAIN_REPLACE_CODE
        """.trimIndent()
            .replace(FILE_CONTENT_IMPORTS_REPLACE_CODE, imports)
            .replace(FILE_CONTENT_UTILS_REPLACE_CODE, utils)
            .replace(FILE_CONTENT_EXPECTED_SOLUTION_REPLACE_CODE, solution)
            .replace(FILE_CONTENT_ACTUAL_SOLUTION_REPLACE_CODE, userCode)
            .replace(FILE_CONTENT_MAIN_REPLACE_CODE, main)

        return filledFileContent
    }

    private const val FILE_CONTENT_IMPORTS_REPLACE_CODE =           "%$%$#$%#%^&%^$%#$^%$&$%^$%^"
    private const val FILE_CONTENT_UTILS_REPLACE_CODE =             "$#%$%$%^@#$#^$&$%^$%&$%^@#$"
    private const val FILE_CONTENT_EXPECTED_SOLUTION_REPLACE_CODE = "&^$%^$%*%&^&%*#$%#$^%^*&^&*"
    private const val FILE_CONTENT_ACTUAL_SOLUTION_REPLACE_CODE =   "@#$@#$%$^&^*$%%^$%#$%@#$^$%"
    private const val FILE_CONTENT_MAIN_REPLACE_CODE =              "@#$@#$%^$%*^&*^&(^&*$%^%$&^"
}
package com.techullurgy.codehorn.javascript_execution.test_utils

import com.techullurgy.codehorn.common.code.execution.services.templates.utils.JavascriptTemplates

object TestFileContent {
    object AddTwoNumbers {
        fun buildWithUserCode(userCode: String): String {
            return """
                ${JavascriptTemplates.IMPORTS}
                
                ${JavascriptTemplates.UTILS}
                
                function original_AddTwoNumbers(x,y) {
                    return x + y
                }
                
                $userCode
                
                (async () => {
                  await MainUtils.readFromFileAndSaveInMap("/tmp/testcase.txt")
                  
                  console.log("GOOOOOOOOD")
                  
                  const codeAccepted = parseInt(process.env.CODE_ACCEPTED, 10)
                  const codeWrongAnswer = parseInt(process.env.CODE_WRONG_ANSWER, 10)
                  
                  const x = MainUtils.getInteger()
                  const y = MainUtils.getInteger()
                  
                  const expected = original_AddTwoNumbers(x,y)
                  MainUtils.writeExpected(expected)
                  const actual = addTwoNumbers(x,y)
                  MainUtils.writeActual(actual)
                  
                  if(actual == expected) {
                    // Accepted
                    process.exit(codeAccepted)
                  } else {
                    // Wrong Answer
                    process.exit(codeWrongAnswer)
                  }
                })()
            """.trimIndent()
        }
    }
}
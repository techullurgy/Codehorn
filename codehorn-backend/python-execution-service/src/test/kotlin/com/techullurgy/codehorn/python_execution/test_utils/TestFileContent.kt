package com.techullurgy.codehorn.python_execution.test_utils

import com.techullurgy.codehorn.common.code.execution.services.templates.utils.PythonTemplates

object TestFileContent {
    object AddTwoNumbers {
        fun buildWithUserCode(userCode: String): String {
            return """
                ${PythonTemplates.IMPORTS}
                
                ${PythonTemplates.UTILS}
                
                def original_addTwoNumbers(x: int, y: int) -> int:
                  return x + y
                
                $userCode
                
                def main():
                  MainUtils.readFromFileAndSaveInDictionary("/tmp/testcase.txt")
                  
                  accepted_code = int(os.getenv("CODE_ACCEPTED"))
                  wrong_answer_code = int(os.getenv("CODE_WRONG_ANSWER"))
                
                  # Your code starts here
                  x = MainUtils.getInteger()
                  y = MainUtils.getInteger()
                  
                  expected = original_addTwoNumbers(x, y)
                  MainUtils.writeExpected(str(expected))
                  actual = addTwoNumbers(x, y)
                  MainUtils.writeActual(str(actual))
                  
                  if(expected == actual):
                    sys.exit(accepted_code)
                  else:
                    sys.exit(wrong_answer_code)
                  
                  # Your code ends here
                
                if __name__ == "__main__":
                  main()
            """.trimIndent()
        }
    }
}
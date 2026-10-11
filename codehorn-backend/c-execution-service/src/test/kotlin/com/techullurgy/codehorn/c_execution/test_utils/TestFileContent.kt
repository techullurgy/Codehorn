package com.techullurgy.codehorn.c_execution.test_utils

import com.techullurgy.codehorn.common.code.execution.services.templates.utils.CTemplates

object TestFileContent {
    object AddTwoNumbers {
        fun buildWithUserCode(userCode: String): String {
            return """
                ${CTemplates.IMPORTS}
                
                ${CTemplates.UTILS}
                
                int original_addTwoNumbers(int x, int y) {
                    return x + y;
                }
                
                $userCode
                
                int main(int argc, char* argv[]) {
                    __readFromFileAndSaveInMap__("/tmp/testcase.txt");
                    
                    int acceptedCode = atoi(getenv("CODE_ACCEPTED"));
                    int wrongAnswerCode = atoi(getenv("CODE_WRONG_ANSWER"));
                    
                    int x = __getInteger__();
                    int y = __getInteger__();
                    
                    int expected = original_addTwoNumbers(x, y);
                    char expectedStr[12];
                    sprintf(expectedStr, "%d", expected);
                    __writeExpected__(expectedStr);
                    
                    int actual = addTwoNumbers(x, y);
                    char actualStr[12];
                    sprintf(actualStr, "%d", actual);
                    __writeActual__(actualStr);
                    
                    if(expected == actual) {
                        return acceptedCode;
                    }
                    
                    return wrongAnswerCode;
                }
            """.trimIndent()
        }
    }
}
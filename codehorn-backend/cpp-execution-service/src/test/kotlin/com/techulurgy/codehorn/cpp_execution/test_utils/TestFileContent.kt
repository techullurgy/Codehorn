package com.techulurgy.codehorn.cpp_execution.test_utils

import com.techullurgy.codehorn.common.code.execution.services.templates.utils.CppTemplates

object TestFileContent {
    object AddTwoNumbers {
        fun buildWithUserCode(userCode: String): String {
            return """
                ${CppTemplates.IMPORTS}
                
                ${CppTemplates.UTILS}
                
                int original_addTwoNumbers(int x, int y) {
                    return x + y;
                }
                
                $userCode
                
                int main(int argc, char* argv[]) {
                    MainUtils::readFromFileAndSaveInMap("/tmp/testcase.txt");
                    
                    int acceptedCode = std::stoi(std::getenv("CODE_ACCEPTED"));
                    int wrongAnswerCode = std::stoi(std::getenv("CODE_WRONG_ANSWER"));
        
                    int x = MainUtils::getInteger();
                    int y = MainUtils::getInteger();
                    
                    int expected = original_addTwoNumbers(x, y);
                    MainUtils::writeExpected(std::to_string(expected));
                    int actual = addTwoNumbers(x, y);
                    MainUtils::writeActual(std::to_string(actual));
                    
                    if(expected == actual) {
                        return acceptedCode;
                    }
                    
                    return wrongAnswerCode;
                }
            """.trimIndent()
        }
    }
}
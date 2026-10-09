package com.techullurgy.codehorn.java_execution.test_utils

import com.techullurgy.codehorn.common.code.execution.services.templates.utils.JavaTemplates

object TestFileContent {
    object AddTwoNumbers {
        fun buildWithUserCode(userCode: String): String {
            return """
                ${JavaTemplates.IMPORTS}
                
                ${JavaTemplates.UTILS}
                
                class OriginalSolution {
                    public int addTwoNumbers(int x, int y) {
                        return x + y;
                    }
                }
                
                $userCode
                
                public class Main {
                    public static void main(String[] args) throws Exception{
                        MainUtils.readFromFileAndSaveInMap("/tmp/testcase.txt");
                        
                        int acceptedCode = Integer.parseInt(System.getenv("CODE_ACCEPTED"));
                        int wrongAnswerCode = Integer.parseInt(System.getenv("CODE_WRONG_ANSWER"));
                        
                        int x = MainUtils.getInteger();
                        int y = MainUtils.getInteger();
                        
                        int eResult = new OriginalSolution().addTwoNumbers(x,y);
                        int result = new Solution().addTwoNumbers(x,y);
                        
                        MainUtils.writeResults(String.valueOf(eResult), String.valueOf(result));
                        
                        if(eResult == result) {
                            // Accepted
                            System.exit(acceptedCode);
                        } else {
                            // Wrong Answer
                            System.exit(wrongAnswerCode);
                        }
                    }
                }
            """.trimIndent()
        }
    }
}
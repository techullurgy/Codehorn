package com.techullurgy.codehorn.common.code.execution.services.templates.utils

object PythonTemplates {
    val UTILS = """
        class MainUtils:
          input_map = dict()
          line_index = 1

          @staticmethod
          def readFromFileAndSaveInDictionary(filename):
            with open(filename, "r") as file:
              line = file.readline()
              while line:
                MainUtils.input_map[MainUtils.line_index] = line.strip()
                MainUtils.line_index += 1
                line = file.readline()
            MainUtils.line_index = 1
            
          @staticmethod
          def getString():
            ans = MainUtils.input_map[MainUtils.line_index]
            MainUtils.line_index += 1
            return ans

          @staticmethod
          def getInteger():
            ans = int(MainUtils.input_map[MainUtils.line_index])
            MainUtils.line_index += 1
            return ans
          
          @staticmethod
          def writeOutputs(eResult, result, tNo):
            expected_result_filename = "outputs/eResult"+tNo+".txt"
            result_filename = "outputs/result"+tNo+".txt"

            with open(expected_result_filename, "w") as file:
              file.write(eResult)

            with open(result_filename, "w") as file:
              file.write(result)
    """.trimIndent()

    val MAIN = """
        def main():
          testcaseType = sys.argv[1].lower()
          tNo = sys.argv[2]
          MainUtils.readFromFileAndSaveInDictionary("/tmp/python/testcases/"+testcaseType+"-input"+tNo+".txt")
        
          # Your code starts here
          # Your code ends here
        
          MainUtils.writeOutputs("", "", tNo)
        
        if __name__ == "__main__":
          main()
    """.trimIndent()

    val IMPORTS = """
        import sys
    """.trimIndent()
}
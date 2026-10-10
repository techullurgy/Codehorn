package com.techullurgy.codehorn.common.code.execution.services.templates.utils

object PythonTemplates {
    const val UTILS = """
        |class MainUtils:
        |  input_map = dict()
        |  line_index = 1
        |
        |  @staticmethod
        |  def readFromFileAndSaveInDictionary(filename):
        |    with open(filename, "r") as file:
        |      line = file.readline()
        |      while line:
        |        MainUtils.input_map[MainUtils.line_index] = line.strip()
        |        MainUtils.line_index += 1
        |        line = file.readline()
        |    MainUtils.line_index = 1
        |    
        |  @staticmethod
        |  def getString():
        |    ans = MainUtils.input_map[MainUtils.line_index]
        |    MainUtils.line_index += 1
        |    return ans
        |
        |  @staticmethod
        |  def getInteger():
        |    ans = int(MainUtils.input_map[MainUtils.line_index])
        |    MainUtils.line_index += 1
        |    return ans
        |  
        |  @staticmethod
        |  def writeExpected(value):
        |    filename = "/tmp/expected.txt"
        |
        |    with open(filename, "w") as file:
        |      file.write(value)
        |
        |  @staticmethod
        |  def writeActual(value):
        |    filename = "/tmp/actual.txt"
        |    
        |    with open(filename, "w") as file:
        |      file.write(value)
    """

    const val MAIN = """
        |def main():
        |  MainUtils.readFromFileAndSaveInDictionary("/tmp/testcase.txt")
        |  
        |  accepted_code = os.getenv("CODE_ACCEPTED")
        |  wrong_answer_code = os.getenv("CODE_WRONG_ANSWER")
        |
        |  # Your code starts here
        |  
        |  # Your code ends here
        |
        |if __name__ == "__main__":
        |  main()
    """

    const val IMPORTS = """
        |import sys
        |import os
    """
}
package com.techullurgy.codehorn.common.code.execution.services.templates.utils

object JavascriptTemplates {
    val UTILS = """
        class MainUtils {
          static inputMap = {};
          static lineIndex = 1;

          static async readFromFileAndSaveInMap(fileName) {
              const fileStream = fs.createReadStream(fileName)
              const rl = readline.createInterface({
                input: fileStream,
                crlfDelay: Infinity
              })
            
              return new Promise(res => {
                rl.on('line', line => { MainUtils.inputMap[MainUtils.lineIndex++] = line })
                rl.on('close', _ => {
                    MainUtils.lineIndex = 1
                    res() 
                })
              })
          }

          static getString() {
            const ans = MainUtils.inputMap[MainUtils.lineIndex];
            MainUtils.lineIndex++;
            return ans;
          }

          static getInteger() {
            const ans = parseInt(MainUtils.inputMap[MainUtils.lineIndex]);
            MainUtils.lineIndex++;
            return ans;
          }
          
          static writeExpected(value) {
            const expectedPath = "/tmp/expected.txt";
            fs.writeFileSync(expectedPath, value);
          }
          
          static writeActual(value) {
            const actualPath = "/tmp/actual.txt";
            fs.writeFileSync(actualPath, value);
          }
        }
    """.trimIndent()

    val MAIN = """
        (async () => {
          await MainUtils.readFromFileAndSaveInMap("/tmp/testcase.txt")
          
          let codeAccepted = parseInt(process.env.CODE_ACCEPTED, 10)
          let codeWrongAnswer = parseInt(process.env.CODE_WRONG_ANSWER, 10)
          
          // Your code starts here
          
          // Your code ends here
        })()
    """.trimIndent()

    val IMPORTS = """
        const fs = require('fs');
        const path = require('path');
        const readline = require('readline')
    """.trimIndent()
}
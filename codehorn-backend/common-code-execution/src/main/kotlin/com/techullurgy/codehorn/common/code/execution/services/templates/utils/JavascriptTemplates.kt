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

          static writeOutputs(expectedResult, result, tNo) {
            const expectedPath = path.join("outputs", "eResult"+tNo+".txt");
            const resultPath = path.join("outputs", "result"+tNo+".txt");

            fs.writeFileSync(expectedPath, expectedResult);
            fs.writeFileSync(resultPath, result);
          }
        }
    """.trimIndent()

    val MAIN = """
        (async () => {
          const args = process.argv.slice(2)
        
          const testcaseType = args[0].toLowerCase()
          const tNo = args[1]
        
          const testcasePath = `/tmp/javascript/testcases/${'$'}{testcaseType}-input${'$'}{tNo}.txt`
          await MainUtils.readFromFileAndSaveInMap(testcasePath)
          
          // Your code starts here
          // Your code ends here
          
          MainUtils.writeOutputs("", "", tNo);
        })()
    """.trimIndent()

    val IMPORTS = """
        const fs = require('fs');
        const path = require('path');
        const readline = require('readline')
    """.trimIndent()
}
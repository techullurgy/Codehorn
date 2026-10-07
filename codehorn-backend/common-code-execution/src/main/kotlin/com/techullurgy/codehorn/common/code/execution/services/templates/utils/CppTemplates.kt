package com.techullurgy.codehorn.common.code.execution.services.templates.utils

object CppTemplates {
    val UTILS = """
        class MainUtils {
        public:
            static std::map<int, std::string> input_map;
            static int line_index;

            static void readFromFileAndSaveInMap(const std::string& filename) {
                std::ifstream infile(filename);
                std::string line;
                while (std::getline(infile, line)) {
                    input_map[line_index] = line;
                    ++line_index;
                }
                line_index = 1;
                
                infile.close();
            }

            static std::string getString() {
                return input_map[line_index++];
            }

            static int getInteger() {
                return std::stoi(input_map[line_index++]);
            }

            static void writeOutputs(const std::string& eResult, const std::string& result, const std::string& tNo) {
                std::ofstream expected("outputs/eResult"+tNo+".txt");
                expected << eResult;
                expected.close();

                std::ofstream actual("outputs/result"+tNo+".txt");
                actual << result;
                actual.close();
            }
        };

        std::map<int, std::string> MainUtils::input_map;
        int MainUtils::line_index = 1;

        std::string toLowerCase(std::string str) {
            std::transform(str.begin(), str.end(), str.begin(), ::tolower);
            return str;
        }
    """.trimIndent()

    val MAIN = """
        int main(int argc, char* argv[]) {
            std::string testcaseType = toLowerCase(argv[1]);
            std::string tNo = argv[2];

            std::string inputFile = "/tmp/cpp/testcases/" + testcaseType + "-input" + tNo + ".txt";
            MainUtils::readFromFileAndSaveInMap(inputFile);

            // Your code starts here

            // Your code ends here

            MainUtils::writeOutputs("", "", tNo);

            return 0;
        }
    """.trimIndent()

    val IMPORTS = """
        #include <iostream>
        #include <fstream>
        #include <map>
        #include <string>
        #include <cctype>
        #include <algorithm>
    """.trimIndent()
}
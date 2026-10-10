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
            
            static void writeExpected(const std::string& value) {
                std::ofstream expected("/tmp/expected.txt");
                expected << value;
                expected.close();
            }
            
            static void writeActual(const std::string& value) {
                std::ofstream actual("/tmp/actual.txt");
                actual << value;
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
            MainUtils::readFromFileAndSaveInMap("/tmp/testcase.txt");
            
            int acceptedCode = std::stoi(std::getenv("CODE_ACCEPTED"));
            int wrongAnswerCode = std::stoi(std::getenv("CODE_WRONG_ANSWER"));

            // Your code starts here

            // Your code ends here
        }
    """.trimIndent()

    val IMPORTS = """
        #include <iostream>
        #include <fstream>
        #include <map>
        #include <string>
        #include <cctype>
        #include <algorithm>
        #include <cstdlib>
    """.trimIndent()
}
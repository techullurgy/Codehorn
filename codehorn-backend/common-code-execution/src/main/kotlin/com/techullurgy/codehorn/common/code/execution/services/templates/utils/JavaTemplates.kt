package com.techullurgy.codehorn.common.code.execution.services.templates.utils

object JavaTemplates {
    val UTILS = """
        class MainUtils {
          private static Map<Integer, String> map = new HashMap();
          private static int lineIndex = 1;

          public static void readFromFileAndSaveInMap(String fileName) {
            try(Scanner scanner = new Scanner(new File(fileName))) {
              while(scanner.hasNextLine()) {
                String current = scanner.nextLine();
                map.put(lineIndex++, current);
              }
              lineIndex = 1;
            } catch(FileNotFoundException e) {}
          }

          public static int getInteger() {
            return Integer.parseInt(map.get(lineIndex++));
          }

          public static String getString() {
            return map.get(lineIndex++);
          }

          public static List<String> getListOfStrings() {
            List<String> ans = new ArrayList();
            int length = getInteger();
            for(int i = 0; i < length; i++) {
              ans.add(getString());
            }
            return ans;
          }

          public static List<List<String>> getListOfListOfStrings() {
            List<List<String>> ans = new ArrayList();
            int rows = getInteger();
            for(int i = 0; i < rows; i++) {
              List<String> inner = new ArrayList();
              int columns = getInteger();
              for(int j = 0; j < columns; j++) {
                inner.add(getString());
              }
              ans.add(new ArrayList(inner));
            }
            return ans;
          }
          
          public static void writeExpected(String value) {
            String expectedResultFileName = "/tmp/expected.txt";
            
            try (FileWriter writer = new FileWriter(expectedResultFileName)) {
                writer.write(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
          }
          
          public static void writeActual(String value) {
            String actualResultFileName = "/tmp/actual.txt";
            
            try (FileWriter writer = new FileWriter(actualResultFileName)) {
                writer.write(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
          }
        }
    """.trimIndent()

    val MAIN = """
        public class Main {
          public static void main(String[] args) throws Exception {
            MainUtils.readFromFileAndSaveInMap("/tmp/testcase.txt");
            
            // Your code starts here
            
            // Your code ends here
            
            MainUtils.writeResults(String.valueOf(eResult), String.valueOf(result));
          }
        }
    """.trimIndent()

    val IMPORTS = """
        import java.io.*;
        import java.util.*;
    """.trimIndent()
}
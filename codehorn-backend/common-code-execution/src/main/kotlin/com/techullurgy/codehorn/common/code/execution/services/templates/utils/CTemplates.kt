package com.techullurgy.codehorn.common.code.execution.services.templates.utils

object CTemplates {
    val UTILS = """
        #define __SIZE__ 100

        int __MAP_LINE_NO__ = 1;

        // Node for linked list in each hash bucket
        typedef struct __Node__ {
            int key;
            char* value;
            struct __Node__* next;
        } __Node__;

        __Node__* __hashTable__[__SIZE__]; // Array of linked list heads

        // Hash function for int keys
        unsigned int __hash__(int key) {
            return key % __SIZE__;
        }

        // Insert (key, value) into the map
        void __put__(int key, const char* value) {
            unsigned int index = __hash__(key);
            __Node__* newNode = malloc(sizeof(__Node__));
            newNode->key = key;
            newNode->value = strdup(value); // Duplicate the string
            newNode->next = __hashTable__[index];
            __hashTable__[index] = newNode;
        }

        char* __get__(int key) {
            unsigned int index = __hash__(key);
            __Node__* temp = __hashTable__[index];
            while (temp) {
                if (temp->key == key) return temp->value;
                temp = temp->next;
            }
            return NULL; // not found
        }

        void __freeMap__() {
            for (int i = 0; i < __SIZE__; i++) {
                __Node__* temp = __hashTable__[i];
                while (temp) {
                    __Node__* toFree = temp;
                    temp = temp->next;
                    free(toFree->value);
                    free(toFree);
                }
            }
        }

        void __readFromFileAndSaveInMap__(char* fileName) {
          FILE *file = fopen(fileName, "r");
          if (file == NULL) {
              perror("Error opening file");
              return;
          }

          char line[1024];

          while (fgets(line, sizeof(line), file)) {
            size_t length = strlen(line);
            if (length > 0 && line[length - 1] == '\n') {
                line[length - 1] = '\0';  // Replace newline with null terminator
            }
            char* a = (char*)malloc(sizeof(line));
            strcpy(a, line);
            __put__(__MAP_LINE_NO__++, a);
          }
          __MAP_LINE_NO__ = 1;

          fclose(file);
        }

        int __getInteger__() {
          int a;
          char* value = __get__(__MAP_LINE_NO__++);
          sscanf(value, "%d", &a);
          return a;
        }

        long long int __getLong__() {
          long long int a;
          char* value = __get__(__MAP_LINE_NO__++);
          sscanf(value, "%lld", &a);
          return a;
        }

        double __getDouble__() {
          double a;
          char* value = __get__(__MAP_LINE_NO__++);
          sscanf(value, "%lf", &a);
          return a;
        }

        char* __getString__() {
          char* value = __get__(__MAP_LINE_NO__++);
          return value;
        }
        
        void __writeOutputs__(const char* eResult, const char* result, const char* tNo) {
            char resultFileName[100];
            char eResultFileName[100];
        
            sprintf(resultFileName, "outputs/result%s.txt", tNo);
            sprintf(eResultFileName, "outputs/eResult%s.txt", tNo);
        
            FILE *resultFile = fopen(resultFileName, "w");
            if (resultFile == NULL) {
                perror("Error opening result file");
            } else {
                fprintf(resultFile, "%s", result);
                fclose(resultFile);
            }
        
            FILE *eResultFile = fopen(eResultFileName, "w");
            if (eResultFile == NULL) {
                perror("Error opening eResult file");
            } else {
                fprintf(eResultFile, "%s", eResult);
                fclose(eResultFile);
            }
        }
        
        void __toLowercase__(char* str) {
          for (int i = 0; str[i]; i++) {
              str[i] = tolower((unsigned char)str[i]);
          }
        }
    """.trimIndent()

    val MAIN = """
        int main(int argc, char *argv[]) {
            char* testcaseType = argv[1];
            char* tNo = argv[2];
            
            __toLowercase__(testcaseType);
            
            char filePath[100];
            sprintf(filePath, "/tmp/c/testcases/%s-input%s.txt", testcaseType, tNo);
            __readFromFileAndSaveInMap__(filePath);
            
            // Your code here
            
            // Your code ends here
            
            __writeOutputs__(eResult, result, tNo);
        }
    """.trimIndent()

    val IMPORTS = """
        #include <stdio.h>
        #include <stdlib.h>
        #include <string.h>
    """.trimIndent()
}
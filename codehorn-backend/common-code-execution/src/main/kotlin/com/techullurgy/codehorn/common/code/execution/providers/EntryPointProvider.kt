package com.techullurgy.codehorn.common.code.execution.providers

abstract class EntryPointProvider(
    private val testcaseProvider: TestcaseProvider
): Provider {
    protected abstract fun runCommand(): String
    protected open fun compileCommand(): String? = null

    final override fun provide(): String {
        return $$"""
            |#!/bin/bash
            |
            |set -e
            
            $${compileCommand()?.let {
            """
                        |# Compile the code
                        |$it 2> outputs/compilation_err.log || {
                        |  echo "COMPILATION_ERROR";
                        |  exit 1;
                        |}
                    """.trimMargin()
        } ?: ""}

            |(
            |  set +e
            |  failed=0
            |
            |  for i in $${testcaseProvider.sampleIds().joinToString(" ")}; do
            |    timeout 5s $${runCommand()} SAMPLE "$i" > outputs/sample-out"$i".log 2> outputs/sample-outerr"$i".log
            |    exit_code=$?
            |
            |    if [[ $exit_code -eq 124 ]]; then
            |      echo "$i-TIME_LIMIT_EXCEEDED"
            |      failed=124
            |    elif [[ $exit_code -ne 0 ]]; then
            |      echo "$i-RUNTIME_ERROR"
            |      failed=1
            |    else
            |      if cmp -s outputs/eResult"$i".txt outputs/result"$i".txt; then
            |          echo "$i-ACCEPTED"
            |      else
            |          echo "$i-WRONG_ANSWER"
            |          failed=1
            |      fi
            |    fi
            |  done
            |
            |  if [[ $failed -ne 0 ]]; then
            |    exit 1
            |  fi
            |) || exit 1
            |
            |if [[ $? -ne 0 ]]; then
            |  exit 1
            |fi
            |
            |for i in $${testcaseProvider.hiddenIds().joinToString(" ")}; do
            |  timeout 5s $${runCommand()} HIDDEN "$i" > outputs/hidden-out"$i".log 2> outputs/hidden-outerr"$i".log
            |  exit_code=$?
            |
            |  if [[ $exit_code -eq 124 ]]; then
            |    echo "$i-TIME_LIMIT_EXCEEDED"
            |    exit 1
            |  elif [[ $exit_code -ne 0 ]]; then
            |    echo "$i-RUNTIME_ERROR"
            |    exit 1
            |  else
            |    if cmp -s outputs/eResult"$i".txt outputs/result"$i".txt; then
            |        echo "$i-ACCEPTED"
            |    else
            |        echo "$i-WRONG_ANSWER"
            |        exit 1
            |    fi
            |  fi
            |done
        """.trimMargin()
    }
}
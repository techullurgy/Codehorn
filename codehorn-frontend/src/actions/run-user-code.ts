'use server'

import { ProblemTestCase } from "../types/Problem"
import { TestcaseResult } from "../types/Submission"

export const runUserCodeForSubmission: (testcases: ProblemTestCase[]) => Promise<TestcaseResult[]> = async (testcases) => {
  await new Promise(res => setTimeout(res, 5000))
  return testcases.map(t => (
    {
      verdict: 'Accepted',
      inputs: t.inputs,
      actual: "954",
      expected: t.expectedOutput,
      stderr: "",
      stdout: ""
    }
  ))
}
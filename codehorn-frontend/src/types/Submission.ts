
export type CodeLanguage = 'c' | 'cpp' | 'java' | 'python' | 'javascript'

export type Verdict = 'Accepted' | 'Wrong Answer' | 'Time Limit Exceeded' | 'Compilation Error' | 'Runtime Error' | 'Memory Limit Exceeded'

export type TestcaseResult = {
  verdict: Verdict,
  inputs: Record<string, string>,
  stdout: string,
  stderr: string,
  expected: string,
  actual: string
}

export interface Submission {
  id: string
  language: CodeLanguage
  code: string
  verdict: Verdict
  timestamp: number // in milliseconds
  runtime: number // in milliseconds
  memory: number // in MB
  failedTestcaseResult?: TestcaseResult
}
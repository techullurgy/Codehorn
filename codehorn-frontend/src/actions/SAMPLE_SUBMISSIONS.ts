import { Submission } from "../types/Submission";

export const SAMPLE_SUBMISSIONS: Submission[] = [
  {
    id: "submission-89qw8e",
    language: 'java',
    code: 'public static void main(String[] args) {}',
    verdict: 'Accepted',
    timestamp: (new Date()).getMilliseconds(), // in milliseconds
    runtime: 8000, // in milliseconds
    memory: 0.23, // in MB
  },
  {
    id: "submission-89as67",
    language: 'cpp',
    code: 'void main {}',
    verdict: 'Wrong Answer',
    timestamp: (new Date()).getMilliseconds() - 8273, // in milliseconds
    runtime: 4200, // in milliseconds
    memory: 0.43, // in MB
    failedTestcaseResult: {
      verdict: 'Wrong Answer',
      inputs: {
        a: '863',
        b: '635'
      },
      expected: '1498',
      actual: '1627',
      stdout: 'Adding two numbers',
      stderr: ''
    }
  }
]
export interface Problem {
  id: string
  title: string
  slug: string
  difficulty: ProblemDifficulty
  category: string[]
  acceptanceRate: number
  description: string
  examples: ProblemExample[]
  constraints: string[]
  hints: string[],
  status: ProblemStatus
}

export type ProblemDifficulty = 'Easy' | 'Medium' | 'Hard'

export type ProblemStatus = 'Todo' | 'Attempted' | 'Solved'

export interface ProblemTestCase {
  id: string
  inputs: Record<string, string>
  expectedOutput: string
}

export interface ProblemExample {
  id: number
  inputs: Record<string, string>
  output: string
  explanation?: string
}
'use server'

import { Problem } from "../types/Problem"

export const getProblemBySlug: (slug: string) => Promise<Problem> = async (slug: string) => {
  await new Promise((res) => setTimeout(res, 1000))

  return {
    id: '89s87da9s8da98s7da89sd7a98sd7as7d',
    slug: 'add-two-numbers',
    title: 'Add Two Numbers',
    description: 'Add Two Numbers Description',
    category: ['Arrays', 'Two Pointers', 'Math'],
    acceptanceRate: 90.33,
    difficulty: 'Easy',
    examples: [
      {
        id: 1,
        inputs: {
          "a": "11",
          "b": "283"
        },
        output: "294",
        explanation: 'The addition of 11 and 283 is 294'
      }
    ],
    constraints: [
      'a is integer',
      'b is integer'
    ],
    hints: [
      'Add two numbers'
    ],
    status: 'Solved',
    testcases: [
      {
        id: "1",
        inputs: {
          a: "12",
          b: "29"
        },
        expectedOutput: "41"
      },
      {
        id: "2",
        inputs: {
          a: "-12",
          b: "293"
        },
        expectedOutput: "281"
      },

      {
        id: "3",
        inputs: {
          a: "-927",
          b: "-26"
        },
        expectedOutput: "-953"
      }
    ]
  }
}
'use server'

import { Submission } from "../types/Submission"

export const submitUserCodeForSubmission: () => Promise<Submission> = async () => {
  await new Promise(res => setTimeout(res, 5000))
  return {
    id: "submission-89qw8e",
    language: 'java',
    code: 'public static void main(String[] args) {}',
    verdict: 'Accepted',
    timestamp: (new Date()).getMilliseconds(), // in milliseconds
    runtime: 8000, // in milliseconds
    memory: 0.23, // in MB
  }
}
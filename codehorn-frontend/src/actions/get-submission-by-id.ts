'use server'

import { Submission } from "../types/Submission";
import { SAMPLE_SUBMISSIONS } from "./SAMPLE_SUBMISSIONS";

export const getSubmissionById: (submissionId: string) => Promise<Submission> = async (submissionId) => {
  await new Promise(res => setTimeout(res, 3000))
  return SAMPLE_SUBMISSIONS.find(s => s.id === submissionId)
}
'use server'

import { Submission } from "../types/Submission"
import { SAMPLE_SUBMISSIONS } from "./SAMPLE_SUBMISSIONS"

export const getSubmissionsForProblem: (problemSlug: string) => Promise<Submission[]> = async (problemSlug) => {
  await new Promise(res => setTimeout(res, 5000))
  return SAMPLE_SUBMISSIONS
}
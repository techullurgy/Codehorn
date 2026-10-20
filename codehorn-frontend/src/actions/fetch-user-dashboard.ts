'use server'

import { UserDashboard } from "../types/Dashboard";

export async function fetchUserDashboard(user: string): Promise<UserDashboard> {
  await new Promise(res => setTimeout(res, 3000))
  return {
    easySolvedCount: 89,
    mediumSolvedCount: 73,
    hardSolvedCount: 35,
    completedDailyChallenges: [],
    attemptedDailyChallenges: [],
    solvedProblems: [],
    attemptedProblems: [],
    streakPoints: 893
  }
}
'use server'

import { Problem } from "../types/Problem";

export async function fetchProblems(
  user: string,
  page: number,
  loadSize: number,
): Promise<Problem[]> {
  await new Promise((res) => setTimeout(res, 1000));
  return [];
}
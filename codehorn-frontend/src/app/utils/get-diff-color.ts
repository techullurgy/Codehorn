import { ProblemDifficulty } from "@/src/types/Problem";


// Get difficulty badge color rules
export const getDiffColor = (diff: ProblemDifficulty) => {
  switch (diff) {
    case 'Easy': return 'text-emerald-400 bg-emerald-500/10 border-emerald-500/20';
    case 'Medium': return 'text-amber-400 bg-amber-500/10 border-amber-500/20';
    case 'Hard': return 'text-rose-400 bg-rose-500/10 border-rose-500/20';
  }
};
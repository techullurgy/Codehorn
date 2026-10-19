"use client";

import { getSubmissionsForProblem } from "@/src/actions/get-submissions-for-problem";
import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";
import { Submission } from "@/src/types/Submission";
import { useQuery } from "@tanstack/react-query";
import { HelpCircle } from "lucide-react";

// SubmissionsContent
export default () => {
  const problemSlug = useProblemPlaygroundStore((state) => state.slug);
  const addToLeftTabs = useProblemPlaygroundStore(
    (state) => state.addToLeftTabs,
  );

  const { data: submissions, isPending } = useQuery<Submission[]>({
    queryKey: ["problem", problemSlug, "submissions"],
    queryFn: () => getSubmissionsForProblem(problemSlug),
  });

  if (isPending) {
    return <>Loading Submissions.....</>;
  }

  if (submissions.length == 0) {
    return (
      <div className="py-12 text-center text-zinc-500 font-medium">
        <HelpCircle className="w-10 h-10 mx-auto text-zinc-700 mb-2 animate-pulse" />
        <p>No submissions found for this problem.</p>
        <p className="text-xs text-zinc-600 mt-1">
          Submit your code to see records here.
        </p>
      </div>
    );
  }

  return (
    <div className="space-y-3">
      {submissions.map((submission) => (
        <div
          key={submission.id}
          onClick={() => addToLeftTabs(submission.id)}
          className="p-4 rounded-xl border border-zinc-800 bg-zinc-900/30 hover:bg-zinc-900/60 cursor-pointer transition-all duration-150 flex items-center justify-between group"
          id={`sub-item-${submission.id}`}
        >
          <div className="space-y-1.5">
            <div className="flex items-center space-x-2.5">
              <span
                className={`font-bold text-sm ${submission.verdict === "Accepted" ? "text-emerald-400" : "text-rose-400"}`}
              >
                {submission.verdict}
              </span>
              <span className="text-xs text-zinc-500">•</span>
              <span className="text-xs text-zinc-400 font-mono capitalize">
                {submission.language}
              </span>
            </div>
            <div className="text-xs text-zinc-500 font-mono">
              {new Date(submission.timestamp).toLocaleString(undefined, {
                dateStyle: "short",
                timeStyle: "short",
              })}
            </div>
          </div>

          <div className="flex items-center space-x-4">
            <div className="text-right text-xs font-mono space-y-0.5">
              <div className="text-zinc-300">{submission.runtime} ms</div>
              <div className="text-zinc-500">{submission.memory} MB</div>
            </div>
            <div className="text-zinc-500 group-hover:text-amber-500 text-xs font-semibold">
              View Code
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

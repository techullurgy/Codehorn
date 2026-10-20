"use client";

import { getSubmissionById } from "@/src/actions/get-submission-by-id";
import { StaticCodeViewer } from "@/src/app/components/StaticCodeViewer";
import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";
import { Submission } from "@/src/types/Submission";
import { useIsMutating, useQuery } from "@tanstack/react-query";
import { AlertTriangle, CheckCircle, Copy } from "lucide-react";

type SubmissionDetailsProps = {
  submissionId: string;
};

// SubmissionDetails
export default ({ submissionId }: SubmissionDetailsProps) => {
  const slug = useProblemPlaygroundStore((state) => state.slug);

  const isFromMutation = submissionId.includes("submission-mutation");

  const isMutationPending =
    useIsMutating({
      mutationKey: ["problem", slug, "submit-code"],
      exact: true,
    }) > 0;

  const { data: submission, isPending } = useQuery<Submission>({
    queryKey: ["problem", slug, "submission", submissionId],
    queryFn: async () => getSubmissionById(submissionId),
    enabled: !isFromMutation,
    staleTime: Infinity,
  });

  if (isFromMutation && isMutationPending) {
    return <>Mutation Pending</>;
  }

  if (isPending) {
    return <>Loading Submission</>;
  }

  const isAccepted = submission.verdict === "Accepted";

  const percentPassed = 13;
  const totalTestcases: number = 19;
  const passedTestcases: number = 13;

  return (
    <div
      className="flex-1 overflow-y-auto px-5 py-6 space-y-6 text-zinc-300 text-sm"
      id={`sub-details-${submission.id}`}
    >
      {/* Header summary & Verdict */}
      <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-5 space-y-4 shadow-sm">
        <div className="flex items-center justify-between">
          <span className="text-[10px] uppercase font-mono tracking-wider text-zinc-500 font-bold">
            Submission Report
          </span>
          <span className="text-[11px] font-mono text-zinc-400">
            {new Date(submission.timestamp).toLocaleTimeString()}
          </span>
        </div>

        <div className="flex items-center space-x-4">
          <div
            className={`p-3 rounded-xl border ${
              isAccepted
                ? "bg-emerald-500/10 border-emerald-500/20 text-emerald-400"
                : "bg-rose-500/10 border-rose-500/20 text-rose-400"
            }`}
          >
            <CheckCircle className="w-8 h-8" />
          </div>

          <div className="space-y-1">
            <div className="flex items-center space-x-2 flex-wrap gap-y-1">
              <h2
                className={`text-xl font-black tracking-tight ${isAccepted ? "text-emerald-400" : "text-rose-400"}`}
              >
                {submission.verdict}
              </h2>
              {isAccepted && (
                <span className="bg-amber-500/15 text-amber-400 border border-amber-500/25 px-2 py-0.5 rounded-full text-[10px] font-extrabold flex items-center space-x-1 uppercase">
                  <span>👑</span>
                  <span>Solved (+XP)</span>
                </span>
              )}
            </div>
            <p className="text-xs text-zinc-400 font-medium font-sans">
              {percentPassed}% testcases passed
            </p>
          </div>
        </div>

        {/* Tech Metrics rows */}
        <div className="grid grid-cols-2 gap-3.5 pt-1">
          <div className="bg-zinc-950 border border-zinc-850 p-3 rounded-lg">
            <span className="text-[9px] font-mono text-zinc-500 uppercase tracking-widest block font-bold">
              Runtime Speed
            </span>
            <span className="text-sm font-bold text-zinc-200 mt-1 block font-mono">
              {submission.runtime} ms
            </span>
            <span className="text-[10px] text-zinc-500 block">
              Beats 82% of users
            </span>
          </div>
          <div className="bg-zinc-950 border border-zinc-850 p-3 rounded-lg">
            <span className="text-[9px] font-mono text-zinc-500 uppercase tracking-widest block font-bold">
              Memory Usage
            </span>
            <span className="text-sm font-bold text-zinc-200 mt-1 block font-mono">
              {submission.memory} MB
            </span>
            <span className="text-[10px] text-zinc-500 block">
              Beats 88% of users
            </span>
          </div>
        </div>
      </div>

      {/* Testcase Progress breakdown */}
      <div className="space-y-3">
        <h3 className="text-xs font-bold uppercase tracking-wider text-zinc-400 font-sans">
          Execution Breakdown
        </h3>

        <div className="space-y-2.5">
          {/* Sample Case Status bar */}
          <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-3.5 flex items-center justify-between">
            <div className="space-y-0.5">
              <span className="text-xs font-bold text-zinc-200 block">
                Sample Test cases
              </span>
              <span className="text-[10px] text-zinc-500 font-mono">
                Predefined test suite cases
              </span>
            </div>
            <div className="flex items-center space-x-2">
              <span
                className={`text-xs font-mono font-bold px-2 py-0.5 rounded-lg ${
                  passedTestcases === totalTestcases
                    ? "text-emerald-400 bg-emerald-500/10 border border-emerald-500/15"
                    : "text-amber-500 bg-amber-500/10 border border-amber-500/15"
                }`}
              >
                {passedTestcases} / {totalTestcases} Passed
              </span>
            </div>
          </div>
        </div>
      </div>

      {/* Failed TestCase details section */}
      {!isAccepted &&
        submission.failedTestcaseResult &&
        (() => {
          const parsedInputs = submission.failedTestcaseResult.inputs;
          return (
            <div className="bg-rose-950/10 border border-rose-950/20 rounded-xl p-5 space-y-4">
              <div className="flex items-center space-x-2 border-b border-rose-900/15 pb-2.5">
                <AlertTriangle className="w-4 h-4 text-rose-450" />
                <h4 className="text-xs font-bold text-rose-300 uppercase tracking-wide">
                  Failed testcase metrics
                </h4>
              </div>

              <div className="space-y-4 text-xs font-mono">
                {/* Inputs breakdown block */}
                <div className="space-y-3.5">
                  {Object.entries(parsedInputs).map(([key, value]) => (
                    <div key={key}>
                      <span className="text-[10px] font-mono text-zinc-500 uppercase tracking-widest font-extrabold">
                        {key}
                      </span>
                      <div className="bg-zinc-950 border border-zinc-900 rounded-lg p-2.5 text-zinc-300 font-mono overflow-x-auto select-all max-h-32">
                        {value}
                      </div>
                    </div>
                  ))}
                </div>

                {/* Outputs expected / actual grid layout */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-1">
                  <div className="space-y-1">
                    <span className="text-[10px] font-mono text-emerald-500 block uppercase tracking-wider font-extrabold">
                      Expected Output:
                    </span>
                    <div className="bg-emerald-950/15 border border-emerald-950/25 rounded-lg p-2.5 text-emerald-350 overflow-x-auto select-all font-mono">
                      {submission.failedTestcaseResult.expected}
                    </div>
                  </div>
                  <div className="space-y-1">
                    <span className="text-[10px] font-mono text-rose-550 block uppercase tracking-wider font-extrabold">
                      Actual Output:
                    </span>
                    <div className="bg-rose-950/15 border border-rose-950/25 rounded-lg p-2.5 text-rose-350 overflow-x-auto select-all font-mono font-bold">
                      {submission.failedTestcaseResult.actual}
                    </div>
                  </div>
                </div>

                {/* Stdout Console Logs if any exists */}
                <div className="space-y-1 bg-zinc-950/40 rounded-lg p-3.5 border border-zinc-900">
                  <span className="text-[10px] font-mono text-zinc-550 block uppercase tracking-wider font-extrabold">
                    Stdout
                  </span>
                  {submission.failedTestcaseResult.stdout ? (
                    <pre className="text-zinc-400 whitespace-pre-wrap overflow-x-auto select-text font-mono leading-relaxed mt-1.5 text-[11px] bg-zinc-950 p-2.5 rounded max-h-40 border border-zinc-900/60 shadow-inner">
                      {submission.failedTestcaseResult.stdout}
                    </pre>
                  ) : (
                    <span className="text-zinc-600 text-[10px] block font-mono italic mt-1 font-medium">
                      No standard output logged.
                    </span>
                  )}
                </div>
              </div>
            </div>
          );
        })()}

      {/* Submitted Code snippet with Copy action */}
      <div className="bg-zinc-900 border border-zinc-800 rounded-xl overflow-hidden shadow-sm">
        <div className="flex items-center justify-between px-3.5 py-2.5 border-b border-zinc-800/80 bg-zinc-900/35">
          <span className="text-[10px] font-mono text-zinc-500 font-bold uppercase tracking-wide">
            Submitted Source ({submission.language})
          </span>
          <button
            onClick={() => {
              navigator.clipboard.writeText(submission.code);
              alert("Code copied to clipboard.");
            }}
            className="px-2 py-1 text-[10px] text-zinc-400 hover:text-zinc-200 bg-zinc-950 hover:bg-zinc-850 rounded border border-zinc-800 transition-all flex items-center space-x-1 font-semibold"
          >
            <Copy className="w-3 h-3" />
            <span>Copy Code</span>
          </button>
        </div>
        <div className="h-62.5 border-t border-zinc-900 overflow-hidden relative">
          <StaticCodeViewer
            language={submission.language}
            code={submission.code}
          />
        </div>
      </div>
    </div>
  );
};

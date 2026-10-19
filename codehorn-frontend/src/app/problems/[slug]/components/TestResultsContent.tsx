import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";
import { TestcaseResult } from "@/src/types/Submission";
import { useMutationState } from "@tanstack/react-query";
import { Terminal } from "lucide-react";
import { useState } from "react";

export const TestResultsContent = () => {
  const slug = useProblemPlaygroundStore((state) => state.slug);

  const runs = useMutationState<{
    results: TestcaseResult[];
    isPending: Boolean;
  }>({
    filters: {
      mutationKey: ["problem", slug, "run-code"],
      exact: true,
    },
    select: (mutation) => ({
      results: mutation.state.data as TestcaseResult[],
      isPending: mutation.state.status === "pending",
    }),
  });

  const { results, isPending } = runs[runs.length - 1] ?? {
    results: null,
    isPending: null,
  };

  const [focusedTestResultIndex, setFocusedTestResultIndex] = useState(0);

  const focusedTestResult =
    results && results.length > 0 && results[focusedTestResultIndex];

  if (isPending) {
    return <>Running Sample Testcases....</>;
  }

  if (!results || results.length < 1) {
    return (
      <div className="p-6 text-center text-zinc-500 text-xs font-medium flex flex-col items-center justify-center space-y-1.5 h-full">
        <Terminal className="w-7 h-7 text-zinc-700" />
        <p>Execution console is empty.</p>
        <p className="text-zinc-600">
          Hit "Run" to test sample inputs; "Submit" to grade all secret
          parameters.
        </p>
      </div>
    );
  }

  return (
    <div className="p-4 space-y-4 font-sans">
      <div className="flex flex-wrap items-center gap-1 border-b border-zinc-900/60 pb-2.5 select-none">
        {results.map((_, index) => (
          <div key={index + 1}>
            <button
              key={index}
              onClick={() => setFocusedTestResultIndex(index)}
              className={`px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center space-x-1.5 transition-all border ${
                focusedTestResultIndex === index
                  ? "bg-zinc-900 border-zinc-800 text-zinc-150 shadow-sm"
                  : "bg-zinc-950 border-transparent text-zinc-500 hover:text-zinc-300 hover:bg-zinc-900/40"
              }`}
            >
              <span>Case {index + 1}</span>
            </button>
          </div>
        ))}
      </div>

      <div className="space-y-3.5">
        {Object.entries(focusedTestResult.inputs).map(([name, value]) => (
          <div key={name} className="flex flex-col space-y-1.5">
            <div className="flex items-center justify-between">
              <label className="text-[10px] font-semibold text-zinc-500 uppercase tracking-wider font-mono flex items-center space-x-1">
                <span className="text-zinc-300">{name}</span>
              </label>
            </div>
            <input
              type="text"
              value={value}
              readOnly={true}
              className="w-full font-mono text-xs focus:outline-none transition-all rounded-lg px-3 py-2 border bg-zinc-900/90 text-zinc-200 border-zinc-850 focus:border-zinc-705 focus:ring-0 placeholder-zinc-700"
            />
          </div>
        ))}

        <div className="flex flex-col space-y-1.5">
          <div className="flex items-center justify-between">
            <label className="text-[10px] font-semibold text-zinc-500 uppercase tracking-wider font-mono flex items-center space-x-1">
              <span className="text-zinc-300">Expected Output</span>
            </label>
          </div>
          <input
            type="text"
            value={focusedTestResult.expected}
            readOnly={true}
            className="w-full font-mono text-xs focus:outline-none transition-all rounded-lg px-3 py-2 border bg-zinc-900/90 text-zinc-200 border-zinc-850 focus:border-zinc-705 focus:ring-0 placeholder-zinc-700"
          />
        </div>

        <div className="flex flex-col space-y-1.5">
          <div className="flex items-center justify-between">
            <label className="text-[10px] font-semibold text-zinc-500 uppercase tracking-wider font-mono flex items-center space-x-1">
              <span className="text-zinc-300">Actual Output</span>
            </label>
          </div>
          <input
            type="text"
            value={focusedTestResult.actual}
            readOnly={true}
            className="w-full font-mono text-xs focus:outline-none transition-all rounded-lg px-3 py-2 border bg-zinc-900/90 text-zinc-200 border-zinc-850 focus:border-zinc-705 focus:ring-0 placeholder-zinc-700"
          />
        </div>

        <div className="flex flex-col space-y-1.5">
          <div className="flex items-center justify-between">
            <label className="text-[10px] font-semibold text-zinc-500 uppercase tracking-wider font-mono flex items-center space-x-1">
              <span className="text-zinc-300">Standard Output</span>
            </label>
          </div>
          <input
            type="text"
            value={focusedTestResult.stdout}
            readOnly={true}
            className="w-full font-mono text-xs focus:outline-none transition-all rounded-lg px-3 py-2 border bg-zinc-900/90 text-zinc-200 border-zinc-850 focus:border-zinc-705 focus:ring-0 placeholder-zinc-700"
          />
        </div>

        <div className="flex flex-col space-y-1.5">
          <div className="flex items-center justify-between">
            <label className="text-[10px] font-semibold text-zinc-500 uppercase tracking-wider font-mono flex items-center space-x-1">
              <span className="text-zinc-300">Standard Error</span>
            </label>
          </div>
          <input
            type="text"
            value={focusedTestResult.stderr}
            readOnly={true}
            className="w-full font-mono text-xs focus:outline-none transition-all rounded-lg px-3 py-2 border bg-zinc-900/90 text-zinc-200 border-zinc-850 focus:border-zinc-705 focus:ring-0 placeholder-zinc-700"
          />
        </div>
      </div>
    </div>
  );
};

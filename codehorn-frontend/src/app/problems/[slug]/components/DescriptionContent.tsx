import MarkdownRenderer from "@/src/app/components/MarkdownRenderer";
import { getDiffColor } from "@/src/app/utils/get-diff-color";
import { Problem } from "@/src/types/Problem";
import { Trophy } from "lucide-react";

type DescriptionContentProps = {
  isDailyChallenge: boolean;
  problem: Problem;
};

// DescriptionContent
export default ({ isDailyChallenge, problem }: DescriptionContentProps) => {
  const processInputs = (inputs: Record<string, string>) =>
    Object.entries(inputs)
      .map(([key, value]) => `${key} = ${value}`)
      .join(", ");

  return (
    <div
      className="flex-1 overflow-y-auto px-5 py-6 space-y-6"
      id="detail-left-description"
    >
      {/* Title & Stats */}
      <div className="space-y-3">
        {isDailyChallenge && (
          <div className="flex items-center space-x-2 bg-amber-500/10 border border-amber-500/25 rounded-md px-3 py-1.5 text-amber-400 max-w-max select-none animate-fade-in">
            <Trophy className="w-4 h-4 text-amber-500 animate-pulse shrink-0" />
          </div>
        )}
        <div className="flex flex-wrap items-center gap-2.5">
          <span
            className={`px-2.5 py-0.5 rounded-full text-xs font-bold border ${getDiffColor(problem.difficulty)}`}
          >
            {problem.difficulty}
          </span>
          <span className="text-zinc-500 text-xs">•</span>
          <span className="text-zinc-400 text-xs font-medium bg-zinc-900 border border-zinc-800 px-2.5 py-0.5 rounded-md">
            {problem.category}
          </span>
          <span className="text-zinc-500 text-xs">•</span>
          <span className="text-zinc-400 text-xs font-mono">
            Acceptance: {problem.acceptanceRate}%
          </span>
        </div>
        <h1 className="text-xl lg:text-2xl font-extrabold text-zinc-100 tracking-tight">
          {problem.id}. {problem.title}
        </h1>
      </div>

      {/* Problem markdown representation */}
      <div className="leading-relaxed text-zinc-300 antialiased font-sans">
        <MarkdownRenderer content={problem.description} />
      </div>

      {/* Examples visual layout */}
      <div className="space-y-4">
        <h3 className="text-sm font-bold text-zinc-200">Examples:</h3>
        {problem.examples.map((ex) => (
          <div
            key={ex.id}
            className="bg-zinc-900/60 border border-zinc-800/80 rounded-xl p-4 space-y-2 text-xs font-mono"
          >
            <div className="text-xs font-semibold text-zinc-400">
              Example {ex.id}:
            </div>
            <div className="space-y-1">
              <div>
                <span className="text-amber-500/80 font-bold">Input:</span>{" "}
                <span className="text-zinc-300 select-all">
                  {processInputs(ex.inputs)}
                </span>
              </div>
              <div>
                <span className="text-emerald-500/85 font-bold">Output:</span>{" "}
                <span className="text-zinc-300 select-all">{ex.output}</span>
              </div>
              {ex.explanation && (
                <div className="text-zinc-400 pt-1.5 border-t border-zinc-800/60 leading-relaxed">
                  <span className="text-zinc-500 italic">Explanation:</span>{" "}
                  {ex.explanation}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Constraints Block */}
      <div className="space-y-2.5 pt-4 border-t border-zinc-800/60">
        <h3 className="text-sm font-bold text-zinc-200">Constraints:</h3>
        <ul className="space-y-1.5 list-disc pl-5 text-xs text-zinc-400 font-mono">
          {problem.constraints.map((c, idx) => (
            <li key={idx}>
              <span>{c}</span>
            </li>
          ))}
        </ul>
      </div>

      {/* Hints lists togglers list */}
      <div className="space-y-3 pt-4 border-t border-zinc-800/60">
        <h3 className="text-sm font-bold text-zinc-200">Hints:</h3>
        {problem.hints.map((hint, index) => (
          <details
            key={index}
            className="group bg-zinc-900/30 border border-zinc-800/60 rounded-lg overflow-hidden transition-all duration-150"
          >
            <summary className="flex justify-between items-center px-4 py-2.5 text-xs font-semibold text-amber-500/90 cursor-pointer select-none hover:bg-zinc-900/40">
              <span>Hint #{index + 1}</span>
              <span className="text-zinc-500 group-open:scale-y-[-1] transition-transform duration-100">
                ▼
              </span>
            </summary>
            <p className="px-4 pb-3.5 text-xs text-zinc-400 leading-relaxed border-t border-zinc-800/40 pt-2.5 font-sans">
              {hint}
            </p>
          </details>
        ))}
      </div>
    </div>
  );
};

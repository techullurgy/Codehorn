'use client'

import { Trophy } from "lucide-react"
import { useCodeHornStore } from "../store/useCodeHornStore"
import MarkdownRenderer from "./MarkdownRenderer"

export default function DescriptionContent() {
  const { selectedProblem, isDailyChallenge, dailyChallengeDate } = useCodeHornStore()
  
  if (!selectedProblem) return null

  // Get difficulty badge color rules
  const getDiffColor = (diff: string) => {
    switch (diff) {
      case 'Easy': return 'text-emerald-400 bg-emerald-500/10 border-emerald-500/20';
      case 'Medium': return 'text-amber-400 bg-amber-500/10 border-amber-500/20';
      case 'Hard': return 'text-rose-400 bg-rose-500/10 border-rose-500/20';
      default: return 'text-zinc-400 bg-zinc-800';
    }
  };
  
  return (
    <div className="flex-1 overflow-y-auto px-5 py-6 space-y-6" id="detail-left-description">
        {/* Title & Stats */}
        <div className="space-y-3">
            {isDailyChallenge && dailyChallengeDate && (
            <div className="flex items-center space-x-2 bg-amber-500/10 border border-amber-500/25 rounded-md px-3 py-1.5 text-amber-400 max-w-max select-none animate-fade-in">
                <Trophy className="w-4 h-4 text-amber-500 animate-pulse shrink-0" />
                <span className="text-[10px] font-black tracking-wider uppercase font-sans">
                OFFICIAL DAILY CHALLENGE • {new Date(dailyChallengeDate).toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' })}
                </span>
            </div>
            )}
            <div className="flex flex-wrap items-center gap-2.5">
            <span className={`px-2.5 py-0.5 rounded-full text-xs font-bold border ${getDiffColor(selectedProblem.difficulty)}`}>
                {selectedProblem.difficulty}
            </span>
            <span className="text-zinc-500 text-xs">•</span>
            <span className="text-zinc-400 text-xs font-medium bg-zinc-900 border border-zinc-800 px-2.5 py-0.5 rounded-md">
                {selectedProblem.category}
            </span>
            <span className="text-zinc-500 text-xs">•</span>
            <span className="text-zinc-400 text-xs font-mono">Acceptance: {selectedProblem.acceptanceRate}%</span>
            </div>
            <h1 className="text-xl lg:text-2xl font-extrabold text-zinc-100 tracking-tight">
            {selectedProblem.id}. {selectedProblem.title}
            </h1>
        </div>

        {/* Problem markdown representation */}
        <div className="leading-relaxed text-zinc-300 antialiased font-sans">
            <MarkdownRenderer content={selectedProblem.description}/>
        </div>

        {/* Examples visual layout */}
        <div className="space-y-4">
            <h3 className="text-sm font-bold text-zinc-200">Examples:</h3>
            {selectedProblem.examples.map((ex) => (
            <div key={ex.id} className="bg-zinc-900/60 border border-zinc-800/80 rounded-xl p-4 space-y-2 text-xs font-mono">
                <div className="text-xs font-semibold text-zinc-400">Example {ex.id}:</div>
                <div className="space-y-1">
                <div>
                    <span className="text-amber-500/80 font-bold">Input:</span>{' '}
                    <span className="text-zinc-300 select-all">{ex.input}</span>
                </div>
                <div>
                    <span className="text-emerald-500/85 font-bold">Output:</span>{' '}
                    <span className="text-zinc-300 select-all">{ex.output}</span>
                </div>
                {ex.explanation && (
                    <div className="text-zinc-400 pt-1.5 border-t border-zinc-800/60 leading-relaxed">
                    <span className="text-zinc-500 italic">Explanation:</span> {ex.explanation}
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
            {selectedProblem.constraints.map((c, idx) => (
                <li key={idx}>
                <span>{c}</span>
                </li>
            ))}
            </ul>
        </div>

        {/* Hints lists togglers list */}
        <div className="space-y-3 pt-4 border-t border-zinc-800/60">
            <h3 className="text-sm font-bold text-zinc-200">Hints:</h3>
            {selectedProblem.hints.map((hint, index) => (
            <details
                key={index}
                className="group bg-zinc-900/30 border border-zinc-800/60 rounded-lg overflow-hidden transition-all duration-150"
            >
                <summary className="flex justify-between items-center px-4 py-2.5 text-xs font-semibold text-amber-500/90 cursor-pointer select-none hover:bg-zinc-900/40">
                <span>Hint #{index + 1}</span>
                <span className="text-zinc-500 group-open:scale-y-[-1] transition-transform duration-100">▼</span>
                </summary>
                <p className="px-4 pb-3.5 text-xs text-zinc-400 leading-relaxed border-t border-zinc-800/40 pt-2.5 font-sans">
                {hint}
                </p>
            </details>
            ))}
        </div>
    </div>
  )
}
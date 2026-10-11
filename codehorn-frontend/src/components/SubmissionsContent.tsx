'use client'

import { Clock, HelpCircle } from "lucide-react";
import { Submission } from "../types";

export default function SubmissionsContent(
    { problemSubmissions, openSubTabs, setLocalLeftTab, setOpenSubTabs }: { 
        problemSubmissions: Submission[]
        openSubTabs: Submission[]
        setOpenSubTabs: React.Dispatch<React.SetStateAction<Submission[]>>
        setLocalLeftTab: React.Dispatch<React.SetStateAction<string>>
    }
) {
  return (
    <div className="flex-1 overflow-y-auto px-5 py-6 space-y-6 text-zinc-300 text-sm" id="detail-left-submissions">
        <div className="flex items-center justify-between pb-2 border-b border-zinc-800">
            <div className="flex items-center space-x-2">
            <Clock className="w-5 h-5 text-amber-500" />
            <h2 className="text-lg font-bold text-zinc-100">Submission History logs</h2>
            </div>
            <span className="text-xs text-zinc-500 font-mono">Total checks: {problemSubmissions.length}</span>
        </div>

        {problemSubmissions.length > 0 ? (
            <div className="space-y-3">
            {problemSubmissions.map((sub) => (
                <div
                key={sub.id}
                onClick={() => {
                    if (!openSubTabs.some(t => t.id === sub.id)) {
                      setOpenSubTabs(prev => [...prev, sub]);
                    }
                    setLocalLeftTab(`submission-${sub.id}`);
                }}
                className="p-4 rounded-xl border border-zinc-800 bg-zinc-900/30 hover:bg-zinc-900/60 cursor-pointer transition-all duration-150 flex items-center justify-between group"
                id={`sub-history-item-${sub.id}`}
                >
                <div className="space-y-1.5">
                    <div className="flex items-center space-x-2.5">
                    <span className={`font-bold text-sm ${sub.status === 'Accepted' ? 'text-emerald-400' : 'text-rose-400'
                        }`}>
                        {sub.status}
                    </span>
                    <span className="text-xs text-zinc-500">•</span>
                    <span className="text-xs text-zinc-400 font-mono capitalize">{sub.language}</span>
                    </div>
                    <div className="text-xs text-zinc-500 font-mono">
                    {new Date(sub.timestamp).toLocaleString(undefined, { dateStyle: 'short', timeStyle: 'short' })}
                    </div>
                </div>

                <div className="flex items-center space-x-4">
                    <div className="text-right text-xs font-mono space-y-0.5">
                    <div className="text-zinc-300">{sub.runtime} ms</div>
                    <div className="text-zinc-500">{sub.memory} MB</div>
                    </div>
                    <div className="text-zinc-500 group-hover:text-amber-500 text-xs font-semibold">
                    View Code
                    </div>
                </div>
                </div>
            ))}
            </div>
        ) : (
            <div className="py-12 text-center text-zinc-500 font-medium">
            <HelpCircle className="w-10 h-10 mx-auto text-zinc-700 mb-2 animate-pulse" />
            <p>No submissions found for this problem.</p>
            <p className="text-xs text-zinc-600 mt-1">Submit your code inside the editor sandbox to see records here.</p>
            </div>
        )}
    </div>
  )
}



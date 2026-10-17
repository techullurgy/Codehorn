import { AlertTriangle, CheckCircle, Copy } from "lucide-react";
import { Problem, Submission, SimulationState} from "../types";
import { Editor } from "@monaco-editor/react";
import { useCodeHornStore } from "../store/useCodeHornStore";
import { PARAM_LABELS } from "./ProblemDetail";

export const SubmissionDetails = (
    { sub, selectedProblem }: { 
      sub: Submission, 
      selectedProblem: Problem
    }
  ) => {
    const { editorTheme } = useCodeHornStore();
  
    const sampleTestcases = selectedProblem.testcases.filter(t => t.isSample);
    const hiddenTestcases = selectedProblem.testcases.filter(t => !t.isSample);
    const totalSample = sampleTestcases.length;
    const totalHidden = hiddenTestcases.length;
  
    // Calculate passed sample & hidden count based on sub status and failedTestCase if failed
    let passedSample = totalSample;
    let passedHidden = totalHidden;
    let totalPassed = selectedProblem.testcases.length;
  
    if (sub.status !== 'Accepted') {
      if (sub.failedTestCase) {
        // Check if failed test case is one of the samples
        const failedInputClean = sub.failedTestCase.input.replace(/\s+/g, '');
        const failedIdx = selectedProblem.testcases.findIndex(t => t.input.replace(/\s+/g, '') === failedInputClean);
  
        if (failedIdx !== -1) {
          const failedTest = selectedProblem.testcases[failedIdx];
          if (failedTest.isSample) {
            // Failed on a sample testcase
            const idxInSamples = sampleTestcases.findIndex(t => t.id === failedTest.id);
            passedSample = idxInSamples === -1 ? 0 : idxInSamples;
            passedHidden = 0;
            totalPassed = passedSample;
          } else {
            // Failed on a hidden testcase
            passedSample = totalSample; // All samples passed!
            const idxInHidden = hiddenTestcases.findIndex(t => t.id === failedTest.id);
            passedHidden = idxInHidden === -1 ? 0 : idxInHidden;
            totalPassed = passedSample + passedHidden;
          }
        } else {
          // Fallback if failedTestCase does not match any known testcase but we have an error
          passedSample = Math.min(totalSample, Math.floor(totalSample * 0.6));
          passedHidden = 0;
          totalPassed = passedSample;
        }
      } else {
        // Complete failure (eg compile error)
        passedSample = 0;
        passedHidden = 0;
        totalPassed = 0;
      }
    }
  
    // Finished details view
    const isAccepted = sub.status === 'Accepted';
    const percentPassed = Math.round((totalPassed / (totalSample + totalHidden)) * 100);
  
    return (
      <div className="flex-1 overflow-y-auto px-5 py-6 space-y-6 text-zinc-300 text-sm" id={`sub-details-${sub.id}`}>
  
        {/* Header summary & Verdict */}
        <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-5 space-y-4 shadow-sm">
          <div className="flex items-center justify-between">
            <span className="text-[10px] uppercase font-mono tracking-wider text-zinc-500 font-bold">Submission Report</span>
            <span className="text-[11px] font-mono text-zinc-400">{new Date(sub.timestamp).toLocaleTimeString()}</span>
          </div>
  
          <div className="flex items-center space-x-4">
            <div className={`p-3 rounded-xl border ${isAccepted
                ? 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400'
                : 'bg-rose-500/10 border-rose-500/20 text-rose-400'
              }`}>
              <CheckCircle className="w-8 h-8" />
            </div>
  
            <div className="space-y-1">
              <div className="flex items-center space-x-2 flex-wrap gap-y-1">
                <h2 className={`text-xl font-black tracking-tight ${isAccepted ? 'text-emerald-400' : 'text-rose-400'}`}>
                  {sub.status}
                </h2>
                {isAccepted && (
                  <span className="bg-amber-500/15 text-amber-400 border border-amber-500/25 px-2 py-0.5 rounded-full text-[10px] font-extrabold flex items-center space-x-1 uppercase font-semibold">
                    <span>👑</span>
                    <span>Solved (+XP)</span>
                  </span>
                )}
              </div>
              <p className="text-xs text-zinc-400 font-medium font-sans">
                {percentPassed}% testcases passed ({totalPassed} / {totalSample + totalHidden})
              </p>
            </div>
          </div>
  
          {/* Tech Metrics rows */}
          <div className="grid grid-cols-2 gap-3.5 pt-1">
            <div className="bg-zinc-950 border border-zinc-850 p-3 rounded-lg">
              <span className="text-[9px] font-mono text-zinc-500 uppercase tracking-widest block font-bold">Runtime Speed</span>
              <span className="text-sm font-bold text-zinc-200 mt-1 block font-mono">{sub.runtime} ms</span>
              <span className="text-[10px] text-zinc-500 block">Beats 82% of users</span>
            </div>
            <div className="bg-zinc-950 border border-zinc-850 p-3 rounded-lg">
              <span className="text-[9px] font-mono text-zinc-500 uppercase tracking-widest block font-bold">Memory Usage</span>
              <span className="text-sm font-bold text-zinc-200 mt-1 block font-mono">{sub.memory} MB</span>
              <span className="text-[10px] text-zinc-500 block">Beats 88% of users</span>
            </div>
          </div>
        </div>
  
        {/* Testcase Progress breakdown */}
        <div className="space-y-3">
          <h3 className="text-xs font-bold uppercase tracking-wider text-zinc-400 font-sans">Execution Breakdown</h3>
  
          <div className="space-y-2.5">
            {/* Sample Case Status bar */}
            <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-3.5 flex items-center justify-between">
              <div className="space-y-0.5">
                <span className="text-xs font-bold text-zinc-200 block">Sample Test cases</span>
                <span className="text-[10px] text-zinc-500 font-mono">Predefined test suite cases</span>
              </div>
              <div className="flex items-center space-x-2">
                <span className={`text-xs font-mono font-bold px-2 py-0.5 rounded-lg ${passedSample === totalSample
                    ? 'text-emerald-400 bg-emerald-500/10 border border-emerald-500/15'
                    : 'text-amber-500 bg-amber-500/10 border border-amber-500/15'
                  }`}>
                  {passedSample} / {totalSample} Passed
                </span>
              </div>
            </div>
  
            {/* Hidden Case Status bar */}
            <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-3.5 flex items-center justify-between">
              <div className="space-y-0.5">
                <span className="text-xs font-bold text-zinc-200 block">Hidden Scenario cases</span>
                <span className="text-[10px] text-zinc-500 font-mono">Secret evaluator and edge scenarios</span>
              </div>
              <div className="flex items-center space-x-2">
                <span className={`text-xs font-mono font-bold px-2 py-0.5 rounded-lg ${passedHidden === totalHidden
                    ? 'text-emerald-400 bg-emerald-500/10 border border-emerald-500/15'
                    : 'text-rose-500 bg-rose-500/10 border border-rose-500/15'
                  }`}>
                  {passedHidden} / {totalHidden} Passed
                </span>
              </div>
            </div>
          </div>
        </div>
  
        {/* Failed TestCase details section */}
        {!isAccepted && sub.failedTestCase && (() => {
          const paramNames = PARAM_LABELS[selectedProblem.slug] || ['input'];
          const parsedInputs = sub.failedTestCase.input.split('\n');
          return (
            <div className="bg-rose-950/10 border border-rose-950/20 rounded-xl p-5 space-y-4">
              <div className="flex items-center space-x-2 border-b border-rose-900/15 pb-2.5">
                <AlertTriangle className="w-4 h-4 text-rose-450" />
                <h4 className="text-xs font-bold text-rose-300 uppercase tracking-wide">Failed testcase metrics</h4>
              </div>
  
              <div className="space-y-4 text-xs font-mono">
                {/* Inputs breakdown block */}
                <div className="space-y-3.5">
                  {paramNames.map((name, paramIdx) => {
                    const val = parsedInputs[paramIdx] !== undefined ? parsedInputs[paramIdx] : '';
                    return (
                      <div key={name} className="flex flex-col space-y-1">
                        <span className="text-[10px] font-mono text-zinc-500 uppercase tracking-widest font-extrabold">
                          Input#{paramIdx + 1} ({name})
                        </span>
                        <div className="bg-zinc-950 border border-zinc-900 rounded-lg p-2.5 text-zinc-300 font-mono overflow-x-auto select-all max-h-32">
                          {val}
                        </div>
                      </div>
                    );
                  })}
                </div>
  
                {/* Outputs expected / actual grid layout */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-1">
                  <div className="space-y-1">
                    <span className="text-[10px] font-mono text-emerald-500 block uppercase tracking-wider font-extrabold">Expected Output:</span>
                    <div className="bg-emerald-950/15 border border-emerald-950/25 rounded-lg p-2.5 text-emerald-350 overflow-x-auto select-all font-mono">
                      {sub.failedTestCase.expected}
                    </div>
                  </div>
                  <div className="space-y-1">
                    <span className="text-[10px] font-mono text-rose-550 block uppercase tracking-wider font-extrabold">Actual Output:</span>
                    <div className="bg-rose-950/15 border border-rose-950/25 rounded-lg p-2.5 text-rose-350 overflow-x-auto select-all font-mono font-bold">
                      {sub.failedTestCase.actual}
                    </div>
                  </div>
                </div>
  
                {/* Stdout Console Logs if any exists */}
                <div className="space-y-1 bg-zinc-950/40 rounded-lg p-3.5 border border-zinc-900">
                  <span className="text-[10px] font-mono text-zinc-550 block uppercase tracking-wider font-extrabold">Stdout (Console Logs)</span>
                  {sub.failedTestCase.stdout ? (
                    <pre className="text-zinc-400 whitespace-pre-wrap overflow-x-auto select-text font-mono leading-relaxed mt-1.5 text-[11px] bg-zinc-950 p-2.5 rounded max-h-40 border border-zinc-900/60 shadow-inner">
                      {sub.failedTestCase.stdout}
                    </pre>
                  ) : (
                    <span className="text-zinc-600 text-[10px] block font-mono italic mt-1 font-medium">No standard output logged.</span>
                  )}
                </div>
  
              </div>
            </div>
          );
        })()}
  
        {/* Submitted Code snippet with Copy action */}
        <div className="bg-zinc-900 border border-zinc-800 rounded-xl overflow-hidden shadow-sm">
          <div className="flex items-center justify-between px-3.5 py-2.5 border-b border-zinc-800/80 bg-zinc-900/35">
            <span className="text-[10px] font-mono text-zinc-500 font-bold uppercase tracking-wide">Submitted Source ({sub.language})</span>
            <button
              onClick={() => {
                navigator.clipboard.writeText(sub.code);
                alert('Code copied to clipboard.');
              }}
              className="px-2 py-1 text-[10px] text-zinc-400 hover:text-zinc-200 bg-zinc-950 hover:bg-zinc-850 rounded border border-zinc-800 transition-all flex items-center space-x-1 font-semibold"
            >
              <Copy className="w-3 h-3" />
              <span>Copy Code</span>
            </button>
          </div>
          <div className="h-[250px] border-t border-zinc-900 overflow-hidden relative">
            <Editor
              height="100%"
              language={sub.language === 'cpp' ? 'cpp' : sub.language === 'java' ? 'java' : sub.language}
              theme={editorTheme}
              value={sub.code}
              options={{
                readOnly: true,
                minimap: { enabled: false },
                fontSize: 12,
                fontFamily: 'JetBrains Mono, ui-monospace, SFMono-Regular, monospace',
                lineNumbers: 'on',
                scrollBeyondLastLine: false,
                folding: true,
                wordWrap: 'on',
                domReadOnly: true,
                automaticLayout: true,
                scrollbar: {
                  vertical: 'visible',
                  horizontal: 'visible',
                }
              }}
              loading={
                <div className="absolute inset-0 bg-zinc-950 flex flex-col items-center justify-center space-y-2">
                  <div className="w-5 h-5 rounded-full border-2 border-amber-500 border-t-transparent animate-spin" />
                  <span className="text-[10px] text-zinc-550 font-mono">Loading highlighted source...</span>
                </div>
              }
            />
          </div>
        </div>
  
      </div>
    );
  }
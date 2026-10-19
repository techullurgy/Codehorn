import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";
import { TestcasesContent } from "./TestcasesContent";
import { TestResultsContent } from "./TestResultsContent";
import { ToggleConsoleButton } from "./ToggleConsoleButton";
import { RunCodeButton } from "./RunCodeButton";
import { SubmitCodeButton } from "./SubmitCodeButton";
import { Layers, Terminal } from "lucide-react";

export const ConsoleContent = () => {
  const activeConsoleTab = useProblemPlaygroundStore(
    (state) => state.activeConsoleTab,
  );
  const switchToConsoleTab = useProblemPlaygroundStore(
    (state) => state.switchToConsoleTab,
  );

  const render = () => {
    switch (activeConsoleTab) {
      case "testcases":
        return <TestcasesContent />;
      case "testresults":
        return <TestResultsContent />;
    }
  };

  return (
    <div className="flex-1 flex flex-col bg-zinc-950 min-h-0">
      {/* Console Tabs segment Header */}
      <div className="h-10 bg-zinc-900 border-y border-zinc-850 px-4 flex items-center justify-between shrink-0 select-none">
        <div className="flex items-center space-x-1">
          <button
            onClick={() => switchToConsoleTab("testcases")}
            title="Test Cases"
            className={`h-8 w-8 sm:h-auto sm:w-auto px-0 sm:px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center justify-center sm:space-x-1.5 shrink-0 transition-all ${
              activeConsoleTab === "testcases"
                ? "bg-zinc-800 text-amber-500"
                : "text-zinc-400 hover:text-zinc-200"
            }`}
          >
            <Terminal className="w-4 h-4 sm:w-3.5 sm:h-3.5" />
            <span className="hidden sm:inline">Test Cases</span>
          </button>

          <button
            onClick={() => switchToConsoleTab("testresults")}
            title="Run Results"
            className={`h-8 w-8 sm:h-auto sm:w-auto px-0 sm:px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center justify-center sm:space-x-1.5 shrink-0 transition-all ${
              activeConsoleTab === "testresults"
                ? "bg-zinc-800 text-amber-500"
                : "text-zinc-400 hover:text-zinc-200"
            }`}
          >
            <Layers className="w-4 h-4 sm:w-3.5 sm:h-3.5" />
            <span className="hidden sm:inline">Run Results</span>
          </button>
        </div>
      </div>

      {/* Console content body */}
      <div className="flex-1 overflow-y-auto custom-minimal-scrollbar min-h-0 bg-zinc-950">
        {render()}
      </div>

      {/* Bottom compiler actions bar */}
      <div className="h-12 bg-zinc-950 px-4 border-t border-zinc-850 flex items-center justify-between shrink-0 select-none">
        <span className="text-[9px] font-mono text-zinc-650 font-bold uppercase tracking-wider">
          CodeHorn System Sandbox v1.0
        </span>

        <div className="flex items-center space-x-2">
          <ToggleConsoleButton />
          <RunCodeButton />
          <SubmitCodeButton />
        </div>
      </div>
    </div>
  );
};

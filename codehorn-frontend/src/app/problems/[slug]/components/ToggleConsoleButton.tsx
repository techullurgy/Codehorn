import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";
import { Terminal } from "lucide-react";

export const ToggleConsoleButton = () => {
  const showConsole = useProblemPlaygroundStore((state) => state.showConsole);
  const toggleShowConsole = useProblemPlaygroundStore(
    (state) => state.toggleShowConsole,
  );

  return (
    <button
      onClick={toggleShowConsole}
      title={showConsole ? "Hide Console Pane" : "Show Console Pane"}
      className={`h-10 w-10 sm:h-auto sm:w-auto px-0 sm:px-3 py-1.5 text-xs rounded-lg transition-all flex items-center justify-center sm:space-x-1.5 shrink-0 border ${
        showConsole
          ? "bg-zinc-800 text-amber-500 border-zinc-700 hover:bg-zinc-750"
          : "bg-zinc-900 text-zinc-400 hover:text-zinc-100 hover:bg-zinc-850 border-zinc-805"
      }`}
      id="console-tab-switcher"
    >
      <Terminal className="w-4.5 h-4.5 sm:w-3.5 sm:h-3.5" />
      <span className="hidden sm:inline">
        {showConsole ? "Hide Console" : "Console view"}
      </span>
    </button>
  );
};

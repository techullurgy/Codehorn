import { CodeEditor } from "@/src/app/components/CodeEditor";
import { ConsoleContent } from "./ConsoleContent";
import ResizablePanel from "@/src/app/components/ResizablePanel";
import { RunCodeButton } from "./RunCodeButton";
import { Terminal } from "lucide-react";
import { useEffect } from "react";
import { useSuspenseQuery } from "@tanstack/react-query";
import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";

export const PlaygroundEditorWithConsole = () => {
  const language = useProblemPlaygroundStore(
    (state) => state.selectedCodeLanguage,
  );
  const slug = useProblemPlaygroundStore((state) => state.slug);
  const showConsole = useProblemPlaygroundStore((state) => state.showConsole);
  const toggleShowConsole = useProblemPlaygroundStore(
    (state) => state.toggleShowConsole,
  );

  const codeDraft = useProblemPlaygroundStore((state) => state.codeDraft);
  const updateCodeDraft = useProblemPlaygroundStore(
    (state) => state.updateCodeDraft,
  );

  const { data: snippet } = useSuspenseQuery<string>({
    queryKey: ["problem", slug, "snippet", language],
    queryFn: async () => null,
  });

  useEffect(() => {
    updateCodeDraft(snippet);
  }, [snippet]);

  return (
    <div className="flex-1 min-h-0 relative">
      {!showConsole ? (
        <div className="absolute inset-0 flex flex-col bg-zinc-950">
          <CodeEditor
            language={language}
            code={codeDraft}
            onCodeChange={updateCodeDraft}
            fontSize={12}
          />

          {/* Minimal button bar when console is fully collapsed */}
          <div className="absolute bottom-4 right-4 flex items-center space-x-2 z-10 px-2.5 py-1.5 bg-zinc-900/90 backdrop-blur border border-zinc-800 rounded-xl shadow-lg select-none">
            <button
              onClick={toggleShowConsole}
              className="px-3 py-1.5 bg-zinc-800 text-zinc-300 hover:text-white rounded-lg text-xs font-bold transition flex items-center space-x-1 hover:border-zinc-700 border border-transparent"
              title="Restore code runners"
            >
              <Terminal className="w-3.5 h-3.5 text-amber-500" />
              <span>Show Console</span>
            </button>

            <RunCodeButton />
          </div>
        </div>
      ) : (
        <ResizablePanel
          direction="vertical"
          initialSplit={55}
          minSize={30}
          maxSize={85}
          idPrefix="editor-console-split"
          left={
            <div className="flex-1 min-h-0 relative">
              <CodeEditor
                language={language}
                code={codeDraft}
                onCodeChange={updateCodeDraft}
                fontSize={12}
              />
            </div>
          }
          right={<ConsoleContent />}
        />
      )}
    </div>
  );
};

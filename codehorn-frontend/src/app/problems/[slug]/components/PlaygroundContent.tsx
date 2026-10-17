"use client";

import { CodeEditor } from "@/src/app/components/CodeEditor";
import ResizablePanel from "@/src/app/components/ResizablePanel";
import { useEditorStore } from "@/src/store/hooks/use-editor-store";
import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";
import { CodeLanguage } from "@/src/types/Submission";
import { useSuspenseQuery } from "@tanstack/react-query";
import { Check, Copy, Play, RotateCcw, Terminal } from "lucide-react";
import { useEffect, useState } from "react";

// PlaygroundContent
export default () => {
  return (
    <div className="flex flex-col h-full bg-zinc-950 text-zinc-100 min-w-0">
      {/* Settings bar row inside compilation wrapper */}
      <PlaygroundSettings />
      {/* Playground Editor with Console */}
      <PlaygroundEditorWithConsole />
    </div>
  );
};

const PlaygroundSettings = () => {
  const theme = useEditorStore((state) => state.theme);
  const setTheme = useEditorStore((state) => state.setTheme);

  const language = useProblemPlaygroundStore(
    (state) => state.selectedCodeLanguage,
  );
  const setLanguage = useProblemPlaygroundStore(
    (state) => state.changeSelectedCodeLanguage,
  );

  const [copiedDraft] = useState(false);

  const handleResetDraft = () => {};
  const handleCopyCode = () => {};

  return (
    <div className="px-4 h-12 flex items-center justify-between border-b border-zinc-800 bg-zinc-900/10 shrink-0 select-none">
      {/* Language selector selection field */}
      <div className="flex items-center space-x-3">
        <select
          id="editor-language-select"
          value={language}
          onChange={(e) => setLanguage(e.target.value as CodeLanguage)}
          className="bg-zinc-900 border border-zinc-800 rounded-lg py-1 px-2.5 text-xs font-semibold text-zinc-300 pointer-events-auto cursor-pointer focus:outline-none focus:border-amber-500"
        >
          <option value="javascript">JavaScript</option>
          <option value="python">Python 3</option>
          <option value="cpp">C++ (Gcc)</option>
          <option value="c">C (Gcc)</option>
          <option value="java">Java 25</option>
        </select>
      </div>

      {/* Editing and settings widgets */}
      <div className="flex items-center space-x-2">
        {/* Editor Theme Dropdown */}
        <select
          value={theme}
          onChange={(e) => setTheme(e.target.value as any)}
          className="bg-zinc-900 border border-zinc-800 rounded-lg py-1 px-2 text-xs font-semibold text-zinc-400 focus:outline-none cursor-pointer hover:border-zinc-700 transition"
          title="Monaco Editor Theme"
        >
          <option value="vs-dark">Dark</option>
          <option value="hc-black">Contrast</option>
          <option value="light">Light</option>
        </select>

        <div className="h-4 w-px bg-zinc-805" />

        {/* Copy draft btn */}
        <button
          onClick={handleCopyCode}
          className="h-8 w-8 flex items-center justify-center rounded-lg border border-zinc-800 bg-zinc-900 text-zinc-400 hover:text-zinc-200 hover:bg-zinc-850 transition-all shadow-sm"
          title="Copy Code"
        >
          {copiedDraft ? (
            <Check className="w-4 h-4 text-emerald-400" />
          ) : (
            <Copy className="w-4 h-4" />
          )}
        </button>

        {/* Reset draft btn */}
        <button
          onClick={handleResetDraft}
          className="h-8 w-8 flex items-center justify-center rounded-lg border border-zinc-800 bg-zinc-900 text-zinc-400 hover:text-zinc-200 hover:bg-zinc-850 transition-all shadow-sm"
          title="Reset Code Draft"
        >
          <RotateCcw className="w-4 h-4" />
        </button>
      </div>
    </div>
  );
};

const PlaygroundEditorWithConsole = () => {
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

  const isRunning = false;
  const isSubmitting = false;

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

            <button
              onClick={() => {
                toggleShowConsole();
                // runUserCode();
              }}
              disabled={isRunning || isSubmitting}
              className="px-3.5 py-1.5 bg-indigo-600 hover:bg-indigo-555 text-white rounded-lg text-xs font-semibold transition flex items-center space-x-1 shadow"
            >
              <Play className="w-3 h-3 fill-white" />
              <span>Run</span>
            </button>
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
          right={<BottomElement />}
        />
      )}
    </div>
  );
};

const BottomElement = () => {
  return <p>Bottom Element</p>;
};

import { useEditorStore } from "@/src/store/hooks/use-editor-store";
import { Check, Copy, RotateCcw } from "lucide-react";
import { useState } from "react";
import { CodeLanguageSelector } from "./CodeLanguageSelector";

export const PlaygroundSettings = () => {
  const theme = useEditorStore((state) => state.theme);
  const setTheme = useEditorStore((state) => state.setTheme);

  const [copiedDraft] = useState(false);

  const handleResetDraft = () => {};
  const handleCopyCode = () => {};

  return (
    <div className="px-4 h-12 flex items-center justify-between border-b border-zinc-800 bg-zinc-900/10 shrink-0 select-none">
      {/* Language selector selection field */}
      <CodeLanguageSelector />

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

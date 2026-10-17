"use client";

import React, { useState } from "react";
import Editor from "@monaco-editor/react";
import { Check, Terminal, Copy } from "lucide-react";
import { useCodeHornStore } from "../store/useCodeHornStore";

interface MarkdownCodeBlockProps {
  lang: string;
  code: string;
}

export default function MarkdownCodeBlock({
  lang,
  code,
}: MarkdownCodeBlockProps) {
  const { editorTheme: theme } = useCodeHornStore();
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(code);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const lines = code.split("\n").length;
  const calculatedHeight = Math.min(Math.max(lines * 20 + 24, 80), 400);
  const normalizedLang =
    lang === "cpp"
      ? "cpp"
      : lang === "java"
        ? "java"
        : lang === "py" || lang === "python3" || lang === "python"
          ? "python"
          : lang;

  return (
    <div
      className="my-4 rounded-xl border border-zinc-900 bg-zinc-950 overflow-hidden shadow-xl"
      id="markdown-sc-block"
    >
      <div className="flex items-center justify-between px-4 py-2 bg-zinc-900/60 border-b border-zinc-900 text-[11px] text-zinc-400 font-mono select-none">
        <div className="flex items-center space-x-2">
          <Terminal className="w-3.5 h-3.5 text-amber-500 animate-pulse" />
          <span className="uppercase tracking-wider font-bold text-zinc-350">
            {normalizedLang}
          </span>
        </div>
        <button
          onClick={handleCopy}
          className="flex items-center space-x-1.5 px-2.5 py-1 rounded bg-zinc-800/60 hover:bg-zinc-800 text-zinc-300 hover:text-white transition-all duration-150 border border-zinc-700/30"
        >
          {copied ? (
            <>
              <Check className="w-3.5 h-3.5 text-emerald-400" />
              <span className="text-[10px] text-emerald-400 font-bold">
                Copied!
              </span>
            </>
          ) : (
            <>
              <Copy className="w-3.5 h-3.5" />
              <span className="text-[10px] font-semibold">Copy Code</span>
            </>
          )}
        </button>
      </div>
      <div
        style={{ height: `${calculatedHeight}px` }}
        className="relative overflow-hidden w-full select-text"
      >
        <Editor
          height="100%"
          language={normalizedLang}
          theme={theme}
          value={code}
          options={{
            readOnly: true,
            minimap: { enabled: false },
            fontSize: 12,
            fontFamily:
              "JetBrains Mono, ui-monospace, SFMono-Regular, monospace",
            lineNumbers: "on",
            scrollBeyondLastLine: false,
            folding: true,
            wordWrap: "on",
            domReadOnly: true,
            automaticLayout: true,
            scrollbar: {
              vertical: "visible",
              horizontal: "visible",
            },
            padding: { top: 12, bottom: 12 },
          }}
          loading={
            <div className="absolute inset-0 bg-zinc-950 flex flex-col items-center justify-center space-y-2">
              <div className="w-4 h-4 rounded-full border-2 border-amber-500 border-t-transparent animate-spin" />
            </div>
          }
        />
      </div>
    </div>
  );
}

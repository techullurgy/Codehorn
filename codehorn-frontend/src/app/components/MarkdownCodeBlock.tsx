"use client";

import React, { useState } from "react";
import { Check, Terminal, Copy } from "lucide-react";
import { StaticCodeViewer } from "./StaticCodeViewer";
import { CodeLanguage } from "@/src/types/Submission";

interface MarkdownCodeBlockProps {
  language: CodeLanguage;
  code: string;
}

export default function MarkdownCodeBlock({
  language,
  code,
}: MarkdownCodeBlockProps) {
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(code);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const lines = code.split("\n").length;
  const calculatedHeight = Math.min(Math.max(lines * 20 + 24, 80), 400);

  return (
    <div
      className="my-4 rounded-xl border border-zinc-900 bg-zinc-950 overflow-hidden shadow-xl"
      id="markdown-sc-block"
    >
      <div className="flex items-center justify-between px-4 py-2 bg-zinc-900/60 border-b border-zinc-900 text-[11px] text-zinc-400 font-mono select-none">
        <div className="flex items-center space-x-2">
          <Terminal className="w-3.5 h-3.5 text-amber-500 animate-pulse" />
          <span className="uppercase tracking-wider font-bold text-zinc-350">
            {language}
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
        <StaticCodeViewer language={language} code={code} />
      </div>
    </div>
  );
}

"use client";

import { useEditorStore } from "@/src/store/hooks/use-editor-store";
import { CodeLanguage } from "@/src/types/Submission";
import { Editor } from "@monaco-editor/react";

type CodeEditorProps = {
  language: CodeLanguage;
  code: string;
  onCodeChange: (code: string) => void;
  fontSize: number;
};

export const CodeEditor = ({
  language,
  code,
  onCodeChange,
  fontSize = 12,
}: CodeEditorProps) => {
  const theme = useEditorStore((state) => state.theme);

  return (
    <Editor
      height="100%"
      language={language}
      theme={theme}
      value={code}
      onChange={(value) => onCodeChange(value)}
      loading={
        <div className="absolute inset-0 bg-zinc-950 flex flex-col items-center justify-center space-y-2">
          <div className="w-8 h-8 rounded-full border-4 border-amber-500 border-t-transparent animate-spin" />
          <span className="text-xs text-zinc-500 font-mono">
            Initializing Monaco...
          </span>
        </div>
      }
      options={{
        fontSize: fontSize,
        fontFamily: "Fira Code, JetBrains Mono, monospace",
        minimap: { enabled: false },
        scrollBeyondLastLine: false,
        automaticLayout: true,
        lineNumbers: "on",
        tabSize: 4,
        cursorBlinking: "smooth",
        padding: { top: 12, bottom: 12 },
        renderLineHighlight: "all",
        scrollbar: {
          vertical: "visible",
          horizontal: "visible",
          useShadows: false,
          verticalScrollbarSize: 10,
          horizontalScrollbarSize: 10,
        },
      }}
    />
  );
};

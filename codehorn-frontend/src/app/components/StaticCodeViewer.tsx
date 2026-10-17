import { useEditorStore } from "@/src/store/hooks/use-editor-store";
import { Editor } from "@monaco-editor/react";

type StaticCodeViewerProps = {
  language: string;
  code: string;
};

export const StaticCodeViewer = ({ language, code }: StaticCodeViewerProps) => {
  const theme = useEditorStore((state) => state.theme);

  return (
    <Editor
      height="100%"
      language={language}
      theme={theme}
      value={code}
      options={{
        readOnly: true,
        minimap: { enabled: false },
        fontSize: 12,
        fontFamily: "JetBrains Mono, ui-monospace, SFMono-Regular, monospace",
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
  );
};

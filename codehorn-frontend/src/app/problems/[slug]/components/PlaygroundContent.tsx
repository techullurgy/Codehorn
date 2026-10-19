"use client";

import { PlaygroundEditorWithConsole } from "./PlaygroundEditorWithConsole";
import { PlaygroundSettings } from "./PlaygroundSettings";

// PlaygroundContent
export default () => {
  return (
    <div className="flex flex-col h-full bg-zinc-950 text-zinc-100 min-w-0">
      <PlaygroundSettings />
      <PlaygroundEditorWithConsole />
    </div>
  );
};

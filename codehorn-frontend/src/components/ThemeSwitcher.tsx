"use client";

import { Moon, Sun } from "lucide-react";
import { useState } from "react";

export const ThemeSwitcher = () => {
  const [theme, setTheme] = useState("dark");

  return (
    <div className="flex items-center bg-zinc-900 p-1 rounded-lg border border-zinc-800">
      <button
        onClick={() => setTheme("dark")}
        className={`p-1 rounded-md transition-all ${
          theme === "dark"
            ? "bg-zinc-800 text-zinc-100 shadow-sm"
            : "text-zinc-500 hover:text-zinc-300"
        }`}
        title="Dark Global Theme"
      >
        <Moon className="w-3.5 h-3.5" />
      </button>

      <button
        onClick={() => setTheme("light")}
        className={`p-1 rounded-md transition-all ${
          theme === "light"
            ? "bg-zinc-800 text-zinc-100 shadow-sm"
            : "text-zinc-500 hover:text-zinc-300"
        }`}
        title="Light Global Theme"
      >
        <Sun className="w-3.5 h-3.5" />
      </button>
    </div>
  );
};

"use client";

import React, { useRef } from "react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import Navbar from "./NavBar";
import { getQueryClient } from "../libs/get-query-client";

interface AppLayoutProps {
  children: React.ReactNode;
}

export default function AppLayout({ children }: AppLayoutProps) {
  const queryClient = useRef<QueryClient>(getQueryClient());

  const globalTheme = "light";

  return (
    <QueryClientProvider client={queryClient.current}>
      <div
        className={`${"min-h-screen"} w-screen flex flex-col font-sans ${"overflow-y-auto custom-minimal-scrollbar"} ${
          globalTheme === "light" ? "global-theme-light" : ""
        } bg-zinc-950 text-zinc-100`}
      >
        {/* Top command navbar */}
        <Navbar />

        {/* Main viewport area panel */}
        <main
          className={`flex-1 flex flex-col ${"overflow-hidden min-h-0"} relative`}
        >
          {children}
        </main>
      </div>
    </QueryClientProvider>
  );
}

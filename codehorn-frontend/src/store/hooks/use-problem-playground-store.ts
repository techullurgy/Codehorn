"use client"

import { ProblemPlaygroundContext } from "@/src/contexts/problem-playground-context";
import { ProblemPlaygroundStore } from "../problem-playground-store";
import { useContext } from "react";
import { useStore } from "zustand";

export function useProblemPlaygroundStore<T>(
  selector: (state: ProblemPlaygroundStore) => T
) {
  const store = useContext(ProblemPlaygroundContext)

  if (!store) {
    throw new Error('useProblemPlaygroundStore must be used within a ProblemPlaygroundStoreProvider')
  }

  return useStore(store, selector)
}
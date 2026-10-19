"use client";

import { createContext, PropsWithChildren, useState } from "react";
import {
  createProblemPlaygroundStore,
  ProblemPlaygroundStore,
} from "../store/problem-playground-store";
import { StoreApi } from "zustand";
import { ProblemTestCase } from "../types/Problem";

export const ProblemPlaygroundContext = createContext<
  StoreApi<ProblemPlaygroundStore> | undefined
>(undefined);

type ProblemPlaygroundContextProviderProps = {
  slug: string;
  testcases: ProblemTestCase[];
} & PropsWithChildren;

export const ProblemPlaygroundContextProvider = ({
  children,
  slug,
  testcases,
}: ProblemPlaygroundContextProviderProps) => {
  const [store] = useState<StoreApi<ProblemPlaygroundStore>>(() =>
    createProblemPlaygroundStore({
      slug,
      testcases,
      originalTestcasesCount: testcases.length,
    }),
  );

  return (
    <ProblemPlaygroundContext.Provider value={store}>
      {children}
    </ProblemPlaygroundContext.Provider>
  );
};

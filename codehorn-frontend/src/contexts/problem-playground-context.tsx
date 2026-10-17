"use client";

import { createContext, PropsWithChildren, useState } from "react";
import {
  createProblemPlaygroundStore,
  ProblemPlaygroundStore,
} from "../store/problem-playground-store";
import { StoreApi } from "zustand";

export const ProblemPlaygroundContext = createContext<
  StoreApi<ProblemPlaygroundStore> | undefined
>(undefined);

type ProblemPlaygroundContextProviderProps = {
  slug: string;
} & PropsWithChildren;

export const ProblemPlaygroundContextProvider = ({
  children,
  slug,
}: ProblemPlaygroundContextProviderProps) => {
  const [store] = useState<StoreApi<ProblemPlaygroundStore>>(() =>
    createProblemPlaygroundStore({ slug }),
  );

  return (
    <ProblemPlaygroundContext.Provider value={store}>
      {children}
    </ProblemPlaygroundContext.Provider>
  );
};

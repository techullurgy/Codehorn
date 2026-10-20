"use client";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { getQueryClient } from "../libs/get-query-client";
import { useRef } from "react";

export default ({ children }) => {
  const queryClient = useRef<QueryClient>(getQueryClient());

  return (
    <QueryClientProvider client={queryClient.current}>
      {children}
    </QueryClientProvider>
  );
};

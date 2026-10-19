import { runUserCodeForSubmission } from "@/src/actions/run-user-code";
import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";
import { TestcaseResult } from "@/src/types/Submission";
import {
  useIsMutating,
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";
import { Play } from "lucide-react";

export const RunCodeButton = () => {
  const slug = useProblemPlaygroundStore((state) => state.slug);
  const testcases = useProblemPlaygroundStore((state) => state.testcases);
  const switchToConsoleTab = useProblemPlaygroundStore(
    (state) => state.switchToConsoleTab,
  );

  const { mutate: runUserCode, isPending: isRunning } = useMutation<
    TestcaseResult[]
  >({
    mutationKey: ["problem", slug, "run-code"],
    mutationFn: async () => runUserCodeForSubmission(testcases),
    onMutate: () => {
      switchToConsoleTab("testresults");
    },
  });

  const isSubmitting =
    useIsMutating({
      mutationKey: ["problem", slug, "submit-code"],
      exact: true,
    }) > 0;

  return (
    <button
      id="codehorn-run-btn"
      onClick={() => runUserCode()}
      disabled={isRunning || isSubmitting}
      title="Run tests"
      className="h-10 w-10 sm:h-auto sm:w-auto px-0 sm:px-4 py-1.5 text-xs bg-indigo-600 hover:bg-indigo-550 text-white border border-transparent rounded-lg font-medium transition-all flex items-center justify-center sm:space-x-1.5 shadow-sm hover:shadow-indigo-600/10 disabled:opacity-40 disabled:cursor-not-allowed shrink-0"
    >
      {isRunning ? (
        <div className="w-4 h-4 sm:w-3.5 sm:h-3.5 rounded-full border border-white border-t-transparent animate-spin" />
      ) : (
        <Play className="w-4 h-4 sm:w-3 sm:h-3 text-white fill-white" />
      )}
      <span className="hidden sm:inline">Run tests</span>
    </button>
  );
};

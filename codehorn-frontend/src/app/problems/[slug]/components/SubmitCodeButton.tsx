import { submitUserCodeForSubmission } from "@/src/actions/submit-user-code";
import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";
import { Submission } from "@/src/types/Submission";
import {
  useIsMutating,
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";
import { CheckCircle } from "lucide-react";

export const SubmitCodeButton = () => {
  const slug = useProblemPlaygroundStore((state) => state.slug);
  const addToLeftTabs = useProblemPlaygroundStore(
    (state) => state.addToLeftTabs,
  );
  const replaceLeftTab = useProblemPlaygroundStore(
    (state) => state.replaceLeftTab,
  );

  const queryClient = useQueryClient();

  const { mutate: submitUserCode, isPending: isSubmitting } =
    useMutation<Submission>({
      mutationKey: ["problem", slug, "submit-code"],
      mutationFn: async () => submitUserCodeForSubmission(),
      onMutate: () => {
        addToLeftTabs("submission-mutation");
      },
      onSuccess: (data) => {
        queryClient.setQueryData(
          ["problem", slug, "submission", data.id],
          data,
        );

        replaceLeftTab("submission-mutation", data.id);

        queryClient.invalidateQueries({
          queryKey: ["problem", slug, "submissions"],
        });
      },
    });

  const isRunning =
    useIsMutating({
      mutationKey: ["problem", slug, "run-code"],
      exact: true,
    }) > 0;

  return (
    <button
      id="codehorn-submit-btn"
      onClick={() => submitUserCode()}
      disabled={isRunning || isSubmitting}
      title="Submit Solution"
      className="h-10 w-10 sm:h-auto sm:w-auto px-0 sm:px-4 py-1.5 text-xs bg-emerald-600 hover:bg-emerald-555 text-white border border-transparent rounded-lg font-semibold transition-all flex items-center justify-center sm:space-x-1.5 shadow-sm hover:shadow-emerald-600/10 disabled:opacity-40 disabled:cursor-not-allowed shrink-0"
    >
      {isSubmitting ? (
        <div className="w-4 h-4 sm:w-3.5 sm:h-3.5 rounded-full border border-white border-t-transparent animate-spin" />
      ) : (
        <CheckCircle className="w-4.5 h-4.5 sm:w-3.5 sm:h-3.5 text-white" />
      )}
      <span className="hidden sm:inline">Submit Solution</span>
    </button>
  );
};

"use client";

import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";
import { ReactNode, Suspense } from "react";
import SubmissionDetails from "./SubmissionDetails";
import { X } from "lucide-react";

type LeftPanelProps = LeftPanelElementProps;

type LeftPanelElementProps = {
  descriptionElement: ReactNode;
  editorialElement: ReactNode;
  submissionsElement: ReactNode;
};

// LeftPanel
export default ({
  descriptionElement,
  editorialElement,
  submissionsElement,
}: LeftPanelProps) => {
  const activeLeftTab = useProblemPlaygroundStore(
    (state) => state.activeLeftTab,
  );

  const switchToLeftTab = useProblemPlaygroundStore(
    (state) => state.switchToLeftTab,
  );
  const closeLeftTab = useProblemPlaygroundStore((state) => state.closeLeftTab);

  const leftTabs = useProblemPlaygroundStore((state) => state.leftTabs);

  const staticTabs = leftTabs.filter((tab) => !tab.includes("submission-"));
  const submissionsTabs = leftTabs.filter((tab) => tab.includes("submission-"));

  return (
    <div className="flex flex-col h-full bg-zinc-950 border-r border-zinc-800 text-zinc-100 min-w-0">
      <div className="flex border-b border-zinc-850 shrink-0 bg-zinc-950/45 text-xs font-medium px-4 select-none overflow-x-auto scrollbar-none gap-0.5">
        {staticTabs.map((tab) => (
          <button
            key={tab}
            onClick={() => switchToLeftTab(tab)}
            className={`px-3 py-2.5 border-b-2 font-semibold transition-all duration-150 shrink-0 ${
              activeLeftTab === tab
                ? "border-zinc-200 text-zinc-100"
                : "border-transparent text-zinc-500 hover:text-zinc-200"
            }`}
          >
            {(() => {
              switch (tab) {
                case "description":
                  return "Description";
                case "editorial":
                  return "Editorial";
                case "submissions":
                  return "Submissions";
              }
            })()}
          </button>
        ))}

        {submissionsTabs.map((sub) => (
          <div
            key={sub}
            className={`flex items-center space-x-1.5 py-1.5 px-2.5 border-b-2 transition-all duration-150 shrink-0 ${
              activeLeftTab === sub
                ? "border-zinc-200 text-zinc-100 bg-zinc-900/40"
                : "border-transparent text-zinc-500 hover:text-zinc-200"
            }`}
          >
            <button
              type="button"
              onClick={() => switchToLeftTab(sub)}
              className="font-semibold text-xs flex items-center space-x-1 cursor-pointer"
            >
              {(() => {
                if (sub.includes("submission-")) {
                  if (sub.includes("mutation")) {
                    return "Sub *";
                  } else {
                    return `Sub #${sub.replace("submission-", "").substr(0, 11)}`;
                  }
                }
              })()}
            </button>
            <button
              type="button"
              onClick={(e) => {
                e.stopPropagation();
                closeLeftTab(sub);
              }}
              className="p-0.5 rounded hover:bg-zinc-800 text-zinc-600 hover:text-zinc-300 transition-all cursor-pointer"
              title="Close tab"
            >
              <X className="w-3 h-3" />
            </button>
          </div>
        ))}
      </div>

      <LeftPanelContent
        activeLeftTab={activeLeftTab}
        descriptionElement={descriptionElement}
        editorialElement={editorialElement}
        submissionsElement={submissionsElement}
      />
    </div>
  );
};

const LeftPanelContent = ({
  activeLeftTab,
  descriptionElement,
  editorialElement,
  submissionsElement,
}: { activeLeftTab: string } & LeftPanelElementProps) => {
  switch (activeLeftTab) {
    case "description":
      return descriptionElement;
    case "editorial":
      return editorialElement;
    case "submissions":
      return submissionsElement;
    default:
      return (
        <>
          <Suspense fallback={`Loading Submission ${activeLeftTab}`}>
            <SubmissionDetails submissionId={activeLeftTab} />
          </Suspense>
        </>
      );
  }
};

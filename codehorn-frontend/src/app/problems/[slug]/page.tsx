import { getProblemBySlug } from "@/src/actions/get-problem-by-slug";
import { ProblemPlaygroundContextProvider } from "@/src/contexts/problem-playground-context";
import { getQueryClient } from "@/src/libs/get-query-client";
import { Problem } from "@/src/types/Problem";
import { dehydrate, HydrationBoundary, noop } from "@tanstack/react-query";
import LeftPanel from "./components/LeftPanel";
import SubmissionsContent from "./components/SubmissionsContent";
import EditorialContent from "./components/EditorialContent";
import DescriptionContent from "./components/DescriptionContent";
import AppLayout from "@/src/components/AppLayout";
import PlaygroundContent from "./components/PlaygroundContent";
import ResizablePanel from "../../components/ResizablePanel";
import { Suspense } from "react";

type ProblemPlaygroundPageProps = {
  params: Promise<{ slug: string }>;
};

// ProblemPlaygrondPage
export default async ({ params }: ProblemPlaygroundPageProps) => {
  const slug = (await params).slug;

  const queryClient = getQueryClient();

  const problem = await queryClient
    .fetchQuery<Problem>({
      queryKey: ["problem", slug],
      queryFn: () => getProblemBySlug(slug),
    })
    .catch(noop);

  if (!problem) {
    throw Error("No problem found");
  }

  const LeftPanelElement = (
    <LeftPanel
      descriptionElement={
        <DescriptionContent isDailyChallenge={false} problem={problem} />
      }
      editorialElement={<EditorialContent editorial={"Editorial"} />}
      submissionsElement={<SubmissionsContent />}
    />
  );

  const RightPanelElement = <PlaygroundContent />;

  return (
    <>
      <AppLayout>
        <HydrationBoundary state={dehydrate(queryClient)}>
          <ProblemPlaygroundContextProvider
            slug={slug}
            testcases={problem.testcases}
          >
            <div
              className="flex-1 min-h-0 bg-zinc-950 overflow-hidden"
              id="workspace-splits-panel"
            >
              <ResizablePanel
                direction="horizontal"
                initialSplit={40}
                minSize={25}
                maxSize={75}
                idPrefix="workspace-split"
                left={LeftPanelElement}
                right={RightPanelElement}
              />
            </div>
          </ProblemPlaygroundContextProvider>
        </HydrationBoundary>
      </AppLayout>
    </>
  );
};

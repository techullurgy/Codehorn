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

  return (
    <>
      <AppLayout>
        <HydrationBoundary state={dehydrate(queryClient)}>
          <ProblemPlaygroundContextProvider slug={slug}>
            <LeftPanel
              descriptionElement={
                <DescriptionContent
                  isDailyChallenge={false}
                  problem={problem}
                />
              }
              editorialElement={<EditorialContent editorial={"Editorial"} />}
              submissionsElement={<SubmissionsContent />}
            />
          </ProblemPlaygroundContextProvider>
        </HydrationBoundary>
      </AppLayout>
    </>
  );
};

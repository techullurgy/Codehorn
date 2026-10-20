import { fetchProblems } from "@/src/actions/fetch-problems";
import { fetchTotalProblemsCount } from "@/src/actions/fetch-total-problems-count";
import { DailyChallengeCalendar } from "./components/DailyChallengeCalendar";
import { FilterBox } from "./components/FilterBox";
import { Pagination } from "./components/Pagination";
import { ProblemsTable } from "./components/ProblemsTable";
import { ProgressTrackCards } from "./components/ProgressTrackCards";

type ProblemsPageProps = {
  searchParams: Promise<{
    page: number;
    loadSize: number;
    query: string;
    difficulty: string;
    status: string;
    category: string;
  }>;
};

// ProblemsPage
export default async ({ searchParams }: ProblemsPageProps) => {
  const {
    page,
    loadSize,
    query,
    difficulty,
    status,
    category: cat,
  } = await searchParams;

  const user: string = null;

  const [problems, totalProblemsCount] = await Promise.all([
    fetchProblems(user, page, loadSize),
    fetchTotalProblemsCount(),
  ]);

  const categories = ["All", "Arrays", "Linked List", "Two Pointers", "Math"];

  return (
    <div
      className="flex-1 bg-zinc-950 text-zinc-100 flex flex-col px-4 md:px-6 py-5 md:py-6"
      id="problem-listing-panel"
    >
      <ProgressTrackCards
        solvedTotal={100}
        totalProblems={150}
        totalEasy={33}
        totalMedium={44}
        totalHard={74}
        easySolvedCount={23}
        mediumSolvedCount={33}
        hardSolvedCount={43}
      />

      <DailyChallengeCalendar />

      <FilterBox
        categories={categories}
        filterCategory={cat}
        status={status}
        difficulty={difficulty}
        query={query}
      />

      <ProblemsTable problems={problems} />

      <Pagination />
    </div>
  );
};

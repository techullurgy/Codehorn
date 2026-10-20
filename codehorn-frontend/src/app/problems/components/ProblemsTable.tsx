import { BookOpen, ChevronRight } from "lucide-react";

export const ProblemsTable = ({ problems }) => (
  <div className="bg-zinc-900 border border-zinc-800 rounded-xl overflow-x-auto custom-minimal-scrollbar shadow-sm">
    <div className="min-w-187.5 md:min-w-full">
      <table className="w-full text-left border-collapse" id="problems-table">
        <thead>
          <tr className="border-b border-zinc-800 text-zinc-500 text-[10px] tracking-wider font-bold uppercase select-none bg-zinc-950/20">
            <th className="py-3.5 px-5 w-16 text-center">Status</th>
            <th className="py-3.5 px-4">Title</th>
            <th className="py-3.5 px-4 w-32">Difficulty</th>
            <th className="py-3.5 px-4 w-40">Acceptance Rate</th>
            <th className="py-3.5 px-4 w-44">Category</th>
            <th className="py-3.5 px-5 w-24 text-center">Action</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-zinc-800/45">
          {problems.length > 0 ? (
            problems.map((problem) => (
              <tr
                key={problem.id}
                id={`problems-row-${problem.slug}`}
                // onClick={() => router.push(`/problems/${problem.slug}`)}
                className="hover:bg-zinc-950/40 group cursor-pointer transition-colors duration-150 text-sm"
              >
                {/* Status checklist icon */}
                <td
                  className="py-4 px-5 text-center"
                  onClick={(e) => e.stopPropagation()}
                >
                  <div className="flex justify-center items-center">
                    {/* {getStatusIcon(problem.id)} */}
                  </div>
                </td>

                {/* Title and custom slug identifier */}
                <td className="py-4 px-4 font-medium text-zinc-200 group-hover:text-zinc-100 transition-colors duration-150">
                  <div className="flex items-center space-x-2.5">
                    <span>
                      {problem.id}. {problem.title}
                    </span>
                  </div>
                </td>

                {/* Difficulty labels */}
                <td className="py-4 px-4">
                  {/* {getDifficultyBadge(problem.difficulty)} */}
                </td>

                {/* Acceptance Rate with custom styled bar */}
                <td className="py-4 px-4 text-zinc-400 font-mono text-xs">
                  <div className="flex items-center space-x-2">
                    <span className="w-10 text-right">
                      {problem.acceptanceRate}%
                    </span>
                    <div className="flex-1 max-w-24 bg-zinc-950 h-1 rounded-full overflow-hidden border border-zinc-850">
                      <div
                        className="bg-zinc-600 h-1"
                        style={{ width: `${problem.acceptanceRate}%` }}
                      />
                    </div>
                  </div>
                </td>

                {/* Topic tag tags */}
                <td className="py-4 px-4 text-zinc-400 text-xs">
                  <span className="bg-zinc-950 px-2 py-1 rounded border border-zinc-850 font-normal">
                    {problem.category}
                  </span>
                </td>

                {/* Immediate compiler links */}
                <td className="py-4 px-5 text-center">
                  <button
                    id={`solve-btn-${problem.slug}`}
                    className="inline-flex items-center space-x-1 bg-zinc-950 text-zinc-400 hover:text-zinc-100 hover:bg-zinc-800 border border-zinc-800 hover:border-zinc-700 px-3 py-1 rounded-lg text-xs font-medium transition-all duration-150"
                  >
                    <span>Code</span>
                    <ChevronRight className="w-3.5 h-3.5 text-zinc-500" />
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td
                colSpan={6}
                className="py-12 text-center text-zinc-500 font-medium"
              >
                <div className="flex flex-col items-center justify-center space-y-2">
                  <BookOpen className="w-8 h-8 text-zinc-700 animate-pulse" />
                  <p className="text-zinc-400">
                    No problems found matching active queries.
                  </p>
                  <button
                    // onClick={resetFilters}
                    className="text-xs text-zinc-300 underline hover:text-zinc-100"
                  >
                    Clear filters and try again
                  </button>
                </div>
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  </div>
);

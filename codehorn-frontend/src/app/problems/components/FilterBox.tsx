import { Filter, RotateCcw, Search, Zap } from "lucide-react";

export const FilterBox = ({
  categories,
  filterCategory,
  query,
  status,
  difficulty,
}) => (
  <div className="bg-zinc-900/40 border border-zinc-800 rounded-xl p-4 mb-6">
    <div className="flex flex-col lg:flex-row gap-4 justify-between items-stretch">
      {/* Text Input Search */}
      <div className="relative flex-1">
        <span className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
          <Search className="w-4 h-4 text-zinc-500" />
        </span>
        <input
          type="text"
          id="problem-search-input"
          value={query}
          // onChange={(e) => setFilterSearch(e.target.value)}
          placeholder="Search problems by title, keywords, or topics..."
          className="w-full bg-zinc-950 border border-zinc-800 rounded-lg py-2 pl-10 pr-4 text-xs text-zinc-100 placeholder-zinc-500 focus:outline-none focus:border-zinc-700/80 transition-all duration-150 font-sans"
        />
      </div>

      {/* Selector controllers */}
      <div className="flex flex-wrap items-center gap-2.5">
        {/* Difficulty selector dropdown */}
        <div className="flex items-center space-x-1.5 bg-zinc-950 border border-zinc-800 rounded-lg px-2.5 py-1.5">
          <Filter className="w-3.5 h-3.5 text-zinc-500" />
          <select
            id="problem-difficulty-filter"
            value={difficulty}
            // onChange={(e) => setFilterDifficulty(e.target.value as any)}
            className="bg-transparent text-xs text-zinc-300 font-medium focus:outline-none cursor-pointer"
          >
            <option value="All" className="bg-zinc-950 text-zinc-300">
              All Difficulties
            </option>
            <option value="Easy" className="bg-zinc-950 text-emerald-400">
              Easy
            </option>
            <option value="Medium" className="bg-zinc-950 text-amber-400">
              Medium
            </option>
            <option value="Hard" className="bg-zinc-950 text-rose-400">
              Hard
            </option>
          </select>
        </div>

        {/* Status selector dropdown */}
        <div className="flex items-center space-x-1.5 bg-zinc-950 border border-zinc-800 rounded-lg px-2.5 py-1.5">
          <Zap className="w-3.5 h-3.5 text-zinc-500" />
          <select
            id="problem-status-filter"
            value={status}
            // onChange={(e) => setFilterStatus(e.target.value as any)}
            className="bg-transparent text-xs text-zinc-300 font-medium focus:outline-none cursor-pointer"
          >
            <option value="All" className="bg-zinc-950">
              All Statuses
            </option>
            <option value="Todo" className="bg-zinc-950">
              Todo
            </option>
            <option value="Attempted" className="bg-zinc-950">
              Attempted
            </option>
            <option value="Solved" className="bg-zinc-950">
              Solved
            </option>
          </select>
        </div>

        {/* Reset Filters button */}
        <button
          id="problem-reset-filters-btn"
          // onClick={resetFilters}
          className="flex items-center space-x-1.5 px-3 py-1.5 rounded-lg border border-zinc-800 text-zinc-400 hover:text-zinc-100 hover:bg-zinc-950 text-xs font-semibold transition-colors duration-150"
          title="Reset query filters"
        >
          <RotateCcw className="w-3.5 h-3.5" />
          <span>Reset</span>
        </button>
      </div>
    </div>

    {/* Categories / Topic Taxonomy selectors */}
    <div className="flex items-center space-x-2 mt-4 overflow-x-auto pb-1 border-t border-zinc-800/40 pt-3">
      <span className="text-zinc-500 text-[10px] font-bold uppercase select-none shrink-0 pr-2 tracking-wider">
        Topics:
      </span>
      {categories.map((category) => (
        <button
          key={category}
          id={`topic-tag-${category.toLowerCase().replace(/\s+/g, "-")}`}
          // onClick={() => setFilterCategory(category)}
          className={`px-3 py-1 rounded-full text-[11px] font-medium transition-all duration-150 shrink-0 border ${
            category === filterCategory
              ? "bg-zinc-800 text-zinc-100 border-zinc-700"
              : "bg-transparent text-zinc-400 border-transparent hover:text-zinc-200 hover:bg-zinc-900/40"
          }`}
        >
          {category}
        </button>
      ))}
    </div>
  </div>
);

export const ProgressTrackCards = ({
  solvedTotal,
  totalProblems,
  totalEasy,
  totalMedium,
  totalHard,
  easySolvedCount,
  mediumSolvedCount,
  hardSolvedCount,
}) => (
  <div className="grid grid-cols-2 lg:grid-cols-4 gap-3 lg:gap-4 mb-5">
    {/* Progress Tracker Meter */}
    <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-3.5 lg:p-4.5 flex flex-col justify-between shadow-sm col-span-2 lg:col-span-1">
      <div>
        <span className="text-zinc-500 text-[10px] uppercase tracking-wider font-semibold block mb-0.5">
          Your Total Progress
        </span>
        <div className="flex items-baseline space-x-2">
          <span className="text-xl lg:text-2xl font-bold text-zinc-100">
            {solvedTotal}
          </span>
          <span className="text-zinc-500 text-xs">
            / {totalProblems} Solved
          </span>
        </div>
      </div>
      <div className="mt-3">
        <div className="flex justify-between text-[10px] font-medium text-zinc-500 mb-1 uppercase tracking-wider">
          <span>Completion Rate</span>
          <span className="text-zinc-300">{90}%</span>
        </div>
        <div className="w-full bg-zinc-950 rounded-full h-1.5 overflow-hidden border border-zinc-850">
          <div
            className="bg-zinc-300 h-full rounded-full transition-all duration-500"
            style={{ width: `${90}%` }}
          />
        </div>
      </div>
    </div>

    {/* Easy Cards stats */}
    <div className="bg-emerald-950/15 border border-emerald-900/30 rounded-xl p-3.5 lg:p-4.5 flex items-center justify-between shadow-sm">
      <div>
        <span className="text-emerald-400/80 text-[10px] font-bold uppercase tracking-wider block mb-0.5">
          Easy Progress
        </span>
        <span className="text-lg lg:text-xl font-bold text-zinc-100">
          {easySolvedCount}
          <span className="text-zinc-500 text-xs font-normal font-mono">
            /{totalEasy}
          </span>
        </span>
        <span className="text-zinc-500 text-[9px] block mt-0.5">
          Acceptance ~ 50%
        </span>
      </div>
      <div className="w-8 h-8 lg:w-9 lg:h-9 rounded-full border border-emerald-900/50 bg-emerald-950/40 flex items-center justify-center font-bold text-emerald-400 text-xs shrink-0 ml-1">
        E
      </div>
    </div>

    {/* Medium Cards stats */}
    <div className="bg-amber-950/15 border border-amber-900/30 rounded-xl p-3.5 lg:p-4.5 flex items-center justify-between shadow-sm">
      <div>
        <span className="text-amber-400/80 text-[10px] font-bold uppercase tracking-wider block mb-0.5">
          Medium Progress
        </span>
        <span className="text-lg lg:text-xl font-bold text-zinc-100">
          {mediumSolvedCount}
          <span className="text-zinc-500 text-xs font-normal font-mono">
            /{totalMedium}
          </span>
        </span>
        <span className="text-zinc-500 text-[9px] block mt-0.5">
          Acceptance ~ 35%
        </span>
      </div>
      <div className="w-8 h-8 lg:w-9 lg:h-9 rounded-full border border-amber-900/50 bg-amber-950/40 flex items-center justify-center font-bold text-amber-500 text-xs shrink-0 ml-1">
        M
      </div>
    </div>

    {/* Hard Cards stats */}
    <div className="bg-rose-950/15 border border-rose-900/30 rounded-xl p-3.5 lg:p-4.5 flex items-center justify-between shadow-sm">
      <div>
        <span className="text-rose-400/80 text-[10px] font-bold uppercase tracking-wider block mb-0.5">
          Hard Progress
        </span>
        <span className="text-lg lg:text-xl font-bold text-zinc-100">
          {hardSolvedCount}
          <span className="text-zinc-500 text-xs font-normal font-mono">
            /{totalHard}
          </span>
        </span>
        <span className="text-zinc-500 text-[9px] block mt-0.5">
          Acceptance ~ 20%
        </span>
      </div>
      <div className="w-8 h-8 lg:w-9 lg:h-9 rounded-full border border-rose-900/50 bg-rose-950/40 flex items-center justify-center font-bold text-rose-500 text-xs shrink-0 ml-1">
        H
      </div>
    </div>
  </div>
);

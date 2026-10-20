import { Flame, Library, Terminal, Trophy } from "lucide-react";
import Link from "next/link";
import { fetchTotalProblemsCount } from "../../../../actions/fetch-total-problems-count";
import { fetchUserDashboard } from "../../../../actions/fetch-user-dashboard";
import { ThemeSwitcher } from "../../../../components/ThemeSwitcher";

export default async function PlaygroundNavbar() {
  let userId: string = null;

  const { easySolvedCount, mediumSolvedCount, hardSolvedCount, streakPoints } =
    await fetchUserDashboard(userId);

  const totalProblems = fetchTotalProblemsCount();

  return (
    <header className="bg-zinc-950 border-b border-zinc-800 text-zinc-100 px-6 h-14 flex items-center justify-between select-none z-30 shrink-0">
      {/* Brand Logo & Tab Links */}
      <div className="flex items-center space-x-8">
        <Link
          href="/"
          className="flex items-center space-x-2.5 cursor-pointer group"
          id="navbar-logo"
        >
          {/* Flame Horn Logo block - Minimalist Styled */}
          <div className="bg-zinc-900 border border-zinc-850 p-1.5 rounded-lg text-zinc-400 shadow-sm group-hover:bg-zinc-800 group-hover:text-zinc-100 group-hover:border-zinc-700 transition-all duration-200">
            <Flame className="w-5 h-5 fill-transparent text-current" />
          </div>
          <span className="font-semibold tracking-tight text-base text-zinc-100 transition-colors duration-150">
            Code<span className="text-zinc-500 font-normal">Horn</span>
          </span>
        </Link>

        {/* View Toggle tabs */}
        <nav className="flex items-center space-x-1.5">
          <Link
            id="navbar-problems-tab"
            href="/problems"
            title="Problems"
            className="flex items-center space-x-2 px-3.5 py-1.5 rounded-lg font-medium text-xs transition-all duration-150 border whitespace-nowrap shrink-0 bg-zinc-900 border-zinc-800 text-zinc-100"
          >
            <Library className="w-3.5 h-3.5" />
            <span className="hidden sm:inline">Problems</span>
          </Link>

          <Link
            id="navbar-playground-tab"
            href="/playground"
            title="Developer Sandbox"
            className='flex items-center space-x-2 px-3.5 py-1.5 rounded-lg font-medium text-xs transition-all duration-150 border whitespace-nowrap shrink-0 "bg-zinc-900 border-zinc-800 text-zinc-100'
          >
            <Terminal className="w-3.5 h-3.5" />
            <span className="hidden md:inline">Developer Sandbox</span>
          </Link>
        </nav>
      </div>

      {/* Stats trackers, XP points, and editor theme adjustment */}
      <div className="flex items-center space-x-3 md:space-x-5">
        {/* Simple Progress HUD */}
        <div className="hidden lg:flex items-center space-x-3 bg-zinc-900 border border-zinc-800 px-3 py-1.5 rounded-lg text-xs font-medium shrink-0 whitespace-nowrap">
          <div className="text-zinc-500">Solved:</div>
          <div className="flex space-x-1.5 font-bold font-mono">
            <span className="text-zinc-100">E:{easySolvedCount}</span>
            <span className="text-zinc-700">|</span>
            <span className="text-zinc-100">M:{mediumSolvedCount}</span>
            <span className="text-zinc-700">|</span>
            <span className="text-zinc-100">H:{hardSolvedCount}</span>
          </div>
          <div className="h-3 w-px bg-zinc-800" />
          <div className="font-semibold text-zinc-400">
            {easySolvedCount + mediumSolvedCount + hardSolvedCount} /{" "}
            {totalProblems}
          </div>
        </div>

        {/* Dynamic XP Awards badge */}
        <div
          className="flex items-center space-x-1.5 px-3 py-1.5 bg-linear-to-r from-amber-500/12 via-yellow-500/5 to-amber-500/12 border border-amber-500/25 rounded-lg shrink-0 whitespace-nowrap cursor-help shadow-sm shadow-amber-500/5 hover:border-amber-400/40 transition-all duration-200 animate-none"
          title={`${streakPoints} Experience Points Earned / Developer Credits`}
        >
          <Trophy className="w-3.5 h-3.5 text-amber-400 fill-amber-400/15 animate-pulse" />
          <span className="font-extrabold text-amber-400 text-xs tracking-wide">
            {streakPoints} XP
          </span>
        </div>

        <div className="h-5 w-px bg-zinc-800" />

        {/* Global Theme Adjuster */}
        <ThemeSwitcher />

        {/* User profile avatar */}
        <div className="flex items-center space-x-2 border-l border-zinc-800 pl-4 h-8">
          <div className="w-8 h-8 rounded-full bg-zinc-900 border border-zinc-850 p-0.5 shadow-sm">
            <div className="w-full h-full rounded-full bg-zinc-950 flex items-center justify-center font-semibold text-xs text-zinc-300">
              CH
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}

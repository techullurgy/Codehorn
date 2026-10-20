"use client";

import { Problem } from "@/src/types/Problem";
import { Calendar, ChevronLeft, ChevronRight, Trophy } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

export const DailyChallengeCalendar = () => {
  const router = useRouter();

  const today = new Date();

  const [calendarDate, setCalendarDate] = useState(today);
  const [selectedDayObj, setSelectedDayObj] = useState(today);

  const currentYear = calendarDate.getFullYear();
  const currentMonth = calendarDate.getMonth();

  const handlePrevMonth = () => {
    setCalendarDate(new Date(currentYear, currentMonth - 1, 1));
  };

  const handleNextMonth = () => {
    setCalendarDate(new Date(currentYear, currentMonth + 1, 1));
  };

  const handleTodayMonth = () => {
    const d = new Date();
    setCalendarDate(d);
    setSelectedDayObj(d);
  };

  // Grid calculations
  const firstDayIndex = new Date(currentYear, currentMonth, 1).getDay();
  const totalDaysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();

  const prevMonthTotalDays = new Date(currentYear, currentMonth, 0).getDate();
  const prevDays = Array.from({ length: firstDayIndex }, (_, i) => {
    const d = prevMonthTotalDays - firstDayIndex + 1 + i;
    return {
      day: d,
      isCurrentMonth: false,
      date: new Date(currentYear, currentMonth - 1, d),
    };
  });

  const currentDays = Array.from({ length: totalDaysInMonth }, (_, i) => {
    const d = i + 1;
    return {
      day: d,
      isCurrentMonth: true,
      date: new Date(currentYear, currentMonth, d),
    };
  });

  const totalCells = prevDays.length + currentDays.length;
  const remainingCells = totalCells % 7 === 0 ? 0 : 7 - (totalCells % 7);
  const nextDays = Array.from({ length: remainingCells }, (_, i) => {
    const d = i + 1;
    return {
      day: d,
      isCurrentMonth: false,
      date: new Date(currentYear, currentMonth + 1, d),
    };
  });

  const allCalendarDays = [...prevDays, ...currentDays, ...nextDays];

  // Map to deterministic problem ID
  const getDailyChallengeForDate: (date: Date) => Problem = (date: Date) => {
    return null;
  };

  const handleDayClick = (dayDate: Date) => {
    const targetProblem = getDailyChallengeForDate(dayDate);
    if (targetProblem) {
      const dStr = `${dayDate.getFullYear()}-${String(dayDate.getMonth() + 1).padStart(2, "0")}-${String(dayDate.getDate()).padStart(2, "0")}`;
      // selectDailyChallenge(targetProblem, dStr);
      router.push(`/problems/${targetProblem.slug}?daily=${dStr}`);
    }
  };

  const getDayStatus: (
    day: Date,
  ) => "Completed" | "Partial" | "Attempted" | "Todo" = (day: Date) =>
    "Completed";

  return (
    <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-4 lg:p-5 mb-5 shadow-sm">
      <div className="flex items-center space-x-2.5 mb-5 border-b border-zinc-800/65 pb-3 font-sans">
        <Trophy className="w-5 h-5 text-amber-550 animate-pulse" />
        <div>
          <h2 className="text-sm lg:text-base font-extrabold tracking-tight text-zinc-100 font-sans">
            Daily Challenge Hub
          </h2>
          <p className="text-[10px] text-zinc-500 uppercase tracking-widest font-semibold mt-0.5 font-sans">
            Solve a challenge every day to build consistency & track history
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-5 gap-5 items-stretch">
        {/* Calendar Grid Section: spans 3 columns on desktops */}
        <div className="lg:col-span-3 flex flex-col justify-between">
          <div>
            {/* Calendar header controls */}
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 mb-4">
              <span className="text-xs font-bold text-zinc-350 flex items-center space-x-1.5 bg-zinc-950 px-2.5 py-1 rounded-md border border-zinc-850 self-start select-none font-sans">
                <Calendar className="w-3.5 h-3.5 text-amber-400" />
                <span>
                  Selected:{" "}
                  {selectedDayObj.toLocaleDateString("en-US", {
                    month: "short",
                    day: "numeric",
                    year: "numeric",
                  })}
                </span>
              </span>

              <div className="flex items-center bg-zinc-950 border border-zinc-850 rounded-lg p-0.5 space-x-1 select-none">
                <button
                  onClick={handlePrevMonth}
                  className="p-1 hover:bg-zinc-900 text-zinc-400 hover:text-zinc-200 rounded transition cursor-pointer"
                  title="Previous Month"
                >
                  <ChevronLeft className="w-4 h-4" />
                </button>
                <span className="text-xs font-mono font-bold text-zinc-300 px-2 min-w-28 text-center select-none">
                  {MONTH_NAMES[currentMonth]} {currentYear}
                </span>
                <button
                  onClick={handleNextMonth}
                  className="p-1 hover:bg-zinc-900 text-zinc-400 hover:text-zinc-200 rounded transition cursor-pointer"
                  title="Next Month"
                >
                  <ChevronRight className="w-4 h-4" />
                </button>
                <button
                  onClick={handleTodayMonth}
                  className="text-[10px] px-2 py-1 hover:bg-zinc-900 text-amber-500 font-bold rounded transition tracking-tight cursor-pointer font-sans"
                  title="Jump to Today"
                >
                  Today
                </button>
              </div>
            </div>

            {/* Day of week labels */}
            <div className="grid grid-cols-7 gap-1 text-center text-[10px] font-bold text-zinc-500 uppercase select-none tracking-widest border-b border-zinc-850 pb-1.5 mb-2 font-sans">
              {WEEKDAYS.map((w) => (
                <span key={w}>{w}</span>
              ))}
            </div>

            {/* Days grid */}
            <div className="grid grid-cols-7 gap-1.5">
              {allCalendarDays.map((day, idx) => {
                const isSelected =
                  selectedDayObj.getDate() === day.day &&
                  selectedDayObj.getMonth() === day.date.getMonth() &&
                  selectedDayObj.getFullYear() === day.date.getFullYear();
                const isToday =
                  today.getDate() === day.day &&
                  today.getMonth() === day.date.getMonth() &&
                  today.getFullYear() === day.date.getFullYear();
                const status = day.isCurrentMonth
                  ? getDayStatus(day.date)
                  : "Todo";

                // Base classes
                let cellClasses =
                  "aspect-square rounded-lg flex flex-col items-center justify-between p-1.5 relative text-xs font-semibold select-none transition-all duration-150 border ";

                if (!day.isCurrentMonth) {
                  cellClasses +=
                    "text-zinc-700 bg-zinc-950/20 opacity-30 cursor-not-allowed border-transparent";
                } else {
                  cellClasses += "cursor-pointer ";

                  if (isSelected) {
                    cellClasses +=
                      "ring-1.5 ring-amber-500 border-amber-500 scale-[1.03] shadow-[0_0_12px_rgba(245,158,11,0.15)] z-10 ";
                  } else if (isToday) {
                    cellClasses +=
                      "ring-1 ring-zinc-500/50 border-zinc-500/80 ";
                  }

                  switch (status) {
                    case "Completed":
                      cellClasses +=
                        "bg-emerald-500/10 border-emerald-500/30 text-emerald-400 hover:bg-emerald-500/20";
                      break;
                    case "Partial":
                      cellClasses +=
                        "bg-amber-500/10 border-amber-500/30 text-amber-400 hover:bg-amber-500/20";
                      break;
                    case "Attempted":
                      cellClasses +=
                        "bg-rose-500/10 border-rose-500/30 text-rose-400 hover:bg-rose-500/20";
                      break;
                    default:
                      cellClasses += isSelected
                        ? "bg-zinc-850 border-zinc-750 text-zinc-100"
                        : "bg-zinc-905/60 border-zinc-850/40 text-zinc-400 hover:bg-zinc-800/60 hover:text-zinc-200";
                  }
                }

                return (
                  <div
                    key={idx}
                    onClick={() =>
                      day.isCurrentMonth && setSelectedDayObj(day.date)
                    }
                    onDoubleClick={() =>
                      day.isCurrentMonth && handleDayClick(day.date)
                    }
                    className={cellClasses}
                    title={
                      day.isCurrentMonth
                        ? `Challenge for ${day.date.toLocaleDateString()}: ${getDailyChallengeForDate(day.date)?.title || ""} (${status})`
                        : ""
                    }
                  >
                    {/* Day number */}
                    <span className="self-start text-[11px] leading-none">
                      {day.day}
                    </span>

                    {/* Status Dot / Indicator */}
                    {day.isCurrentMonth && (
                      <div className="flex items-center space-x-1 mt-1 justify-center w-full">
                        {status === "Completed" && (
                          <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 shadow-[0_0_6px_rgba(16,185,129,0.5)]" />
                        )}
                        {status === "Partial" && (
                          <span className="w-1.5 h-1.5 rounded-full bg-amber-500 shadow-[0_0_6px_rgba(245,158,11,0.5)]" />
                        )}
                        {status === "Attempted" && (
                          <span className="w-1.5 h-1.5 rounded-full bg-rose-500 shadow-[0_0_6px_rgba(239,68,68,0.5)]" />
                        )}
                      </div>
                    )}
                  </div>
                );
              })}
            </div>
          </div>

          {/* Minor Legend labels */}
          <div className="flex flex-wrap items-center gap-x-4 gap-y-1.5 pt-3 mt-4 border-t border-zinc-850 text-[10px] font-bold text-zinc-500 uppercase select-none tracking-wider font-sans">
            <span className="flex items-center space-x-1.5">
              <span className="w-2.5 h-2.5 rounded bg-emerald-500/15 border border-emerald-500/30 inline-block" />
              <span>Completed (Solved)</span>
            </span>
            <span className="flex items-center space-x-1.5">
              <span className="w-2.5 h-2.5 rounded bg-amber-500/15 border border-amber-500/30 inline-block" />
              <span>Partial (WA)</span>
            </span>
            <span className="flex items-center space-x-1.5">
              <span className="w-2.5 h-2.5 rounded bg-rose-500/15 border border-rose-500/30 inline-block" />
              <span>Attempted</span>
            </span>
            <span className="flex items-center space-x-1.5">
              <span className="w-2.5 h-2.5 rounded bg-zinc-900 border border-zinc-800 inline-block" />
              <span>Todo</span>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

const MONTH_NAMES = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

const WEEKDAYS = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

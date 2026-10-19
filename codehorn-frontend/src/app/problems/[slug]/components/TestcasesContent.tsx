import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";
import { ProblemTestCase } from "@/src/types/Problem";
import { Plus, X } from "lucide-react";

export const TestcasesContent = () => {
  const testcases = useProblemPlaygroundStore((state) => state.testcases);
  const originalTestcasesCount = useProblemPlaygroundStore(
    (state) => state.originalTestcasesCount,
  );
  const focusedTestcaseIndex = useProblemPlaygroundStore(
    (state) => state.focusedTestcaseIndex,
  );
  const focusToTestcaseIndex = useProblemPlaygroundStore(
    (state) => state.focusToTestcaseIndex,
  );
  const addTestcase = useProblemPlaygroundStore((state) => state.addTestcase);
  const updateTestcaseInput = useProblemPlaygroundStore(
    (state) => state.updateTestcaseInput,
  );

  const focusedTestcase = testcases[focusedTestcaseIndex];

  const handleDeleteTestCase = (index: number) => {};

  const handleAddTestcase = () => {
    let lastTestcase = testcases[testcases.length - 1];

    let newTestcase: ProblemTestCase = {
      ...lastTestcase,
      id: `${testcases.length + 1}`,
    };

    addTestcase(newTestcase);
  };

  return (
    <div className="p-4 space-y-4 font-sans">
      <div className="flex justify-between items-center text-xs">
        <span className="text-zinc-500 font-bold uppercase text-[9px] tracking-wider">
          Testcases ({testcases.length}/8)
        </span>
      </div>
      <div className="flex flex-wrap items-center gap-1 border-b border-zinc-900/60 pb-2.5 select-none">
        {testcases.map((_, index) => (
          <div key={index + 1}>
            <button
              key={index}
              onClick={() => focusToTestcaseIndex(index)}
              className={`px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center space-x-1.5 transition-all border ${
                focusedTestcaseIndex === index
                  ? "bg-zinc-900 border-zinc-800 text-zinc-150 shadow-sm"
                  : "bg-zinc-950 border-transparent text-zinc-500 hover:text-zinc-300 hover:bg-zinc-900/40"
              }`}
            >
              <span>Case {index + 1}</span>
              {index >= originalTestcasesCount && (
                <span
                  onClick={() => handleDeleteTestCase(index)}
                  className="text-zinc-600 hover:text-rose-450 p-0.5 rounded transition-colors cursor-pointer"
                  title="Remove custom case"
                >
                  <X className="w-3 h-3" />
                </span>
              )}
            </button>
          </div>
        ))}

        {testcases.length < 8 && (
          <button
            key="add-case-btn"
            onClick={handleAddTestcase}
            className="px-2.5 py-1.5 rounded-lg text-xs font-medium bg-zinc-950 border border-dashed border-zinc-850 hover:border-zinc-800 text-zinc-550 hover:text-zinc-350 transition-all flex items-center space-x-1 cursor-pointer"
            title="Add new test case"
          >
            <Plus className="w-3 h-3" />
            <span>Add Case</span>
          </button>
        )}
      </div>

      <div className="space-y-3.5">
        {Object.entries(focusedTestcase.inputs).map(([name, value]) => (
          <div key={name} className="flex flex-col space-y-1.5">
            <div className="flex items-center justify-between">
              <label className="text-[10px] font-semibold text-zinc-500 uppercase tracking-wider font-mono flex items-center space-x-1">
                <span className="text-zinc-300">{name}</span>
              </label>
            </div>
            <input
              type="text"
              value={value}
              readOnly={focusedTestcaseIndex < originalTestcasesCount}
              disabled={focusedTestcaseIndex < originalTestcasesCount}
              onChange={(e) =>
                updateTestcaseInput(focusedTestcase.id, name, e.target.value)
              }
              placeholder={name}
              className={`w-full font-mono text-xs focus:outline-none transition-all rounded-lg px-3 py-2 border ${
                focusedTestcaseIndex < originalTestcasesCount
                  ? "bg-zinc-950/60 text-zinc-400 border-zinc-900 cursor-not-allowed"
                  : "bg-zinc-900/90 text-zinc-200 border-zinc-850 focus:border-zinc-705 focus:ring-0 placeholder-zinc-700"
              }`}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

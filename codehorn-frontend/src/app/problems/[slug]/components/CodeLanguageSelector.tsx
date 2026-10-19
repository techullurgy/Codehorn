import { useProblemPlaygroundStore } from "@/src/store/hooks/use-problem-playground-store";
import { CodeLanguage } from "@/src/types/Submission";

export const CodeLanguageSelector = () => {
  const language = useProblemPlaygroundStore(
    (state) => state.selectedCodeLanguage,
  );
  const setLanguage = useProblemPlaygroundStore(
    (state) => state.changeSelectedCodeLanguage,
  );

  return (
    <div className="flex items-center space-x-3">
      <select
        id="editor-language-select"
        value={language}
        onChange={(e) => setLanguage(e.target.value as CodeLanguage)}
        className="bg-zinc-900 border border-zinc-800 rounded-lg py-1 px-2.5 text-xs font-semibold text-zinc-300 pointer-events-auto cursor-pointer focus:outline-none focus:border-amber-500"
      >
        <option value="javascript">JavaScript</option>
        <option value="python">Python 3</option>
        <option value="cpp">C++ (Gcc)</option>
        <option value="c">C (Gcc)</option>
        <option value="java">Java 25</option>
      </select>
    </div>
  );
};

import { AlertTriangle, Check, CheckCircle, Copy, Layers, Play, Plus, RotateCcw, Terminal, Trophy, X } from "lucide-react";
import { PARAM_LABELS } from "./ProblemDetail";
import { Editor } from "@monaco-editor/react";
import ResizablePanel from "./ResizablePanel";


export const RightPanel = ({ 
    isMobile, 
    currentLanguage, 
    setLanguage, 
    getLanguageLabel, 
    editorTheme, 
    setEditorTheme, 
    handleCopyCode, 
    copiedDraft, 
    handleResetDraft, 
    consoleExpanded, 
    currentCode, 
    updateDraft, 
    selectedProblem, 
    setConsoleExpanded, 
    runUserCode, 
    activeResultTab, 
    customTestcase, 
    setResultTab, 
    activeCaseIdx, 
    setCustomTestcase, 
    setActiveCaseIdx, 
    isRunning, 
    isSubmitting,
    latestResult, 
    handleLocalSubmitSolution, 
    selectedSubmissionCode, 
    setSelectedSubmissionCode 
  }) => {
  
    const renderRunnerLogs = () => {
      if (isRunning) {
        return (
          <div className="p-6 flex flex-col items-center justify-center space-y-3">
            <div className="w-8 h-8 rounded-full border-4 border-amber-500 border-t-transparent animate-spin" />
            <p className="text-zinc-400 font-semibold text-xs font-mono animate-pulse">
              Executing tests on CodeHorn servers...
            </p>
          </div>
        );
      }
  
      if (isSubmitting) {
        return (
          <div className="p-6 flex flex-col items-center justify-center space-y-3">
            <div className="w-8 h-8 rounded-full border-4 border-emerald-500 border-t-transparent animate-spin" />
            <p className="text-zinc-400 font-semibold text-xs font-mono animate-pulse">
              Grading tests against full submission suites...
            </p>
          </div>
        );
      }
  
      if (!latestResult) {
        return (
          <div className="p-6 text-center text-zinc-500 text-xs font-medium flex flex-col items-center justify-center space-y-1.5 h-full">
            <Terminal className="w-7 h-7 text-zinc-700" />
            <p>Execution console is empty.</p>
            <p className="text-zinc-600">Hit "Run" to test sample inputs; "Submit" to grade all secret parameters.</p>
          </div>
        );
      }
  
      const { status, runtime, memory, errorMessage, passedCount, totalCount, failedTestCase } = latestResult;
  
      // Return view base on result status
      const isAccepted = status === 'Accepted';
  
      return (
        <div className="p-5 space-y-4 text-xs font-mono select-text" id="execution-logs-container">
  
          {/* Banner with status and evaluation title */}
          <div className={`p-4 rounded-xl border flex items-center justify-between ${isAccepted
              ? 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400'
              : 'bg-rose-500/10 border-rose-500/20 text-rose-400'
            }`}>
            <div className="space-y-1">
              <div className="flex items-center space-x-2">
                <CheckCircle className={`w-5 h-5 ${isAccepted ? 'text-emerald-500' : 'text-rose-500'}`} />
                <span className="font-extrabold text-sm uppercase tracking-wider">{status}</span>
              </div>
              <div className="text-zinc-400 text-xs">
                Passed: {passedCount} / {totalCount} testcases evaluated
              </div>
            </div>
  
            <div className="flex items-center space-x-4 border-l border-zinc-800 pl-4 text-right">
              <div>
                <div className="text-zinc-500 uppercase font-black text-[9px] tracking-wider">Runtime</div>
                <div className="text-zinc-200 text-xs font-bold">{runtime} ms</div>
              </div>
              <div>
                <div className="text-zinc-500 uppercase font-black text-[9px] tracking-wider">Memory</div>
                <div className="text-zinc-200 text-xs font-bold">{memory} MB</div>
              </div>
            </div>
          </div>
  
          {/* Compile Error display banner */}
          {errorMessage && (
            <div className="p-4 bg-zinc-900 border border-zinc-800 rounded-lg">
              <div className="text-xs font-bold text-red-400 flex items-center space-x-1.5 mb-115">
                <AlertTriangle className="w-4 h-4 text-red-500" />
                <span>Compilation / Runtime Error output</span>
              </div>
              <pre className="text-zinc-400 whitespace-pre-wrap overflow-x-auto select-text font-mono leading-relaxed mt-2 text-[11px] bg-zinc-950 p-2 rounded">
                {errorMessage}
              </pre>
            </div>
          )}
  
          {/* Failed TestCase details overlay */}
          {failedTestCase && (() => {
            const paramNames = PARAM_LABELS[selectedProblem.slug] || ['input'];
            const parsedInputs = failedTestCase.input.split('\n');
            return (
              <div className="space-y-4 bg-rose-950/5 border border-rose-950/20 rounded-xl p-4.5 mt-2">
                <div className="text-xs font-black text-rose-405 uppercase tracking-widest flex items-center space-x-1.5 border-b border-rose-900/15 pb-2">
                  <AlertTriangle className="w-4 h-4 text-rose-450" />
                  <span>Failed Test Case Details</span>
                </div>
  
                <div className="space-y-3.5">
                  {/* Inputs breakdown block */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                    {paramNames.map((name, paramIdx) => {
                      const val = parsedInputs[paramIdx] !== undefined ? parsedInputs[paramIdx] : '';
                      return (
                        <div key={name} className="bg-zinc-900 border border-zinc-800/80 rounded-lg p-3 space-y-1.5 min-w-0">
                          <div className="text-zinc-500 font-bold uppercase text-[10px] font-mono">
                            Input#{paramIdx + 1} ({name}):
                          </div>
                          <pre className="text-zinc-300 font-bold select-all font-mono text-[11px] overflow-x-auto whitespace-pre-wrap" title={val}>
                            {val}
                          </pre>
                        </div>
                      );
                    })}
                  </div>
  
                  {/* Expected & Actual Output block */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                    <div className="bg-zinc-900 border border-zinc-800/80 rounded-lg p-3 space-y-1.5 min-w-0">
                      <div className="text-emerald-500 font-bold uppercase text-[10px] font-mono">Expected Output:</div>
                      <pre className="text-emerald-400 font-extrabold select-all font-mono text-[11px] overflow-x-auto whitespace-pre-wrap">
                        {failedTestCase.expected}
                      </pre>
                    </div>
  
                    <div className="bg-zinc-900 border border-zinc-800/80 rounded-lg p-3 space-y-1.5 min-w-0">
                      <div className="text-rose-500 font-bold uppercase text-[10px] font-mono">Actual Output:</div>
                      <pre className="text-rose-400 font-extrabold select-all font-mono text-[11px] overflow-x-auto whitespace-pre-wrap">
                        {failedTestCase.actual}
                      </pre>
                    </div>
                  </div>
  
                  {/* Stdout block */}
                  <div className="bg-zinc-900 border border-zinc-800/80 rounded-lg p-3 space-y-1.5">
                    <div className="text-zinc-500 font-bold uppercase text-[10px] font-mono">Stdout (Debugger logs):</div>
                    {failedTestCase.stdout ? (
                      <pre className="text-zinc-400 whitespace-pre-wrap overflow-x-auto select-text font-mono leading-relaxed text-[11px] bg-zinc-950 p-2 rounded max-h-32 border border-zinc-900/60 shadow-inner">
                        {failedTestCase.stdout}
                      </pre>
                    ) : (
                      <div className="text-zinc-600 text-[10px] font-mono italic">No standard output logged.</div>
                    )}
                  </div>
  
                </div>
              </div>
            );
          })()}
  
          {/* Success celebration remarks */}
          {isAccepted && (
            <div className="bg-gradient-to-r from-emerald-500/5 to-cyan-500/5 border border-emerald-500/10 p-4 rounded-xl text-xs flex items-center space-x-3 text-zinc-400">
              <Trophy className="w-6 h-6 text-amber-500 animate-pulse shrink-0" />
              <div>
                <span className="font-bold text-zinc-200">First-time accepts award XP.</span> Double check the
                Editorial module to review memory improvements or trade space constraints for speed!
              </div>
            </div>
          )}
  
        </div>
      );
    };
  
    return (
      <div className="flex flex-col h-full bg-zinc-950 text-zinc-100 min-w-0">
  
        {/* Settings bar row inside compilation wrapper */}
        <div className="px-4 h-12 flex items-center justify-between border-b border-zinc-800 bg-zinc-900/10 shrink-0 select-none">
  
          {/* Language selector selection field */}
          <div className="flex items-center space-x-3">
            {!isMobile ? (
              <select
                id="editor-language-select"
                value={currentLanguage}
                onChange={(e) => setLanguage(e.target.value as any)}
                className="bg-zinc-900 border border-zinc-800 rounded-lg py-1 px-2.5 text-xs font-semibold text-zinc-300 pointer-events-auto cursor-pointer focus:outline-none focus:border-amber-500"
              >
                <option value="javascript">JavaScript (ES6)</option>
                <option value="python">Python 3</option>
                <option value="cpp">C++ (Clang)</option>
                <option value="java">Java (JDK)</option>
              </select>
            ) : (
              <span className="text-[11px] font-mono font-bold text-zinc-400 capitalize bg-zinc-900/50 px-2.5 py-1 rounded border border-zinc-800">
                {getLanguageLabel(currentLanguage)}
              </span>
            )}
          </div>
  
          {/* Editing and settings widgets */}
          <div className="flex items-center space-x-2">
  
            {/* Editor Theme Dropdown */}
            <select
              value={editorTheme}
              onChange={(e) => setEditorTheme(e.target.value as any)}
              className="bg-zinc-900 border border-zinc-800 rounded-lg py-1 px-2 text-xs font-semibold text-zinc-400 focus:outline-none cursor-pointer hover:border-zinc-700 transition"
              title="Monaco Editor Theme"
            >
              <option value="vs-dark">Editor Theme: Dark</option>
              <option value="hc-black">Editor Theme: Contrast</option>
              <option value="light">Editor Theme: Light</option>
            </select>
  
            <div className="h-4 w-px bg-zinc-805" />
  
            {/* Copy draft btn */}
            <button
              onClick={handleCopyCode}
              className="h-8 w-8 flex items-center justify-center rounded-lg border border-zinc-800 bg-zinc-900 text-zinc-400 hover:text-zinc-200 hover:bg-zinc-850 transition-all shadow-sm"
              title="Copy Code"
            >
              {copiedDraft ? <Check className="w-4 h-4 text-emerald-400" /> : <Copy className="w-4 h-4" />}
            </button>
  
            {/* Reset draft btn */}
            <button
              onClick={handleResetDraft}
              className="h-8 w-8 flex items-center justify-center rounded-lg border border-zinc-800 bg-zinc-900 text-zinc-400 hover:text-zinc-200 hover:bg-zinc-850 transition-all shadow-sm"
              title="Reset Code Draft"
            >
              <RotateCcw className="w-4 h-4" />
            </button>
  
          </div>
        </div>
  
        {/* Editor Space and Bottom Console (Split vert) */}
        <div className="flex-1 min-h-0 relative">
          {!consoleExpanded ? (
            <div className="absolute inset-0 flex flex-col bg-zinc-950">
              <Editor
                height="100%"
                language={currentLanguage === 'cpp' ? 'cpp' : currentLanguage === 'java' ? 'java' : currentLanguage}
                theme={editorTheme}
                value={currentCode}
                onChange={(value) => updateDraft(selectedProblem.id, currentLanguage, value || '')}
                loading={
                  <div className="absolute inset-0 bg-zinc-950 flex flex-col items-center justify-center space-y-2">
                    <div className="w-8 h-8 rounded-full border-4 border-amber-500 border-t-transparent animate-spin" />
                    <span className="text-xs text-zinc-500 font-mono">Initializing Monaco...</span>
                  </div>
                }
                options={{
                  fontSize: isMobile ? 12 : 14,
                  fontFamily: 'Fira Code, JetBrains Mono, monospace',
                  minimap: { enabled: false },
                  scrollBeyondLastLine: false,
                  automaticLayout: true,
                  lineNumbers: 'on',
                  tabSize: 4,
                  cursorBlinking: 'smooth',
                  padding: { top: 12, bottom: 12 },
                  renderLineHighlight: 'all',
                  scrollbar: {
                    vertical: 'visible',
                    horizontal: 'visible',
                    useShadows: false,
                    verticalScrollbarSize: 10,
                    horizontalScrollbarSize: 10,
                  }
                }}
              />
              {/* Minimal button bar when console is fully collapsed */}
              <div className="absolute bottom-4 right-4 flex items-center space-x-2 z-10 px-2.5 py-1.5 bg-zinc-900/90 backdrop-blur border border-zinc-800 rounded-xl shadow-lg select-none">
                <button
                  onClick={() => {
                    setConsoleExpanded(true);
                  }}
                  className="px-3 py-1.5 bg-zinc-800 text-zinc-300 hover:text-white rounded-lg text-xs font-bold transition flex items-center space-x-1 hover:border-zinc-700 border border-transparent"
                  title="Restore code runners"
                >
                  <Terminal className="w-3.5 h-3.5 text-amber-500" />
                  <span>Show Console</span>
                </button>
  
                <button
                  onClick={() => {
                    setConsoleExpanded(true);
                    runUserCode();
                  }}
                  disabled={isRunning || isSubmitting}
                  className="px-3.5 py-1.5 bg-indigo-600 hover:bg-indigo-555 text-white rounded-lg text-xs font-semibold transition flex items-center space-x-1 shadow"
                >
                  <Play className="w-3 h-3 fill-white" />
                  <span>Run</span>
                </button>
              </div>
            </div>
          ) : (
            <ResizablePanel
              direction="vertical"
              initialSplit={isMobile ? 55 : 65}
              minSize={30}
              maxSize={85}
              idPrefix="editor-console-split"
              leftElement={
                <div className="flex-1 min-h-0 relative">
                  <Editor
                    height="100%"
                    language={currentLanguage === 'cpp' ? 'cpp' : currentLanguage === 'java' ? 'java' : currentLanguage}
                    theme={editorTheme}
                    value={currentCode}
                    onChange={(value) => updateDraft(selectedProblem.id, currentLanguage, value || '')}
                    loading={
                      <div className="absolute inset-0 bg-zinc-950 flex flex-col items-center justify-center space-y-2">
                        <div className="w-8 h-8 rounded-full border-4 border-amber-500 border-t-transparent animate-spin" />
                        <span className="text-xs text-zinc-500 font-mono">Initializing Monaco dependencies...</span>
                      </div>
                    }
                    options={{
                      fontSize: 14,
                      fontFamily: 'Fira Code, JetBrains Mono, monospace',
                      minimap: { enabled: false },
                      scrollBeyondLastLine: false,
                      automaticLayout: true,
                      lineNumbers: 'on',
                      tabSize: 4,
                      cursorBlinking: 'smooth',
                      padding: { top: 12, bottom: 12 },
                      renderLineHighlight: 'all',
                      scrollbar: {
                        vertical: 'visible',
                        horizontal: 'visible',
                        useShadows: false,
                        verticalScrollbarSize: 10,
                        horizontalScrollbarSize: 10,
                      }
                    }}
                  />
                </div>
              }
              rightElement={<BottomElement />}
            />
          )}
        </div>
  
        {/* Code submitted overlays modal */}
        {selectedSubmissionCode && (
          <div
            className="fixed inset-0 bg-zinc-950/80 backdrop-blur-sm flex items-center justify-center p-6 z-50 select-text"
            id="code-submission-overlay"
          >
            <div className="bg-zinc-900 border border-zinc-800 rounded-xl w-full max-w-2xl max-h-[85vh] flex flex-col shadow-2xl overflow-hidden">
  
              {/* Header */}
              <div className="px-5 py-4 border-b border-zinc-800 bg-zinc-950/40 flex items-center justify-between">
                <div>
                  <h3 className="font-extrabold text-sm text-zinc-100">Historical solution run code</h3>
                  <p className="text-[10px] text-zinc-500 font-mono mt-0.5">Submitted status: {selectedSubmissionCode.status} • {selectedSubmissionCode.runtime}ms</p>
                </div>
                <button
                  onClick={() => setSelectedSubmissionCode(null)}
                  className="text-zinc-500 hover:text-zinc-300 text-xs font-black bg-zinc-950 px-2.5 py-1.5 rounded-lg border border-zinc-850"
                >
                  Close View
                </button>
              </div>
  
              {/* Code Body */}
              <div className="flex-1 overflow-auto bg-zinc-950/80 p-5">
                <pre className="text-xs font-mono text-amber-500/90 whitespace-pre scroll-all leading-relaxed bg-zinc-950 p-4 border border-zinc-850 rounded-xl select-all">
                  {selectedSubmissionCode.code}
                </pre>
              </div>
  
              {/* Copy button */}
              <div className="px-5 py-3 border-t border-zinc-800 bg-zinc-900/60 flex justify-end">
                <button
                  onClick={() => {
                    navigator.clipboard.writeText(selectedSubmissionCode.code);
                    alert('Submission code copied to clipboard successfully!');
                  }}
                  className="px-3.5 py-1.5 bg-zinc-850 hover:bg-zinc-800 border border-zinc-800 text-zinc-300 rounded-lg text-xs font-bold transition-all"
                >
                  Copy solutions code
                </button>
              </div>
  
            </div>
          </div>
        )}
  
      </div>
    );  
  }
  
  const BottomElement = () => {
    return <div className="flex-1 flex flex-col bg-zinc-950 min-h-0">

      {/* Console Tabs segment Header */}
      <div className="h-10 bg-zinc-900 border-y border-zinc-850 px-4 flex items-center justify-between shrink-0 select-none">
        <div className="flex items-center space-x-1">

          <button
            onClick={() => setResultTab('testcase')}
            title="Test Cases"
            className={`h-8 w-8 sm:h-auto sm:w-auto px-0 sm:px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center justify-center sm:space-x-1.5 shrink-0 transition-all ${activeResultTab === 'testcase'
                ? 'bg-zinc-800 text-amber-500'
                : 'text-zinc-400 hover:text-zinc-200'
              }`}
          >
            <Terminal className="w-4 h-4 sm:w-3.5 sm:h-3.5" />
            <span className="hidden sm:inline">Test Cases</span>
          </button>

          <button
            onClick={() => setResultTab('result')}
            title="Run Results"
            className={`h-8 w-8 sm:h-auto sm:w-auto px-0 sm:px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center justify-center sm:space-x-1.5 shrink-0 transition-all ${activeResultTab === 'result'
                ? 'bg-zinc-800 text-amber-500'
                : 'text-zinc-400 hover:text-zinc-200'
              }`}
          >
            <Layers className="w-4 h-4 sm:w-3.5 sm:h-3.5" />
            <span className="hidden sm:inline">Run Results</span>
          </button>

        </div>

        <div className="text-[10px] text-zinc-500 font-mono font-bold uppercase tracking-widest flex items-center space-x-1.5">
          <div className="w-1.5 h-1.5 rounded-full bg-emerald-500" />
          <span>Sandbox Runner Status</span>
        </div>
      </div>

      {/* Console content body */}
      <div className="flex-1 overflow-y-auto custom-minimal-scrollbar min-h-0 bg-zinc-950">
        {activeResultTab === 'testcase' ? (() => {
          const paramNames = PARAM_LABELS[selectedProblem.slug] || ['input'];
          const sampleCasesCount = selectedProblem.testcases.filter(t => t.isSample).length;
          const parsedCases = customTestcase ? customTestcase.split('\n---\n').map(c => {
            const lines = c.split('\n');
            return paramNames.map((_, idx) => lines[idx] || '');
          }) : [paramNames.map(() => '')];

          // Guard active index bounds
          const currentActiveIdx = activeCaseIdx >= parsedCases.length ? Math.max(0, parsedCases.length - 1) : activeCaseIdx;

          const handleUpdateCaseValue = (caseIdx: number, paramIdx: number, value: string) => {
            const updated = parsedCases.map((c, cIdx) => {
              if (cIdx === caseIdx) {
                return c.map((v, pIdx) => pIdx === paramIdx ? value : v);
              }
              return c;
            });
            const joined = updated.map(c => c.join('\n')).join('\n---\n');
            setCustomTestcase(joined);
          };

          const handleAddTestCase = () => {
            if (parsedCases.length >= 8) return;
            let defaultValues = paramNames.map(() => '');
            if (selectedProblem.slug === 'two-sum') {
              defaultValues = ['[1,2,3]', '5'];
            } else if (selectedProblem.slug === 'valid-parentheses') {
              defaultValues = ['"()"'];
            } else if (selectedProblem.slug === 'longest-substring-without-repeating-characters') {
              defaultValues = ['"(abc)"'];
            } else if (selectedProblem.slug === 'container-with-most-water') {
              defaultValues = ['[1,2,1]'];
            } else if (selectedProblem.slug === 'median-of-two-sorted-arrays') {
              defaultValues = ['[1,2]', '[3,4]'];
            }
            const updated = [...parsedCases, defaultValues];
            const joined = updated.map(c => c.join('\n')).join('\n---\n');
            setCustomTestcase(joined);
            setActiveCaseIdx(updated.length - 1);
          };

          const handleDeleteTestCase = (idx: number, e: React.MouseEvent) => {
            e.stopPropagation();
            if (parsedCases.length <= 1) return;
            const updated = parsedCases.filter((_, cIdx) => cIdx !== idx);
            const joined = updated.map(c => c.join('\n')).join('\n---\n');
            setCustomTestcase(joined);
            if (currentActiveIdx >= updated.length) {
              setActiveCaseIdx(updated.length - 1);
            }
          };

          return (
            <div className="p-4 space-y-4 font-sans">
              {/* Header metadata layout */}
              <div className="flex justify-between items-center text-xs">
                <span className="text-zinc-500 font-bold uppercase text-[9px] tracking-wider">Testcases ({parsedCases.length}/8)</span>
                <span className="text-zinc-650 text-[10px] italic">Edit specific variables dynamically</span>
              </div>

              {/* Case selector tabs */}
              <div className="flex flex-wrap items-center gap-1 border-b border-zinc-900/60 pb-2.5 select-none">
                {parsedCases.map((_, idx) => {
                  const isSample = idx < sampleCasesCount;
                  return (
                    <button
                      key={idx}
                      type="button"
                      onClick={() => setActiveCaseIdx(idx)}
                      className={`px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center space-x-1.5 transition-all border ${currentActiveIdx === idx
                          ? 'bg-zinc-900 border-zinc-800 text-zinc-150 shadow-sm'
                          : 'bg-zinc-950 border-transparent text-zinc-500 hover:text-zinc-300 hover:bg-zinc-900/40'
                        }`}
                    >
                      <span>Case {idx + 1}</span>
                      {!isSample && parsedCases.length > 1 && (
                        <span
                          onClick={(e) => handleDeleteTestCase(idx, e)}
                          className="text-zinc-600 hover:text-rose-450 p-0.5 rounded transition-colors cursor-pointer"
                          title="Remove custom case"
                        >
                          <X className="w-3 h-3" />
                        </span>
                      )}
                    </button>
                  );
                })}

                {parsedCases.length < 8 && (
                  <button
                    key="add-case-btn"
                    type="button"
                    onClick={handleAddTestCase}
                    className="px-2.5 py-1.5 rounded-lg text-xs font-medium bg-zinc-950 border border-dashed border-zinc-850 hover:border-zinc-800 text-zinc-550 hover:text-zinc-350 transition-all flex items-center space-x-1 cursor-pointer"
                    title="Add new test case"
                  >
                    <Plus className="w-3 h-3" />
                    <span>Add Case</span>
                  </button>
                )}
              </div>

              {/* Param Fields for Active Case */}
              <div className="space-y-3.5">
                {paramNames.map((name, paramIdx) => {
                  const val = parsedCases[currentActiveIdx]?.[paramIdx] || '';
                  const isCurrentSample = currentActiveIdx < sampleCasesCount;
                  return (
                    <div key={name} className="flex flex-col space-y-1.5">
                      <div className="flex items-center justify-between">
                        <label className="text-[10px] font-semibold text-zinc-500 uppercase tracking-wider font-mono flex items-center space-x-1">
                          <span className="text-zinc-600">Input#{paramIdx + 1}:</span>
                          <span className="text-zinc-300">{name}</span>
                        </label>
                        {isCurrentSample && (
                          <span className="text-[9px] font-mono text-zinc-500 bg-zinc-900 px-1.5 py-0.5 rounded border border-zinc-800/80">
                            Predefined Sample
                          </span>
                        )}
                      </div>
                      <input
                        type="text"
                        value={val}
                        readOnly={isCurrentSample}
                        disabled={isCurrentSample}
                        onChange={(e) => handleUpdateCaseValue(currentActiveIdx, paramIdx, e.target.value)}
                        placeholder={`e.g. valid structure mapping for ${name}`}
                        className={`w-full font-mono text-xs focus:outline-none transition-all rounded-lg px-3 py-2 border ${isCurrentSample
                            ? 'bg-zinc-950/60 text-zinc-400 border-zinc-900 cursor-not-allowed'
                            : 'bg-zinc-900/90 text-zinc-200 border-zinc-850 focus:border-zinc-705 focus:ring-0 placeholder-zinc-700'
                          }`}
                      />
                    </div>
                  );
                })}
              </div>

              <div className="text-[10px] text-zinc-550 font-sans border-t border-zinc-900/50 pt-2 flex items-center space-x-1">
                <span className="text-amber-500/80">💡</span>
                <span>Format follows JSON rules. Lists like `[2,7,11]` should have brackets; strings like `"()"` require quotes.</span>
              </div>
            </div>
          );
        })() : (
          renderRunnerLogs()
        )}
      </div>

      {/* Bottom compiler actions bar */}
      <div className="h-12 bg-zinc-950 px-4 border-t border-zinc-850 flex items-center justify-between shrink-0 select-none">
        <span className="text-[9px] font-mono text-zinc-650 font-bold uppercase tracking-wider">
          CodeHorn System Sandbox v1.0
        </span>

        <div className="flex items-center space-x-2">
          {/* Console expand compiler switch */}
          <button
            onClick={() => {
              setConsoleExpanded(!consoleExpanded);
            }}
            title={consoleExpanded ? "Hide Console Pane" : "Show Console Pane"}
            className={`h-10 w-10 sm:h-auto sm:w-auto px-0 sm:px-3 py-1.5 text-xs rounded-lg transition-all flex items-center justify-center sm:space-x-1.5 shrink-0 border ${consoleExpanded
                ? 'bg-zinc-800 text-amber-500 border-zinc-700 hover:bg-zinc-750'
                : 'bg-zinc-900 text-zinc-400 hover:text-zinc-100 hover:bg-zinc-850 border-zinc-805'
              }`}
            id="console-tab-switcher"
          >
            <Terminal className="w-4.5 h-4.5 sm:w-3.5 sm:h-3.5" />
            <span className="hidden sm:inline">{consoleExpanded ? "Hide Console" : "Console view"}</span>
          </button>

          {/* Run Code Compiler */}
          <button
            id="codehorn-run-btn"
            onClick={() => {
              setConsoleExpanded(true);
              runUserCode();
            }}
            disabled={isRunning || isSubmitting}
            title="Run tests"
            className="h-10 w-10 sm:h-auto sm:w-auto px-0 sm:px-4 py-1.5 text-xs bg-indigo-600 hover:bg-indigo-550 text-white border border-transparent rounded-lg font-medium transition-all flex items-center justify-center sm:space-x-1.5 shadow-sm hover:shadow-indigo-600/10 disabled:opacity-40 disabled:cursor-not-allowed shrink-0"
          >
            {isRunning ? (
              <div className="w-4 h-4 sm:w-3.5 sm:h-3.5 rounded-full border border-white border-t-transparent animate-spin" />
            ) : (
              <Play className="w-4 h-4 sm:w-3 sm:h-3 text-white fill-white" />
            )}
            <span className="hidden sm:inline">Run tests</span>
          </button>

          {/* Submit solution and awards */}
          <button
            id="codehorn-submit-btn"
            onClick={handleLocalSubmitSolution}
            disabled={isRunning || isSubmitting}
            title="Submit Solution"
            className="h-10 w-10 sm:h-auto sm:w-auto px-0 sm:px-4 py-1.5 text-xs bg-emerald-600 hover:bg-emerald-555 text-white border border-transparent rounded-lg font-semibold transition-all flex items-center justify-center sm:space-x-1.5 shadow-sm hover:shadow-emerald-600/10 disabled:opacity-40 disabled:cursor-not-allowed shrink-0"
          >
            {isSubmitting ? (
              <div className="w-4 h-4 sm:w-3.5 sm:h-3.5 rounded-full border border-white border-t-transparent animate-spin" />
            ) : (
              <CheckCircle className="w-4.5 h-4.5 sm:w-3.5 sm:h-3.5 text-white" />
            )}
            <span className="hidden sm:inline">Submit Solution</span>
          </button>

        </div>
      </div>

    </div>
  }
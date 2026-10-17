'use client';

import React, { useState } from 'react';
import { useCodeHornStore } from '../store/useCodeHornStore';
import { CodeHornApiService } from '../services/api';
import ResizablePanel from './ResizablePanel';
import { Submission} from '../types';
import { LeftPanel } from './LeftPanel';
import { RightPanel } from './RightPanel';
import { MobileLayout } from './MobileLayout';

export const PARAM_LABELS: Record<string, string[]> = {
  'two-sum': ['nums', 'target'],
  'valid-parentheses': ['s'],
  'longest-substring-without-repeating-characters': ['s'],
  'container-with-most-water': ['height'],
  'median-of-two-sorted-arrays': ['nums1', 'nums2']
};

export default function ProblemDetail() {
  const {
    selectedProblem,
    currentLanguage,
    setLanguage,
    codeDrafts,
    updateDraft,
    customTestcase,
    setCustomTestcase,
    editorTheme,
    setEditorTheme,
    submissions,
    isRunning,
    isSubmitting,
    latestResult,
    activeResultTab,
    activeLeftTab,
    setResultTab,
    runUserCode,
  } = useCodeHornStore();

  const [consoleExpanded, setConsoleExpanded] = useState(true);
  const [selectedSubmissionCode, setSelectedSubmissionCode] = useState<Submission | null>(null);
  const [copiedDraft, setCopiedDraft] = useState(false);
  const [activeCaseIdx, setActiveCaseIdx] = useState(0);

  const [localLeftTab, setLocalLeftTab] = useState<string>('description');
  const [openSubTabs, setOpenSubTabs] = useState<Submission[]>([]);

  const [isMobile, setIsMobile] = useState(false);
  const [mobileTab, setMobileTab] = useState<'problem' | 'code'>('problem');

  React.useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 768);
    };
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  // Safety trigger: Reset active selected case index when switching problem
  React.useEffect(() => {
    setActiveCaseIdx(0);
    setOpenSubTabs([]);
    setLocalLeftTab('description');
  }, [selectedProblem?.id]);

  // Keep local left tab in sync with the global store
  React.useEffect(() => {
    if (activeLeftTab === 'description' || activeLeftTab === 'editorial' || activeLeftTab === 'submissions') {
      setLocalLeftTab(activeLeftTab);
    }
  }, [activeLeftTab]);

  if (!selectedProblem) return null;

  const currentCode = codeDrafts[selectedProblem.id]?.[currentLanguage] || '';

  const handleResetDraft = () => {
    if (window.confirm('Reset draft: This will overwrite your current solution with the starter code. Are you sure?')) {
      const initialCode = selectedProblem.starterCode[currentLanguage] || '';
      updateDraft(selectedProblem.id, currentLanguage, initialCode);
    }
  };

  const handleCopyCode = () => {
    navigator.clipboard.writeText(currentCode);
    setCopiedDraft(true);
    setTimeout(() => setCopiedDraft(false), 2000);
  };

  const handleLocalSubmitSolution = async () => {
    if (isRunning || isSubmitting) return;

    // Set store grading/submitting state to disable controls during run
    useCodeHornStore.setState({ isSubmitting: true });

    // Generate unique temp ID for visual representation
    const tempId = `sub_${Math.random().toString(36).substr(2, 5)}`;

    // Initial temporary submission representation
    const tempSub: Submission = {
      id: tempId,
      problemId: selectedProblem.id,
      problemTitle: selectedProblem.title,
      problemSlug: selectedProblem.slug,
      language: currentLanguage,
      code: currentCode,
      status: 'Accepted',
      timestamp: new Date().toISOString(),
      runtime: 0,
      memory: 0,
    };

    // Open tab & transition active view
    setOpenSubTabs((prev) => [...prev, tempSub]);
    setLocalLeftTab(`submission-${tempId}`);
    if (isMobile) {
      setMobileTab('problem');
    }

    try {
      const realSubmission = await CodeHornApiService.submitCode(
        selectedProblem.slug,
        currentLanguage,
        currentCode
      );

      // Save submission to our global state store list
      const updatedSubmissions = [realSubmission, ...submissions];
      useCodeHornStore.setState({
        submissions: updatedSubmissions,
        isSubmitting: false,
        latestResult: {
          status: realSubmission.status,
          runtime: realSubmission.runtime,
          memory: realSubmission.memory,
          errorMessage: realSubmission.compileError,
          passedCount: realSubmission.status === 'Accepted' ? selectedProblem.testcases.length : Math.max(0, selectedProblem.testcases.length - 1),
          totalCount: selectedProblem.testcases.length,
          failedTestCase: realSubmission.failedTestCase,
        }
      });
      localStorage.setItem('codehorn_subs_v1', JSON.stringify(updatedSubmissions));

      // Gamification updates
      if (realSubmission.status === 'Accepted') {
        const alreadySolved = submissions.some(
          s => s.problemId === selectedProblem.id && s.status === 'Accepted'
        );

        if (!alreadySolved) {
          const pointsAwarded = selectedProblem.difficulty === 'Easy' ? 10 : selectedProblem.difficulty === 'Medium' ? 20 : 30;
          const newXP = useCodeHornStore.getState().xpPoints + pointsAwarded;
          localStorage.setItem('codehorn_xp', String(newXP));

          const newStreak = useCodeHornStore.getState().streak + 1;
          localStorage.setItem('codehorn_streak', String(newStreak));

          useCodeHornStore.setState({ xpPoints: newXP, streak: newStreak });
        }
      }

      // Conclude the simulation at 3000ms
      setTimeout(() => {
        setOpenSubTabs((prev) =>
          prev.map((tab) => (tab.id === tempId ? { ...realSubmission, id: tempId } : tab))
        );
      }, 3000);

    } catch (err) {
      console.error(err);
      useCodeHornStore.setState({ isSubmitting: false });
    }
  };

  // Get difficulty badge color rules
  const getDiffColor = (diff: string) => {
    switch (diff) {
      case 'Easy': return 'text-emerald-400 bg-emerald-500/10 border-emerald-500/20';
      case 'Medium': return 'text-amber-400 bg-amber-500/10 border-amber-500/20';
      case 'Hard': return 'text-rose-400 bg-rose-500/10 border-rose-500/20';
      default: return 'text-zinc-400 bg-zinc-800';
    }
  };

  // Language display labels
  const getLanguageLabel = (lang: string) => {
    switch (lang) {
      case 'javascript': return 'JavaScript (ES6)';
      case 'python': return 'Python 3.10';
      case 'cpp': return 'C++ (Clang 15)';
      case 'java': return 'Java (JDK 17)';
      default: return lang;
    }
  };

  // Top Section of Left Layout Frame (Problems backlink & general navigation tabs)
  const leftPanel = <LeftPanel 
    isMobile={isMobile}
    selectedProblem={selectedProblem}
    setLocalLeftTab={setLocalLeftTab}
    localLeftTab={localLeftTab}
    openSubTabs={openSubTabs}
    setOpenSubTabs={setOpenSubTabs}
    submissions={submissions}
  />

  // Right Code Editor Panel Layout including Monaco component & collapsible runs
  const rightPanel = <RightPanel 
    isMobile={isMobile}
    currentLanguage={currentLanguage} 
    setLanguage={setLanguage} 
    getLanguageLabel={getLanguageLabel} 
    editorTheme={editorTheme} 
    setEditorTheme={setEditorTheme} 
    handleCopyCode={handleCopyCode} 
    copiedDraft={copiedDraft} 
    handleResetDraft={handleResetDraft} 
    consoleExpanded={consoleExpanded} 
    currentCode={currentCode} 
    updateDraft={updateDraft} 
    selectedProblem={selectedProblem} 
    setConsoleExpanded={setConsoleExpanded} 
    runUserCode={runUserCode} 
    activeResultTab={activeResultTab} 
    customTestcase={customTestcase} 
    setResultTab={setResultTab} 
    activeCaseIdx={activeCaseIdx} 
    setCustomTestcase={setCustomTestcase} 
    setActiveCaseIdx={setActiveCaseIdx} 
    isRunning={isRunning} 
    isSubmitting={isSubmitting}
    latestResult={latestResult} 
    handleLocalSubmitSolution={handleLocalSubmitSolution} 
    selectedSubmissionCode={selectedSubmissionCode} 
    setSelectedSubmissionCode={setSelectedSubmissionCode} 
  />

  if (isMobile) {
    return (
      <MobileLayout 
        language={currentLanguage}
        setLanguage={setLanguage}
        diffColor={getDiffColor(selectedProblem.difficulty)}
        difficulty={selectedProblem.difficulty}
        leftPanel={leftPanel}
        rightPanel={rightPanel}
      />
    );
  }

  return (
    <div className="flex-1 min-h-0 bg-zinc-950 overflow-hidden" id="workspace-splits-panel">
      <ResizablePanel
        direction="horizontal"
        initialSplit={40}
        minSize={25}
        maxSize={75}
        idPrefix="workspace-split"
        leftElement={leftPanel}
        rightElement={rightPanel}
      />
    </div>
  );
}
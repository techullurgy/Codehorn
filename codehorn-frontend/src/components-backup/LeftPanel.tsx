import { useRouter } from "next/router";
import { SubmissionDetails } from "./SubmissionDetails";
import EditorialContent from "./EditorialContent";
import SubmissionsContent from "./SubmissionsContent";
import DescriptionContent from "./DescriptionContent";
import { ChevronLeft, X } from "lucide-react";

export const LeftPanel = ({
    isMobile,
    selectedProblem,
    submissions,
    localLeftTab,
    setLocalLeftTab,
    openSubTabs,
    setOpenSubTabs
  }) => {
    const router = useRouter()
  
    // Render left panel depending on tabs (Description, Editorial, Submissions history)
    const renderLeftPanelContent = () => {
      if (localLeftTab.startsWith('submission-')) {
        const subId = localLeftTab.replace('submission-', '');
        const sub = openSubTabs.find(t => t.id === subId);
        if (sub) {
          return <SubmissionDetails sub={sub} selectedProblem={selectedProblem}/>;
        }
      }
  
      switch (localLeftTab) {
        case 'editorial':
          return (
            <EditorialContent />
          );
  
        case 'submissions':
          const problemSubmissions = submissions.filter((s) => s.problemId === selectedProblem.id);
          return (
            <SubmissionsContent 
              problemSubmissions={problemSubmissions} 
              openSubTabs={openSubTabs} 
              setLocalLeftTab={setLocalLeftTab} 
              setOpenSubTabs={setOpenSubTabs}
            />
          );
  
        case 'description':
        default:
          return (
            <DescriptionContent />
          );
      }
    };
  
    return (
      <div className="flex flex-col h-full bg-zinc-950 border-r border-zinc-800 text-zinc-100 min-w-0">
  
        {/* Backlink title row */}
        {!isMobile && (
          <div className="px-4 h-12 flex items-center justify-between border-b border-zinc-800 bg-zinc-900/10 shrink-0 select-none">
            <button
              id="back-to-problems-btn"
              onClick={() => router.push('/')}
              className="flex items-center space-x-1.5 text-zinc-400 hover:text-zinc-150 text-xs font-semibold transition-colors duration-150"
            >
              <ChevronLeft className="w-3.5 h-3.5" />
              <span>Back to Problem List</span>
            </button>
            <span className="text-[10px] text-zinc-500 uppercase tracking-widest font-mono font-medium">Workspace Sandbox</span>
          </div>
        )}
  
        
        <div className="flex border-b border-zinc-850 shrink-0 bg-zinc-950/45 text-xs font-medium px-4 select-none overflow-x-auto scrollbar-none gap-0.5">
          <button
            id="tab-desc-btn"
            onClick={() => setLocalLeftTab('description')}
            className={`px-3 py-2.5 border-b-2 font-semibold transition-all duration-150 shrink-0 ${localLeftTab === 'description'
                ? 'border-zinc-200 text-zinc-100'
                : 'border-transparent text-zinc-500 hover:text-zinc-200'
              }`}
          >
            Description
          </button>
  
          <button
            id="tab-editorial-btn"
            onClick={() => setLocalLeftTab('editorial')}
            className={`px-3 py-2.5 border-b-2 font-semibold transition-all duration-150 shrink-0 ${localLeftTab === 'editorial'
                ? 'border-zinc-200 text-zinc-100'
                : 'border-transparent text-zinc-500 hover:text-zinc-200'
              }`}
          >
            Editorial
          </button>
  
          <button
            id="tab-subhistory-btn"
            onClick={() => setLocalLeftTab('submissions')}
            className={`px-3 py-2.5 border-b-2 font-semibold transition-all duration-150 shrink-0 ${localLeftTab === 'submissions'
                ? 'border-zinc-200 text-zinc-100'
                : 'border-transparent text-zinc-500 hover:text-zinc-200'
              }`}
          >
            Submissions
          </button>
  
          {openSubTabs.map((sub) => {
            const isSelected = localLeftTab === `submission-${sub.id}`;
            return (
              <div
                key={sub.id}
                className={`flex items-center space-x-1.5 py-1.5 px-2.5 border-b-2 transition-all duration-150 shrink-0 ${isSelected
                    ? 'border-zinc-200 text-zinc-100 bg-zinc-900/40'
                    : 'border-transparent text-zinc-500 hover:text-zinc-200'
                  }`}
              >
                <button
                  type="button"
                  onClick={() => setLocalLeftTab(`submission-${sub.id}`)}
                  className="font-semibold text-xs flex items-center space-x-1 cursor-pointer"
                >
                  {false ? (
                    <div className="w-2.5 h-2.5 rounded-full border-2 border-amber-500 border-t-transparent animate-spin mr-1" />
                  ) : (
                    <div className={`w-1.5 h-1.5 rounded-full mr-1 ${sub.status === 'Accepted' ? 'bg-emerald-400' : 'bg-rose-400'
                      }`} />
                  )}
                  <span>Sub #{sub.id.replace('sub_', '').substr(0, 4)}</span>
                </button>
                <button
                  type="button"
                  onClick={(e) => {
                    e.stopPropagation();
                    setOpenSubTabs(prev => prev.filter(t => t.id !== sub.id));
                    if (isSelected) {
                      setLocalLeftTab('submissions');
                    }
                  }}
                  className="p-0.5 rounded hover:bg-zinc-800 text-zinc-600 hover:text-zinc-300 transition-all cursor-pointer"
                  title="Close tab"
                >
                  <X className="w-3 h-3" />
                </button>
              </div>
            );
          })}
        </div>
  
        {renderLeftPanelContent()}
      </div>
    );
  }
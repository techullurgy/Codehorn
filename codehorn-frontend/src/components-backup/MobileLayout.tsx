import { ChevronLeft } from "lucide-react"
import { useRouter } from "next/router"
import { useState } from "react"

export const MobileLayout = ({ language, setLanguage, diffColor, difficulty, leftPanel, rightPanel }) =>  {
    const router = useRouter()
    const [mobileTab, setMobileTab] = useState<'problem' | 'code'>()
  
    return (
      <div className="flex-1 flex flex-col bg-zinc-950 overflow-hidden" id="workspace-splits-panel-mobile">
  
      {/* Unified Mobile Adaptive Tabs Controller Header */}
      <div className="h-14 bg-zinc-900 border-b border-zinc-800 px-4 flex items-center justify-between shrink-0 select-none w-full shadow-sm">
        <button
          onClick={() => router.push('/')}
          className="flex items-center space-x-1 text-zinc-400 hover:text-zinc-200 text-xs font-semibold bg-zinc-950/40 px-2.5 py-1.5 rounded-lg border border-zinc-800"
        >
          <ChevronLeft className="w-4 h-4" />
          <span>Back</span>
        </button>
  
        {/* Sliding pills layout bar */}
        <div className="bg-zinc-950 p-1 rounded-xl border border-zinc-800 flex items-center space-x-0.5 select-none shrink-0 shadow-inner">
          <button
            onClick={() => setMobileTab('problem')}
            className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${mobileTab === 'problem'
                ? 'bg-zinc-850 text-amber-500 shadow-sm border border-zinc-800'
                : 'text-zinc-500 hover:text-zinc-300 border border-transparent'
              }`}
          >
            Description
          </button>
          <button
            onClick={() => setMobileTab('code')}
            className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${mobileTab === 'code'
                ? 'bg-zinc-850 text-amber-500 shadow-sm border border-zinc-800'
                : 'text-zinc-500 hover:text-zinc-300 border border-transparent'
              }`}
          >
            Code Editor
          </button>
        </div>
  
        <div className="flex items-center shrink-0">
          {mobileTab === 'code' ? (
            <select
              id="editor-language-select-mobile"
              value={language}
              onChange={(e) => setLanguage(e.target.value as any)}
              className="bg-zinc-950 border border-zinc-800 rounded-lg py-1 px-2.5 text-xs font-bold text-zinc-300 focus:outline-none cursor-pointer"
            >
              <option value="javascript">JS</option>
              <option value="python">PY</option>
              <option value="cpp">C++</option>
              <option value="java">JAVA</option>
            </select>
          ) : (
            <span className={`px-2 py-1 rounded-md text-[10px] font-mono font-bold tracking-tight border ${diffColor}`}>
              {difficulty}
            </span>
          )}
        </div>
      </div>
  
      {/* Swipeable or standard full panel viewport area */}
      <div className="flex-1 min-h-0 relative overflow-hidden bg-zinc-950">
        <div className="absolute inset-0 flex flex-col">
          {mobileTab === 'problem' ? leftPanel : rightPanel}
        </div>
      </div>
  
      </div>
    )
  }
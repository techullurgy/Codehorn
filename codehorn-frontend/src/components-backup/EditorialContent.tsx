'use client'

import { useCodeHornStore } from "../store/useCodeHornStore"
import MarkdownRenderer from "./MarkdownRenderer"
import { BookOpen } from "lucide-react"


export default function EditorialContent() {
  const { selectedProblem } = useCodeHornStore()
  
  if (!selectedProblem) return null

  return (
    <div className="flex-1 overflow-y-auto px-5 py-6 space-y-6 text-zinc-300 text-sm leading-relaxed" id="detail-left-editorial">
        <div className="flex items-center space-x-2 pb-2 border-b border-zinc-800">
            <BookOpen className="w-5 h-5 text-amber-500" />
            <h2 className="text-lg font-bold text-zinc-100">Official Editorial & Solution</h2>
        </div>

        <div className="prose prose-invert max-w-none text-zinc-300 space-y-4">
            {/* Editorial text formatted section */}
            <div className="leading-relaxed text-zinc-300 antialiased font-sans">
                <MarkdownRenderer content={selectedProblem.editorial}/>
            </div>
        </div>
    </div>
  )
}
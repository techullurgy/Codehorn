import { createStore } from "zustand"
import { CodeLanguage } from "../types/Submission"


type LeftTabs = 'description' | 'editorial' | 'submissions'

export type ProblemPlaygroundStore = ProblemPlaygroundStoreState & ProblemPlaygroundStoreActions

type ProblemPlaygroundStoreState = {
  slug: string,
  leftTabs: (LeftTabs | (string & {}))[],
  activeLeftTab: LeftTabs | (string & {}),

  showConsole: boolean
  selectedCodeLanguage: CodeLanguage
  codeDraft: string
}

type ProblemPlaygroundStoreActions = {
  addToLeftTabs: (tab: string) => void
  closeLeftTab: (tab: string) => void
  switchToLeftTab: (tab: string) => void

  toggleShowConsole: () => void
  changeSelectedCodeLanguage: (language: CodeLanguage) => void
  updateCodeDraft: (code: string) => void
}

export const createProblemPlaygroundStore = (initProps: Partial<ProblemPlaygroundStoreState>) => {
  const DEFAULT_PROPS: ProblemPlaygroundStoreState = {
    slug: null,
    leftTabs: ['description', 'editorial', 'submissions'],
    activeLeftTab: 'description',
    showConsole: true,
    selectedCodeLanguage: 'java',
    codeDraft: null
  }

  return createStore<ProblemPlaygroundStore>((set) => ({
    ...DEFAULT_PROPS,
    ...initProps,

    addToLeftTabs: (tab) => {
      set(state => ({
        leftTabs: [...state.leftTabs, tab],
        activeLeftTab: tab
      }))
    },
    closeLeftTab: (tab) => {
      set(state => ({
        leftTabs: state.leftTabs.filter(t => t !== tab),
        activeLeftTab: state.activeLeftTab === tab ? state.leftTabs[2] : state.activeLeftTab
      }))
    },
    switchToLeftTab: (tab) => set({ activeLeftTab: tab }),

    toggleShowConsole: () => set(state => ({ showConsole: !state.showConsole })),
    changeSelectedCodeLanguage: (language) => set({ selectedCodeLanguage: language }),
    updateCodeDraft: (code) => set({ codeDraft: code })
  }))
}
import { createStore } from "zustand"
import { CodeLanguage } from "../types/Submission"
import { ProblemTestCase } from "../types/Problem"

type LeftTab = 'description' | 'editorial' | 'submissions'
type ConsoleTab = 'testcases' | 'testresults'

export type ProblemPlaygroundStore = ProblemPlaygroundStoreState & ProblemPlaygroundStoreActions

type ProblemPlaygroundStoreState = {
  slug: string,
  leftTabs: (LeftTab | (string & {}))[],
  activeLeftTab: LeftTab | (string & {}),

  showConsole: boolean
  selectedCodeLanguage: CodeLanguage
  codeDraft: string

  consoleTabs: ConsoleTab[]
  activeConsoleTab: ConsoleTab

  testcases: ProblemTestCase[]
  originalTestcasesCount: number
  focusedTestcaseIndex: number
}

type ProblemPlaygroundStoreActions = {
  addToLeftTabs: (tab: string) => void
  closeLeftTab: (tab: string) => void
  switchToLeftTab: (tab: LeftTab | (string & {})) => void
  replaceLeftTab: (oldTab: LeftTab | (string & {}), newTab: LeftTab | (string & {})) => void,

  toggleShowConsole: () => void
  changeSelectedCodeLanguage: (language: CodeLanguage) => void
  updateCodeDraft: (code: string) => void

  switchToConsoleTab: (tab: ConsoleTab) => void

  setTestcases: (testcases: ProblemTestCase[]) => void
  addTestcase: (testcase: ProblemTestCase) => void
  removeTestcase: (testcaseId: string) => void
  focusToTestcaseIndex: (index: number) => void
  updateTestcaseInput: (testcaseId: string, input: string, value: string) => void
}

export const createProblemPlaygroundStore = (initProps: Partial<ProblemPlaygroundStoreState>) => {
  const DEFAULT_PROPS: ProblemPlaygroundStoreState = {
    slug: null,
    leftTabs: ['description', 'editorial', 'submissions'],
    activeLeftTab: 'description',
    showConsole: false,
    selectedCodeLanguage: 'java',
    codeDraft: null,
    consoleTabs: ['testcases', 'testresults'],
    activeConsoleTab: 'testcases',
    testcases: [],
    originalTestcasesCount: 0,
    focusedTestcaseIndex: 0
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
    replaceLeftTab: (oldTab, newTab) => set(state => {
      let newLeftTabs = state.leftTabs.map(tab => tab === oldTab ? newTab : tab)
      let activeLeftTab = state.activeLeftTab === oldTab ? newTab : state.activeLeftTab
      return {
        leftTabs: newLeftTabs,
        activeLeftTab
      }
    }),

    toggleShowConsole: () => set(state => ({ showConsole: !state.showConsole })),
    changeSelectedCodeLanguage: (language) => set({ selectedCodeLanguage: language }),
    updateCodeDraft: (code) => set({ codeDraft: code }),

    switchToConsoleTab: (tab) => set({ activeConsoleTab: tab }),

    setTestcases: (testcases) => set({ testcases }),
    addTestcase: (testcase) => set(state => ({ testcases: [...state.testcases, testcase] })),
    removeTestcase: (testcaseId) => set(state => ({ testcases: state.testcases.filter(t => t.id !== testcaseId) })),
    focusToTestcaseIndex: (index) => set({ focusedTestcaseIndex: index }),
    updateTestcaseInput: (testcaseId, input, value) => set(state => ({
      testcases: state.testcases.map(tc =>
        tc.id === testcaseId ? {
          ...tc,
          inputs: {
            ...tc.inputs,
            [input]: value
          }
        } : tc
      )
    }))
  }))
}
import { create } from "zustand"

type EditorTheme = 'vs-dark' | 'hc-black' | 'light'

type EditorStore = {
  theme: EditorTheme
} & EditorStoreActions

type EditorStoreActions = {
  setTheme: (theme: EditorTheme) => void
}

export const useEditorStore = create<EditorStore>((set) => (
  {
    theme: 'vs-dark',
    setTheme: (theme) => set({ theme })
  }
))
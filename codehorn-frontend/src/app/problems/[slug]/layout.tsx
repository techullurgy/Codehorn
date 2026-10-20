import PlaygroundNavbar from "./components/PlaygroundNavBar";

export default async function ({ children }) {
  const globalTheme = "light";
  return (
    <div
      className={`h-screen w-screen flex flex-col font-sans ${"overflow-hidden custom-minimal-scrollbar"} ${
        globalTheme === "light" ? "global-theme-light" : ""
      } bg-zinc-950 text-zinc-100`}
    >
      <PlaygroundNavbar />
      {/* Main viewport area panel */}
      <main
        className={`flex-1 flex flex-col ${"overflow-hidden min-h-0"} relative`}
      >
        {children}
      </main>
    </div>
  );
}

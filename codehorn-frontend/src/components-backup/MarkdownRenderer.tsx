import dynamic from "next/dynamic";
import ReactMarkdown from "react-markdown";

const MarkdownCodeBlock = dynamic(() => import("./MarkdownCodeBlock"), {
  ssr: false,
  loading: ({ error }) => {
    if (error) {
      return <p>Failed to Load the code</p>;
    }

    return <p>Loading Code....</p>;
  },
});

export default function ({ content }: { content: string }) {
  return (
    <ReactMarkdown
      components={{
        p: ({ children }) => (
          <p className="mb-4 text-zinc-300 leading-relaxed font-sans">
            {children}
          </p>
        ),
        h1: ({ children }) => (
          <h1 className="text-xl font-extrabold text-zinc-100 tracking-tight mt-6 mb-3">
            {children}
          </h1>
        ),
        h2: ({ children }) => (
          <h2 className="text-lg font-extrabold text-zinc-100 tracking-tight mt-5 mb-2.5">
            {children}
          </h2>
        ),
        h3: ({ children }) => (
          <h3 className="text-md font-bold text-zinc-200 tracking-tight mt-4 mb-2">
            {children}
          </h3>
        ),
        strong: ({ children }) => (
          <strong className="font-extrabold text-zinc-100">{children}</strong>
        ),
        em: ({ children }) => (
          <em className="italic text-zinc-200">{children}</em>
        ),
        code: ({ node, className, children, ...props }) => {
          const match = /language-(\w+)/.exec(className || "");
          const rawCode = String(children).replace(/\n$/, "");
          if (match) {
            return <MarkdownCodeBlock lang={match[1]} code={rawCode} />;
          }
          return (
            <code
              className="bg-zinc-900 border border-zinc-800/60 text-amber-400 px-1.5 py-0.5 rounded text-[12px] font-mono leading-none font-medium mx-0.5"
              {...props}
            >
              {children}
            </code>
          );
        },
        ol: ({ children }) => (
          <ol className="list-decimal pl-5 mb-4 space-y-1.5 text-zinc-300">
            {children}
          </ol>
        ),
        ul: ({ children }) => (
          <ul className="list-disc pl-5 mb-4 space-y-1.5 text-zinc-300">
            {children}
          </ul>
        ),
        li: ({ children }) => (
          <li className="leading-relaxed mb-1">{children}</li>
        ),
        pre: ({ children }) => <>{children}</>,
      }}
    >
      {content}
    </ReactMarkdown>
  );
}

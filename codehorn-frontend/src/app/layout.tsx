import type { Metadata } from "next";
import "../index.css";
import QueryClientProvider from "../components/QueryClientProvider";

export const metadata: Metadata = {
  title: "CodeHorn - Sandbox & Coding Platform",
  description:
    "A professional platform for coding challenges, sandboxing, and performance evaluations.",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className="antialiased" suppressHydrationWarning>
        <QueryClientProvider>{children}</QueryClientProvider>
      </body>
    </html>
  );
}

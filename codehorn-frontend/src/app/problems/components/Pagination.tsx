export const Pagination = () => (
  <div className="flex justify-between items-center mt-6 text-xs text-zinc-500 px-2 font-medium">
    <div className="flex space-x-1">
      <button
        className="px-3 py-1 rounded border border-zinc-800 bg-zinc-900 cursor-not-allowed"
        disabled
      >
        Prev
      </button>
      <button className="px-3 py-1.5 rounded border border-zinc-700 bg-zinc-900 text-zinc-100">
        1
      </button>
      <button
        className="px-3 py-1 rounded border border-zinc-800 bg-zinc-900 cursor-not-allowed"
        disabled
      >
        Next
      </button>
    </div>
  </div>
);

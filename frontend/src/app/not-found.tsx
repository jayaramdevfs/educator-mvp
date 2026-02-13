import Link from "next/link";

export default function NotFound() {
  return (
    <main className="relative flex min-h-screen items-center justify-center overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6">
      <div className="pointer-events-none absolute left-[10%] top-16 h-96 w-96 rounded-full bg-purple-500/20 blur-3xl" />
      <div className="pointer-events-none absolute right-[12%] top-12 h-80 w-80 rounded-full bg-pink-500/15 blur-3xl" />

      <div className="relative w-full max-w-lg rounded-3xl border border-slate-800 bg-slate-900/60 p-8 text-center shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
        <p className="mb-3 inline-flex rounded-full border border-purple-500/30 bg-purple-500/10 px-3 py-1 text-xs font-medium tracking-wide text-purple-300">
          404
        </p>
        <h1 className="bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-4xl font-bold text-transparent">
          Page Not Found
        </h1>
        <p className="mt-3 text-slate-400">The page you requested does not exist.</p>
        <div className="mt-6">
          <Link
            href="/"
            className="inline-flex rounded-lg bg-gradient-to-r from-purple-600 to-purple-500 px-4 py-2 text-sm font-medium text-white shadow-lg shadow-purple-500/30 transition-all hover:from-purple-500 hover:to-purple-400"
          >
            Back to Home
          </Link>
        </div>
      </div>
    </main>
  );
}

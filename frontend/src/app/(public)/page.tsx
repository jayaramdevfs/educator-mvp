import Link from "next/link";

export default function PublicHomePage() {
  return (
    <main className="relative min-h-screen overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6 py-14">
      <div className="pointer-events-none absolute left-[8%] top-14 h-96 w-96 rounded-full bg-purple-500/20 blur-3xl" />
      <div className="pointer-events-none absolute right-[10%] top-8 h-80 w-80 rounded-full bg-pink-500/15 blur-3xl" />
      <div className="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_center,transparent_40%,rgba(0,0,0,0.58)_100%)]" />

      <div className="relative mx-auto flex min-h-[calc(100vh-7rem)] w-full max-w-6xl flex-col justify-center">
        <div className="rounded-3xl border border-slate-800 bg-slate-900/60 p-8 shadow-2xl shadow-purple-500/10 backdrop-blur-xl md:p-12">
          <p className="mb-4 inline-flex rounded-full border border-purple-500/30 bg-purple-500/10 px-3 py-1 font-mono text-xs tracking-wide text-purple-300">
            Educator MVP
          </p>
          <h1 className="max-w-3xl bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-3xl font-semibold tracking-tight text-transparent md:text-5xl">
            Sprint 8 frontend theme is now unified.
          </h1>
          <p className="mt-4 max-w-3xl text-base text-slate-400 md:text-lg">
            All primary routes now share the same purple/pink design system with consistent slate
            surfaces, gradients, and interaction states.
          </p>
          <div className="mt-8 flex flex-wrap gap-3">
            <Link
              className="rounded-lg bg-gradient-to-r from-purple-600 to-purple-500 px-4 py-2 text-sm font-medium text-white shadow-lg shadow-purple-500/30 transition-all hover:from-purple-500 hover:to-purple-400"
              href="/login"
            >
              Login
            </Link>
            <Link
              className="rounded-lg bg-gradient-to-r from-purple-600 to-purple-500 px-4 py-2 text-sm font-medium text-white shadow-lg shadow-purple-500/30 transition-all hover:from-purple-500 hover:to-purple-400"
              href="/learner"
            >
              Learner Area
            </Link>
            <Link
              className="rounded-lg border border-slate-700 bg-slate-800/50 px-4 py-2 text-sm font-medium text-slate-300 transition-colors hover:border-purple-500/30 hover:text-purple-300"
              href="/admin"
            >
              Admin Area
            </Link>
            <Link
              className="rounded-lg border border-slate-700 bg-slate-800/50 px-4 py-2 text-sm font-medium text-slate-300 transition-colors hover:border-purple-500/30 hover:text-purple-300"
              href="/instructor"
            >
              Instructor Area
            </Link>
          </div>
        </div>
      </div>
    </main>
  );
}

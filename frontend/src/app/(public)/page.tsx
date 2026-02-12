import Link from "next/link";

export default function PublicHomePage() {
  return (
    <main className="mx-auto flex min-h-screen w-full max-w-6xl flex-col justify-center px-6 py-14">
      <div className="rounded-[var(--radius-lg)] border bg-[var(--surface)] p-8 shadow-sm md:p-12">
        <p className="mb-3 inline-flex rounded-full bg-[var(--surface-muted)] px-3 py-1 font-mono text-xs tracking-wide text-[var(--accent-foreground)]">
          Educator MVP
        </p>
        <h1 className="max-w-2xl text-3xl font-semibold tracking-tight md:text-5xl">
          Sprint 7 foundation is active. Frontend route groups are ready.
        </h1>
        <p className="mt-4 max-w-2xl text-base text-zinc-700 md:text-lg">
          Continue implementation from this baseline: typed contracts, API client, auth store, and
          grouped route structure are now in place.
        </p>
        <div className="mt-8 flex flex-wrap gap-3">
          <Link
            className="rounded-[var(--radius-md)] bg-[var(--accent)] px-4 py-2 text-sm font-medium text-[var(--accent-foreground)]"
            href="/login"
          >
            Login
          </Link>
          <Link
            className="rounded-[var(--radius-md)] bg-[var(--primary)] px-4 py-2 text-sm font-medium text-[var(--primary-foreground)]"
            href="/learner"
          >
            Learner Area
          </Link>
          <Link
            className="rounded-[var(--radius-md)] border bg-white px-4 py-2 text-sm font-medium"
            href="/admin"
          >
            Admin Area
          </Link>
          <Link
            className="rounded-[var(--radius-md)] border bg-white px-4 py-2 text-sm font-medium"
            href="/instructor"
          >
            Instructor Area
          </Link>
        </div>
      </div>
    </main>
  );
}

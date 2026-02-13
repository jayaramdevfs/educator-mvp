import Link from "next/link";
import { PublicFooter, TopNav } from "@/components/layout";

interface HomepageSection {
  id: string;
  title: string;
  position: string;
}

interface HomepageBlock {
  id: string;
  blockType: string;
}

interface HomepageResponseItem {
  section: HomepageSection;
  blocks: HomepageBlock[];
}

interface PaginatedResponse<T> {
  content: T[];
}

const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL?.trim() || "http://localhost:8080";

async function fetchHomepageSections() {
  try {
    const response = await fetch(`${API_BASE_URL}/api/public/homepage`, {
      cache: "no-store",
    });

    if (!response.ok) {
      return [];
    }

    const data = (await response.json()) as PaginatedResponse<HomepageResponseItem>;
    return data.content ?? [];
  } catch {
    return [];
  }
}

export default async function PublicHomePage() {
  const homepageSections = await fetchHomepageSections();

  return (
    <div className="min-h-screen bg-slate-950">
      <TopNav />
      <main className="relative overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6 py-14">
        <div className="pointer-events-none absolute left-[8%] top-14 h-96 w-96 rounded-full bg-purple-500/20 blur-3xl" />
        <div className="pointer-events-none absolute right-[10%] top-8 h-80 w-80 rounded-full bg-pink-500/15 blur-3xl" />
        <div className="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_center,transparent_40%,rgba(0,0,0,0.58)_100%)]" />

        <div className="relative mx-auto flex min-h-[calc(100vh-7rem)] w-full max-w-6xl flex-col justify-center">
          <div className="rounded-3xl border border-slate-800 bg-slate-900/60 p-8 shadow-2xl shadow-purple-500/10 backdrop-blur-xl md:p-12">
            <p className="mb-4 inline-flex rounded-full border border-purple-500/30 bg-purple-500/10 px-3 py-1 font-mono text-xs tracking-wide text-purple-300">
              Educator MVP
            </p>
            <h1 className="max-w-3xl bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-3xl font-semibold tracking-tight text-transparent md:text-5xl">
              Sprint 9 public experience starts here.
            </h1>
            <p className="mt-4 max-w-3xl text-base text-slate-400 md:text-lg">
              Browse the public catalog, view course details, and continue to role-based learning
              flows with a unified purple/pink design system.
            </p>
            <div className="mt-8 flex flex-wrap gap-3">
              <Link
                className="rounded-lg bg-gradient-to-r from-purple-600 to-purple-500 px-4 py-2 text-sm font-medium text-white shadow-lg shadow-purple-500/30 transition-all hover:from-purple-500 hover:to-purple-400"
                href="/catalog"
              >
                Open Catalog
              </Link>
              <Link
                className="rounded-lg bg-gradient-to-r from-purple-600 to-purple-500 px-4 py-2 text-sm font-medium text-white shadow-lg shadow-purple-500/30 transition-all hover:from-purple-500 hover:to-purple-400"
                href="/login-new"
              >
                Sign In
              </Link>
              <Link
                className="rounded-lg border border-slate-700 bg-slate-800/50 px-4 py-2 text-sm font-medium text-slate-300 transition-colors hover:border-purple-500/30 hover:text-purple-300"
                href="/register"
              >
                Create Account
              </Link>
            </div>
          </div>

          <div className="mt-6 rounded-3xl border border-slate-800 bg-slate-900/60 p-8 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
            <h2 className="text-2xl font-semibold text-slate-100">Dynamic Homepage Sections</h2>
            <p className="mt-2 text-slate-400">
              Rendered from <code>/api/public/homepage</code>.
            </p>

            {homepageSections.length === 0 ? (
              <div className="mt-5 rounded-xl border border-slate-800 bg-slate-950/50 p-4 text-sm text-slate-400">
                No homepage sections are published yet.
              </div>
            ) : (
              <div className="mt-5 grid gap-4 md:grid-cols-2">
                {homepageSections.map((item) => (
                  <article
                    key={item.section.id}
                    className="rounded-xl border border-slate-800 bg-slate-950/50 p-4"
                  >
                    <div className="flex items-center justify-between">
                      <h3 className="text-lg font-medium text-slate-100">{item.section.title}</h3>
                      <span className="rounded-full bg-purple-500/10 px-2 py-1 text-xs text-purple-300">
                        {item.section.position}
                      </span>
                    </div>
                    <p className="mt-2 text-sm text-slate-400">
                      Blocks: {item.blocks.length > 0 ? item.blocks.length : "None"}
                    </p>
                    {item.blocks.length > 0 && (
                      <div className="mt-3 flex flex-wrap gap-2">
                        {item.blocks.map((block) => (
                          <span
                            key={block.id}
                            className="rounded-full border border-slate-700 bg-slate-800/60 px-2 py-1 text-xs text-slate-300"
                          >
                            {block.blockType}
                          </span>
                        ))}
                      </div>
                    )}
                  </article>
                ))}
              </div>
            )}
          </div>
        </div>
      </main>
      <PublicFooter />
    </div>
  );
}

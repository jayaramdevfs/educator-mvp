import Link from "next/link";
import { PublicFooter, TopNav } from "@/components/layout";

interface PaginatedResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

interface CourseItem {
  id: number;
  titleEn: string;
  descriptionEn: string;
  difficulty: string;
  status: string;
  estimatedDurationMinutes: number;
}

interface HierarchyNode {
  id: number;
  nameEn: string;
}

const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL?.trim() || "http://localhost:8080";

async function fetchCourses(
  q: string,
  difficulty: string,
  status: string,
  page: number,
) {
  const params = new URLSearchParams({
    page: String(page),
    size: "20",
  });

  if (q) params.set("q", q);
  if (difficulty) params.set("difficulty", difficulty);
  if (status) params.set("status", status);

  try {
    const response = await fetch(`${API_BASE_URL}/api/public/courses/search?${params}`, {
      cache: "no-store",
    });

    if (!response.ok) {
      return null;
    }

    return (await response.json()) as PaginatedResponse<CourseItem>;
  } catch {
    return null;
  }
}

async function fetchHierarchyRoots() {
  try {
    const response = await fetch(
      `${API_BASE_URL}/api/hierarchy/roots?page=0&size=20`,
      { cache: "no-store" },
    );

    if (!response.ok) {
      return [];
    }

    const data = (await response.json()) as PaginatedResponse<HierarchyNode>;
    return data.content;
  } catch {
    return [];
  }
}

export default async function CatalogPage({
  searchParams,
}: {
  searchParams?: Promise<Record<string, string | string[] | undefined>>;
}) {
  const resolvedSearchParams = (await searchParams) ?? {};
  const q = typeof resolvedSearchParams.q === "string" ? resolvedSearchParams.q : "";
  const difficulty =
    typeof resolvedSearchParams.difficulty === "string" ? resolvedSearchParams.difficulty : "";
  const status =
    typeof resolvedSearchParams.status === "string" ? resolvedSearchParams.status : "";
  const pageRaw = typeof resolvedSearchParams.page === "string" ? resolvedSearchParams.page : "0";
  const page = Number.isFinite(Number(pageRaw)) ? Math.max(Number(pageRaw), 0) : 0;

  const [courses, roots] = await Promise.all([
    fetchCourses(q, difficulty, status, page),
    fetchHierarchyRoots(),
  ]);

  const list = courses?.content ?? [];

  return (
    <div className="min-h-screen bg-slate-950">
      <TopNav />
      <main className="relative overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6 py-10">
        <div className="pointer-events-none absolute left-[8%] top-16 h-96 w-96 rounded-full bg-purple-500/20 blur-3xl" />
        <div className="pointer-events-none absolute right-[10%] top-10 h-80 w-80 rounded-full bg-pink-500/15 blur-3xl" />

        <div className="relative mx-auto max-w-7xl">
          <div className="mb-8 rounded-2xl border border-slate-800 bg-slate-900/60 p-6 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
            <h1 className="bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-4xl font-bold text-transparent">
              Course Catalog
            </h1>
            <p className="mt-2 text-slate-400">
              Discover courses with search and filters powered by Sprint 9 APIs.
            </p>

            <form className="mt-6 grid gap-3 md:grid-cols-4" method="get">
              <input
                type="text"
                name="q"
                defaultValue={q}
                placeholder="Search by title or description"
                className="h-11 rounded-lg border border-slate-700 bg-slate-950/60 px-3 text-slate-100 placeholder:text-slate-500 focus:border-purple-500 focus:outline-none"
              />
              <select
                name="difficulty"
                defaultValue={difficulty}
                className="h-11 appearance-none rounded-lg border border-slate-700 bg-slate-900 px-3 pr-8 text-slate-100 focus:border-purple-500 focus:outline-none focus:ring-1 focus:ring-purple-500/30 cursor-pointer [&>option]:bg-slate-900 [&>option]:text-slate-100 [&>option]:py-2"
                style={{ backgroundImage: `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 24 24' fill='none' stroke='%239ca3af' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='6 9 12 15 18 9'%3E%3C/polyline%3E%3C/svg%3E")`, backgroundRepeat: 'no-repeat', backgroundPosition: 'right 10px center' }}
              >
                <option value="">All difficulties</option>
                <option value="BEGINNER">Beginner</option>
                <option value="INTERMEDIATE">Intermediate</option>
                <option value="ADVANCED">Advanced</option>
              </select>
              <select
                name="status"
                defaultValue={status}
                className="h-11 appearance-none rounded-lg border border-slate-700 bg-slate-900 px-3 pr-8 text-slate-100 focus:border-purple-500 focus:outline-none focus:ring-1 focus:ring-purple-500/30 cursor-pointer [&>option]:bg-slate-900 [&>option]:text-slate-100 [&>option]:py-2"
                style={{ backgroundImage: `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 24 24' fill='none' stroke='%239ca3af' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='6 9 12 15 18 9'%3E%3C/polyline%3E%3C/svg%3E")`, backgroundRepeat: 'no-repeat', backgroundPosition: 'right 10px center' }}
              >
                <option value="">All statuses</option>
                <option value="PUBLISHED">Published</option>
              </select>
              <button
                type="submit"
                className="h-11 rounded-lg bg-gradient-to-r from-purple-600 to-purple-500 font-semibold text-white shadow-lg shadow-purple-500/30 transition-all hover:from-purple-500 hover:to-purple-400"
              >
                Apply Filters
              </button>
            </form>

            {roots.length > 0 && (
              <div className="mt-5 flex flex-wrap gap-2">
                {roots.map((root) => (
                  <span
                    key={root.id}
                    className="rounded-full border border-purple-500/30 bg-purple-500/10 px-3 py-1 text-xs text-purple-300"
                  >
                    {root.nameEn}
                  </span>
                ))}
              </div>
            )}
          </div>

          {list.length === 0 ? (
            <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-8 text-center text-slate-400 backdrop-blur-xl">
              No courses found with current filters.
            </div>
          ) : (
            <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
              {list.map((course) => (
                <article
                  key={course.id}
                  className="rounded-2xl border border-slate-800 bg-slate-900/60 p-5 shadow-xl shadow-purple-500/10 backdrop-blur-xl"
                >
                  <div className="mb-3 flex items-center justify-between">
                    <span className="rounded-full bg-purple-500/10 px-2 py-1 text-xs text-purple-300">
                      {course.difficulty}
                    </span>
                    <span className="text-xs text-slate-500">{course.status}</span>
                  </div>
                  <h2 className="text-lg font-semibold text-slate-100">{course.titleEn}</h2>
                  <p className="mt-2 line-clamp-3 text-sm text-slate-400">
                    {course.descriptionEn || "No description available."}
                  </p>
                  <p className="mt-3 text-xs text-slate-500">
                    {course.estimatedDurationMinutes} mins
                  </p>
                  <Link
                    href={`/courses/${course.id}`}
                    className="mt-4 inline-flex rounded-lg border border-slate-700 bg-slate-800/50 px-3 py-2 text-sm text-slate-300 transition-colors hover:border-purple-500/30 hover:text-purple-300"
                  >
                    View Details
                  </Link>
                </article>
              ))}
            </div>
          )}

          {courses && (
            <div className="mt-8 flex items-center justify-between text-sm text-slate-400">
              <p>
                Page {courses.pageNumber + 1} of {Math.max(courses.totalPages, 1)} •{" "}
                {courses.totalElements} courses
              </p>
              <div className="flex gap-2">
                <Link
                  href={`/catalog?${new URLSearchParams({
                    q,
                    difficulty,
                    status,
                    page: String(Math.max(page - 1, 0)),
                  })}`}
                  className={`rounded-lg border border-slate-700 px-3 py-2 ${
                    page <= 0
                      ? "pointer-events-none opacity-40"
                      : "hover:border-purple-500/30 hover:text-purple-300"
                  }`}
                >
                  Previous
                </Link>
                <Link
                  href={`/catalog?${new URLSearchParams({
                    q,
                    difficulty,
                    status,
                    page: String(courses.last ? page : page + 1),
                  })}`}
                  className={`rounded-lg border border-slate-700 px-3 py-2 ${
                    courses.last
                      ? "pointer-events-none opacity-40"
                      : "hover:border-purple-500/30 hover:text-purple-300"
                  }`}
                >
                  Next
                </Link>
              </div>
            </div>
          )}
        </div>
      </main>
      <PublicFooter />
    </div>
  );
}

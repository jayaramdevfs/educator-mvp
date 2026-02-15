import Link from "next/link";
import { notFound } from "next/navigation";
import { PublicFooter, TopNav } from "@/components/layout";
import EnrollButton from "@/components/course/enroll-button";

interface CourseDetail {
  id: number;
  titleEn: string;
  descriptionEn: string;
  difficulty: string;
  status: string;
  languageCode: string;
  estimatedDurationMinutes: number;
  hierarchyNode?: {
    id: number;
    nameEn: string;
  };
}

interface LessonTreeNode {
  id: number;
  orderIndex: number;
  depthLevel: number;
  children: LessonTreeNode[];
}

interface PaginatedResponse<T> {
  content: T[];
}

const API_BASE_URL =
    process.env.NEXT_PUBLIC_API_BASE_URL?.trim() || "http://localhost:8080";

async function fetchCourse(courseId: string): Promise<CourseDetail | null> {
  try {
    const response = await fetch(
        `${API_BASE_URL}/api/public/courses/${courseId}`,
        { cache: "no-store" }
    );

    if (!response.ok) {
      return null;
    }

    return (await response.json()) as CourseDetail;
  } catch {
    return null;
  }
}

async function fetchLessonTree(
    courseId: string
): Promise<LessonTreeNode[]> {
  try {
    const response = await fetch(
        `${API_BASE_URL}/api/public/lesson-tree/course/${courseId}`,
        { cache: "no-store" }
    );

    if (!response.ok) {
      return [];
    }

    const data = (await response.json()) as PaginatedResponse<LessonTreeNode>;
    return data.content ?? [];
  } catch {
    return [];
  }
}

function renderTree(nodes: LessonTreeNode[]) {
  return (
      <ul className="space-y-2">
        {nodes.map((node) => (
            <li
                key={node.id}
                className="rounded-lg border border-slate-800 bg-slate-950/50 px-3 py-2 text-sm text-slate-300"
            >
              <div className="flex items-center justify-between">
                <span>Lesson #{node.id}</span>
                <span className="text-xs text-slate-500">
              Depth {node.depthLevel}
            </span>
              </div>
              {node.children.length > 0 && (
                  <div className="mt-2 border-l border-slate-700 pl-3">
                    {renderTree(node.children)}
                  </div>
              )}
            </li>
        ))}
      </ul>
  );
}

export default async function CourseDetailPage({
                                                 params,
                                               }: {
  params: Promise<{ courseId: string }>;
}) {
  const { courseId } = await params;

  const [course, tree] = await Promise.all([
    fetchCourse(courseId),
    fetchLessonTree(courseId),
  ]);

  if (!course) {
    notFound();
  }

  return (
      <div className="min-h-screen bg-slate-950">
        <TopNav />

        <main className="relative overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6 py-10">
          <div className="pointer-events-none absolute left-[8%] top-20 h-96 w-96 rounded-full bg-purple-500/20 blur-3xl" />
          <div className="pointer-events-none absolute right-[10%] top-8 h-80 w-80 rounded-full bg-pink-500/15 blur-3xl" />

          <div className="relative mx-auto max-w-6xl space-y-6">
            {/* Course Info */}
            <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-6 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
              <div className="mb-3 flex flex-wrap items-center gap-2">
              <span className="rounded-full border border-purple-500/30 bg-purple-500/10 px-3 py-1 text-xs text-purple-300">
                {course.difficulty}
              </span>

                <span className="rounded-full border border-slate-700 bg-slate-800/60 px-3 py-1 text-xs text-slate-300">
                {course.status}
              </span>

                {course.hierarchyNode?.nameEn && (
                    <span className="rounded-full border border-slate-700 bg-slate-800/60 px-3 py-1 text-xs text-slate-300">
                  {course.hierarchyNode.nameEn}
                </span>
                )}
              </div>

              <h1 className="bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-4xl font-bold text-transparent">
                {course.titleEn}
              </h1>

              <p className="mt-3 text-slate-400">
                {course.descriptionEn ||
                    "No description available for this course yet."}
              </p>

              <div className="mt-4 flex flex-wrap gap-4 text-sm text-slate-400">
                <span>Language: {course.languageCode}</span>
                <span>
                Duration: {course.estimatedDurationMinutes} mins
              </span>
              </div>

              {/* 🔥 FIXED ENROLL BUTTON */}
              <div className="mt-6 flex gap-3">
                <EnrollButton courseId={course.id} />

                <Link
                    href="/catalog"
                    className="rounded-lg border border-slate-700 bg-slate-800/50 px-4 py-2 text-sm text-slate-300 transition-colors hover:border-purple-500/30 hover:text-purple-300"
                >
                  Back to Catalog
                </Link>
              </div>
            </div>

            {/* Lesson Tree */}
            <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-6 shadow-xl shadow-purple-500/10 backdrop-blur-xl">
              <h2 className="text-xl font-semibold text-slate-100">
                Lesson Tree
              </h2>

              <p className="mt-1 text-sm text-slate-400">
                Structured lesson hierarchy for this course.
              </p>

              <div className="mt-4">
                {tree.length > 0 ? (
                    renderTree(tree)
                ) : (
                    <div className="rounded-lg border border-slate-800 bg-slate-950/50 px-4 py-3 text-sm text-slate-400">
                      No lessons are available yet.
                    </div>
                )}
              </div>
            </div>
          </div>
        </main>

        <PublicFooter />
      </div>
  );
}

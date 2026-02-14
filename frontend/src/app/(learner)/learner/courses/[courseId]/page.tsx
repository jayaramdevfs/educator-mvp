"use client";

import React, { useState, useMemo } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useParams, useRouter } from "next/navigation";
import { apiGet, apiPost, apiDelete } from "@/lib/api/client";
import { Button } from "@/components/ui/button";
import {
  ChevronRight,
  ChevronDown,
  BookOpen,
  Video,
  FileText,
  CheckCircle2,
  Circle,
  ArrowLeft,
  Loader2,
  Play,
  LogOut,
  GraduationCap,
} from "lucide-react";
import { toast } from "sonner";
import type { LessonType } from "@/types/enums";

/* ---------- Types ---------- */

interface LessonNode {
  id: number;
  orderIndex: number;
  depthLevel: number;
  type: LessonType;
  textContent: string | null;
  videoUrl: string | null;
  documentUrl: string | null;
  childLessons: LessonNode[];
  deleted: boolean;
}

interface CourseDetail {
  id: number;
  titleEn: string;
  descriptionEn: string;
  difficulty: string;
  estimatedDurationMinutes: number;
  status: string;
}

interface EnrollmentResponse {
  id: number;
  status: string;
  enrolledAt: string;
  course: { id: number };
}

/* ---------- Lesson Type Icons ---------- */

const lessonTypeIcon: Record<LessonType, React.ReactNode> = {
  TEXT: <FileText className="h-4 w-4" />,
  VIDEO: <Video className="h-4 w-4" />,
  DOCUMENT: <BookOpen className="h-4 w-4" />,
};

/* ---------- Component ---------- */

export default function CourseLearningPage() {
  const { courseId } = useParams<{ courseId: string }>();
  const router = useRouter();
  const queryClient = useQueryClient();

  const [selectedLessonId, setSelectedLessonId] = useState<number | null>(null);
  const [expandedNodes, setExpandedNodes] = useState<Set<number>>(new Set());
  const [completedLessons, setCompletedLessons] = useState<Set<number>>(new Set());

  // Fetch enrollment status
  const { data: enrollmentData } = useQuery({
    queryKey: ["learner-enrollments"],
    queryFn: () =>
      apiGet<{ content: EnrollmentResponse[] }>("/api/learner/enrollments?page=0&size=100"),
  });

  const enrollments = enrollmentData?.content ?? [];
  const enrollment = enrollments.find(
    (e) => String(e.course.id) === courseId && e.status === "ACTIVE"
  );

  // Fetch course details
  const { data: course, isLoading: courseLoading } = useQuery({
    queryKey: ["course-detail", courseId],
    queryFn: () => apiGet<CourseDetail>(`/api/public/courses/${courseId}`),
  });

  // Fetch lessons
  const { data: lessons = [], isLoading: lessonsLoading } = useQuery({
    queryKey: ["course-lessons", courseId],
    queryFn: () => apiGet<LessonNode[]>(`/api/public/lessons/course/${courseId}`),
  });

  // Enroll mutation
  const enrollMutation = useMutation({
    mutationFn: () => apiPost<EnrollmentResponse>(`/api/learner/enrollments/course/${courseId}`),
    onSuccess: (data) => {
      toast.success("Successfully enrolled!");
      queryClient.invalidateQueries({ queryKey: ["learner-enrollments"] });
      // Start progress tracking
      apiPost(`/api/learner/progress/enrollment/${data.id}/start`).catch(() => {});
    },
    onError: () => {
      toast.error("Failed to enroll. You may already be enrolled.");
    },
  });

  // Drop enrollment mutation (F3.13)
  const dropMutation = useMutation({
    mutationFn: () => apiDelete(`/api/learner/enrollments/${enrollment!.id}`),
    onSuccess: () => {
      toast.success("Course dropped successfully.");
      queryClient.invalidateQueries({ queryKey: ["learner-enrollments"] });
      router.push("/learner/dashboard");
    },
    onError: () => {
      toast.error("Failed to drop course.");
    },
  });

  const handleDrop = () => {
    if (confirm("Are you sure you want to drop this course? This action cannot be undone.")) {
      dropMutation.mutate();
    }
  };

  // Mark lesson complete mutation
  const completeLessonMutation = useMutation({
    mutationFn: (lessonId: number) =>
      apiPost(`/api/learner/progress/enrollment/${enrollment?.id}/lesson/${lessonId}/complete`),
    onSuccess: (_, lessonId) => {
      setCompletedLessons((prev) => new Set(prev).add(lessonId));
      toast.success("Lesson marked as complete!");
    },
    onError: () => {
      toast.error("Failed to mark lesson complete.");
    },
  });

  // Flat list for navigation
  const flatLessons = useMemo(() => {
    const flat: LessonNode[] = [];
    function walk(nodes: LessonNode[]) {
      for (const node of nodes.filter((n) => !n.deleted)) {
        flat.push(node);
        if (node.childLessons?.length) walk(node.childLessons);
      }
    }
    walk(lessons);
    return flat;
  }, [lessons]);

  const selectedLesson = flatLessons.find((l) => l.id === selectedLessonId) ?? flatLessons[0] ?? null;
  const currentIndex = selectedLesson ? flatLessons.indexOf(selectedLesson) : 0;

  const toggleExpand = (id: number) => {
    setExpandedNodes((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  };

  if (courseLoading || lessonsLoading) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-slate-950">
        <Loader2 className="h-8 w-8 animate-spin text-purple-400" />
      </div>
    );
  }

  return (
    <div className="flex min-h-screen flex-col bg-slate-950 lg:flex-row">
      {/* Sidebar */}
      <aside className="w-full border-b border-slate-800 bg-slate-900/80 lg:w-80 lg:border-b-0 lg:border-r lg:overflow-y-auto lg:h-[calc(100vh-4rem)] lg:sticky lg:top-16">
        {/* Course header in sidebar */}
        <div className="border-b border-slate-800 p-4">
          <button
            onClick={() => router.push("/learner/dashboard")}
            className="mb-3 flex items-center gap-1 text-sm text-slate-400 hover:text-purple-300 transition-colors"
          >
            <ArrowLeft className="h-4 w-4" />
            Back to Dashboard
          </button>
          <h2 className="text-lg font-semibold text-slate-100 line-clamp-2">
            {course?.titleEn}
          </h2>
          <p className="mt-1 text-xs text-slate-500">
            {flatLessons.length} lessons
            {completedLessons.size > 0 && ` · ${completedLessons.size} completed`}
          </p>
        </div>

        {/* Enrollment CTA if not enrolled */}
        {!enrollment && (
          <div className="border-b border-slate-800 p-4">
            <Button
              onClick={() => enrollMutation.mutate()}
              disabled={enrollMutation.isPending}
              className="w-full bg-gradient-to-r from-purple-600 to-pink-500 text-white hover:from-purple-500 hover:to-pink-400"
            >
              {enrollMutation.isPending ? (
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              ) : null}
              Enroll in this Course
            </Button>
          </div>
        )}

        {/* Course Actions */}
        {enrollment && (
          <div className="border-b border-slate-800 p-4 space-y-2">
            <Button
              onClick={() => router.push(`/learner/courses/${courseId}/exam`)}
              variant="ghost"
              className="w-full justify-start text-slate-300 hover:text-purple-300 hover:bg-purple-500/10"
            >
              <GraduationCap className="mr-2 h-4 w-4" />
              Take Exam
            </Button>
            <Button
              onClick={handleDrop}
              disabled={dropMutation.isPending}
              variant="ghost"
              className="w-full justify-start text-red-400 hover:text-red-300 hover:bg-red-500/10"
            >
              <LogOut className="mr-2 h-4 w-4" />
              Drop Course
            </Button>
          </div>
        )}

        {/* Lesson tree */}
        <nav className="p-2">
          {lessons.filter((l) => !l.deleted).map((lesson) => (
            <LessonTreeItem
              key={lesson.id}
              lesson={lesson}
              depth={0}
              selectedId={selectedLesson?.id ?? null}
              expandedNodes={expandedNodes}
              completedLessons={completedLessons}
              onSelect={setSelectedLessonId}
              onToggle={toggleExpand}
            />
          ))}
        </nav>
      </aside>

      {/* Content Area */}
      <main className="flex-1 p-6 lg:p-8 lg:overflow-y-auto lg:h-[calc(100vh-4rem)]">
        {selectedLesson ? (
          <div className="mx-auto max-w-3xl">
            {/* Lesson header */}
            <div className="mb-6 flex items-center gap-2 text-sm text-slate-400">
              <span className="flex items-center gap-1">
                {lessonTypeIcon[selectedLesson.type]}
                {selectedLesson.type}
              </span>
              <span>·</span>
              <span>Lesson {currentIndex + 1} of {flatLessons.length}</span>
            </div>

            {/* Lesson Content */}
            <div className="rounded-xl border border-slate-800 bg-slate-900/60 p-6">
              <LessonRenderer lesson={selectedLesson} />
            </div>

            {/* Actions */}
            <div className="mt-6 flex items-center justify-between">
              <Button
                variant="ghost"
                disabled={currentIndex === 0}
                onClick={() => setSelectedLessonId(flatLessons[currentIndex - 1]?.id ?? null)}
                className="text-slate-400 hover:text-purple-300"
              >
                Previous
              </Button>

              <div className="flex gap-3">
                {enrollment && !completedLessons.has(selectedLesson.id) && (
                  <Button
                    onClick={() => completeLessonMutation.mutate(selectedLesson.id)}
                    disabled={completeLessonMutation.isPending}
                    className="bg-green-600 text-white hover:bg-green-500"
                  >
                    {completeLessonMutation.isPending ? (
                      <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    ) : (
                      <CheckCircle2 className="mr-2 h-4 w-4" />
                    )}
                    Mark Complete
                  </Button>
                )}
                {completedLessons.has(selectedLesson.id) && (
                  <span className="flex items-center gap-1 text-sm text-green-400">
                    <CheckCircle2 className="h-4 w-4" />
                    Completed
                  </span>
                )}
              </div>

              <Button
                variant="ghost"
                disabled={currentIndex >= flatLessons.length - 1}
                onClick={() => setSelectedLessonId(flatLessons[currentIndex + 1]?.id ?? null)}
                className="text-slate-400 hover:text-purple-300"
              >
                Next
              </Button>
            </div>
          </div>
        ) : (
          <div className="flex flex-col items-center justify-center py-20 text-center">
            <BookOpen className="mb-4 h-12 w-12 text-slate-600" />
            <p className="text-lg text-slate-400">
              {flatLessons.length === 0
                ? "No lessons available for this course yet."
                : "Select a lesson from the sidebar to begin."}
            </p>
          </div>
        )}
      </main>
    </div>
  );
}

/* ---------- Lesson Tree Item ---------- */

function LessonTreeItem({
  lesson,
  depth,
  selectedId,
  expandedNodes,
  completedLessons,
  onSelect,
  onToggle,
}: {
  lesson: LessonNode;
  depth: number;
  selectedId: number | null;
  expandedNodes: Set<number>;
  completedLessons: Set<number>;
  onSelect: (id: number) => void;
  onToggle: (id: number) => void;
}) {
  const children = lesson.childLessons?.filter((c) => !c.deleted) ?? [];
  const hasChildren = children.length > 0;
  const isExpanded = expandedNodes.has(lesson.id);
  const isSelected = lesson.id === selectedId;
  const isCompleted = completedLessons.has(lesson.id);

  return (
    <div>
      <button
        onClick={() => {
          onSelect(lesson.id);
          if (hasChildren) onToggle(lesson.id);
        }}
        className={`flex w-full items-center gap-2 rounded-lg px-3 py-2 text-sm transition-colors ${
          isSelected
            ? "bg-purple-500/15 text-purple-300"
            : "text-slate-300 hover:bg-slate-800/60 hover:text-slate-100"
        }`}
        style={{ paddingLeft: `${12 + depth * 16}px` }}
      >
        {hasChildren ? (
          isExpanded ? (
            <ChevronDown className="h-3.5 w-3.5 shrink-0 text-slate-500" />
          ) : (
            <ChevronRight className="h-3.5 w-3.5 shrink-0 text-slate-500" />
          )
        ) : (
          <span className="w-3.5" />
        )}

        {isCompleted ? (
          <CheckCircle2 className="h-4 w-4 shrink-0 text-green-400" />
        ) : (
          <span className="shrink-0">{lessonTypeIcon[lesson.type]}</span>
        )}

        <span className="truncate">Lesson {lesson.orderIndex + 1}</span>
      </button>

      {hasChildren && isExpanded && (
        <div>
          {children.map((child) => (
            <LessonTreeItem
              key={child.id}
              lesson={child}
              depth={depth + 1}
              selectedId={selectedId}
              expandedNodes={expandedNodes}
              completedLessons={completedLessons}
              onSelect={onSelect}
              onToggle={onToggle}
            />
          ))}
        </div>
      )}
    </div>
  );
}

/* ---------- F3.3: Lesson Renderers ---------- */

function LessonRenderer({ lesson }: { lesson: LessonNode }) {
  switch (lesson.type) {
    case "TEXT":
      return (
        <div
          className="prose prose-invert prose-purple max-w-none prose-headings:text-slate-100 prose-p:text-slate-300 prose-a:text-purple-400"
          dangerouslySetInnerHTML={{ __html: lesson.textContent ?? "<p>No content available.</p>" }}
        />
      );

    case "VIDEO":
      return lesson.videoUrl ? (
        <div className="space-y-4">
          <div className="relative aspect-video w-full overflow-hidden rounded-lg border border-slate-700 bg-black">
            <iframe
              src={lesson.videoUrl}
              title="Lesson video"
              className="absolute inset-0 h-full w-full"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            />
          </div>
        </div>
      ) : (
        <div className="flex flex-col items-center py-12 text-slate-400">
          <Play className="mb-2 h-10 w-10" />
          <p>Video not available.</p>
        </div>
      );

    case "DOCUMENT":
      return lesson.documentUrl ? (
        <div className="space-y-4">
          <div className="relative aspect-[8.5/11] w-full overflow-hidden rounded-lg border border-slate-700 bg-white">
            <iframe
              src={lesson.documentUrl}
              title="Lesson document"
              className="absolute inset-0 h-full w-full"
            />
          </div>
          <a
            href={lesson.documentUrl}
            target="_blank"
            rel="noopener noreferrer"
            className="inline-flex items-center gap-2 rounded-lg border border-slate-700 bg-slate-800/50 px-4 py-2 text-sm text-slate-300 hover:border-purple-500/30 hover:text-purple-300 transition-colors"
          >
            <FileText className="h-4 w-4" />
            Open Document
          </a>
        </div>
      ) : (
        <div className="flex flex-col items-center py-12 text-slate-400">
          <FileText className="mb-2 h-10 w-10" />
          <p>Document not available.</p>
        </div>
      );

    default:
      return <p className="text-slate-400">Unknown lesson type.</p>;
  }
}

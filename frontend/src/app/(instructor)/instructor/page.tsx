"use client";

import React, { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { apiGet } from "@/lib/api/client";
import { useAuthStore } from "@/store/auth-store";
import { Button } from "@/components/ui/button";
import {
  BookOpen,
  ClipboardList,
  Search,
  Timer,
  Trophy,
  Shuffle,
  FileText,
  Loader2,
  ChevronRight,
  AlertCircle,
  X,
} from "lucide-react";

/* ---------- Types ---------- */

interface CourseDto {
  id: number;
  titleEn: string;
  descriptionEn: string;
  difficulty: string;
  estimatedDurationMinutes: number;
  status: string;
}

interface CoursePage {
  content: CourseDto[];
  totalElements: number;
  totalPages: number;
}

interface ExamDto {
  id: string;
  courseId: number;
  title: string;
  description: string;
  instructions: string;
  rulesSummary: string;
  passPercentage: number;
  maxAttempts: number;
  timeLimitMinutes: number;
  shuffleQuestions: boolean;
  shuffleOptions: boolean;
  status: string;
}

/* ---------- Main Page ---------- */

export default function InstructorPage() {
  const { user } = useAuthStore();
  const greeting = user?.email?.split("@")[0] ?? "Instructor";

  const [selectedCourseId, setSelectedCourseId] = useState<number | null>(null);
  const [searchQuery, setSearchQuery] = useState("");

  // Fetch published courses
  const {
    data: coursesData,
    isLoading: coursesLoading,
    isError: coursesError,
  } = useQuery({
    queryKey: ["instructor-courses"],
    queryFn: () =>
      apiGet<CoursePage>("/api/public/courses/search?page=0&size=20"),
  });

  const courses = coursesData?.content ?? [];

  const filteredCourses = searchQuery.trim()
    ? courses.filter((c) =>
        c.titleEn.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : courses;

  // Fetch exam for selected course
  const {
    data: examData,
    isLoading: examLoading,
    isError: examError,
    error: examErrorObj,
  } = useQuery({
    queryKey: ["instructor-exam", selectedCourseId],
    queryFn: () =>
      apiGet<ExamDto>(`/api/public/exams/course/${selectedCourseId}`),
    enabled: selectedCourseId !== null,
  });

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-950 via-purple-950/30 to-slate-950">
      {/* Decorative blurs */}
      <div className="pointer-events-none fixed left-[10%] top-16 h-80 w-80 rounded-full bg-purple-500/20 blur-3xl" />
      <div className="pointer-events-none fixed right-[10%] top-10 h-72 w-72 rounded-full bg-pink-500/15 blur-3xl" />

      <div className="relative mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold bg-gradient-to-r from-purple-200 via-pink-200 to-purple-200 bg-clip-text text-transparent">
            Welcome back, {greeting}
          </h1>
          <p className="mt-1 text-slate-400">
            Manage your courses and review exam configurations.
          </p>
        </div>

        {/* Stats Row */}
        <div className="mb-8 grid gap-4 sm:grid-cols-3">
          <StatCard
            icon={<BookOpen className="h-5 w-5 text-blue-400" />}
            label="Available Courses"
            value={coursesData?.totalElements ?? 0}
            isLoading={coursesLoading}
          />
          <StatCard
            icon={<ClipboardList className="h-5 w-5 text-purple-400" />}
            label="Courses Shown"
            value={filteredCourses.length}
            isLoading={coursesLoading}
          />
          <StatCard
            icon={<Trophy className="h-5 w-5 text-amber-400" />}
            label="Exam Selected"
            value={selectedCourseId !== null ? "Yes" : "None"}
            isLoading={false}
          />
        </div>

        {/* My Courses Section */}
        <section className="mb-8">
          <div className="mb-4 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
            <h2 className="text-xl font-semibold text-slate-100">
              My Courses
            </h2>
            <div className="relative">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-500" />
              <input
                type="text"
                placeholder="Search courses..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full rounded-lg border border-slate-800 bg-slate-900/60 py-2 pl-9 pr-4 text-sm text-slate-200 placeholder:text-slate-500 focus:border-purple-500/50 focus:outline-none focus:ring-1 focus:ring-purple-500/30 sm:w-64"
              />
            </div>
          </div>

          {coursesError && (
            <div className="mb-6 rounded-xl border border-red-500/30 bg-red-500/5 p-4 text-center text-red-300">
              <AlertCircle className="mr-2 inline h-4 w-4" />
              Failed to load courses. Please refresh the page.
            </div>
          )}

          {coursesLoading && (
            <div className="flex items-center justify-center rounded-xl border border-slate-800 bg-slate-900/50 py-16">
              <Loader2 className="h-8 w-8 animate-spin text-purple-400" />
            </div>
          )}

          {!coursesLoading && !coursesError && filteredCourses.length === 0 && (
            <div className="rounded-xl border border-slate-800 bg-slate-900/50 px-6 py-12 text-center text-slate-400">
              No courses found.
            </div>
          )}

          {!coursesLoading && !coursesError && filteredCourses.length > 0 && (
            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
              {filteredCourses.map((course) => (
                <CourseCard
                  key={course.id}
                  course={course}
                  isSelected={selectedCourseId === course.id}
                  onViewExam={() => setSelectedCourseId(course.id)}
                />
              ))}
            </div>
          )}
        </section>

        {/* Exam Details Section */}
        {selectedCourseId !== null && (
          <section>
            <div className="mb-4 flex items-center justify-between">
              <h2 className="text-xl font-semibold text-slate-100">
                Exam Details
              </h2>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => setSelectedCourseId(null)}
                className="text-slate-400 hover:text-slate-200"
              >
                <X className="mr-1 h-4 w-4" />
                Clear
              </Button>
            </div>

            {examLoading && (
              <div className="flex items-center justify-center rounded-xl border border-slate-800 bg-slate-900/50 py-16">
                <Loader2 className="h-8 w-8 animate-spin text-purple-400" />
              </div>
            )}

            {examError && (
              <div className="rounded-xl border border-red-500/30 bg-red-500/5 p-4 text-center text-red-300">
                <AlertCircle className="mr-2 inline h-4 w-4" />
                {(examErrorObj as { message?: string })?.message ??
                  "No exam found for this course."}
              </div>
            )}

            {!examLoading && !examError && examData && (
              <ExamDetailCard exam={examData} />
            )}
          </section>
        )}
      </div>
    </div>
  );
}

/* ---------- Course Card ---------- */

function CourseCard({
  course,
  isSelected,
  onViewExam,
}: {
  course: CourseDto;
  isSelected: boolean;
  onViewExam: () => void;
}) {
  const difficultyColor: Record<string, string> = {
    BEGINNER: "text-green-400 bg-green-400/10",
    INTERMEDIATE: "text-amber-400 bg-amber-400/10",
    ADVANCED: "text-red-400 bg-red-400/10",
  };

  return (
    <div
      className={`group rounded-xl border p-5 transition-all ${
        isSelected
          ? "border-purple-500/50 bg-purple-500/5 shadow-lg shadow-purple-500/10"
          : "border-slate-800 bg-slate-900/60 hover:border-slate-700"
      }`}
    >
      <div className="mb-3 flex items-start justify-between">
        <h3 className="text-base font-semibold text-slate-100 line-clamp-2">
          {course.titleEn}
        </h3>
        <span
          className={`ml-2 shrink-0 rounded-full px-2 py-0.5 text-xs font-medium ${
            difficultyColor[course.difficulty] ?? "text-slate-400 bg-slate-400/10"
          }`}
        >
          {course.difficulty}
        </span>
      </div>

      <p className="mb-4 text-sm text-slate-400 line-clamp-2">
        {course.descriptionEn}
      </p>

      <div className="mb-4 flex items-center gap-4 text-xs text-slate-500">
        <span className="flex items-center gap-1">
          <Timer className="h-3.5 w-3.5" />
          {course.estimatedDurationMinutes} min
        </span>
        <span className="flex items-center gap-1">
          <FileText className="h-3.5 w-3.5" />
          {course.status}
        </span>
      </div>

      <Button
        onClick={onViewExam}
        size="sm"
        className={`w-full ${
          isSelected
            ? "bg-purple-600 text-white hover:bg-purple-500"
            : "bg-slate-800 text-slate-300 hover:bg-slate-700 hover:text-white"
        }`}
      >
        <ClipboardList className="mr-1.5 h-4 w-4" />
        View Exam
        <ChevronRight className="ml-auto h-4 w-4" />
      </Button>
    </div>
  );
}

/* ---------- Exam Detail Card ---------- */

function ExamDetailCard({ exam }: { exam: ExamDto }) {
  const statusColor: Record<string, string> = {
    DRAFT: "text-amber-400 bg-amber-400/10 border-amber-400/20",
    PUBLISHED: "text-green-400 bg-green-400/10 border-green-400/20",
    ARCHIVED: "text-slate-400 bg-slate-400/10 border-slate-400/20",
  };

  return (
    <div className="rounded-xl border border-slate-800 bg-slate-900/60 p-6">
      {/* Header */}
      <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h3 className="text-lg font-semibold text-slate-100">{exam.title}</h3>
          <p className="mt-1 text-sm text-slate-400">{exam.description}</p>
        </div>
        <span
          className={`inline-flex shrink-0 items-center rounded-full border px-3 py-1 text-xs font-medium ${
            statusColor[exam.status] ?? "text-slate-400 bg-slate-400/10 border-slate-400/20"
          }`}
        >
          {exam.status}
        </span>
      </div>

      {/* Info Grid */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        <InfoField label="Exam ID" value={exam.id} mono />
        <InfoField label="Course ID" value={String(exam.courseId)} mono />
        <InfoField
          label="Pass Percentage"
          value={`${exam.passPercentage}%`}
          icon={<Trophy className="h-4 w-4 text-amber-400" />}
        />
        <InfoField
          label="Max Attempts"
          value={String(exam.maxAttempts)}
          icon={<ClipboardList className="h-4 w-4 text-blue-400" />}
        />
        <InfoField
          label="Time Limit"
          value={`${exam.timeLimitMinutes} minutes`}
          icon={<Timer className="h-4 w-4 text-purple-400" />}
        />
        <InfoField
          label="Shuffle Questions"
          value={exam.shuffleQuestions ? "Yes" : "No"}
          icon={<Shuffle className="h-4 w-4 text-pink-400" />}
        />
        <InfoField
          label="Shuffle Options"
          value={exam.shuffleOptions ? "Yes" : "No"}
          icon={<Shuffle className="h-4 w-4 text-pink-400" />}
        />
      </div>

      {/* Instructions & Rules */}
      {exam.instructions && (
        <div className="mt-6">
          <h4 className="mb-2 text-sm font-medium text-slate-300">
            Instructions
          </h4>
          <div className="rounded-lg border border-slate-800 bg-slate-950/50 p-4 text-sm text-slate-400">
            {exam.instructions}
          </div>
        </div>
      )}

      {exam.rulesSummary && (
        <div className="mt-4">
          <h4 className="mb-2 text-sm font-medium text-slate-300">
            Rules Summary
          </h4>
          <div className="rounded-lg border border-slate-800 bg-slate-950/50 p-4 text-sm text-slate-400">
            {exam.rulesSummary}
          </div>
        </div>
      )}
    </div>
  );
}

/* ---------- Info Field ---------- */

function InfoField({
  label,
  value,
  icon,
  mono,
}: {
  label: string;
  value: string;
  icon?: React.ReactNode;
  mono?: boolean;
}) {
  return (
    <div className="rounded-lg border border-slate-800/60 bg-slate-950/40 p-3">
      <p className="mb-1 text-xs text-slate-500">{label}</p>
      <div className="flex items-center gap-2">
        {icon}
        <p
          className={`text-sm font-medium text-slate-200 ${
            mono ? "font-mono text-xs break-all" : ""
          }`}
        >
          {value}
        </p>
      </div>
    </div>
  );
}

/* ---------- Stat Card ---------- */

function StatCard({
  icon,
  label,
  value,
  isLoading,
}: {
  icon: React.ReactNode;
  label: string;
  value: number | string;
  isLoading: boolean;
}) {
  return (
    <div className="flex items-center gap-4 rounded-xl border border-slate-800 bg-slate-900/60 p-4">
      <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-white/5">
        {icon}
      </div>
      <div>
        <p className="text-sm text-slate-400">{label}</p>
        {isLoading ? (
          <div className="mt-1 h-7 w-10 animate-pulse rounded bg-slate-800" />
        ) : (
          <p className="text-2xl font-bold text-slate-100">{value}</p>
        )}
      </div>
    </div>
  );
}

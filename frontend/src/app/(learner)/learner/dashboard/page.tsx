"use client";

import React, { useState, useMemo } from "react";
import { useQuery } from "@tanstack/react-query";
import Link from "next/link";
import EnrollmentCard from "@/components/learner/enrollment-card";
import { apiGet } from "@/lib/api/client";
import { useAuthUser } from "@/store/auth-store";
import {
  BookOpen,
  CheckCircle2,
  TrendingUp,
  Search,
  GraduationCap,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import type { EnrollmentStatus } from "@/types/enums";

interface EnrollmentResponse {
  id: number;
  status: EnrollmentStatus;
  enrolledAt: string;
  completedAt: string | null;
  lastAccessedAt: string | null;
  course: {
    id: number;
    titleEn: string;
    descriptionEn: string;
    difficulty: string;
    estimatedDurationMinutes: number;
  };
  user: {
    id: number;
    email: string;
  };
}

type FilterTab = "ALL" | "ACTIVE" | "COMPLETED" | "DROPPED";

export default function DashboardPage() {
  const user = useAuthUser();
  const [activeTab, setActiveTab] = useState<FilterTab>("ALL");
  const [searchQuery, setSearchQuery] = useState("");

  const { data, isLoading, isError } = useQuery({
    queryKey: ["learner-enrollments"],
    queryFn: () =>
      apiGet<{ content: EnrollmentResponse[] }>("/api/learner/enrollments?page=0&size=100"),
  });

  const enrollments = data?.content ?? [];

  const stats = useMemo(() => {
    const active = enrollments.filter((e) => e.status === "ACTIVE").length;
    const completed = enrollments.filter((e) => e.status === "COMPLETED").length;
    return { total: enrollments.length, active, completed };
  }, [enrollments]);

  const filtered = useMemo(() => {
    let result = enrollments;
    if (activeTab !== "ALL") {
      result = result.filter((e) => e.status === activeTab);
    }
    if (searchQuery.trim()) {
      const q = searchQuery.toLowerCase();
      result = result.filter((e) =>
        e.course.titleEn.toLowerCase().includes(q)
      );
    }
    return result;
  }, [enrollments, activeTab, searchQuery]);

  const tabs: { key: FilterTab; label: string; count: number }[] = [
    { key: "ALL", label: "All Courses", count: stats.total },
    { key: "ACTIVE", label: "In Progress", count: stats.active },
    { key: "COMPLETED", label: "Completed", count: stats.completed },
    { key: "DROPPED", label: "Dropped", count: enrollments.filter((e) => e.status === "DROPPED").length },
  ];

  const greeting = user?.email?.split("@")[0] ?? "Learner";

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-950 via-purple-950/30 to-slate-950">
      <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold bg-gradient-to-r from-purple-200 via-pink-200 to-purple-200 bg-clip-text text-transparent">
            Welcome back, {greeting}
          </h1>
          <p className="mt-1 text-slate-400">
            Track your learning progress and continue where you left off.
          </p>
        </div>

        {/* Stats Cards */}
        <div className="mb-8 grid gap-4 sm:grid-cols-3">
          <div className="flex items-center gap-4 rounded-xl border border-slate-800 bg-slate-900/60 p-4">
            <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-blue-500/10">
              <BookOpen className="h-5 w-5 text-blue-400" />
            </div>
            <div>
              <p className="text-sm text-slate-400">Enrolled Courses</p>
              {isLoading ? (
                <div className="mt-1 h-7 w-10 animate-pulse rounded bg-slate-800" />
              ) : (
                <p className="text-2xl font-bold text-slate-100">{stats.total}</p>
              )}
            </div>
          </div>

          <div className="flex items-center gap-4 rounded-xl border border-slate-800 bg-slate-900/60 p-4">
            <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-amber-500/10">
              <TrendingUp className="h-5 w-5 text-amber-400" />
            </div>
            <div>
              <p className="text-sm text-slate-400">In Progress</p>
              {isLoading ? (
                <div className="mt-1 h-7 w-10 animate-pulse rounded bg-slate-800" />
              ) : (
                <p className="text-2xl font-bold text-slate-100">{stats.active}</p>
              )}
            </div>
          </div>

          <div className="flex items-center gap-4 rounded-xl border border-slate-800 bg-slate-900/60 p-4">
            <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-green-500/10">
              <CheckCircle2 className="h-5 w-5 text-green-400" />
            </div>
            <div>
              <p className="text-sm text-slate-400">Completed</p>
              {isLoading ? (
                <div className="mt-1 h-7 w-10 animate-pulse rounded bg-slate-800" />
              ) : (
                <p className="text-2xl font-bold text-slate-100">{stats.completed}</p>
              )}
            </div>
          </div>
        </div>

        {/* Error state */}
        {isError && (
          <div className="mb-6 rounded-xl border border-red-500/30 bg-red-500/5 p-4 text-center text-red-300">
            Failed to load your enrollments. Please refresh the page to try again.
          </div>
        )}

        {/* Loading skeleton */}
        {isLoading && (
          <div className="grid gap-6 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
            {[1, 2, 3].map((i) => (
              <div
                key={i}
                className="h-64 animate-pulse rounded-xl border border-slate-800 bg-slate-900/50"
              />
            ))}
          </div>
        )}

        {/* Content */}
        {!isLoading && !isError && (
          <>
            {enrollments.length === 0 ? (
              <div className="flex flex-col items-center justify-center rounded-2xl border border-slate-800 bg-slate-900/50 px-6 py-16 text-center">
                <div className="mb-4 flex h-16 w-16 items-center justify-center rounded-2xl bg-purple-500/10">
                  <GraduationCap className="h-8 w-8 text-purple-400" />
                </div>
                <h2 className="mb-2 text-xl font-semibold text-slate-200">
                  No courses yet
                </h2>
                <p className="mb-6 max-w-sm text-slate-400">
                  Start your learning journey by exploring our course catalog
                  and enrolling in a course.
                </p>
                <Link href="/catalog">
                  <Button className="bg-gradient-to-r from-purple-600 to-pink-500 text-white shadow-lg shadow-purple-500/20 hover:from-purple-500 hover:to-pink-400">
                    Browse Courses
                  </Button>
                </Link>
              </div>
            ) : (
              <>
                {/* Filter & Search Bar */}
                <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
                  <div className="flex gap-1 rounded-lg border border-slate-800 bg-slate-900/60 p-1">
                    {tabs.map((tab) => (
                      <button
                        key={tab.key}
                        onClick={() => setActiveTab(tab.key)}
                        className={`rounded-md px-3 py-1.5 text-sm font-medium transition-all ${
                          activeTab === tab.key
                            ? "bg-purple-500/20 text-purple-300 shadow-sm"
                            : "text-slate-400 hover:text-slate-200"
                        }`}
                      >
                        {tab.label}
                        <span className="ml-1.5 text-xs opacity-60">
                          {tab.count}
                        </span>
                      </button>
                    ))}
                  </div>

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

                {/* Course Grid */}
                {filtered.length === 0 ? (
                  <div className="rounded-xl border border-slate-800 bg-slate-900/50 px-6 py-12 text-center text-slate-400">
                    No courses match your filter.
                  </div>
                ) : (
                  <div className="grid gap-6 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
                    {filtered.map((enrollment) => (
                      <EnrollmentCard
                        key={enrollment.id}
                        enrollmentId={enrollment.id}
                        courseId={enrollment.course.id}
                        title={enrollment.course.titleEn}
                        description={enrollment.course.descriptionEn}
                        difficulty={enrollment.course.difficulty}
                        estimatedDurationMinutes={enrollment.course.estimatedDurationMinutes}
                        progressPercentage={0}
                        status={enrollment.status}
                        enrolledAt={enrollment.enrolledAt}
                      />
                    ))}
                  </div>
                )}
              </>
            )}
          </>
        )}
      </div>
    </div>
  );
}

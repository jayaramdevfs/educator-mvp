"use client";

import { useQuery } from "@tanstack/react-query";
import { apiGet } from "@/lib/api/client";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import {
  BookOpen,
  Users,
  GraduationCap,
  CreditCard,
  Layout,
  Shield,
  TrendingUp,
  Award,
} from "lucide-react";

interface AdminStats {
  totalUsers: number;
  totalStudents: number;
  totalInstructors: number;
  totalAdmins: number;
  totalCourses: number;
  totalPublishedExams: number;
  activeEnrollments: number;
  totalCompletions: number;
  totalCertificates: number;
}

const statCards = [
  {
    key: "totalCourses" as const,
    label: "Total Courses",
    icon: BookOpen,
    gradient: "from-purple-500 to-indigo-600",
  },
  {
    key: "totalStudents" as const,
    label: "Total Students",
    icon: Users,
    gradient: "from-pink-500 to-rose-600",
  },
  {
    key: "totalPublishedExams" as const,
    label: "Published Exams",
    icon: GraduationCap,
    gradient: "from-cyan-500 to-blue-600",
  },
  {
    key: "activeEnrollments" as const,
    label: "Active Enrollments",
    icon: TrendingUp,
    gradient: "from-emerald-500 to-green-600",
  },
  {
    key: "totalCompletions" as const,
    label: "Completions",
    icon: Award,
    gradient: "from-amber-500 to-orange-600",
  },
  {
    key: "totalCertificates" as const,
    label: "Certificates",
    icon: Shield,
    gradient: "from-violet-500 to-purple-600",
  },
];

const quickLinks = [
  {
    label: "Manage Courses",
    href: "/admin/courses",
    icon: BookOpen,
    description: "Create, publish, and manage courses",
  },
  {
    label: "Manage Exams",
    href: "/admin/exams",
    icon: GraduationCap,
    description: "Create and manage exams",
  },
  {
    label: "Homepage CMS",
    href: "/admin/homepage",
    icon: Layout,
    description: "Manage homepage sections and blocks",
  },
  {
    label: "Subscription Plans",
    href: "/admin/subscriptions",
    icon: CreditCard,
    description: "Manage subscription plans and pricing",
  },
  {
    label: "Users",
    href: "/admin/users",
    icon: Users,
    description: "View and manage user accounts",
  },
];

export default function AdminPage() {
  const { data: stats, isLoading, isError } = useQuery<AdminStats>({
    queryKey: ["admin", "stats"],
    queryFn: () => apiGet<AdminStats>("/api/admin/stats"),
    retry: 1,
  });

  return (
    <main className="relative min-h-screen overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6 py-10">
      {/* Decorative blurs */}
      <div className="pointer-events-none absolute left-[8%] top-16 h-80 w-80 rounded-full bg-purple-500/20 blur-3xl" />
      <div className="pointer-events-none absolute right-[12%] top-10 h-72 w-72 rounded-full bg-pink-500/15 blur-3xl" />

      <div className="relative mx-auto max-w-7xl space-y-8">
        {/* Header */}
        <div className="rounded-3xl border border-slate-800 bg-slate-900/60 p-8 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
          <h1 className="bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-3xl font-semibold tracking-tight text-transparent">
            Admin Dashboard
          </h1>
          <p className="mt-3 text-slate-400">
            Overview of your platform metrics and quick access to management tools.
          </p>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {statCards.map((card) => {
            const Icon = card.icon;
            const value = stats?.[card.key] ?? 0;

            return (
              <div
                key={card.key}
                className="group relative overflow-hidden rounded-2xl border border-slate-800 bg-slate-900/60 p-6 backdrop-blur-xl transition-all hover:border-slate-700 hover:shadow-lg hover:shadow-purple-500/5"
              >
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-slate-400">
                      {card.label}
                    </p>
                    <p className="mt-2 text-3xl font-bold text-slate-100">
                      {isLoading ? (
                        <span className="inline-block h-8 w-16 animate-pulse rounded bg-slate-800" />
                      ) : isError ? (
                        <span className="text-lg text-slate-500">--</span>
                      ) : (
                        value.toLocaleString()
                      )}
                    </p>
                  </div>
                  <div
                    className={`flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br ${card.gradient} shadow-lg`}
                  >
                    <Icon className="h-6 w-6 text-white" />
                  </div>
                </div>
              </div>
            );
          })}
        </div>

        {/* Quick Links */}
        <div>
          <h2 className="mb-4 text-xl font-semibold text-slate-200">
            Quick Actions
          </h2>
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {quickLinks.map((link) => {
              const Icon = link.icon;
              return (
                <Link key={link.href} href={link.href} className="group">
                  <div className="flex h-full items-center gap-4 rounded-2xl border border-slate-800 bg-slate-900/60 p-6 backdrop-blur-xl transition-all hover:border-purple-700/50 hover:bg-slate-900/80 hover:shadow-lg hover:shadow-purple-500/5">
                    <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-xl bg-gradient-to-br from-purple-500/20 to-pink-500/20 transition-colors group-hover:from-purple-500/30 group-hover:to-pink-500/30">
                      <Icon className="h-6 w-6 text-purple-300" />
                    </div>
                    <div>
                      <p className="font-semibold text-slate-200 group-hover:text-purple-200">
                        {link.label}
                      </p>
                      <p className="mt-1 text-sm text-slate-500">
                        {link.description}
                      </p>
                    </div>
                  </div>
                </Link>
              );
            })}
          </div>
        </div>
      </div>
    </main>
  );
}

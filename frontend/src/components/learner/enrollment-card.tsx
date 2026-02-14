"use client";

import React from "react";
import Link from "next/link";
import { ProgressBar } from "./progress-bar";
import { Button } from "@/components/ui/button";
import { BookOpen, Clock, BarChart3, CheckCircle2, XCircle } from "lucide-react";
import type { EnrollmentStatus } from "@/types/enums";

interface EnrollmentCardProps {
  enrollmentId: number | string;
  courseId: number | string;
  title: string;
  description?: string;
  difficulty: string;
  estimatedDurationMinutes?: number;
  progressPercentage: number;
  status: EnrollmentStatus;
  enrolledAt?: string;
}

const difficultyConfig: Record<string, { color: string; label: string }> = {
  BEGINNER: { color: "border-green-500/30 bg-green-500/10 text-green-300", label: "Beginner" },
  INTERMEDIATE: { color: "border-amber-500/30 bg-amber-500/10 text-amber-300", label: "Intermediate" },
  ADVANCED: { color: "border-red-500/30 bg-red-500/10 text-red-300", label: "Advanced" },
};

const statusConfig: Record<EnrollmentStatus, { color: string; icon: React.ReactNode }> = {
  ACTIVE: { color: "border-blue-500/30 bg-blue-500/10 text-blue-300", icon: <BookOpen className="h-3 w-3" /> },
  COMPLETED: { color: "border-green-500/30 bg-green-500/10 text-green-300", icon: <CheckCircle2 className="h-3 w-3" /> },
  DROPPED: { color: "border-red-500/30 bg-red-500/10 text-red-300", icon: <XCircle className="h-3 w-3" /> },
};

const EnrollmentCard: React.FC<EnrollmentCardProps> = ({
  courseId,
  title,
  description,
  difficulty,
  estimatedDurationMinutes,
  progressPercentage,
  status,
  enrolledAt,
}) => {
  const diffStyle = difficultyConfig[difficulty] ?? difficultyConfig.BEGINNER;
  const statusStyle = statusConfig[status] ?? statusConfig.ACTIVE;

  const buttonLabel =
    status === "COMPLETED"
      ? "Review Course"
      : status === "DROPPED"
        ? "View Details"
        : progressPercentage > 0
          ? "Resume Learning"
          : "Start Learning";

  return (
    <div className="group relative flex flex-col rounded-xl border border-slate-800 bg-slate-900/80 p-5 shadow-sm transition-all duration-300 hover:border-purple-500/30 hover:shadow-lg hover:shadow-purple-500/5">
      {/* Status & Difficulty Badges */}
      <div className="mb-3 flex flex-wrap items-center gap-2">
        <span className={`inline-flex items-center gap-1 rounded-full border px-2.5 py-0.5 text-xs font-medium ${statusStyle.color}`}>
          {statusStyle.icon}
          {status}
        </span>
        <span className={`rounded-full border px-2.5 py-0.5 text-xs font-medium ${diffStyle.color}`}>
          {diffStyle.label}
        </span>
      </div>

      {/* Title */}
      <h3 className="mb-2 text-lg font-semibold text-slate-100 line-clamp-2 group-hover:text-purple-200 transition-colors">
        {title}
      </h3>

      {/* Description */}
      {description && (
        <p className="mb-3 text-sm text-slate-400 line-clamp-2">{description}</p>
      )}

      {/* Meta info */}
      <div className="mb-4 flex flex-wrap gap-3 text-xs text-slate-500">
        {estimatedDurationMinutes != null && (
          <span className="flex items-center gap-1">
            <Clock className="h-3 w-3" />
            {estimatedDurationMinutes} min
          </span>
        )}
        {enrolledAt && (
          <span className="flex items-center gap-1">
            <BarChart3 className="h-3 w-3" />
            Enrolled {new Date(enrolledAt).toLocaleDateString()}
          </span>
        )}
      </div>

      {/* Progress */}
      <div className="mb-5 mt-auto">
        <ProgressBar value={progressPercentage} />
      </div>

      {/* Action Button */}
      <Link href={`/learner/courses/${courseId}`}>
        <Button className="w-full bg-gradient-to-r from-purple-600 to-pink-500 text-white shadow-md shadow-purple-500/20 hover:from-purple-500 hover:to-pink-400 transition-all">
          {buttonLabel}
        </Button>
      </Link>
    </div>
  );
};

export default EnrollmentCard;

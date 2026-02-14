"use client";

import React from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { apiGet, apiPut } from "@/lib/api/client";
import {
  Bell,
  CheckCircle2,
  XCircle,
  Trophy,
  Award,
  Mail,
  MailOpen,
  Loader2,
} from "lucide-react";
import { Button } from "@/components/ui/button";

/* ---------- Types ---------- */

interface Notification {
  id: string;
  userId: string;
  type: "COURSE_COMPLETED" | "EXAM_PASSED" | "EXAM_FAILED" | "CERTIFICATE_ELIGIBLE";
  title: string;
  message: string;
  status: string;
  read: boolean;
  createdAt: string;
}

interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
}

const typeConfig: Record<string, { icon: React.ReactNode; color: string }> = {
  COURSE_COMPLETED: {
    icon: <CheckCircle2 className="h-5 w-5" />,
    color: "text-green-400 bg-green-500/10 border-green-500/30",
  },
  EXAM_PASSED: {
    icon: <Trophy className="h-5 w-5" />,
    color: "text-amber-400 bg-amber-500/10 border-amber-500/30",
  },
  EXAM_FAILED: {
    icon: <XCircle className="h-5 w-5" />,
    color: "text-red-400 bg-red-500/10 border-red-500/30",
  },
  CERTIFICATE_ELIGIBLE: {
    icon: <Award className="h-5 w-5" />,
    color: "text-purple-400 bg-purple-500/10 border-purple-500/30",
  },
};

export default function NotificationsPage() {
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: ["notifications"],
    queryFn: () =>
      apiGet<PaginatedResponse<Notification>>(
        "/api/learner/notifications?page=0&size=50"
      ),
  });

  const markReadMutation = useMutation({
    mutationFn: (id: string) =>
      apiPut<Notification>(`/api/learner/notifications/${id}/read`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["notifications"] });
      queryClient.invalidateQueries({ queryKey: ["unread-count"] });
    },
  });

  const notifications = data?.content ?? [];

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-950 via-purple-950/20 to-slate-950">
      <div className="mx-auto max-w-3xl px-4 py-8 sm:px-6">
        <div className="mb-8 flex items-center gap-3">
          <Bell className="h-6 w-6 text-purple-400" />
          <h1 className="text-2xl font-bold text-slate-100">Notifications</h1>
        </div>

        {isLoading ? (
          <div className="flex justify-center py-12">
            <Loader2 className="h-8 w-8 animate-spin text-purple-400" />
          </div>
        ) : notifications.length === 0 ? (
          <div className="rounded-xl border border-slate-800 bg-slate-900/60 px-6 py-16 text-center">
            <Bell className="mx-auto mb-3 h-10 w-10 text-slate-600" />
            <p className="text-slate-400">No notifications yet.</p>
          </div>
        ) : (
          <div className="space-y-3">
            {notifications.map((notification) => {
              const config = typeConfig[notification.type] ?? typeConfig.COURSE_COMPLETED;
              return (
                <div
                  key={notification.id}
                  className={`flex items-start gap-4 rounded-xl border p-4 transition-all ${
                    notification.read
                      ? "border-slate-800 bg-slate-900/40"
                      : "border-purple-500/20 bg-slate-900/70"
                  }`}
                >
                  <div
                    className={`flex h-10 w-10 shrink-0 items-center justify-center rounded-lg border ${config.color}`}
                  >
                    {config.icon}
                  </div>

                  <div className="flex-1 min-w-0">
                    <div className="flex items-start justify-between gap-2">
                      <h3
                        className={`text-sm font-medium ${
                          notification.read ? "text-slate-300" : "text-slate-100"
                        }`}
                      >
                        {notification.title}
                      </h3>
                      {!notification.read && (
                        <span className="mt-0.5 h-2 w-2 shrink-0 rounded-full bg-purple-400" />
                      )}
                    </div>
                    <p className="mt-1 text-sm text-slate-400">{notification.message}</p>
                    <div className="mt-2 flex items-center gap-3">
                      <span className="text-xs text-slate-500">
                        {new Date(notification.createdAt).toLocaleString()}
                      </span>
                      {!notification.read && (
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => markReadMutation.mutate(notification.id)}
                          disabled={markReadMutation.isPending}
                          className="h-auto px-2 py-1 text-xs text-purple-400 hover:text-purple-300"
                        >
                          <MailOpen className="mr-1 h-3 w-3" />
                          Mark read
                        </Button>
                      )}
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}

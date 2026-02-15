"use client";

import React, { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import Link from "next/link";
import { apiGet, apiPut } from "@/lib/api/client";
import { useAuthStore } from "@/store/auth-store";
import { Bell, CheckCircle2, XCircle, Trophy, Award } from "lucide-react";
import { AnimatePresence, motion } from "framer-motion";

interface Notification {
  id: string;
  type: "COURSE_COMPLETED" | "EXAM_PASSED" | "EXAM_FAILED" | "CERTIFICATE_ELIGIBLE";
  title: string;
  message: string;
  read: boolean;
  createdAt: string;
}

interface PaginatedResponse<T> {
  content: T[];
}

const typeIcon: Record<string, React.ReactNode> = {
  COURSE_COMPLETED: <CheckCircle2 className="h-4 w-4 text-green-400" />,
  EXAM_PASSED: <Trophy className="h-4 w-4 text-amber-400" />,
  EXAM_FAILED: <XCircle className="h-4 w-4 text-red-400" />,
  CERTIFICATE_ELIGIBLE: <Award className="h-4 w-4 text-purple-400" />,
};

export function NotificationBell() {
  const [isOpen, setIsOpen] = useState(false);
  const queryClient = useQueryClient();
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

  const { data: unreadData } = useQuery({
    queryKey: ["unread-count"],
    queryFn: () => apiGet<{ unreadCount: number }>("/api/learner/notifications/unread-count"),
    refetchInterval: 30_000,
    enabled: isAuthenticated,
    retry: false,
  });

  const { data: notifData } = useQuery({
    queryKey: ["notifications-preview"],
    queryFn: () =>
      apiGet<PaginatedResponse<Notification>>(
        "/api/learner/notifications?page=0&size=5"
      ),
    enabled: isOpen,
  });

  const markReadMutation = useMutation({
    mutationFn: (id: string) =>
      apiPut<Notification>(`/api/learner/notifications/${id}/read`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["unread-count"] });
      queryClient.invalidateQueries({ queryKey: ["notifications-preview"] });
      queryClient.invalidateQueries({ queryKey: ["notifications"] });
    },
  });

  const unreadCount = unreadData?.unreadCount ?? 0;
  const notifications = notifData?.content ?? [];

  return (
    <div className="relative">
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="relative flex h-9 w-9 items-center justify-center rounded-lg text-slate-300 transition-colors hover:bg-slate-800/60 hover:text-purple-300"
      >
        <Bell className="h-5 w-5" />
        {unreadCount > 0 && (
          <span className="absolute -right-0.5 -top-0.5 flex h-4 min-w-4 items-center justify-center rounded-full bg-purple-500 px-1 text-[10px] font-bold text-white">
            {unreadCount > 99 ? "99+" : unreadCount}
          </span>
        )}
      </button>

      <AnimatePresence>
        {isOpen && (
          <>
            <div className="fixed inset-0 z-40" onClick={() => setIsOpen(false)} />
            <motion.div
              initial={{ opacity: 0, y: -10 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -10 }}
              className="absolute right-0 z-50 mt-2 w-80 rounded-xl border border-slate-700 bg-slate-800 shadow-xl shadow-purple-500/10"
            >
              <div className="flex items-center justify-between border-b border-slate-700 px-4 py-3">
                <h3 className="text-sm font-semibold text-slate-200">Notifications</h3>
                {unreadCount > 0 && (
                  <span className="rounded-full bg-purple-500/20 px-2 py-0.5 text-xs text-purple-300">
                    {unreadCount} new
                  </span>
                )}
              </div>

              <div className="max-h-80 overflow-y-auto">
                {notifications.length === 0 ? (
                  <div className="px-4 py-6 text-center text-sm text-slate-400">
                    No notifications
                  </div>
                ) : (
                  notifications.map((n) => (
                    <div
                      key={n.id}
                      className={`flex items-start gap-3 border-b border-slate-700/50 px-4 py-3 last:border-0 ${
                        n.read ? "opacity-60" : ""
                      }`}
                    >
                      <div className="mt-0.5 shrink-0">
                        {typeIcon[n.type] ?? <Bell className="h-4 w-4 text-slate-400" />}
                      </div>
                      <div className="flex-1 min-w-0">
                        <p className="text-sm font-medium text-slate-200 truncate">
                          {n.title}
                        </p>
                        <p className="mt-0.5 text-xs text-slate-400 line-clamp-2">
                          {n.message}
                        </p>
                        <div className="mt-1 flex items-center gap-2">
                          <span className="text-[10px] text-slate-500">
                            {new Date(n.createdAt).toLocaleDateString()}
                          </span>
                          {!n.read && (
                            <button
                              onClick={() => markReadMutation.mutate(n.id)}
                              className="text-[10px] text-purple-400 hover:text-purple-300"
                            >
                              Mark read
                            </button>
                          )}
                        </div>
                      </div>
                    </div>
                  ))
                )}
              </div>

              <Link
                href="/learner/notifications"
                onClick={() => setIsOpen(false)}
                className="block border-t border-slate-700 px-4 py-2.5 text-center text-sm font-medium text-purple-400 transition-colors hover:bg-slate-700/50 hover:text-purple-300"
              >
                View All
              </Link>
            </motion.div>
          </>
        )}
      </AnimatePresence>
    </div>
  );
}

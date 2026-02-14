"use client";

import React from "react";
import { useQuery } from "@tanstack/react-query";
import { apiGet } from "@/lib/api/client";
import { Award, Download, Loader2, Calendar, CheckCircle2 } from "lucide-react";
import { Button } from "@/components/ui/button";

/* ---------- Types ---------- */

interface Certificate {
  id: string;
  courseId: number;
  userId: string;
  courseCompletionId: string;
  status: "GENERATED" | "ISSUED" | "REVOKED" | "EXPIRED";
  issuedAt: string | null;
  revokedAt: string | null;
  expiredAt: string | null;
  createdAt: string;
}

interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
}

const statusConfig: Record<string, { color: string; label: string }> = {
  GENERATED: { color: "border-amber-500/30 bg-amber-500/10 text-amber-300", label: "Generated" },
  ISSUED: { color: "border-green-500/30 bg-green-500/10 text-green-300", label: "Issued" },
  REVOKED: { color: "border-red-500/30 bg-red-500/10 text-red-300", label: "Revoked" },
  EXPIRED: { color: "border-slate-500/30 bg-slate-500/10 text-slate-400", label: "Expired" },
};

export default function CertificatesPage() {
  const { data, isLoading } = useQuery({
    queryKey: ["certificates"],
    queryFn: () =>
      apiGet<PaginatedResponse<Certificate>>(
        "/api/learner/certificates?page=0&size=50"
      ),
  });

  const certificates = data?.content ?? [];

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-950 via-purple-950/20 to-slate-950">
      <div className="mx-auto max-w-4xl px-4 py-8 sm:px-6">
        <div className="mb-8 flex items-center gap-3">
          <Award className="h-6 w-6 text-purple-400" />
          <h1 className="text-2xl font-bold text-slate-100">My Certificates</h1>
        </div>

        {isLoading ? (
          <div className="flex justify-center py-12">
            <Loader2 className="h-8 w-8 animate-spin text-purple-400" />
          </div>
        ) : certificates.length === 0 ? (
          <div className="rounded-xl border border-slate-800 bg-slate-900/60 px-6 py-16 text-center">
            <Award className="mx-auto mb-3 h-10 w-10 text-slate-600" />
            <h2 className="text-lg font-semibold text-slate-300">No certificates yet</h2>
            <p className="mt-2 text-sm text-slate-400">
              Complete courses and pass exams to earn certificates.
            </p>
          </div>
        ) : (
          <div className="grid gap-4 sm:grid-cols-2">
            {certificates.map((cert) => {
              const status = statusConfig[cert.status] ?? statusConfig.GENERATED;
              return (
                <div
                  key={cert.id}
                  className="relative overflow-hidden rounded-xl border border-slate-800 bg-slate-900/60 p-6"
                >
                  {/* Decorative gradient */}
                  <div className="pointer-events-none absolute -right-6 -top-6 h-24 w-24 rounded-full bg-purple-500/10 blur-2xl" />

                  <div className="relative">
                    <div className="mb-4 flex items-center justify-between">
                      <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-purple-500/20 to-pink-500/20 border border-purple-500/20">
                        <Award className="h-6 w-6 text-purple-300" />
                      </div>
                      <span className={`rounded-full border px-2.5 py-0.5 text-xs font-medium ${status.color}`}>
                        {status.label}
                      </span>
                    </div>

                    <h3 className="text-lg font-semibold text-slate-100">
                      Course #{cert.courseId}
                    </h3>

                    <div className="mt-3 space-y-1.5 text-sm text-slate-400">
                      <div className="flex items-center gap-2">
                        <Calendar className="h-3.5 w-3.5" />
                        <span>
                          Generated {new Date(cert.createdAt).toLocaleDateString()}
                        </span>
                      </div>
                      {cert.issuedAt && (
                        <div className="flex items-center gap-2">
                          <CheckCircle2 className="h-3.5 w-3.5 text-green-400" />
                          <span>
                            Issued {new Date(cert.issuedAt).toLocaleDateString()}
                          </span>
                        </div>
                      )}
                    </div>

                    {(cert.status === "GENERATED" || cert.status === "ISSUED") && (
                      <Button
                        variant="ghost"
                        size="sm"
                        className="mt-4 w-full border border-slate-700 text-slate-300 hover:border-purple-500/30 hover:text-purple-300"
                      >
                        <Download className="mr-2 h-4 w-4" />
                        Download Certificate
                      </Button>
                    )}
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

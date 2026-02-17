"use client";

import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { apiPost } from "@/lib/api/client";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { GraduationCap, ArrowLeft, Plus, CheckCircle, Archive, AlertCircle } from "lucide-react";
import Link from "next/link";

interface ExamCreatePayload {
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
}

interface Exam {
  id: string;
  title: string;
  status: string;
}

export default function ExamsPage() {
  const queryClient = useQueryClient();

  // Create form state
  const [courseId, setCourseId] = useState<number>(0);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [instructions, setInstructions] = useState("");
  const [rulesSummary, setRulesSummary] = useState("");
  const [passPercentage, setPassPercentage] = useState(70);
  const [maxAttempts, setMaxAttempts] = useState(3);
  const [timeLimitMinutes, setTimeLimitMinutes] = useState(60);
  const [shuffleQuestions, setShuffleQuestions] = useState(true);
  const [shuffleOptions, setShuffleOptions] = useState(true);

  // Action form state
  const [actionExamId, setActionExamId] = useState("");
  const [lastResult, setLastResult] = useState<{ type: "success" | "error"; message: string } | null>(null);

  const createMutation = useMutation({
    mutationFn: (payload: ExamCreatePayload) =>
      apiPost<Exam>("/api/admin/exams", payload),
    onSuccess: (data) => {
      setLastResult({ type: "success", message: `Exam created with ID: ${data.id}` });
      resetCreateForm();
    },
    onError: (err: Error) => {
      setLastResult({ type: "error", message: err.message });
    },
  });

  const publishMutation = useMutation({
    mutationFn: (examId: string) =>
      apiPost<Exam>(`/api/admin/exams/${examId}/publish`),
    onSuccess: (data) => {
      setLastResult({ type: "success", message: `Exam "${data.title}" published successfully` });
    },
    onError: (err: Error) => {
      setLastResult({ type: "error", message: err.message });
    },
  });

  const archiveMutation = useMutation({
    mutationFn: (examId: string) =>
      apiPost<Exam>(`/api/admin/exams/${examId}/archive`),
    onSuccess: (data) => {
      setLastResult({ type: "success", message: `Exam "${data.title}" archived successfully` });
    },
    onError: (err: Error) => {
      setLastResult({ type: "error", message: err.message });
    },
  });

  function resetCreateForm() {
    setCourseId(0);
    setTitle("");
    setDescription("");
    setInstructions("");
    setRulesSummary("");
    setPassPercentage(70);
    setMaxAttempts(3);
    setTimeLimitMinutes(60);
    setShuffleQuestions(true);
    setShuffleOptions(true);
  }

  function handleCreate() {
    createMutation.mutate({
      courseId,
      title,
      description,
      instructions,
      rulesSummary,
      passPercentage,
      maxAttempts,
      timeLimitMinutes,
      shuffleQuestions,
      shuffleOptions,
    });
  }

  const inputClass = "border-slate-700 bg-slate-800 text-slate-100";
  const labelClass = "mb-1 block text-sm text-slate-400";

  return (
    <main className="relative min-h-screen overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6 py-10">
      <div className="pointer-events-none absolute left-[8%] top-16 h-80 w-80 rounded-full bg-purple-500/20 blur-3xl" />
      <div className="pointer-events-none absolute right-[12%] top-10 h-72 w-72 rounded-full bg-pink-500/15 blur-3xl" />

      <div className="relative mx-auto max-w-5xl space-y-6">
        {/* Header */}
        <div className="flex items-center gap-4 rounded-3xl border border-slate-800 bg-slate-900/60 p-8 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
          <Link href="/admin">
            <Button variant="ghost" size="icon" className="text-slate-400 hover:text-slate-200">
              <ArrowLeft className="h-5 w-5" />
            </Button>
          </Link>
          <div>
            <h1 className="flex items-center gap-3 bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-3xl font-semibold tracking-tight text-transparent">
              <GraduationCap className="h-8 w-8 text-purple-400" />
              Exam Management
            </h1>
            <p className="mt-1 text-slate-400">
              Create exams and manage their lifecycle.
            </p>
          </div>
        </div>

        {/* Result notification */}
        {lastResult && (
          <div
            className={`flex items-center gap-3 rounded-xl border p-4 ${
              lastResult.type === "success"
                ? "border-emerald-700/50 bg-emerald-900/20 text-emerald-300"
                : "border-red-700/50 bg-red-900/20 text-red-300"
            }`}
          >
            {lastResult.type === "success" ? (
              <CheckCircle className="h-5 w-5 shrink-0" />
            ) : (
              <AlertCircle className="h-5 w-5 shrink-0" />
            )}
            <p className="text-sm">{lastResult.message}</p>
            <button
              onClick={() => setLastResult(null)}
              className="ml-auto text-xs opacity-60 hover:opacity-100"
            >
              Dismiss
            </button>
          </div>
        )}

        {/* Create Exam Form */}
        <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-6 backdrop-blur-xl">
          <h2 className="mb-4 flex items-center gap-2 text-lg font-semibold text-slate-200">
            <Plus className="h-5 w-5 text-purple-400" />
            Create New Exam
          </h2>
          <div className="space-y-4">
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
              <div>
                <label className={labelClass}>Course ID</label>
                <Input
                  type="number"
                  value={courseId || ""}
                  onChange={(e) => setCourseId(Number(e.target.value))}
                  placeholder="e.g. 1"
                  className={inputClass}
                />
              </div>
              <div>
                <label className={labelClass}>Title</label>
                <Input
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  placeholder="Exam title"
                  className={inputClass}
                />
              </div>
            </div>
            <div>
              <label className={labelClass}>Description</label>
              <Input
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Exam description"
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>Instructions</label>
              <Input
                value={instructions}
                onChange={(e) => setInstructions(e.target.value)}
                placeholder="Instructions for students"
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>Rules Summary</label>
              <Input
                value={rulesSummary}
                onChange={(e) => setRulesSummary(e.target.value)}
                placeholder="Rules summary"
                className={inputClass}
              />
            </div>
            <div className="grid grid-cols-2 gap-4 md:grid-cols-4">
              <div>
                <label className={labelClass}>Pass %</label>
                <Input
                  type="number"
                  value={passPercentage}
                  onChange={(e) => setPassPercentage(Number(e.target.value))}
                  className={inputClass}
                />
              </div>
              <div>
                <label className={labelClass}>Max Attempts</label>
                <Input
                  type="number"
                  value={maxAttempts}
                  onChange={(e) => setMaxAttempts(Number(e.target.value))}
                  className={inputClass}
                />
              </div>
              <div>
                <label className={labelClass}>Time Limit (min)</label>
                <Input
                  type="number"
                  value={timeLimitMinutes}
                  onChange={(e) => setTimeLimitMinutes(Number(e.target.value))}
                  className={inputClass}
                />
              </div>
              <div className="flex flex-col justify-end gap-2">
                <label className="flex items-center gap-2 text-sm text-slate-300">
                  <input
                    type="checkbox"
                    checked={shuffleQuestions}
                    onChange={(e) => setShuffleQuestions(e.target.checked)}
                    className="rounded border-slate-600"
                  />
                  Shuffle Questions
                </label>
                <label className="flex items-center gap-2 text-sm text-slate-300">
                  <input
                    type="checkbox"
                    checked={shuffleOptions}
                    onChange={(e) => setShuffleOptions(e.target.checked)}
                    className="rounded border-slate-600"
                  />
                  Shuffle Options
                </label>
              </div>
            </div>
            <Button
              onClick={handleCreate}
              disabled={!title || !courseId || createMutation.isPending}
              className="bg-gradient-to-r from-purple-600 to-pink-600 text-white hover:from-purple-700 hover:to-pink-700"
            >
              {createMutation.isPending ? "Creating..." : "Create Exam"}
            </Button>
          </div>
        </div>

        {/* Publish / Archive Actions */}
        <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-6 backdrop-blur-xl">
          <h2 className="mb-4 text-lg font-semibold text-slate-200">
            Exam Actions
          </h2>
          <p className="mb-4 text-sm text-slate-500">
            Enter an exam UUID to publish or archive it.
          </p>
          <div className="flex flex-col gap-4 sm:flex-row sm:items-end">
            <div className="flex-1">
              <label className={labelClass}>Exam ID (UUID)</label>
              <Input
                value={actionExamId}
                onChange={(e) => setActionExamId(e.target.value)}
                placeholder="e.g. 550e8400-e29b-41d4-a716-446655440000"
                className={inputClass}
              />
            </div>
            <div className="flex gap-2">
              <Button
                onClick={() => publishMutation.mutate(actionExamId)}
                disabled={!actionExamId || publishMutation.isPending}
                className="bg-emerald-600 text-white hover:bg-emerald-700"
              >
                <CheckCircle className="mr-1 h-4 w-4" />
                {publishMutation.isPending ? "Publishing..." : "Publish"}
              </Button>
              <Button
                onClick={() => archiveMutation.mutate(actionExamId)}
                disabled={!actionExamId || archiveMutation.isPending}
                variant="outline"
                className="border-slate-700 text-slate-300 hover:bg-slate-800"
              >
                <Archive className="mr-1 h-4 w-4" />
                {archiveMutation.isPending ? "Archiving..." : "Archive"}
              </Button>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}

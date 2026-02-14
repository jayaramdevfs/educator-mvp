"use client";

import React, { useState, useEffect, useCallback, useMemo } from "react";
import { useQuery, useMutation } from "@tanstack/react-query";
import { useParams, useRouter } from "next/navigation";
import { apiGet, apiPost } from "@/lib/api/client";
import { Button } from "@/components/ui/button";
import {
  Clock,
  AlertTriangle,
  CheckCircle2,
  XCircle,
  ArrowLeft,
  Loader2,
  Trophy,
  RotateCcw,
  ChevronLeft,
  ChevronRight,
  FileQuestion,
} from "lucide-react";
import { toast } from "sonner";

/* ---------- Types ---------- */

interface ExamDetail {
  id: string;
  courseId: number;
  title: string;
  description: string;
  instructions: string;
  rulesSummary: string;
  passPercentage: number;
  maxAttempts: number | null;
  timeLimitMinutes: number | null;
  shuffleQuestions: boolean;
  shuffleOptions: boolean;
  status: string;
}

interface ExamAttempt {
  id: string;
  examId: string;
  userId: string;
  status: "IN_PROGRESS" | "SUBMITTED" | "EVALUATED" | "EXPIRED";
  totalQuestions: number | null;
  correctAnswers: number | null;
  scorePercentage: number | null;
  passed: boolean | null;
  startedAt: string;
  submittedAt: string | null;
  evaluatedAt: string | null;
}

interface ExamQuestion {
  id: string;
  questionText: string;
  options: ExamOption[];
}

interface ExamOption {
  id: string;
  optionText: string;
}

interface AnswerPayload {
  questionId: string;
  selectedOptionId: string;
}

type ExamView = "start" | "taking" | "results" | "history";

/* ---------- Component ---------- */

export default function ExamPage() {
  const { courseId } = useParams<{ courseId: string }>();
  const router = useRouter();

  const [view, setView] = useState<ExamView>("start");
  const [currentAttempt, setCurrentAttempt] = useState<ExamAttempt | null>(null);
  const [answers, setAnswers] = useState<Map<string, string>>(new Map());
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [timeLeft, setTimeLeft] = useState<number | null>(null);

  // Fetch exam for this course
  const { data: exam, isLoading: examLoading } = useQuery({
    queryKey: ["course-exam", courseId],
    queryFn: async () => {
      // Try to get exam via instructor endpoint (needs the exam ID)
      // For student, we'll look for an exam associated with the course
      const exams = await apiGet<ExamDetail[]>(`/api/public/exams/course/${courseId}`);
      return exams?.[0] ?? null;
    },
    retry: false,
  });

  // Fetch attempt history
  const { data: attemptHistory = [], refetch: refetchHistory } = useQuery({
    queryKey: ["exam-attempts", exam?.id],
    queryFn: () => apiGet<ExamAttempt[]>(`/api/student/exams/${exam!.id}/attempts`),
    enabled: !!exam?.id,
  });

  // Mock questions for the exam (in real app, these come from start attempt response)
  const [questions, setQuestions] = useState<ExamQuestion[]>([]);

  const evaluatedAttempts = attemptHistory.filter((a) => a.status === "EVALUATED");
  const attemptsUsed = attemptHistory.length;
  const attemptsRemaining = exam?.maxAttempts ? exam.maxAttempts - attemptsUsed : null;

  // Start attempt mutation
  const startMutation = useMutation({
    mutationFn: async () => {
      const result = await apiPost<ExamAttempt & { questions?: ExamQuestion[] }>(
        `/api/student/exams/${exam!.id}/start`
      );
      return result;
    },
    onSuccess: (data) => {
      setCurrentAttempt(data);
      if (data.questions) setQuestions(data.questions);
      setAnswers(new Map());
      setCurrentQuestionIndex(0);
      if (exam?.timeLimitMinutes) {
        setTimeLeft(exam.timeLimitMinutes * 60);
      }
      setView("taking");
    },
    onError: () => {
      toast.error("Failed to start exam. You may have reached the maximum attempts.");
    },
  });

  // Submit attempt mutation
  const submitMutation = useMutation({
    mutationFn: async () => {
      const answerArray: AnswerPayload[] = Array.from(answers.entries()).map(
        ([questionId, selectedOptionId]) => ({ questionId, selectedOptionId })
      );
      const result = await apiPost<ExamAttempt>(
        `/api/student/exams/attempts/${currentAttempt!.id}/submit`,
        { answers: answerArray }
      );
      return result;
    },
    onSuccess: (data) => {
      setCurrentAttempt(data);
      setView("results");
      refetchHistory();
      toast.success("Exam submitted successfully!");
    },
    onError: () => {
      toast.error("Failed to submit exam.");
    },
  });

  // Timer
  useEffect(() => {
    if (view !== "taking" || timeLeft === null) return;
    if (timeLeft <= 0) {
      submitMutation.mutate();
      return;
    }
    const timer = setInterval(() => setTimeLeft((t) => (t ?? 1) - 1), 1000);
    return () => clearInterval(timer);
  }, [view, timeLeft]);

  const formatTime = (seconds: number) => {
    const m = Math.floor(seconds / 60);
    const s = seconds % 60;
    return `${m}:${s.toString().padStart(2, "0")}`;
  };

  const handleSelectOption = (questionId: string, optionId: string) => {
    setAnswers((prev) => new Map(prev).set(questionId, optionId));
  };

  const handleSubmit = () => {
    if (answers.size < questions.length) {
      const unanswered = questions.length - answers.size;
      if (!confirm(`You have ${unanswered} unanswered question(s). Submit anyway?`)) {
        return;
      }
    }
    submitMutation.mutate();
  };

  if (examLoading) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-slate-950">
        <Loader2 className="h-8 w-8 animate-spin text-purple-400" />
      </div>
    );
  }

  if (!exam) {
    return (
      <div className="min-h-screen bg-slate-950 p-8">
        <div className="mx-auto max-w-2xl text-center">
          <FileQuestion className="mx-auto mb-4 h-12 w-12 text-slate-600" />
          <h1 className="text-xl font-semibold text-slate-200">No Exam Available</h1>
          <p className="mt-2 text-slate-400">There is no exam configured for this course yet.</p>
          <Button
            onClick={() => router.push(`/learner/courses/${courseId}`)}
            variant="ghost"
            className="mt-4 text-purple-400"
          >
            <ArrowLeft className="mr-2 h-4 w-4" />
            Back to Course
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-950 via-purple-950/20 to-slate-950 p-4 sm:p-8">
      <div className="mx-auto max-w-3xl">
        {/* ---------- START VIEW (F3.4) ---------- */}
        {view === "start" && (
          <div className="space-y-6">
            <button
              onClick={() => router.push(`/learner/courses/${courseId}`)}
              className="flex items-center gap-1 text-sm text-slate-400 hover:text-purple-300 transition-colors"
            >
              <ArrowLeft className="h-4 w-4" />
              Back to Course
            </button>

            <div className="rounded-xl border border-slate-800 bg-slate-900/60 p-6">
              <h1 className="text-2xl font-bold text-slate-100">{exam.title}</h1>
              <p className="mt-2 text-slate-400">{exam.description}</p>

              {/* Rules */}
              <div className="mt-6 space-y-3">
                <h3 className="text-sm font-semibold text-slate-300 uppercase tracking-wide">
                  Exam Rules
                </h3>
                <div className="rounded-lg border border-slate-700 bg-slate-800/40 p-4 text-sm text-slate-300 whitespace-pre-line">
                  {exam.rulesSummary || exam.instructions}
                </div>
              </div>

              {/* Info grid */}
              <div className="mt-6 grid grid-cols-2 gap-4 sm:grid-cols-4">
                <InfoCard
                  label="Pass Score"
                  value={`${exam.passPercentage}%`}
                  icon={<Trophy className="h-4 w-4 text-amber-400" />}
                />
                {exam.timeLimitMinutes && (
                  <InfoCard
                    label="Time Limit"
                    value={`${exam.timeLimitMinutes} min`}
                    icon={<Clock className="h-4 w-4 text-blue-400" />}
                  />
                )}
                {exam.maxAttempts && (
                  <InfoCard
                    label="Attempts Left"
                    value={`${attemptsRemaining ?? "?"} / ${exam.maxAttempts}`}
                    icon={<RotateCcw className="h-4 w-4 text-purple-400" />}
                  />
                )}
                <InfoCard
                  label="Shuffle"
                  value={exam.shuffleQuestions ? "Yes" : "No"}
                  icon={<FileQuestion className="h-4 w-4 text-pink-400" />}
                />
              </div>

              {/* Start button */}
              <div className="mt-8 flex gap-3">
                <Button
                  onClick={() => startMutation.mutate()}
                  disabled={startMutation.isPending || (attemptsRemaining !== null && attemptsRemaining <= 0)}
                  className="bg-gradient-to-r from-purple-600 to-pink-500 text-white shadow-lg shadow-purple-500/20 hover:from-purple-500 hover:to-pink-400"
                >
                  {startMutation.isPending ? (
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  ) : null}
                  Start Exam
                </Button>
                {evaluatedAttempts.length > 0 && (
                  <Button
                    variant="ghost"
                    onClick={() => setView("history")}
                    className="text-slate-400 hover:text-purple-300"
                  >
                    View History ({evaluatedAttempts.length})
                  </Button>
                )}
              </div>

              {attemptsRemaining !== null && attemptsRemaining <= 0 && (
                <p className="mt-3 flex items-center gap-2 text-sm text-red-400">
                  <AlertTriangle className="h-4 w-4" />
                  You have used all available attempts.
                </p>
              )}
            </div>
          </div>
        )}

        {/* ---------- TAKING VIEW (F3.5) ---------- */}
        {view === "taking" && (
          <div className="space-y-6">
            {/* Timer bar */}
            <div className="sticky top-16 z-10 flex items-center justify-between rounded-xl border border-slate-800 bg-slate-900/90 p-4 backdrop-blur-sm">
              <span className="text-sm text-slate-400">
                Question {currentQuestionIndex + 1} of {questions.length}
              </span>
              {timeLeft !== null && (
                <span
                  className={`flex items-center gap-2 font-mono text-lg font-bold ${
                    timeLeft < 60 ? "text-red-400 animate-pulse" : "text-slate-200"
                  }`}
                >
                  <Clock className="h-5 w-5" />
                  {formatTime(timeLeft)}
                </span>
              )}
              <span className="text-sm text-slate-400">
                {answers.size} answered
              </span>
            </div>

            {/* Question */}
            {questions.length > 0 && (
              <div className="rounded-xl border border-slate-800 bg-slate-900/60 p-6">
                <h2 className="mb-4 text-lg font-medium text-slate-100">
                  {questions[currentQuestionIndex]?.questionText}
                </h2>

                <div className="space-y-3">
                  {questions[currentQuestionIndex]?.options.map((option) => {
                    const isSelected =
                      answers.get(questions[currentQuestionIndex].id) === option.id;
                    return (
                      <button
                        key={option.id}
                        onClick={() =>
                          handleSelectOption(questions[currentQuestionIndex].id, option.id)
                        }
                        className={`flex w-full items-center gap-3 rounded-lg border p-4 text-left text-sm transition-all ${
                          isSelected
                            ? "border-purple-500/50 bg-purple-500/10 text-purple-200"
                            : "border-slate-700 bg-slate-800/40 text-slate-300 hover:border-slate-600 hover:bg-slate-800/60"
                        }`}
                      >
                        <div
                          className={`flex h-5 w-5 shrink-0 items-center justify-center rounded-full border ${
                            isSelected
                              ? "border-purple-400 bg-purple-500"
                              : "border-slate-600"
                          }`}
                        >
                          {isSelected && (
                            <div className="h-2 w-2 rounded-full bg-white" />
                          )}
                        </div>
                        {option.optionText}
                      </button>
                    );
                  })}
                </div>
              </div>
            )}

            {/* Question navigation */}
            {questions.length > 0 && (
              <>
                <div className="flex items-center justify-between">
                  <Button
                    variant="ghost"
                    disabled={currentQuestionIndex === 0}
                    onClick={() => setCurrentQuestionIndex((i) => i - 1)}
                    className="text-slate-400"
                  >
                    <ChevronLeft className="mr-1 h-4 w-4" />
                    Previous
                  </Button>
                  <Button
                    variant="ghost"
                    disabled={currentQuestionIndex >= questions.length - 1}
                    onClick={() => setCurrentQuestionIndex((i) => i + 1)}
                    className="text-slate-400"
                  >
                    Next
                    <ChevronRight className="ml-1 h-4 w-4" />
                  </Button>
                </div>

                {/* Question dots */}
                <div className="flex flex-wrap justify-center gap-2">
                  {questions.map((q, i) => (
                    <button
                      key={q.id}
                      onClick={() => setCurrentQuestionIndex(i)}
                      className={`flex h-8 w-8 items-center justify-center rounded-lg text-xs font-medium transition-all ${
                        i === currentQuestionIndex
                          ? "bg-purple-500 text-white"
                          : answers.has(q.id)
                            ? "bg-green-500/20 text-green-300 border border-green-500/30"
                            : "bg-slate-800 text-slate-400 border border-slate-700"
                      }`}
                    >
                      {i + 1}
                    </button>
                  ))}
                </div>

                {/* Submit */}
                <div className="flex justify-center">
                  <Button
                    onClick={handleSubmit}
                    disabled={submitMutation.isPending}
                    className="bg-gradient-to-r from-purple-600 to-pink-500 text-white px-8 shadow-lg shadow-purple-500/20 hover:from-purple-500 hover:to-pink-400"
                  >
                    {submitMutation.isPending ? (
                      <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    ) : null}
                    Submit Exam
                  </Button>
                </div>
              </>
            )}

            {questions.length === 0 && (
              <div className="rounded-xl border border-slate-800 bg-slate-900/60 p-8 text-center text-slate-400">
                <p>Loading questions...</p>
              </div>
            )}
          </div>
        )}

        {/* ---------- RESULTS VIEW (F3.6) ---------- */}
        {view === "results" && currentAttempt && (
          <div className="space-y-6">
            <div className="rounded-xl border border-slate-800 bg-slate-900/60 p-8 text-center">
              {currentAttempt.passed ? (
                <div className="mb-4 inline-flex h-20 w-20 items-center justify-center rounded-full bg-green-500/10">
                  <Trophy className="h-10 w-10 text-green-400" />
                </div>
              ) : (
                <div className="mb-4 inline-flex h-20 w-20 items-center justify-center rounded-full bg-red-500/10">
                  <XCircle className="h-10 w-10 text-red-400" />
                </div>
              )}

              <h1 className="text-2xl font-bold text-slate-100">
                {currentAttempt.passed ? "Congratulations!" : "Keep Trying!"}
              </h1>
              <p className="mt-2 text-slate-400">
                {currentAttempt.passed
                  ? "You passed the exam successfully."
                  : "You didn't pass this time. Review the material and try again."}
              </p>

              {/* Score Card */}
              <div className="mt-8 grid gap-4 sm:grid-cols-3">
                <div className="rounded-lg border border-slate-700 bg-slate-800/40 p-4">
                  <p className="text-sm text-slate-400">Score</p>
                  <p
                    className={`text-3xl font-bold ${
                      currentAttempt.passed ? "text-green-400" : "text-red-400"
                    }`}
                  >
                    {currentAttempt.scorePercentage?.toFixed(1)}%
                  </p>
                </div>
                <div className="rounded-lg border border-slate-700 bg-slate-800/40 p-4">
                  <p className="text-sm text-slate-400">Correct Answers</p>
                  <p className="text-3xl font-bold text-slate-100">
                    {currentAttempt.correctAnswers} / {currentAttempt.totalQuestions}
                  </p>
                </div>
                <div className="rounded-lg border border-slate-700 bg-slate-800/40 p-4">
                  <p className="text-sm text-slate-400">Status</p>
                  <p className="mt-1">
                    {currentAttempt.passed ? (
                      <span className="inline-flex items-center gap-1 rounded-full border border-green-500/30 bg-green-500/10 px-3 py-1 text-sm font-medium text-green-300">
                        <CheckCircle2 className="h-4 w-4" />
                        PASSED
                      </span>
                    ) : (
                      <span className="inline-flex items-center gap-1 rounded-full border border-red-500/30 bg-red-500/10 px-3 py-1 text-sm font-medium text-red-300">
                        <XCircle className="h-4 w-4" />
                        FAILED
                      </span>
                    )}
                  </p>
                </div>
              </div>

              {/* Actions */}
              <div className="mt-8 flex justify-center gap-3">
                <Button
                  onClick={() => router.push(`/learner/courses/${courseId}`)}
                  variant="ghost"
                  className="text-slate-400 hover:text-purple-300"
                >
                  <ArrowLeft className="mr-2 h-4 w-4" />
                  Back to Course
                </Button>
                {!currentAttempt.passed && (attemptsRemaining === null || attemptsRemaining > 0) && (
                  <Button
                    onClick={() => setView("start")}
                    className="bg-gradient-to-r from-purple-600 to-pink-500 text-white"
                  >
                    <RotateCcw className="mr-2 h-4 w-4" />
                    Try Again
                  </Button>
                )}
                <Button
                  variant="ghost"
                  onClick={() => setView("history")}
                  className="text-slate-400 hover:text-purple-300"
                >
                  View History
                </Button>
              </div>
            </div>
          </div>
        )}

        {/* ---------- HISTORY VIEW (F3.7) ---------- */}
        {view === "history" && (
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <h1 className="text-2xl font-bold text-slate-100">Attempt History</h1>
              <Button
                variant="ghost"
                onClick={() => setView("start")}
                className="text-slate-400 hover:text-purple-300"
              >
                <ArrowLeft className="mr-2 h-4 w-4" />
                Back to Exam
              </Button>
            </div>

            {evaluatedAttempts.length === 0 ? (
              <div className="rounded-xl border border-slate-800 bg-slate-900/60 p-8 text-center text-slate-400">
                No previous attempts found.
              </div>
            ) : (
              <div className="space-y-3">
                {evaluatedAttempts.map((attempt, index) => (
                  <div
                    key={attempt.id}
                    className="flex items-center justify-between rounded-xl border border-slate-800 bg-slate-900/60 p-4"
                  >
                    <div className="flex items-center gap-4">
                      <span className="flex h-8 w-8 items-center justify-center rounded-lg bg-slate-800 text-sm font-medium text-slate-400">
                        #{evaluatedAttempts.length - index}
                      </span>
                      <div>
                        <p className="text-sm font-medium text-slate-200">
                          Score: {attempt.scorePercentage?.toFixed(1)}% ({attempt.correctAnswers}/{attempt.totalQuestions})
                        </p>
                        <p className="text-xs text-slate-500">
                          {attempt.evaluatedAt
                            ? new Date(attempt.evaluatedAt).toLocaleString()
                            : "Pending evaluation"}
                        </p>
                      </div>
                    </div>
                    {attempt.passed ? (
                      <span className="inline-flex items-center gap-1 rounded-full border border-green-500/30 bg-green-500/10 px-2.5 py-0.5 text-xs font-medium text-green-300">
                        <CheckCircle2 className="h-3 w-3" />
                        PASSED
                      </span>
                    ) : (
                      <span className="inline-flex items-center gap-1 rounded-full border border-red-500/30 bg-red-500/10 px-2.5 py-0.5 text-xs font-medium text-red-300">
                        <XCircle className="h-3 w-3" />
                        FAILED
                      </span>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

/* ---------- Info Card ---------- */

function InfoCard({
  label,
  value,
  icon,
}: {
  label: string;
  value: string;
  icon: React.ReactNode;
}) {
  return (
    <div className="rounded-lg border border-slate-700 bg-slate-800/40 p-3 text-center">
      <div className="mx-auto mb-1 flex h-8 w-8 items-center justify-center rounded-lg bg-slate-700/50">
        {icon}
      </div>
      <p className="text-xs text-slate-500">{label}</p>
      <p className="text-sm font-semibold text-slate-200">{value}</p>
    </div>
  );
}

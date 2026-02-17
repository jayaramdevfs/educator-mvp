"use client";

import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { apiGet, apiPost } from "@/lib/api/client";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogFooter,
  DialogClose,
} from "@/components/ui/dialog";
import { BookOpen, Plus, ArrowLeft } from "lucide-react";
import Link from "next/link";

interface Course {
  id: number;
  titleEn: string;
  descriptionEn: string;
  difficulty: string;
  status: string;
  languageCode: string;
  estimatedDurationMinutes: number;
  createdByRole: string;
  createdAt: string;
}

interface PaginatedResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

const difficultyOptions = ["BEGINNER", "INTERMEDIATE", "ADVANCED"];

function statusBadgeVariant(status: string) {
  switch (status) {
    case "PUBLISHED":
      return "default";
    case "DRAFT":
      return "secondary";
    case "ARCHIVED":
      return "outline";
    default:
      return "secondary";
  }
}

export default function CoursesPage() {
  const queryClient = useQueryClient();
  const [dialogOpen, setDialogOpen] = useState(false);
  const [page, setPage] = useState(0);

  // Form state
  const [titleEn, setTitleEn] = useState("");
  const [descriptionEn, setDescriptionEn] = useState("");
  const [difficulty, setDifficulty] = useState("BEGINNER");
  const [languageCode, setLanguageCode] = useState("en");
  const [estimatedDuration, setEstimatedDuration] = useState(60);
  const [hierarchyNodeId, setHierarchyNodeId] = useState(1);

  const { data, isLoading, isError } = useQuery<PaginatedResponse<Course>>({
    queryKey: ["admin", "courses", page],
    queryFn: () =>
      apiGet<PaginatedResponse<Course>>(
        `/api/public/courses/search?page=${page}&size=20`
      ),
  });

  const createMutation = useMutation({
    mutationFn: () => {
      const params = new URLSearchParams({
        hierarchyNodeId: String(hierarchyNodeId),
        titleEn,
        descriptionEn,
        difficulty,
        languageCode,
        estimatedDurationMinutes: String(estimatedDuration),
        createdByRole: "ADMIN",
      });
      return apiPost<Course>(`/api/admin/courses?${params.toString()}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["admin", "courses"] });
      setDialogOpen(false);
      resetForm();
    },
  });

  function resetForm() {
    setTitleEn("");
    setDescriptionEn("");
    setDifficulty("BEGINNER");
    setLanguageCode("en");
    setEstimatedDuration(60);
    setHierarchyNodeId(1);
  }

  const courses = data?.content ?? [];

  return (
    <main className="relative min-h-screen overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6 py-10">
      <div className="pointer-events-none absolute left-[8%] top-16 h-80 w-80 rounded-full bg-purple-500/20 blur-3xl" />
      <div className="pointer-events-none absolute right-[12%] top-10 h-72 w-72 rounded-full bg-pink-500/15 blur-3xl" />

      <div className="relative mx-auto max-w-7xl space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between rounded-3xl border border-slate-800 bg-slate-900/60 p-8 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
          <div className="flex items-center gap-4">
            <Link href="/admin">
              <Button variant="ghost" size="icon" className="text-slate-400 hover:text-slate-200">
                <ArrowLeft className="h-5 w-5" />
              </Button>
            </Link>
            <div>
              <h1 className="flex items-center gap-3 bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-3xl font-semibold tracking-tight text-transparent">
                <BookOpen className="h-8 w-8 text-purple-400" />
                Course Management
              </h1>
              <p className="mt-1 text-slate-400">
                {data ? `${data.totalElements} courses total` : "Loading..."}
              </p>
            </div>
          </div>

          <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
            <DialogTrigger asChild>
              <Button className="bg-gradient-to-r from-purple-600 to-pink-600 text-white hover:from-purple-700 hover:to-pink-700">
                <Plus className="mr-2 h-4 w-4" />
                New Course
              </Button>
            </DialogTrigger>
            <DialogContent className="border-slate-700 bg-slate-900 text-slate-100">
              <DialogHeader>
                <DialogTitle className="text-slate-100">Create New Course</DialogTitle>
              </DialogHeader>
              <div className="space-y-4 py-2">
                <div>
                  <label className="mb-1 block text-sm text-slate-400">Title</label>
                  <Input
                    value={titleEn}
                    onChange={(e) => setTitleEn(e.target.value)}
                    placeholder="Course title"
                    className="border-slate-700 bg-slate-800 text-slate-100"
                  />
                </div>
                <div>
                  <label className="mb-1 block text-sm text-slate-400">Description</label>
                  <Input
                    value={descriptionEn}
                    onChange={(e) => setDescriptionEn(e.target.value)}
                    placeholder="Course description"
                    className="border-slate-700 bg-slate-800 text-slate-100"
                  />
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="mb-1 block text-sm text-slate-400">Difficulty</label>
                    <select
                      value={difficulty}
                      onChange={(e) => setDifficulty(e.target.value)}
                      className="h-9 w-full rounded-md border border-slate-700 bg-slate-800 px-3 text-sm text-slate-100 outline-none focus:border-purple-500"
                    >
                      {difficultyOptions.map((d) => (
                        <option key={d} value={d}>
                          {d}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div>
                    <label className="mb-1 block text-sm text-slate-400">Language</label>
                    <Input
                      value={languageCode}
                      onChange={(e) => setLanguageCode(e.target.value)}
                      placeholder="en"
                      className="border-slate-700 bg-slate-800 text-slate-100"
                    />
                  </div>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="mb-1 block text-sm text-slate-400">
                      Duration (minutes)
                    </label>
                    <Input
                      type="number"
                      value={estimatedDuration}
                      onChange={(e) => setEstimatedDuration(Number(e.target.value))}
                      className="border-slate-700 bg-slate-800 text-slate-100"
                    />
                  </div>
                  <div>
                    <label className="mb-1 block text-sm text-slate-400">
                      Hierarchy Node ID
                    </label>
                    <Input
                      type="number"
                      value={hierarchyNodeId}
                      onChange={(e) => setHierarchyNodeId(Number(e.target.value))}
                      className="border-slate-700 bg-slate-800 text-slate-100"
                    />
                  </div>
                </div>
              </div>
              <DialogFooter>
                <DialogClose asChild>
                  <Button variant="outline" className="border-slate-700 text-slate-300">
                    Cancel
                  </Button>
                </DialogClose>
                <Button
                  onClick={() => createMutation.mutate()}
                  disabled={!titleEn || createMutation.isPending}
                  className="bg-gradient-to-r from-purple-600 to-pink-600 text-white"
                >
                  {createMutation.isPending ? "Creating..." : "Create Course"}
                </Button>
              </DialogFooter>
              {createMutation.isError && (
                <p className="text-sm text-red-400">
                  Error: {(createMutation.error as Error).message}
                </p>
              )}
            </DialogContent>
          </Dialog>
        </div>

        {/* Table */}
        <div className="overflow-hidden rounded-2xl border border-slate-800 bg-slate-900/60 backdrop-blur-xl">
          {isLoading ? (
            <div className="flex h-64 items-center justify-center">
              <div className="h-8 w-8 animate-spin rounded-full border-2 border-purple-500 border-t-transparent" />
            </div>
          ) : isError ? (
            <div className="flex h-64 items-center justify-center text-slate-400">
              Failed to load courses. Make sure you are logged in as admin.
            </div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow className="border-slate-800 hover:bg-transparent">
                  <TableHead className="text-slate-400">ID</TableHead>
                  <TableHead className="text-slate-400">Title</TableHead>
                  <TableHead className="text-slate-400">Difficulty</TableHead>
                  <TableHead className="text-slate-400">Status</TableHead>
                  <TableHead className="text-slate-400">Duration</TableHead>
                  <TableHead className="text-slate-400">Created</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {courses.length === 0 ? (
                  <TableRow className="border-slate-800">
                    <TableCell colSpan={6} className="h-24 text-center text-slate-500">
                      No courses found.
                    </TableCell>
                  </TableRow>
                ) : (
                  courses.map((course) => (
                    <TableRow key={course.id} className="border-slate-800 text-slate-300">
                      <TableCell className="font-mono text-xs text-slate-500">
                        {course.id}
                      </TableCell>
                      <TableCell className="font-medium text-slate-200">
                        {course.titleEn}
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline" className="border-slate-700 text-slate-300">
                          {course.difficulty}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <Badge variant={statusBadgeVariant(course.status)}>
                          {course.status}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-slate-400">
                        {course.estimatedDurationMinutes}m
                      </TableCell>
                      <TableCell className="text-slate-500">
                        {course.createdAt
                          ? new Date(course.createdAt).toLocaleDateString()
                          : "--"}
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          )}

          {/* Pagination */}
          {data && data.totalPages > 1 && (
            <div className="flex items-center justify-between border-t border-slate-800 px-4 py-3">
              <p className="text-sm text-slate-500">
                Page {data.pageNumber + 1} of {data.totalPages}
              </p>
              <div className="flex gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  disabled={page === 0}
                  onClick={() => setPage((p) => p - 1)}
                  className="border-slate-700 text-slate-300"
                >
                  Previous
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  disabled={data.last}
                  onClick={() => setPage((p) => p + 1)}
                  className="border-slate-700 text-slate-300"
                >
                  Next
                </Button>
              </div>
            </div>
          )}
        </div>
      </div>
    </main>
  );
}

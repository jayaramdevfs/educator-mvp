"use client";

import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { apiGet } from "@/lib/api/client";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Users, ArrowLeft } from "lucide-react";
import Link from "next/link";

interface AdminUser {
  id: number;
  email: string;
  roles: string[];
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

function roleBadgeClass(role: string) {
  switch (role) {
    case "ADMIN":
      return "border-red-700/50 bg-red-900/30 text-red-300";
    case "INSTRUCTOR":
      return "border-amber-700/50 bg-amber-900/30 text-amber-300";
    case "STUDENT":
      return "border-blue-700/50 bg-blue-900/30 text-blue-300";
    default:
      return "border-slate-700 text-slate-400";
  }
}

export default function UsersPage() {
  const [page, setPage] = useState(0);

  const { data, isLoading, isError } = useQuery<PaginatedResponse<AdminUser>>({
    queryKey: ["admin", "users", page],
    queryFn: () =>
      apiGet<PaginatedResponse<AdminUser>>(
        `/api/admin/users?page=${page}&size=20`
      ),
  });

  const users = data?.content ?? [];

  return (
    <main className="relative min-h-screen overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6 py-10">
      <div className="pointer-events-none absolute left-[8%] top-16 h-80 w-80 rounded-full bg-purple-500/20 blur-3xl" />
      <div className="pointer-events-none absolute right-[12%] top-10 h-72 w-72 rounded-full bg-pink-500/15 blur-3xl" />

      <div className="relative mx-auto max-w-6xl space-y-6">
        {/* Header */}
        <div className="flex items-center gap-4 rounded-3xl border border-slate-800 bg-slate-900/60 p-8 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
          <Link href="/admin">
            <Button variant="ghost" size="icon" className="text-slate-400 hover:text-slate-200">
              <ArrowLeft className="h-5 w-5" />
            </Button>
          </Link>
          <div>
            <h1 className="flex items-center gap-3 bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-3xl font-semibold tracking-tight text-transparent">
              <Users className="h-8 w-8 text-purple-400" />
              User Management
            </h1>
            <p className="mt-1 text-slate-400">
              {data ? `${data.totalElements} users total` : "Loading..."}
            </p>
          </div>
        </div>

        {/* Table */}
        <div className="overflow-hidden rounded-2xl border border-slate-800 bg-slate-900/60 backdrop-blur-xl">
          {isLoading ? (
            <div className="flex h-64 items-center justify-center">
              <div className="h-8 w-8 animate-spin rounded-full border-2 border-purple-500 border-t-transparent" />
            </div>
          ) : isError ? (
            <div className="flex h-64 items-center justify-center text-slate-400">
              Failed to load users. Make sure you are logged in as admin.
            </div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow className="border-slate-800 hover:bg-transparent">
                  <TableHead className="text-slate-400">ID</TableHead>
                  <TableHead className="text-slate-400">Email</TableHead>
                  <TableHead className="text-slate-400">Roles</TableHead>
                  <TableHead className="text-slate-400">Created At</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {users.length === 0 ? (
                  <TableRow className="border-slate-800">
                    <TableCell colSpan={4} className="h-24 text-center text-slate-500">
                      No users found.
                    </TableCell>
                  </TableRow>
                ) : (
                  users.map((user) => (
                    <TableRow key={user.id} className="border-slate-800 text-slate-300">
                      <TableCell className="font-mono text-xs text-slate-500">
                        {user.id}
                      </TableCell>
                      <TableCell className="font-medium text-slate-200">
                        {user.email}
                      </TableCell>
                      <TableCell>
                        <div className="flex flex-wrap gap-1">
                          {(user.roles ?? []).map((role) => (
                            <Badge
                              key={role}
                              variant="outline"
                              className={`text-xs ${roleBadgeClass(role)}`}
                            >
                              {role}
                            </Badge>
                          ))}
                        </div>
                      </TableCell>
                      <TableCell className="text-slate-500">
                        {user.createdAt
                          ? new Date(user.createdAt).toLocaleDateString()
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

"use client";

import { AuthGuard } from "./auth-guard";
import type { RoleName } from "@/types";

interface RoleGuardProps {
  children: React.ReactNode;
  allowedRoles: RoleName[];
  redirectTo?: string;
}

/**
 * Role-based guard that requires specific roles
 * Usage: <RoleGuard allowedRoles={["ADMIN"]}>...</RoleGuard>
 */
export function RoleGuard({
  children,
  allowedRoles,
  redirectTo,
}: RoleGuardProps) {
  return (
    <AuthGuard
      requireAuth={true}
      allowedRoles={allowedRoles}
      redirectTo={redirectTo}
    >
      {children}
    </AuthGuard>
  );
}

/**
 * Admin-only guard
 * Usage: <AdminGuard>...</AdminGuard>
 */
export function AdminGuard({ children }: { children: React.ReactNode }) {
  return (
    <RoleGuard allowedRoles={["ADMIN"]}>
      {children}
    </RoleGuard>
  );
}

/**
 * Instructor-only guard
 * Usage: <InstructorGuard>...</InstructorGuard>
 */
export function InstructorGuard({ children }: { children: React.ReactNode }) {
  return (
    <RoleGuard allowedRoles={["INSTRUCTOR"]}>
      {children}
    </RoleGuard>
  );
}

/**
 * Student-only guard
 * Usage: <StudentGuard>...</StudentGuard>
 */
export function StudentGuard({ children }: { children: React.ReactNode }) {
  return (
    <RoleGuard allowedRoles={["STUDENT"]}>
      {children}
    </RoleGuard>
  );
}

/**
 * Authenticated user guard (any role)
 * Usage: <AuthenticatedGuard>...</AuthenticatedGuard>
 */
export function AuthenticatedGuard({ children }: { children: React.ReactNode }) {
  return (
    <AuthGuard requireAuth={true}>
      {children}
    </AuthGuard>
  );
}

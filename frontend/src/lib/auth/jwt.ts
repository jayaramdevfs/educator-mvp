import type { RoleName } from "@/types";

export interface JwtClaims {
  sub?: string;
  roles?: string[];
  iat?: number;
  exp?: number;
  [key: string]: unknown;
}

export interface AuthUser {
  email: string;
  roles: RoleName[];
}

function isRoleName(value: string): value is RoleName {
  return value === "STUDENT" || value === "INSTRUCTOR" || value === "ADMIN";
}

function decodeBase64Url(input: string): string {
  const normalized = input.replace(/-/g, "+").replace(/_/g, "/");
  const padding = normalized.length % 4 === 0 ? "" : "=".repeat(4 - (normalized.length % 4));
  const base64 = normalized + padding;

  if (typeof window !== "undefined" && typeof window.atob === "function") {
    return window.atob(base64);
  }

  if (typeof Buffer !== "undefined") {
    return Buffer.from(base64, "base64").toString("utf-8");
  }

  throw new Error("No base64 decoder available");
}

export function parseJwtClaims(token: string): JwtClaims | null {
  try {
    const parts = token.split(".");
    if (parts.length !== 3) {
      return null;
    }

    const payloadJson = decodeBase64Url(parts[1]);
    const parsed = JSON.parse(payloadJson) as JwtClaims;
    return parsed;
  } catch {
    return null;
  }
}

export function mapTokenToAuthUser(token: string): AuthUser | null {
  const claims = parseJwtClaims(token);
  if (!claims || typeof claims.sub !== "string" || !claims.sub) {
    return null;
  }

  const rawRoles = Array.isArray(claims.roles)
    ? claims.roles.filter((r): r is string => typeof r === "string")
    : [];

  const roles = rawRoles.filter(isRoleName);

  return {
    email: claims.sub,
    roles,
  };
}


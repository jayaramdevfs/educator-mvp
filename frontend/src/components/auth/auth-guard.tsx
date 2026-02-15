"use client";

import { useEffect } from "react";
import { useRouter, usePathname } from "next/navigation";
import { motion } from "framer-motion";
import { Shield, Loader2 } from "lucide-react";
import { useAuthStore } from "@/store/auth-store";
import type { RoleName } from "@/types";

interface AuthGuardProps {
  children: React.ReactNode;
  requireAuth?: boolean;
  allowedRoles?: RoleName[];
  redirectTo?: string;
}

export function AuthGuard({
                            children,
                            requireAuth = true,
                            allowedRoles,
                            redirectTo = "/login-new",
                          }: AuthGuardProps) {
  const router = useRouter();
  const pathname = usePathname();

  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const user = useAuthStore((state) => state.user);

  useEffect(() => {
    if (!requireAuth) return;

    if (!isAuthenticated || !user) {
      const returnUrl = encodeURIComponent(pathname);
      router.replace(`${redirectTo}?returnUrl=${returnUrl}`);
      return;
    }

    if (allowedRoles && allowedRoles.length > 0) {
      const hasRequiredRole = user.roles.some((role) =>
          allowedRoles.includes(role as RoleName)
      );

      if (!hasRequiredRole) {
        const userDashboard = user.roles.includes("ADMIN")
            ? "/admin"
            : user.roles.includes("INSTRUCTOR")
                ? "/instructor"
                : "/learner/dashboard";

        router.replace(userDashboard);
      }
    }
  }, [
    isAuthenticated,
    user,
    requireAuth,
    allowedRoles,
    router,
    pathname,
    redirectTo,
  ]);

  const isChecking = requireAuth && (!isAuthenticated || !user);

  if (isChecking) {
    return (
        <div className="relative flex min-h-screen items-center justify-center overflow-hidden bg-linear-to-br from-slate-950 via-purple-950 to-slate-900">
          <motion.div
              className="pointer-events-none absolute h-96 w-96 rounded-full bg-purple-500/20 blur-3xl"
              animate={{ scale: [1, 1.2, 1], opacity: [0.3, 0.5, 0.3] }}
              transition={{ duration: 3, repeat: Infinity, ease: "easeInOut" }}
          />
          <motion.div
              className="pointer-events-none absolute -right-16 -top-16 h-80 w-80 rounded-full bg-pink-500/15 blur-3xl"
              animate={{ scale: [1, 1.15, 1], opacity: [0.25, 0.45, 0.25] }}
              transition={{ duration: 3.5, repeat: Infinity, ease: "easeInOut" }}
          />

          <motion.div
              className="relative z-10 w-full max-w-sm rounded-2xl border border-slate-800 bg-slate-900/60 p-8 text-center shadow-2xl shadow-purple-500/10 backdrop-blur-xl"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5 }}
          >
            <motion.div
                className="mb-6 inline-flex items-center justify-center rounded-full border border-purple-500/30 bg-purple-500/10 p-4 backdrop-blur-sm"
                animate={{
                  boxShadow: [
                    "0 0 20px rgba(168,85,247,0.2)",
                    "0 0 40px rgba(168,85,247,0.4)",
                    "0 0 20px rgba(168,85,247,0.2)",
                  ],
                }}
                transition={{ duration: 2, repeat: Infinity, ease: "easeInOut" }}
            >
              <Shield className="h-12 w-12 text-purple-400" />
            </motion.div>

            <h2 className="mb-3 bg-linear-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-2xl font-bold text-transparent">
              Verifying Access
            </h2>

            <div className="flex items-center justify-center gap-2 text-slate-400">
              <Loader2 className="h-5 w-5 animate-spin text-purple-400" />
              <span>Checking authentication...</span>
            </div>
          </motion.div>
        </div>
    );
  }

  return <>{children}</>;
}

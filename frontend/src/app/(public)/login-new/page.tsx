"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useAuthStore } from "@/store/auth-store";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

const loginSchema = z.object({
  email: z.string().email(),
  password: z.string().min(6),
});

type LoginFormData = z.infer<typeof loginSchema>;

export default function LoginPage() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);
  const login = useAuthStore((state) => state.login);

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = async (data: LoginFormData) => {
    try {
      setError(null);
      await login(data);

      // Read roles from the store after login
      const currentUser = useAuthStore.getState().user;
      if (currentUser?.roles.includes("ADMIN")) {
        router.push("/admin");
      } else if (currentUser?.roles.includes("INSTRUCTOR")) {
        router.push("/instructor");
      } else {
        router.push("/learner/dashboard");
      }
    } catch {
      setError("Invalid credentials. Please try again.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-b from-slate-950 via-purple-950 to-slate-900 p-6">
      <div className="w-full max-w-md bg-slate-900 p-8 rounded-xl border border-slate-800 shadow-lg">
        <h1 className="text-2xl font-bold text-purple-200 mb-6 text-center">
          Sign In
        </h1>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div>
            <Input
              type="email"
              placeholder="Email"
              className="h-12 border-slate-700 bg-slate-950/50 text-slate-100 placeholder:text-slate-600 focus:border-purple-500 focus:ring-purple-500/30"
              {...register("email")}
            />
            {errors.email && (
              <p className="text-red-400 text-sm mt-1">
                {errors.email.message}
              </p>
            )}
          </div>

          <div>
            <Input
              type="password"
              placeholder="Password"
              className="h-12 border-slate-700 bg-slate-950/50 text-slate-100 placeholder:text-slate-600 focus:border-purple-500 focus:ring-purple-500/30"
              {...register("password")}
            />
            {errors.password && (
              <p className="text-red-400 text-sm mt-1">
                {errors.password.message}
              </p>
            )}
          </div>

          {error && (
            <p className="text-red-400 text-sm text-center">
              {error}
            </p>
          )}

          <div className="text-right">
            <a
              href="/forgot-password"
              className="text-sm text-purple-400 hover:text-purple-300 transition-colors"
            >
              Forgot password?
            </a>
          </div>

          <Button
            type="submit"
            className="w-full bg-gradient-to-r from-purple-600 to-pink-500 hover:from-purple-500 hover:to-pink-400 text-white"
            disabled={isSubmitting}
          >
            {isSubmitting ? "Signing In..." : "Sign In"}
          </Button>
        </form>
      </div>
    </div>
  );
}

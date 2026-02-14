"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { apiClient } from "@/lib/api/client";
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

      const response = await apiClient.post("/api/auth/login", data);

      const token = response.data.token;
      const roles: string[] = response.data.roles || [];

      localStorage.setItem("token", token);

      // ✅ Correct routing for App Router
      if (roles.includes("ROLE_ADMIN")) {
        router.push("/admin");
      } else if (roles.includes("ROLE_INSTRUCTOR")) {
        router.push("/instructor");
      } else {
        router.push("/dashboard");
      }
    } catch (err: any) {
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

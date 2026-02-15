"use client";

import React, { useState, useEffect, useRef } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useAuthStore } from "@/store/auth-store";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { motion, useScroll, useTransform, useSpring } from "framer-motion";
import gsap from "gsap";
import Lenis from "lenis";
import { Loader2 } from "lucide-react";

const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL?.trim() || "http://localhost:8080";

const loginSchema = z.object({
  email: z.string().email(),
  password: z.string().min(6),
});

type LoginFormData = z.infer<typeof loginSchema>;

interface LoginApiResponse {
  token: string;
}

function decodeJwtPayload(token: string): { sub: string; roles: string[] } {
  const parts = token.split(".");
  if (parts.length !== 3) {
    throw new Error("Invalid JWT format");
  }
  const payload = JSON.parse(atob(parts[1]));
  return { sub: payload.sub, roles: payload.roles ?? [] };
}

export default function LoginPage() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);
  const login = useAuthStore((state) => state.login);
  const containerRef = useRef<HTMLDivElement>(null);

  // Lenis Smooth Scroll
  useEffect(() => {
    const lenis = new Lenis();
    function raf(time: number) {
      lenis.raf(time);
      requestAnimationFrame(raf);
    }
    requestAnimationFrame(raf);
    return () => lenis.destroy();
  }, []);

  // GSAP Orb Animation
  useEffect(() => {
    const ctx = gsap.context(() => {
      gsap.to(".orb", {
        y: -20,
        duration: 2,
        repeat: -1,
        yoyo: true,
        ease: "sine.inOut",
        stagger: 0.5,
      });
    }, containerRef);
    return () => ctx.revert();
  }, []);

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
      const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: data.email, password: data.password }),
      });

      if (!response.ok) throw new Error("Invalid credentials");

      const result: LoginApiResponse = await response.json();
      const { sub: email, roles } = decodeJwtPayload(result.token);

      login(result.token, { id: 0, email, roles });

      if (roles.includes("ADMIN")) router.push("/admin");
      else if (roles.includes("INSTRUCTOR")) router.push("/instructor");
      else router.push("/learner/dashboard");
    } catch {
      setError("Invalid credentials. Please try again.");
    }
  };

  return (
    <div ref={containerRef} className="min-h-screen flex items-center justify-center bg-slate-950 overflow-hidden relative">
      {/* Dynamic Background Orbs */}
      <div className="orb absolute top-[-10%] left-[-10%] w-96 h-96 bg-purple-600/30 rounded-full blur-3xl" />
      <div className="orb absolute bottom-[-10%] right-[-10%] w-96 h-96 bg-pink-600/20 rounded-full blur-3xl delay-1000" />

      <motion.div
        initial={{ y: 50, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ type: "spring", stiffness: 300, damping: 20 }}
        className="w-full max-w-md relative z-10"
      >
        <div className="bg-slate-900/80 backdrop-blur-xl p-8 rounded-2xl border border-slate-800 shadow-2xl">
          <motion.h1
            initial={{ scale: 0.9 }}
            animate={{ scale: 1 }}
            className="text-3xl font-bold bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent mb-2 text-center"
          >
            Welcome Back
          </motion.h1>
          <p className="text-slate-400 text-center mb-8">Sign in to continue your journey</p>

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            <div className="space-y-4">
              <motion.div whileFocus={{ scale: 1.02 }}>
                <Input
                  type="email"
                  placeholder="Email Address"
                  className="h-14 bg-slate-950/50 border-slate-700 text-slate-100 focus:border-purple-500 focus:ring-purple-500/20 transition-all rounded-xl pl-4"
                  {...register("email")}
                />
                {errors.email && <p className="text-red-400 text-sm mt-1 ml-1">{errors.email.message}</p>}
              </motion.div>

              <motion.div whileFocus={{ scale: 1.02 }}>
                <Input
                  type="password"
                  placeholder="Password"
                  className="h-14 bg-slate-950/50 border-slate-700 text-slate-100 focus:border-purple-500 focus:ring-purple-500/20 transition-all rounded-xl pl-4"
                  {...register("password")}
                />
                {errors.password && <p className="text-red-400 text-sm mt-1 ml-1">{errors.password.message}</p>}
              </motion.div>
            </div>

            {error && (
              <motion.div
                initial={{ opacity: 0, height: 0 }}
                animate={{ opacity: 1, height: "auto" }}
                className="p-3 rounded-lg bg-red-500/10 border border-red-500/20 text-red-400 text-sm text-center"
              >
                {error}
              </motion.div>
            )}

            <div className="flex justify-end">
              <a href="/forgot-password" className="text-sm text-purple-400 hover:text-purple-300 transition-colors">
                Forgot password?
              </a>
            </div>

            <motion.div whileHover={{ scale: 1.02 }} whileTap={{ scale: 0.98 }}>
              <Button
                type="submit"
                disabled={isSubmitting}
                className="w-full h-14 bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-500 hover:to-pink-500 text-white font-medium rounded-xl shadow-lg shadow-purple-900/20 transition-all"
              >
                {isSubmitting ? (
                  <div className="flex items-center gap-2">
                    <Loader2 className="w-5 h-5 animate-spin" />
                    <span>Signing In...</span>
                  </div>
                ) : (
                  "Sign In"
                )}
              </Button>
            </motion.div>
          </form>
        </div>

        <p className="text-center mt-6 text-slate-500 text-sm">
          Don't have an account?{" "}
          <a href="/signup" className="text-purple-400 hover:text-purple-300 font-medium">
            Sign up
          </a>
        </p>
      </motion.div>
    </div>
  );
}

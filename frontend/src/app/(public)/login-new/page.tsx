"use client";

import { useEffect, useRef, useState } from "react";
import { motion, useReducedMotion } from "framer-motion";
import { gsap } from "gsap";
import Lenis from "lenis";
import { Eye, EyeOff, LogIn, Sparkles } from "lucide-react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

const loginSchema = z.object({
  email: z.string().email("Enter a valid email address"),
  password: z
    .string()
    .min(8, "Password must be at least 8 characters")
    .regex(/[A-Z]/, "Password must include at least one uppercase letter")
    .regex(/\d/, "Password must include at least one number"),
});

type LoginFormValues = z.infer<typeof loginSchema>;

export default function LoginPage() {
  const router = useRouter();
  const prefersReducedMotion = useReducedMotion();
  const containerRef = useRef<HTMLDivElement>(null);
  const orbRef = useRef<HTMLDivElement>(null);
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const form = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    mode: "onBlur",
    defaultValues: {
      email: "",
      password: "",
    },
  });

  // Lenis smooth scroll
  useEffect(() => {
    if (prefersReducedMotion) return;

    const lenis = new Lenis({
      autoRaf: false,
      smoothWheel: true,
      lerp: 0.1,
    });

    let frameId = 0;
    const raf = (time: number) => {
      lenis.raf(time);
      frameId = requestAnimationFrame(raf);
    };
    frameId = requestAnimationFrame(raf);

    return () => {
      cancelAnimationFrame(frameId);
      lenis.destroy();
    };
  }, [prefersReducedMotion]);

  // GSAP entrance animation
  useEffect(() => {
    if (prefersReducedMotion || !containerRef.current) return;

    const ctx = gsap.context(() => {
      const tl = gsap.timeline({ defaults: { ease: "power3.out" } });

      tl.from(".hero-badge", {
        autoAlpha: 0,
        y: 30,
        duration: 0.8,
      })
        .from(".hero-title", {
          autoAlpha: 0,
          y: 40,
          duration: 0.9,
        }, "-=0.5")
        .from(".hero-subtitle", {
          autoAlpha: 0,
          y: 30,
          duration: 0.8,
        }, "-=0.6")
        .from(".login-card", {
          autoAlpha: 0,
          y: 50,
          scale: 0.95,
          duration: 1,
        }, "-=0.5");

      // Floating orb animation
      if (orbRef.current) {
        gsap.to(orbRef.current, {
          y: 20,
          x: 15,
          duration: 4,
          ease: "sine.inOut",
          repeat: -1,
          yoyo: true,
        });
      }
    }, containerRef);

    return () => ctx.revert();
  }, [prefersReducedMotion]);

  const handleSubmit = form.handleSubmit(async (values) => {
    setIsLoading(true);

    // TODO: Integrate with auth service
    console.log("Login attempt:", values.email);

    // Simulate login
    await new Promise((resolve) => setTimeout(resolve, 2000));

    // Navigate to dashboard
    router.push("/learner/dashboard");
  });

  return (
    <div
      ref={containerRef}
      className="relative min-h-screen overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900"
    >
      {/* Animated background orbs */}
      <div
        ref={orbRef}
        className="pointer-events-none absolute left-[10%] top-[20%] h-96 w-96 rounded-full bg-purple-500/20 blur-3xl"
      />
      <div className="pointer-events-none absolute right-[15%] top-[40%] h-80 w-80 rounded-full bg-pink-500/15 blur-3xl" />

      {/* Vignette */}
      <div className="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_center,transparent_40%,rgba(0,0,0,0.6)_100%)]" />

      {/* Grid overlay */}
      <div className="pointer-events-none absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] [mask-image:radial-gradient(ellipse_at_center,black_20%,transparent_70%)]" />

      {/* Main content */}
      <div className="relative flex min-h-screen items-center justify-center px-6 py-12">
        <div className="w-full max-w-md">
          {/* Hero section */}
          <div className="mb-10 text-center">
            <motion.div
              className="hero-badge mb-6 inline-flex items-center gap-2 rounded-full border border-purple-500/30 bg-purple-500/10 px-4 py-2 backdrop-blur-sm"
              whileHover={prefersReducedMotion ? {} : { scale: 1.05 }}
            >
              <Sparkles className="h-4 w-4 text-purple-400" />
              <span className="text-sm font-medium text-purple-300">
                Educator Platform
              </span>
            </motion.div>

            <h1 className="hero-title mb-4 bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-5xl font-bold text-transparent">
              Welcome Back
            </h1>

            <p className="hero-subtitle text-lg text-slate-400">
              Sign in to continue your learning journey
            </p>
          </div>

          {/* Login card */}
          <motion.div
            className="login-card"
            whileHover={prefersReducedMotion ? {} : { scale: 1.01 }}
            transition={{ type: "spring", stiffness: 300, damping: 25 }}
          >
            <Card className="border-slate-800/50 bg-slate-900/80 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
              <CardHeader className="space-y-1 pb-6">
                <CardTitle className="text-2xl text-slate-100">
                  Sign In
                </CardTitle>
                <CardDescription className="text-slate-400">
                  Enter your credentials to access your account
                </CardDescription>
              </CardHeader>

              <CardContent>
                <form onSubmit={handleSubmit} className="space-y-5" noValidate>
                  {/* Email */}
                  <div className="space-y-2">
                    <label className="block text-xs font-medium uppercase tracking-wider text-slate-400">
                      Email Address
                    </label>
                    <Input
                      type="email"
                      placeholder="you@example.com"
                      autoComplete="email"
                      disabled={isLoading}
                      className="h-12 border-slate-700 bg-slate-950/50 text-slate-100 placeholder:text-slate-600 focus:border-purple-500 focus:ring-purple-500/30"
                      {...form.register("email")}
                    />
                    {form.formState.errors.email && (
                      <motion.p
                        initial={{ opacity: 0, y: -4 }}
                        animate={{ opacity: 1, y: 0 }}
                        className="text-sm text-red-400"
                      >
                        {form.formState.errors.email.message}
                      </motion.p>
                    )}
                  </div>

                  {/* Password */}
                  <div className="space-y-2">
                    <label className="block text-xs font-medium uppercase tracking-wider text-slate-400">
                      Password
                    </label>
                    <div className="relative">
                      <Input
                        type={showPassword ? "text" : "password"}
                        placeholder="••••••••"
                        autoComplete="current-password"
                        disabled={isLoading}
                        className="h-12 border-slate-700 bg-slate-950/50 pr-11 text-slate-100 placeholder:text-slate-600 focus:border-purple-500 focus:ring-purple-500/30"
                        {...form.register("password")}
                      />
                      <button
                        type="button"
                        className="absolute inset-y-0 right-0 inline-flex items-center pr-3 text-slate-500 transition-colors hover:text-slate-300"
                        onClick={() => setShowPassword(!showPassword)}
                        aria-label={showPassword ? "Hide password" : "Show password"}
                      >
                        {showPassword ? (
                          <EyeOff className="h-5 w-5" />
                        ) : (
                          <Eye className="h-5 w-5" />
                        )}
                      </button>
                    </div>
                    {form.formState.errors.password && (
                      <motion.p
                        initial={{ opacity: 0, y: -4 }}
                        animate={{ opacity: 1, y: 0 }}
                        className="text-sm text-red-400"
                      >
                        {form.formState.errors.password.message}
                      </motion.p>
                    )}
                  </div>

                  {/* Submit button */}
                  <motion.div
                    whileTap={prefersReducedMotion ? {} : { scale: 0.98 }}
                  >
                    <Button
                      type="submit"
                      disabled={isLoading}
                      className="h-12 w-full bg-gradient-to-r from-purple-600 to-purple-500 font-semibold text-white shadow-lg shadow-purple-500/30 transition-all hover:from-purple-500 hover:to-purple-400 hover:shadow-purple-500/40"
                    >
                      {isLoading ? (
                        <span className="inline-flex items-center gap-2">
                          <motion.span
                            className="inline-block h-2 w-2 rounded-full bg-white"
                            animate={
                              prefersReducedMotion
                                ? {}
                                : { scale: [1, 1.3, 1] }
                            }
                            transition={{
                              repeat: Infinity,
                              duration: 1,
                              ease: "easeInOut",
                            }}
                          />
                          Signing in...
                        </span>
                      ) : (
                        <span className="inline-flex items-center gap-2">
                          <LogIn className="h-5 w-5" />
                          Sign In
                        </span>
                      )}
                    </Button>
                  </motion.div>
                </form>

                {/* Footer links */}
                <div className="mt-6 flex items-center justify-between text-sm">
                  <button
                    type="button"
                    className="text-slate-400 transition-colors hover:text-purple-400"
                  >
                    Forgot password?
                  </button>
                  <button
                    type="button"
                    className="text-purple-400 transition-colors hover:text-purple-300"
                    onClick={() => router.push("/register")}
                  >
                    Create account
                  </button>
                </div>
              </CardContent>
            </Card>
          </motion.div>

          {/* Bottom text */}
          <p className="mt-8 text-center text-sm text-slate-500">
            Need help? Contact your administrator
          </p>
        </div>
      </div>
    </div>
  );
}

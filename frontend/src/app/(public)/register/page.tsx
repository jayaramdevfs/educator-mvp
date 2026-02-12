"use client";

import { useEffect, useRef, useState } from "react";
import { motion, useReducedMotion } from "framer-motion";
import { gsap } from "gsap";
import Lenis from "lenis";
import { Eye, EyeOff, UserPlus, Sparkles, Check, X } from "lucide-react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

const registerSchema = z
  .object({
    email: z.string().email("Enter a valid email address"),
    password: z
      .string()
      .min(8, "Password must be at least 8 characters")
      .regex(/[A-Z]/, "Password must include at least one uppercase letter")
      .regex(/[a-z]/, "Password must include at least one lowercase letter")
      .regex(/\d/, "Password must include at least one number"),
    confirmPassword: z.string(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords do not match",
    path: ["confirmPassword"],
  });

type RegisterFormValues = z.infer<typeof registerSchema>;

interface PasswordStrength {
  score: number; // 0-4
  label: string;
  color: string;
  bgColor: string;
}

function calculatePasswordStrength(password: string): PasswordStrength {
  let score = 0;

  if (password.length >= 8) score++;
  if (password.length >= 12) score++;
  if (/[A-Z]/.test(password) && /[a-z]/.test(password)) score++;
  if (/\d/.test(password)) score++;
  if (/[^A-Za-z0-9]/.test(password)) score++;

  const levels: PasswordStrength[] = [
    { score: 0, label: "Very Weak", color: "text-red-500", bgColor: "bg-red-500" },
    { score: 1, label: "Weak", color: "text-orange-500", bgColor: "bg-orange-500" },
    { score: 2, label: "Fair", color: "text-yellow-500", bgColor: "bg-yellow-500" },
    { score: 3, label: "Good", color: "text-blue-500", bgColor: "bg-blue-500" },
    { score: 4, label: "Strong", color: "text-green-500", bgColor: "bg-green-500" },
  ];

  return levels[Math.min(score, 4)];
}

export default function RegisterPage() {
  const router = useRouter();
  const prefersReducedMotion = useReducedMotion();
  const containerRef = useRef<HTMLDivElement>(null);
  const orbRef = useRef<HTMLDivElement>(null);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [passwordValue, setPasswordValue] = useState("");

  const form = useForm<RegisterFormValues>({
    resolver: zodResolver(registerSchema),
    mode: "onChange",
    defaultValues: {
      email: "",
      password: "",
      confirmPassword: "",
    },
  });

  const passwordStrength = calculatePasswordStrength(passwordValue);

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
        .from(".register-card", {
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
    console.log("Register attempt:", values.email);

    // Simulate registration
    await new Promise((resolve) => setTimeout(resolve, 2000));

    // Navigate to login
    router.push("/login-new");
  });

  const passwordRequirements = [
    { met: passwordValue.length >= 8, label: "At least 8 characters" },
    { met: /[A-Z]/.test(passwordValue), label: "One uppercase letter" },
    { met: /[a-z]/.test(passwordValue), label: "One lowercase letter" },
    { met: /\d/.test(passwordValue), label: "One number" },
  ];

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
                Join Educator Platform
              </span>
            </motion.div>

            <h1 className="hero-title mb-4 bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-5xl font-bold text-transparent">
              Create Account
            </h1>

            <p className="hero-subtitle text-lg text-slate-400">
              Start your learning journey today
            </p>
          </div>

          {/* Register card */}
          <motion.div
            className="register-card"
            whileHover={prefersReducedMotion ? {} : { scale: 1.01 }}
            transition={{ type: "spring", stiffness: 300, damping: 25 }}
          >
            <Card className="border-slate-800/50 bg-slate-900/80 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
              <CardHeader className="space-y-1 pb-6">
                <CardTitle className="text-2xl text-slate-100">
                  Sign Up
                </CardTitle>
                <CardDescription className="text-slate-400">
                  Create your account to get started
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
                        autoComplete="new-password"
                        disabled={isLoading}
                        className="h-12 border-slate-700 bg-slate-950/50 pr-11 text-slate-100 placeholder:text-slate-600 focus:border-purple-500 focus:ring-purple-500/30"
                        {...form.register("password", {
                          onChange: (e) => setPasswordValue(e.target.value),
                        })}
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

                    {/* Password strength indicator */}
                    {passwordValue && (
                      <motion.div
                        initial={{ opacity: 0, height: 0 }}
                        animate={{ opacity: 1, height: "auto" }}
                        className="space-y-2"
                      >
                        {/* Strength bar */}
                        <div className="flex items-center gap-2">
                          <div className="flex-1 h-2 bg-slate-800 rounded-full overflow-hidden">
                            <motion.div
                              className={`h-full ${passwordStrength.bgColor}`}
                              initial={{ width: 0 }}
                              animate={{ width: `${(passwordStrength.score / 4) * 100}%` }}
                              transition={{ duration: 0.3 }}
                            />
                          </div>
                          <span className={`text-xs font-medium ${passwordStrength.color}`}>
                            {passwordStrength.label}
                          </span>
                        </div>

                        {/* Requirements checklist */}
                        <div className="space-y-1.5">
                          {passwordRequirements.map((req, index) => (
                            <motion.div
                              key={index}
                              initial={{ opacity: 0, x: -10 }}
                              animate={{ opacity: 1, x: 0 }}
                              transition={{ delay: index * 0.05 }}
                              className="flex items-center gap-2 text-xs"
                            >
                              {req.met ? (
                                <Check className="h-3.5 w-3.5 text-green-500" />
                              ) : (
                                <X className="h-3.5 w-3.5 text-slate-600" />
                              )}
                              <span className={req.met ? "text-slate-400" : "text-slate-600"}>
                                {req.label}
                              </span>
                            </motion.div>
                          ))}
                        </div>
                      </motion.div>
                    )}

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

                  {/* Confirm Password */}
                  <div className="space-y-2">
                    <label className="block text-xs font-medium uppercase tracking-wider text-slate-400">
                      Confirm Password
                    </label>
                    <div className="relative">
                      <Input
                        type={showConfirmPassword ? "text" : "password"}
                        placeholder="••••••••"
                        autoComplete="new-password"
                        disabled={isLoading}
                        className="h-12 border-slate-700 bg-slate-950/50 pr-11 text-slate-100 placeholder:text-slate-600 focus:border-purple-500 focus:ring-purple-500/30"
                        {...form.register("confirmPassword")}
                      />
                      <button
                        type="button"
                        className="absolute inset-y-0 right-0 inline-flex items-center pr-3 text-slate-500 transition-colors hover:text-slate-300"
                        onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                        aria-label={showConfirmPassword ? "Hide password" : "Show password"}
                      >
                        {showConfirmPassword ? (
                          <EyeOff className="h-5 w-5" />
                        ) : (
                          <Eye className="h-5 w-5" />
                        )}
                      </button>
                    </div>
                    {form.formState.errors.confirmPassword && (
                      <motion.p
                        initial={{ opacity: 0, y: -4 }}
                        animate={{ opacity: 1, y: 0 }}
                        className="text-sm text-red-400"
                      >
                        {form.formState.errors.confirmPassword.message}
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
                          Creating account...
                        </span>
                      ) : (
                        <span className="inline-flex items-center gap-2">
                          <UserPlus className="h-5 w-5" />
                          Create Account
                        </span>
                      )}
                    </Button>
                  </motion.div>
                </form>

                {/* Footer links */}
                <div className="mt-6 text-center text-sm">
                  <span className="text-slate-400">Already have an account? </span>
                  <button
                    type="button"
                    className="text-purple-400 transition-colors hover:text-purple-300"
                    onClick={() => router.push("/login-new")}
                  >
                    Sign in
                  </button>
                </div>
              </CardContent>
            </Card>
          </motion.div>

          {/* Bottom text */}
          <p className="mt-8 text-center text-sm text-slate-500">
            By creating an account, you agree to our Terms & Privacy Policy
          </p>
        </div>
      </div>
    </div>
  );
}

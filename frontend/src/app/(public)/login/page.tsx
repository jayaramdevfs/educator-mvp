"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import Lenis from "lenis";
import { gsap } from "gsap";
import { Eye, EyeOff, LogIn, ShieldCheck } from "lucide-react";
import {
  motion,
  useMotionTemplate,
  useMotionValue,
  useReducedMotion,
  useSpring,
  useTransform,
  useVelocity,
} from "framer-motion";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";
import { useForm } from "react-hook-form";
import { toast, Toaster } from "sonner";
import { z } from "zod";
import { Button, Card, CardContent, CardDescription, CardHeader, CardTitle, Input } from "@/components/ui";
import { springTokens } from "@/lib/motion/tokens";
import type { ApiClientError } from "@/lib/api/errors";
import { useAuthStore } from "@/store";
import type { LoginRequest, RoleName } from "@/types";

const loginSchema = z.object({
  email: z.email("Enter a valid email address"),
  password: z
    .string()
    .min(8, "Password must be at least 8 characters")
    .regex(/[A-Z]/, "Password must include at least one uppercase letter")
    .regex(/\d/, "Password must include at least one number"),
});

type LoginFormValues = z.infer<typeof loginSchema>;

const roleLandingPath: Record<RoleName, string> = {
  ADMIN: "/admin",
  INSTRUCTOR: "/instructor",
  STUDENT: "/learner",
};

const cinematicPillars = [
  "Spring physics",
  "Velocity-reactive depth",
  "Secure JWT routing",
];

function resolvePostLoginPath(roles: RoleName[] | undefined): string {
  if (!roles || roles.length === 0) {
    return "/learner";
  }

  if (roles.includes("ADMIN")) {
    return roleLandingPath.ADMIN;
  }
  if (roles.includes("INSTRUCTOR")) {
    return roleLandingPath.INSTRUCTOR;
  }
  return roleLandingPath.STUDENT;
}

function getErrorMessage(error: unknown): string {
  const fallback = "Login failed. Please try again.";
  if (!error || typeof error !== "object") {
    return fallback;
  }

  const maybeApiError = error as Partial<ApiClientError>;
  if (typeof maybeApiError.message !== "string" || !maybeApiError.message.trim()) {
    return fallback;
  }

  return maybeApiError.message;
}

export default function LoginPage() {
  const router = useRouter();
  const prefersReducedMotion = useReducedMotion();
  const [showPassword, setShowPassword] = useState(false);
  const [serverError, setServerError] = useState<string | null>(null);

  const status = useAuthStore((state) => state.status);
  const user = useAuthStore((state) => state.user);
  const hydrated = useAuthStore((state) => state.hydrated);
  const hydrate = useAuthStore((state) => state.hydrate);
  const login = useAuthStore((state) => state.login);

  const pageRef = useRef<HTMLElement>(null);
  const introBadgeRef = useRef<HTMLParagraphElement>(null);
  const introTitleRef = useRef<HTMLHeadingElement>(null);
  const introCopyRef = useRef<HTMLParagraphElement>(null);
  const introRailRef = useRef<HTMLDivElement>(null);
  const orbPrimaryRef = useRef<HTMLDivElement>(null);
  const orbAccentRef = useRef<HTMLDivElement>(null);
  const beamRef = useRef<HTMLDivElement>(null);
  const cardRef = useRef<HTMLDivElement>(null);
  const lightSweepRef = useRef<HTMLDivElement>(null);

  const sceneX = useMotionValue(0);
  const sceneY = useMotionValue(0);
  const cardX = useMotionValue(0);
  const cardY = useMotionValue(0);

  const velocityX = useVelocity(cardX);
  const velocityY = useVelocity(cardY);
  const cardSpeed = useTransform(() => {
    const vx = Math.abs(velocityX.get());
    const vy = Math.abs(velocityY.get());
    return Math.min(1, (vx + vy) / 80);
  });

  const rotateX = useSpring(
    useTransform(cardY, [-1, 1], [10, -10]),
    springTokens.smooth,
  );
  const rotateY = useSpring(
    useTransform(cardX, [-1, 1], [-10, 10]),
    springTokens.smooth,
  );

  const glowOpacity = useTransform(cardSpeed, [0, 1], [0.22, 0.58]);
  const glowBlur = useTransform(cardSpeed, [0, 1], [32, 58]);
  const cardShadow = useMotionTemplate`0 34px ${glowBlur}px rgba(15, 95, 102, ${glowOpacity})`;

  const orbPrimaryX = useSpring(
    useTransform(sceneX, [-1, 1], [-28, 28]),
    springTokens.float,
  );
  const orbPrimaryY = useSpring(
    useTransform(sceneY, [-1, 1], [-24, 24]),
    springTokens.float,
  );
  const orbAccentX = useSpring(
    useTransform(sceneX, [-1, 1], [22, -22]),
    springTokens.float,
  );
  const orbAccentY = useSpring(
    useTransform(sceneY, [-1, 1], [16, -16]),
    springTokens.float,
  );
  const vignetteOpacity = useTransform(cardSpeed, [0, 1], [0.34, 0.5]);
  const sweepOpacity = useTransform(cardSpeed, [0, 1], [0.14, 0.3]);

  const form = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    mode: "onBlur",
    defaultValues: {
      email: "",
      password: "",
    },
  });

  useEffect(() => {
    hydrate();
  }, [hydrate]);

  useEffect(() => {
    if (!hydrated || status !== "authenticated" || !user) {
      return;
    }
    router.replace(resolvePostLoginPath(user.roles));
  }, [hydrated, status, user, router]);

  useEffect(() => {
    if (prefersReducedMotion) {
      return;
    }

    const lenis = new Lenis({
      autoRaf: false,
      smoothWheel: true,
      lerp: 0.085,
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

  useEffect(() => {
    if (prefersReducedMotion) {
      return;
    }

    const ctx = gsap.context(() => {
      const introNodes = [
        introBadgeRef.current,
        introTitleRef.current,
        introCopyRef.current,
        introRailRef.current,
      ].filter(
        (
          node,
        ): node is HTMLParagraphElement | HTMLHeadingElement | HTMLDivElement =>
          node !== null,
      );

      const timeline = gsap.timeline({
        defaults: { duration: 0.86, ease: "power3.out" },
      });

      timeline.fromTo(
        introNodes,
        { autoAlpha: 0, y: 28 },
        { autoAlpha: 1, y: 0, stagger: 0.12 },
        0,
      );

      if (cardRef.current) {
        timeline.fromTo(
          cardRef.current,
          { autoAlpha: 0, y: 46, scale: 0.96, rotateX: -4 },
          { autoAlpha: 1, y: 0, scale: 1, rotateX: 0 },
          0.16,
        );
      }

      if (lightSweepRef.current) {
        gsap.fromTo(
          lightSweepRef.current,
          { xPercent: -130 },
          {
            xPercent: 150,
            duration: 2.7,
            ease: "power2.inOut",
            repeat: -1,
            repeatDelay: 1.15,
          },
        );
      }

      if (orbPrimaryRef.current && orbAccentRef.current && beamRef.current) {
        gsap.to(orbPrimaryRef.current, {
          y: 30,
          x: -24,
          duration: 5.5,
          ease: "sine.inOut",
          repeat: -1,
          yoyo: true,
        });
        gsap.to(orbAccentRef.current, {
          y: -26,
          x: 18,
          duration: 4.8,
          ease: "sine.inOut",
          repeat: -1,
          yoyo: true,
        });
        gsap.to(beamRef.current, {
          x: 42,
          duration: 6.3,
          ease: "sine.inOut",
          repeat: -1,
          yoyo: true,
        });
      }
    }, pageRef);

    return () => ctx.revert();
  }, [prefersReducedMotion]);

  const onSubmit = form.handleSubmit(async (values) => {
    setServerError(null);
    try {
      await login(values as LoginRequest);
      const nextUser = useAuthStore.getState().user;
      const nextPath = resolvePostLoginPath(nextUser?.roles);
      toast.success("Login successful");
      router.replace(nextPath);
    } catch (error: unknown) {
      const message = getErrorMessage(error);
      setServerError(message);
      toast.error(message);
    }
  });

  const handleScenePointerMove = (event: React.PointerEvent<HTMLElement>) => {
    if (prefersReducedMotion) {
      return;
    }
    const bounds = event.currentTarget.getBoundingClientRect();
    const x = ((event.clientX - bounds.left) / bounds.width) * 2 - 1;
    const y = ((event.clientY - bounds.top) / bounds.height) * 2 - 1;
    sceneX.set(x);
    sceneY.set(y);
  };

  const resetScenePointer = () => {
    sceneX.set(0);
    sceneY.set(0);
  };

  const handleCardPointerMove = (event: React.PointerEvent<HTMLDivElement>) => {
    if (prefersReducedMotion) {
      return;
    }
    const bounds = event.currentTarget.getBoundingClientRect();
    const x = ((event.clientX - bounds.left) / bounds.width) * 2 - 1;
    const y = ((event.clientY - bounds.top) / bounds.height) * 2 - 1;
    cardX.set(x);
    cardY.set(y);
  };

  const resetCardPointer = () => {
    cardX.set(0);
    cardY.set(0);
  };

  return (
    <main
      ref={pageRef}
      className="relative min-h-screen overflow-hidden bg-[radial-gradient(circle_at_15%_-5%,rgba(15,95,102,0.24),transparent_55%),radial-gradient(circle_at_85%_10%,rgba(187,122,42,0.22),transparent_52%),linear-gradient(135deg,#f0ebe0_0%,#ece7dc_38%,#f6f3ed_100%)]"
      onPointerMove={handleScenePointerMove}
      onPointerLeave={resetScenePointer}
    >
      <Toaster position="top-right" richColors closeButton />

      <motion.div
        className="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_center,transparent_36%,rgba(0,0,0,0.33)_100%)]"
        style={{ opacity: vignetteOpacity }}
      />
      <div className="pointer-events-none absolute inset-0 opacity-[0.1] [background-image:repeating-linear-gradient(0deg,rgba(29,34,40,0.06)_0px,rgba(29,34,40,0.06)_1px,transparent_1px,transparent_3px)]" />
      <div className="pointer-events-none absolute inset-x-0 top-0 h-14 bg-gradient-to-b from-black/26 to-transparent" />
      <div className="pointer-events-none absolute inset-x-0 bottom-0 h-16 bg-gradient-to-t from-black/26 to-transparent" />

      <motion.div
        ref={orbPrimaryRef}
        className="pointer-events-none absolute -left-28 top-[-130px] h-96 w-96 rounded-full bg-[radial-gradient(circle_at_center,rgba(15,95,102,0.42),rgba(15,95,102,0.02)_70%)] blur-2xl"
        style={{ x: orbPrimaryX, y: orbPrimaryY }}
      />
      <motion.div
        ref={orbAccentRef}
        className="pointer-events-none absolute right-[-100px] top-[12%] h-80 w-80 rounded-full bg-[radial-gradient(circle_at_center,rgba(187,122,42,0.36),rgba(187,122,42,0.02)_70%)] blur-2xl"
        style={{ x: orbAccentX, y: orbAccentY }}
      />
      <div
        ref={beamRef}
        className="pointer-events-none absolute left-[12%] top-[35%] h-[38%] w-[40%] rounded-full bg-[radial-gradient(circle_at_center,rgba(15,95,102,0.2),rgba(15,95,102,0.01)_70%)] blur-3xl"
      />

      <div className="relative mx-auto grid min-h-screen w-full max-w-6xl grid-cols-1 items-center gap-10 px-6 py-14 lg:grid-cols-[1.1fr_0.9fr]">
        <section className="max-w-2xl">
          <p
            ref={introBadgeRef}
            className="inline-flex items-center gap-2 rounded-full border border-[color:var(--border)] bg-[var(--surface)] px-3 py-1 font-mono text-xs tracking-wide text-[var(--accent-foreground)]"
          >
            <ShieldCheck className="h-3.5 w-3.5" />
            Auth Sequence
          </p>
          <h1
            ref={introTitleRef}
            className="mt-4 text-balance text-4xl font-semibold tracking-tight text-[var(--foreground)] md:text-6xl"
          >
            Cinematic login with responsive physics and velocity depth.
          </h1>
          <p
            ref={introCopyRef}
            className="mt-4 max-w-xl text-base leading-relaxed text-[var(--muted-foreground)] md:text-lg"
          >
            The interface reacts to motion like a camera rig: subtle parallax, velocity-aware
            lighting, and spring-based transitions while keeping auth behavior strict and predictable.
          </p>

          <div ref={introRailRef} className="mt-7 grid gap-3 sm:grid-cols-3">
            {cinematicPillars.map((item) => (
              <div
                key={item}
                className="rounded-[var(--radius-md)] border border-[color:var(--border)] bg-[var(--surface)]/90 px-3 py-2 font-mono text-[11px] uppercase tracking-wide text-[var(--muted-foreground)]"
              >
                {item}
              </div>
            ))}
          </div>
        </section>

        <div className="w-full max-w-xl lg:justify-self-end" style={{ perspective: "1400px" }}>
          <motion.div
            ref={cardRef}
            style={{
              rotateX: prefersReducedMotion ? 0 : rotateX,
              rotateY: prefersReducedMotion ? 0 : rotateY,
              boxShadow: cardShadow,
              transformStyle: "preserve-3d",
            }}
            onPointerMove={handleCardPointerMove}
            onPointerLeave={resetCardPointer}
            onBlur={resetCardPointer}
          >
            <div className="relative overflow-hidden rounded-[calc(var(--radius-lg)+6px)] bg-[linear-gradient(135deg,rgba(15,95,102,0.55),rgba(187,122,42,0.4),rgba(15,95,102,0.35))] p-[1px]">
              <motion.div
                ref={lightSweepRef}
                className="pointer-events-none absolute inset-y-0 left-[-45%] w-[42%] skew-x-[-18deg] bg-[linear-gradient(90deg,rgba(255,255,255,0)_0%,rgba(255,255,255,0.48)_50%,rgba(255,255,255,0)_100%)] mix-blend-screen"
                style={{ opacity: sweepOpacity }}
              />
              <Card className="relative gap-0 border-0 bg-[var(--surface)]/94 py-0 backdrop-blur-xl">
                <CardHeader className="px-6 pb-4 pt-6">
                  <CardTitle className="text-2xl">Welcome back</CardTitle>
                  <CardDescription>Log in to continue to your role dashboard.</CardDescription>
                </CardHeader>
                <CardContent className="pb-6">
                  <form className="space-y-5" onSubmit={onSubmit} noValidate>
                    <div className="space-y-2">
                      <label className="block text-xs font-medium uppercase tracking-wide text-[var(--muted-foreground)]">
                        Email
                      </label>
                      <Input
                        type="email"
                        autoComplete="email"
                        placeholder="you@educator.local"
                        disabled={status === "loading"}
                        className="h-11 bg-white/80"
                        {...form.register("email")}
                      />
                      {form.formState.errors.email ? (
                        <p className="text-sm text-[var(--destructive)]">
                          {form.formState.errors.email.message}
                        </p>
                      ) : null}
                    </div>

                    <div className="space-y-2">
                      <label className="block text-xs font-medium uppercase tracking-wide text-[var(--muted-foreground)]">
                        Password
                      </label>
                      <div className="relative">
                        <Input
                          type={showPassword ? "text" : "password"}
                          autoComplete="current-password"
                          placeholder="Minimum 8 chars, uppercase, number"
                          disabled={status === "loading"}
                          className="h-11 bg-white/80 pr-11"
                          {...form.register("password")}
                        />
                        <button
                          type="button"
                          className="absolute inset-y-0 right-0 inline-flex items-center pr-3 text-[var(--muted-foreground)]"
                          onClick={() => setShowPassword((v) => !v)}
                          aria-label={showPassword ? "Hide password" : "Show password"}
                        >
                          {showPassword ? (
                            <EyeOff className="h-4 w-4" />
                          ) : (
                            <Eye className="h-4 w-4" />
                          )}
                        </button>
                      </div>
                      {form.formState.errors.password ? (
                        <p className="text-sm text-[var(--destructive)]">
                          {form.formState.errors.password.message}
                        </p>
                      ) : null}
                    </div>

                    {serverError ? (
                      <motion.p
                        initial={prefersReducedMotion ? false : { opacity: 0, x: -10 }}
                        animate={prefersReducedMotion ? undefined : { opacity: 1, x: 0 }}
                        transition={springTokens.snappy}
                        className="rounded-md border border-[color:var(--destructive)]/30 bg-[color:var(--destructive)]/10 px-3 py-2 text-sm text-[var(--destructive)]"
                      >
                        {serverError}
                      </motion.p>
                    ) : null}

                    <motion.div
                      whileTap={prefersReducedMotion ? undefined : { scale: 0.985 }}
                      whileHover={prefersReducedMotion ? undefined : { scale: 1.01 }}
                      transition={springTokens.snappy}
                    >
                      <Button
                        type="submit"
                        disabled={status === "loading"}
                        className="h-11 w-full text-sm font-semibold"
                      >
                        {status === "loading" ? (
                          <span className="inline-flex items-center gap-2">
                            <motion.span
                              className="inline-block h-2.5 w-2.5 rounded-full bg-current"
                              animate={prefersReducedMotion ? undefined : { scale: [1, 1.7, 1] }}
                              transition={{
                                repeat: Number.POSITIVE_INFINITY,
                                duration: 0.8,
                                ease: "easeInOut",
                              }}
                            />
                            Signing in...
                          </span>
                        ) : (
                          <span className="inline-flex items-center gap-2">
                            <LogIn className="h-4 w-4" />
                            Sign in
                          </span>
                        )}
                      </Button>
                    </motion.div>
                  </form>

                  <div className="mt-4 flex items-center justify-between text-xs text-[var(--muted-foreground)]">
                    <span>Need access? Contact platform admin.</span>
                    <Link className="font-medium text-[var(--primary)] hover:underline" href="/">
                      Back to home
                    </Link>
                  </div>
                </CardContent>
              </Card>
            </div>
          </motion.div>
        </div>
      </div>
    </main>
  );
}

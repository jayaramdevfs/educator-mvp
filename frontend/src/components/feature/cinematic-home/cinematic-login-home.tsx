"use client";

import { useEffect, useRef, useState } from "react";
import { useReducedMotion } from "framer-motion";
import { gsap } from "gsap";
import Lenis from "lenis";
import { useRouter } from "next/navigation";
import { LoginForm } from "./login-form";
import { BookshelfScene } from "./bookshelf-scene";
import { DustParticles } from "./dust-particles";
import { StudentSilhouette } from "./student-silhouette";

export interface LoginAnimationState {
  emailFocused: boolean;
  passwordTyping: boolean;
  loginClicked: boolean;
  selectedBookIndex: number;
}

export function CinematicLoginHome() {
  const router = useRouter();
  const prefersReducedMotion = useReducedMotion();
  const containerRef = useRef<HTMLDivElement>(null);

  const [animationState, setAnimationState] = useState<LoginAnimationState>({
    emailFocused: false,
    passwordTyping: false,
    loginClicked: false,
    selectedBookIndex: -1,
  });

  // Smooth scroll setup
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

  // Cinematic entrance animation
  useEffect(() => {
    if (prefersReducedMotion || !containerRef.current) return;

    const ctx = gsap.context(() => {
      gsap.from(".cinematic-layer", {
        autoAlpha: 0,
        duration: 1.2,
        ease: "power2.out",
        stagger: 0.15,
      });
    }, containerRef);

    return () => ctx.revert();
  }, [prefersReducedMotion]);

  const handleEmailFocus = () => {
    setAnimationState((prev) => ({ ...prev, emailFocused: true, selectedBookIndex: 2 }));
  };

  const handlePasswordChange = (hasValue: boolean) => {
    setAnimationState((prev) => ({ ...prev, passwordTyping: hasValue }));
  };

  const handleLoginClick = async () => {
    setAnimationState((prev) => ({ ...prev, loginClicked: true }));

    // Wait for book open animation
    await new Promise((resolve) => setTimeout(resolve, 2400));

    // Route to dashboard (this will be replaced with actual auth redirect)
    router.push("/learner/dashboard");
  };

  return (
    <div
      ref={containerRef}
      className="relative min-h-screen overflow-hidden bg-gradient-to-br from-[#0f172a] via-[#1e293b] to-black"
      style={{ perspective: "1200px" }}
    >
      {/* Radial ambient lighting */}
      <div className="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_70%_40%,rgba(245,196,129,0.15),transparent_60%)] cinematic-layer" />

      {/* Vignette overlay */}
      <div className="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_center,transparent_35%,rgba(0,0,0,0.65)_100%)] cinematic-layer" />

      {/* Top shadow */}
      <div className="pointer-events-none absolute inset-x-0 top-0 h-32 bg-gradient-to-b from-black/50 to-transparent cinematic-layer" />

      {/* Bottom shadow */}
      <div className="pointer-events-none absolute inset-x-0 bottom-0 h-40 bg-gradient-to-t from-black/60 to-transparent cinematic-layer" />

      {/* Dust particles */}
      {!prefersReducedMotion && <DustParticles reducedMotion={false} />}

      {/* Main scene container */}
      <div className="relative mx-auto grid min-h-screen w-full max-w-7xl grid-cols-1 items-center gap-8 px-6 py-12 lg:grid-cols-[1.2fr_1fr]">

        {/* Left side: Login form + silhouette */}
        <div className="relative z-10 flex flex-col items-start justify-center space-y-8 cinematic-layer">
          <StudentSilhouette />

          <div className="w-full max-w-md">
            <LoginForm
              onEmailFocus={handleEmailFocus}
              onPasswordChange={handlePasswordChange}
              onLoginClick={handleLoginClick}
              isLoading={animationState.loginClicked}
            />
          </div>
        </div>

        {/* Right side: Bookshelf */}
        <div className="relative cinematic-layer">
          <BookshelfScene
            animationState={animationState}
            reducedMotion={prefersReducedMotion ?? false}
          />
        </div>
      </div>
    </div>
  );
}

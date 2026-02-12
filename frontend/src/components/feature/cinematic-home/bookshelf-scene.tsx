"use client";

import { useEffect, useRef } from "react";
import { motion } from "framer-motion";
import { gsap } from "gsap";
import { InteractiveBook } from "./interactive-book";
import type { LoginAnimationState } from "./cinematic-login-home";

interface BookData {
  id: number;
  title: string;
  color: string;
  height: string;
  glowColor: string;
}

const BOOKS: BookData[] = [
  { id: 0, title: "Algorithms", color: "from-[#8b4513] to-[#654321]", height: "h-48", glowColor: "rgba(245,196,129,0.4)" },
  { id: 1, title: "Physics", color: "from-[#1e3a8a] to-[#1e40af]", height: "h-52", glowColor: "rgba(96,165,250,0.4)" },
  { id: 2, title: "Mathematics", color: "from-[#78350f] to-[#92400e]", height: "h-44", glowColor: "rgba(251,191,36,0.4)" },
  { id: 3, title: "Chemistry", color: "from-[#134e4a] to-[#115e59]", height: "h-56", glowColor: "rgba(45,212,191,0.4)" },
  { id: 4, title: "History", color: "from-[#7c2d12] to-[#991b1b]", height: "h-50", glowColor: "rgba(248,113,113,0.4)" },
  { id: 5, title: "Literature", color: "from-[#4c1d95] to-[#5b21b6]", height: "h-48", glowColor: "rgba(192,132,252,0.4)" },
  { id: 6, title: "Biology", color: "from-[#166534] to-[#15803d]", height: "h-54", glowColor: "rgba(134,239,172,0.4)" },
];

interface BookshelfSceneProps {
  animationState: LoginAnimationState;
  reducedMotion: boolean;
}

export function BookshelfScene({ animationState, reducedMotion }: BookshelfSceneProps) {
  const shelfRef = useRef<HTMLDivElement>(null);
  const idleAnimationRef = useRef<gsap.core.Timeline | null>(null);

  // Continuous idle animations
  useEffect(() => {
    if (reducedMotion || !shelfRef.current) return;

    const ctx = gsap.context(() => {
      // Book 0: Occasional slide out and back
      gsap.timeline({ repeat: -1, repeatDelay: 8 })
        .to(`[data-book-id="0"]`, {
          x: 20,
          duration: 1.2,
          ease: "power2.out",
        })
        .to(`[data-book-id="0"]`, {
          x: 0,
          duration: 1.2,
          ease: "power2.inOut",
          delay: 2,
        });

      // Book 4: Gentle vertical shift
      gsap.timeline({ repeat: -1, repeatDelay: 6 })
        .to(`[data-book-id="4"]`, {
          y: -8,
          duration: 2.5,
          ease: "sine.inOut",
        })
        .to(`[data-book-id="4"]`, {
          y: 0,
          duration: 2.5,
          ease: "sine.inOut",
        });

      // Book 6: Subtle rotate wobble
      idleAnimationRef.current = gsap.timeline({ repeat: -1 })
        .to(`[data-book-id="6"]`, {
          rotateY: 3,
          duration: 4,
          ease: "sine.inOut",
        })
        .to(`[data-book-id="6"]`, {
          rotateY: -3,
          duration: 4,
          ease: "sine.inOut",
        });
    }, shelfRef);

    return () => ctx.revert();
  }, [reducedMotion]);

  // Email focus → Book 2 slides out
  useEffect(() => {
    if (reducedMotion || !animationState.emailFocused) return;

    gsap.to(`[data-book-id="2"]`, {
      x: 35,
      rotateY: -8,
      duration: 0.8,
      ease: "power2.out",
    });
  }, [animationState.emailFocused, reducedMotion]);

  // Password typing → Book 5 pulls out
  useEffect(() => {
    if (reducedMotion) return;

    gsap.to(`[data-book-id="5"]`, {
      x: animationState.passwordTyping ? 30 : 0,
      rotateY: animationState.passwordTyping ? -6 : 0,
      duration: 0.7,
      ease: "power2.out",
    });
  }, [animationState.passwordTyping, reducedMotion]);

  // Login clicked → Selected book opens
  useEffect(() => {
    if (reducedMotion || !animationState.loginClicked) return;

    const selectedBook = animationState.selectedBookIndex >= 0
      ? `[data-book-id="${animationState.selectedBookIndex}"]`
      : `[data-book-id="2"]`;

    const timeline = gsap.timeline();

    timeline
      .to(selectedBook, {
        x: 60,
        rotateY: -15,
        scale: 1.1,
        duration: 0.6,
        ease: "power2.out",
      })
      .to(`${selectedBook} .book-cover`, {
        rotateY: -125,
        transformOrigin: "left center",
        duration: 1.2,
        ease: "power3.inOut",
      }, "-=0.2")
      .to(`${selectedBook} .book-pages`, {
        opacity: 1,
        duration: 0.4,
      }, "-=0.8");
  }, [animationState.loginClicked, animationState.selectedBookIndex, reducedMotion]);

  return (
    <div
      ref={shelfRef}
      className="relative mx-auto w-full max-w-lg"
      style={{ transformStyle: "preserve-3d" }}
    >
      {/* Bookshelf background */}
      <motion.div
        className="absolute inset-0 -z-10 rounded-2xl bg-gradient-to-br from-[#3e2723] via-[#4e342e] to-[#3e2723] shadow-2xl"
        style={{
          transform: "translateZ(-40px)",
          boxShadow: "0 20px 60px rgba(0,0,0,0.7)",
        }}
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 1, ease: "easeOut" }}
      />

      {/* Shelf boards */}
      <div className="relative space-y-3 p-8">
        {/* Top shelf */}
        <div className="relative mb-6">
          <div className="absolute inset-x-0 -bottom-2 h-3 rounded-sm bg-gradient-to-b from-[#5d4037] to-[#4e342e] shadow-lg" />
          <div className="flex items-end justify-center gap-1 px-2">
            {BOOKS.slice(0, 4).map((book) => (
              <InteractiveBook
                key={book.id}
                book={book}
                reducedMotion={reducedMotion}
              />
            ))}
          </div>
        </div>

        {/* Bottom shelf */}
        <div className="relative">
          <div className="absolute inset-x-0 -bottom-2 h-3 rounded-sm bg-gradient-to-b from-[#5d4037] to-[#4e342e] shadow-lg" />
          <div className="flex items-end justify-center gap-1 px-2">
            {BOOKS.slice(4).map((book) => (
              <InteractiveBook
                key={book.id}
                book={book}
                reducedMotion={reducedMotion}
              />
            ))}
          </div>
        </div>
      </div>

      {/* Ambient shelf glow */}
      <div
        className="pointer-events-none absolute inset-0 rounded-2xl"
        style={{
          background: "radial-gradient(circle at 50% 50%, rgba(245,196,129,0.08), transparent 70%)",
        }}
      />
    </div>
  );
}

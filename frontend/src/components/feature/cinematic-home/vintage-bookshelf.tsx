"use client";

import { forwardRef, useEffect, useImperativeHandle, useRef } from "react";
import { gsap } from "gsap";
import {
  motion,
  useMotionValue,
  useSpring,
  useTransform,
  type MotionValue,
} from "framer-motion";
import { cn } from "@/lib/utils";
import {
  cinematicSpring,
  EMAIL_BOOK_INDEX,
  LOGIN_BOOK_INDEX,
  PASSWORD_BOOK_INDEX,
  SHIFTING_BOOK_INDEX,
  SHELF_BOOKS,
} from "@/components/feature/cinematic-home/constants";

export type ActiveBookKey = "email" | "password" | "login" | null;

export interface VintageBookshelfHandle {
  triggerEmailFocus: () => void;
  releaseEmailFocus: () => void;
  setPasswordProgress: (length: number) => void;
  resetInteractiveBooks: () => void;
  playLoginSequence: () => Promise<void>;
}

interface VintageBookshelfProps {
  reducedMotion: boolean;
  cursorX: MotionValue<number>;
  cursorY: MotionValue<number>;
  activeBook: ActiveBookKey;
}

export const VintageBookshelf = forwardRef<VintageBookshelfHandle, VintageBookshelfProps>(
  ({ reducedMotion, cursorX, cursorY, activeBook }, ref) => {
    const shelfRef = useRef<HTMLDivElement>(null);
    const bookRefs = useRef<Array<HTMLDivElement | null>>([]);
    const loginPageRef = useRef<HTMLDivElement | null>(null);
    const loopTimelineRef = useRef<gsap.core.Timeline | null>(null);

    const passwordPullRaw = useMotionValue(0);
    const passwordPull = useSpring(passwordPullRaw, cinematicSpring);

    const shelfRotateX = useSpring(
      useTransform(cursorY, [-1, 1], [2.6, -2.6]),
      cinematicSpring,
    );
    const shelfRotateY = useSpring(
      useTransform(cursorX, [-1, 1], [-6, 6]),
      cinematicSpring,
    );
    const shelfDepth = useSpring(
      useTransform(cursorX, [-1, 1], [-14, 14]),
      cinematicSpring,
    );

    useEffect(() => {
      if (reducedMotion) {
        return;
      }

      const slidingBook = bookRefs.current[LOGIN_BOOK_INDEX];
      const shiftingBook = bookRefs.current[SHIFTING_BOOK_INDEX];

      if (!slidingBook || !shiftingBook) {
        return;
      }

      const loopTimeline = gsap.timeline({
        repeat: -1,
        repeatDelay: 2.4,
      });

      loopTimeline
        .to(slidingBook, { x: 14, duration: 1.4, ease: "sine.inOut" })
        .to(slidingBook, { x: 0, duration: 1.3, ease: "sine.inOut" })
        .to(shiftingBook, { y: -4, duration: 1.8, ease: "sine.inOut" }, 0.35)
        .to(shiftingBook, { y: 0, duration: 1.8, ease: "sine.inOut" }, 2.0);

      loopTimelineRef.current = loopTimeline;
      return () => {
        loopTimelineRef.current?.kill();
        loopTimelineRef.current = null;
      };
    }, [reducedMotion]);

    useImperativeHandle(
      ref,
      () => ({
        triggerEmailFocus: () => {
          const emailBook = bookRefs.current[EMAIL_BOOK_INDEX];
          if (!emailBook) {
            return;
          }
          if (reducedMotion) {
            gsap.set(emailBook, { x: 16 });
            return;
          }
          gsap.to(emailBook, {
            x: 34,
            duration: 0.6,
            ease: "power2.out",
          });
        },
        releaseEmailFocus: () => {
          const emailBook = bookRefs.current[EMAIL_BOOK_INDEX];
          if (!emailBook) {
            return;
          }
          if (reducedMotion) {
            gsap.set(emailBook, { x: 0 });
            return;
          }
          gsap.to(emailBook, {
            x: 0,
            duration: 0.65,
            ease: "power2.out",
          });
        },
        setPasswordProgress: (length: number) => {
          const clamped = Math.min(34, Math.max(0, length * 2.6));
          passwordPullRaw.set(clamped);
        },
        resetInteractiveBooks: () => {
          const emailBook = bookRefs.current[EMAIL_BOOK_INDEX];
          if (emailBook) {
            gsap.to(emailBook, {
              x: 0,
              duration: reducedMotion ? 0 : 0.45,
              ease: "power2.out",
            });
          }
          const loginBook = bookRefs.current[LOGIN_BOOK_INDEX];
          if (loginBook) {
            gsap.to(loginBook, {
              x: 0,
              rotateY: SHELF_BOOKS[LOGIN_BOOK_INDEX]?.rotateY ?? 0,
              z: SHELF_BOOKS[LOGIN_BOOK_INDEX]?.zDepth ?? 0,
              duration: reducedMotion ? 0 : 0.45,
              ease: "power2.out",
            });
          }
          passwordPullRaw.set(0);

          const page = loginPageRef.current;
          if (page) {
            gsap.set(page, { rotateY: 0, transformOrigin: "left center" });
          }
          loopTimelineRef.current?.resume();
        },
        playLoginSequence: () =>
          new Promise<void>((resolve) => {
            const loginBook = bookRefs.current[LOGIN_BOOK_INDEX];
            if (!loginBook) {
              resolve();
              return;
            }
            if (reducedMotion) {
              gsap.set(loginBook, { x: 40 });
              resolve();
              return;
            }

            loopTimelineRef.current?.pause();

            const page = loginPageRef.current;
            const timeline = gsap.timeline({
              defaults: { ease: "power2.inOut" },
              onComplete: () => resolve(),
            });

            timeline.to(loginBook, {
              x: 46,
              rotateY: -18,
              z: 150,
              duration: 0.75,
            });

            if (page) {
              timeline.to(
                page,
                {
                  rotateY: -168,
                  transformOrigin: "left center",
                  duration: 0.95,
                  ease: "power1.inOut",
                },
                "-=0.34",
              );
            }

            timeline.to(loginBook, {
              x: 58,
              duration: 0.38,
            }, "-=0.45");
          }),
      }),
      [passwordPullRaw, reducedMotion],
    );

    return (
      <motion.aside
        ref={shelfRef}
        className="relative mx-auto w-full max-w-[420px] [transform-style:preserve-3d]"
        style={{
          rotateX: shelfRotateX,
          rotateY: shelfRotateY,
          z: shelfDepth,
        }}
      >
        <div className="pointer-events-none absolute -inset-7 rounded-[34px] bg-[radial-gradient(circle_at_48%_38%,rgba(245,196,129,0.22),transparent_65%)] blur-2xl" />

        <div className="relative overflow-hidden rounded-[28px] border border-[#6f4f34]/60 bg-gradient-to-b from-[#3a271a] via-[#2b1d14] to-[#1a120d] p-5 shadow-[0_40px_90px_rgba(0,0,0,0.6)] [transform-style:preserve-3d]">
          <div className="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_72%_24%,rgba(245,196,129,0.14),transparent_58%)]" />
          <div className="pointer-events-none absolute inset-0 bg-[linear-gradient(110deg,rgba(255,255,255,0.05),transparent_26%,transparent_72%,rgba(255,255,255,0.03))]" />

          <div className="relative grid gap-5">
            {[0, 1, 2].map((shelfIndex) => (
              <div
                key={`shelf-${shelfIndex}`}
                className="relative rounded-xl border border-[#4a3222] bg-gradient-to-b from-[#2f2017] to-[#231810] px-4 pb-4 pt-3 shadow-inner shadow-black/35"
              >
                <div className="mb-3 h-[1px] w-full bg-gradient-to-r from-transparent via-[#af8a68]/35 to-transparent" />
                <div className="flex items-end gap-[7px]">
                  {SHELF_BOOKS.slice(shelfIndex * 3, shelfIndex * 3 + 3).map((book, localIndex) => {
                    const globalIndex = shelfIndex * 3 + localIndex;
                    const highlighted =
                      (activeBook === "email" && globalIndex === EMAIL_BOOK_INDEX) ||
                      (activeBook === "password" && globalIndex === PASSWORD_BOOK_INDEX) ||
                      (activeBook === "login" && globalIndex === LOGIN_BOOK_INDEX);

                    return (
                      <motion.div
                        key={book.id}
                        ref={(node) => {
                          bookRefs.current[globalIndex] = node;
                        }}
                        className="relative [transform-style:preserve-3d] [will-change:transform]"
                        style={{
                          rotateY: book.rotateY,
                          z: book.zDepth,
                          x: globalIndex === PASSWORD_BOOK_INDEX ? passwordPull : 0,
                        }}
                      >
                        <motion.div
                          whileHover={
                            reducedMotion
                              ? undefined
                              : {
                                  y: -4,
                                  scale: 1.04,
                                  boxShadow: "0 0 24px rgba(245,196,129,0.36)",
                                }
                          }
                          className={cn(
                            "group relative w-8 cursor-pointer rounded-t-[6px] border border-black/35 bg-gradient-to-b text-[10px] font-medium uppercase tracking-[0.18em] text-[#f1dfc6]/95 shadow-[0_8px_18px_rgba(0,0,0,0.45)] [transform-style:preserve-3d] [will-change:transform]",
                            book.toneClass,
                            book.heightClass,
                            highlighted && "ring-1 ring-[#f2c78e]/80 ring-offset-1 ring-offset-transparent",
                          )}
                          animate={
                            reducedMotion
                              ? undefined
                              : {
                                  y: [0, -2, 0],
                                }
                          }
                          transition={
                            reducedMotion
                              ? undefined
                              : {
                                  ...cinematicSpring,
                                  duration: 4 + globalIndex * 0.25,
                                  repeat: Number.POSITIVE_INFINITY,
                                  ease: "easeInOut",
                                }
                          }
                        >
                          <div className="absolute inset-y-0 -right-[2px] w-[2px] bg-[#1a140f]/75" />
                          <div
                            className={cn(
                              "absolute inset-y-[3px] right-[1px] w-[2px] rounded-sm bg-gradient-to-b",
                              book.edgeClass,
                            )}
                          />

                          {globalIndex === LOGIN_BOOK_INDEX ? (
                            <div
                              ref={loginPageRef}
                              className="pointer-events-none absolute left-[2px] top-[3px] h-[calc(100%-6px)] w-[68%] origin-left rounded-r-[4px] border border-[#dec8a6]/40 bg-gradient-to-r from-[#f6e4c6] via-[#f1d9b2] to-[#e4c192] opacity-85 shadow-[0_2px_8px_rgba(0,0,0,0.28)]"
                            />
                          ) : null}

                          <span className="absolute bottom-2 left-1/2 -translate-x-1/2 rotate-90 whitespace-nowrap text-[8px] tracking-[0.22em] text-[#f3dfbf]/82">
                            {book.title}
                          </span>
                        </motion.div>
                      </motion.div>
                    );
                  })}
                </div>
                <div className="mt-3 h-[3px] rounded-full bg-gradient-to-r from-[#67472f] via-[#936b4b] to-[#5e412d]" />
              </div>
            ))}
          </div>
        </div>
      </motion.aside>
    );
  },
);

VintageBookshelf.displayName = "VintageBookshelf";

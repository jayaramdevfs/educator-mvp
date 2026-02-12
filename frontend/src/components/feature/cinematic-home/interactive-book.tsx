"use client";

import { motion, useMotionValue, useSpring, useTransform } from "framer-motion";
import { useState } from "react";

interface BookData {
  id: number;
  title: string;
  color: string;
  height: string;
  glowColor: string;
}

interface InteractiveBookProps {
  book: BookData;
  reducedMotion: boolean;
}

const springConfig = {
  stiffness: 200,
  damping: 20,
  mass: 0.8,
};

export function InteractiveBook({ book, reducedMotion }: InteractiveBookProps) {
  const [isHovered, setIsHovered] = useState(false);

  // Physics-based hover tilt
  const x = useMotionValue(0);
  const y = useMotionValue(0);

  const rotateX = useSpring(useTransform(y, [-0.5, 0.5], [5, -5]), springConfig);
  const rotateY = useSpring(useTransform(x, [-0.5, 0.5], [-5, 5]), springConfig);
  const scale = useSpring(1, springConfig);

  const handlePointerMove = (e: React.PointerEvent<HTMLDivElement>) => {
    if (reducedMotion) return;

    const rect = e.currentTarget.getBoundingClientRect();
    const centerX = rect.left + rect.width / 2;
    const centerY = rect.top + rect.height / 2;

    const deltaX = (e.clientX - centerX) / rect.width;
    const deltaY = (e.clientY - centerY) / rect.height;

    x.set(deltaX);
    y.set(deltaY);
  };

  const handlePointerLeave = () => {
    x.set(0);
    y.set(0);
    scale.set(1);
    setIsHovered(false);
  };

  const handlePointerEnter = () => {
    if (!reducedMotion) {
      scale.set(1.05);
    }
    setIsHovered(true);
  };

  return (
    <motion.div
      data-book-id={book.id}
      className={`relative ${book.height} w-12 cursor-pointer`}
      style={{
        transformStyle: "preserve-3d",
        rotateX: reducedMotion ? 0 : rotateX,
        rotateY: reducedMotion ? 0 : rotateY,
        scale: reducedMotion ? 1 : scale,
      }}
      onPointerMove={handlePointerMove}
      onPointerEnter={handlePointerEnter}
      onPointerLeave={handlePointerLeave}
      animate={
        reducedMotion
          ? {}
          : {
              y: [0, -3, 0],
            }
      }
      transition={{
        y: {
          duration: 5 + book.id * 0.4,
          repeat: Infinity,
          ease: "easeInOut",
        },
      }}
    >
      {/* Book spine (front) */}
      <motion.div
        className={`book-cover relative h-full w-full rounded-[3px] bg-gradient-to-b ${book.color} border border-black/40 shadow-[0_4px_12px_rgba(0,0,0,0.6)]`}
        style={{
          transformStyle: "preserve-3d",
          backfaceVisibility: "hidden",
        }}
      >
        {/* Book title */}
        <div className="absolute inset-x-0 top-1/2 -translate-y-1/2 rotate-0">
          <p className="px-1 text-center font-serif text-[9px] font-semibold uppercase tracking-wider text-amber-50/90">
            {book.title}
          </p>
        </div>

        {/* Spine highlight */}
        <div className="absolute inset-y-2 left-1 w-[2px] rounded-full bg-gradient-to-b from-white/30 to-transparent" />

        {/* Spine shadow */}
        <div className="absolute inset-y-2 right-1 w-[2px] rounded-full bg-gradient-to-b from-black/50 to-transparent" />

        {/* Book edge detail */}
        <div className="absolute inset-y-0 -right-[1px] w-1 bg-black/60" />
      </motion.div>

      {/* Pages (visible when book opens) */}
      <div
        className="book-pages pointer-events-none absolute inset-0 opacity-0"
        style={{
          transform: "translateX(-2px) translateZ(-2px)",
        }}
      >
        {[...Array(8)].map((_, i) => (
          <div
            key={i}
            className="absolute inset-0 rounded-[2px] border border-amber-100/20 bg-gradient-to-br from-amber-50 to-amber-100/95"
            style={{
              transform: `translateX(${i * 0.5}px) translateZ(-${i * 0.5}px)`,
              opacity: 1 - i * 0.08,
            }}
          />
        ))}
      </div>

      {/* Hover glow */}
      {isHovered && !reducedMotion && (
        <motion.div
          className="pointer-events-none absolute inset-0 rounded-[3px]"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.3 }}
          style={{
            boxShadow: `0 0 24px ${book.glowColor}`,
            filter: "blur(8px)",
          }}
        />
      )}

      {/* 3D depth shadow */}
      <div
        className="pointer-events-none absolute inset-0"
        style={{
          transform: "translateZ(-8px)",
          background: "rgba(0,0,0,0.3)",
          filter: "blur(4px)",
        }}
      />
    </motion.div>
  );
}

"use client";

import { memo } from "react";
import { motion } from "framer-motion";

interface DustParticle {
  id: number;
  x: number;
  y: number;
  size: number;
  drift: number;
  duration: number;
  delay: number;
  opacity: number;
}

const DUST_PARTICLES: DustParticle[] = Array.from({ length: 24 }, (_, id) => {
  const x = 6 + ((id * 17) % 90);
  const y = 8 + ((id * 29) % 82);
  const size = 1.2 + (id % 3) * 0.9;
  const drift = (id % 2 === 0 ? 1 : -1) * (10 + (id % 5) * 4);
  const duration = 9 + (id % 7) * 1.6;
  const delay = (id % 6) * 0.7;
  const opacity = 0.12 + (id % 4) * 0.05;
  return { id, x, y, size, drift, duration, delay, opacity };
});

interface DustParticlesProps {
  reducedMotion: boolean;
}

function DustParticlesComponent({ reducedMotion }: DustParticlesProps) {
  return (
    <div className="pointer-events-none absolute inset-0 overflow-hidden">
      {DUST_PARTICLES.map((particle) => (
        <motion.span
          key={particle.id}
          className="absolute rounded-full bg-[#f5d9aa] blur-[0.6px]"
          style={{
            left: `${particle.x}%`,
            top: `${particle.y}%`,
            width: particle.size,
            height: particle.size,
            opacity: particle.opacity,
          }}
          animate={
            reducedMotion
              ? undefined
              : {
                  x: [0, particle.drift, 0],
                  y: [0, -16, 0],
                  opacity: [particle.opacity * 0.5, particle.opacity, particle.opacity * 0.5],
                }
          }
          transition={{
            duration: particle.duration,
            delay: particle.delay,
            repeat: Number.POSITIVE_INFINITY,
            ease: "easeInOut",
          }}
        />
      ))}
    </div>
  );
}

export const DustParticles = memo(DustParticlesComponent);


"use client";

import { motion } from "framer-motion";
import { BookOpen, Sparkles } from "lucide-react";

export function StudentSilhouette() {
  return (
    <motion.div
      className="mb-8 flex items-center gap-4"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.8, delay: 0.1 }}
    >
      {/* Glowing icon */}
      <motion.div
        className="relative flex h-16 w-16 items-center justify-center rounded-full bg-gradient-to-br from-amber-600/20 to-amber-800/20 backdrop-blur-sm"
        animate={{
          boxShadow: [
            "0 0 20px rgba(245,196,129,0.3)",
            "0 0 30px rgba(245,196,129,0.5)",
            "0 0 20px rgba(245,196,129,0.3)",
          ],
        }}
        transition={{
          duration: 3,
          repeat: Infinity,
          ease: "easeInOut",
        }}
      >
        <BookOpen className="h-8 w-8 text-amber-500" />

        {/* Sparkle effect */}
        <motion.div
          className="absolute -right-1 -top-1"
          animate={{
            scale: [1, 1.2, 1],
            rotate: [0, 90, 0],
          }}
          transition={{
            duration: 4,
            repeat: Infinity,
            ease: "easeInOut",
          }}
        >
          <Sparkles className="h-4 w-4 text-amber-400" />
        </motion.div>
      </motion.div>

      {/* Text */}
      <div>
        <h2 className="bg-gradient-to-r from-amber-200 via-amber-100 to-amber-200 bg-clip-text text-3xl font-bold text-transparent">
          Your Learning Journey
        </h2>
        <p className="mt-1 text-slate-400">
          Awaits in this digital sanctuary
        </p>
      </div>
    </motion.div>
  );
}

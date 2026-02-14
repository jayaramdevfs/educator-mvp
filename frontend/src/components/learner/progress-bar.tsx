"use client";

import React from "react";

interface ProgressBarProps {
  value: number; // 0 - 100
  showLabel?: boolean;
}

export const ProgressBar: React.FC<ProgressBarProps> = ({
  value,
  showLabel = true,
}) => {
  const safeValue = Math.min(100, Math.max(0, value));

  return (
    <div className="w-full">
      <div className="flex justify-between mb-1">
        {showLabel && (
          <span className="text-xs font-medium text-purple-300">
            Progress
          </span>
        )}
        {showLabel && (
          <span className="text-xs font-medium text-purple-300">
            {safeValue}%
          </span>
        )}
      </div>

      <div className="w-full bg-slate-800 rounded-full h-2 overflow-hidden">
        <div
          className="h-2 rounded-full bg-gradient-to-r from-purple-600 to-pink-500 transition-all duration-500"
          style={{ width: `${safeValue}%` }}
        />
      </div>
    </div>
  );
};

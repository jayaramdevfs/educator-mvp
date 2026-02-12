"use client";

import { Toaster } from "sonner";

export function ToastProvider() {
  return (
    <Toaster
      position="top-right"
      closeButton
      richColors
      expand={false}
      duration={4000}
      toastOptions={{
        classNames: {
          toast: "border-slate-800 bg-slate-900/95 backdrop-blur-xl shadow-2xl",
          title: "text-slate-100 font-medium",
          description: "text-slate-400",
          success: "border-green-500/20 [&>svg]:text-green-500",
          error: "border-red-500/20 [&>svg]:text-red-500",
          warning: "border-yellow-500/20 [&>svg]:text-yellow-500",
          info: "border-purple-500/20 [&>svg]:text-purple-500",
          closeButton: "bg-slate-800 border-slate-700 text-slate-400 hover:bg-slate-700 hover:text-slate-200",
        },
      }}
    />
  );
}

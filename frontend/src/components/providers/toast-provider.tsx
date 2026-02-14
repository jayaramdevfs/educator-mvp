"use client";

import * as React from "react";
import { Toaster as SonnerToaster } from "sonner";

/**
 * Centralized Toast Provider
 * Uses Sonner internally
 * Must be exported for use in RootLayout
 */

export function Toaster() {
  return (
    <SonnerToaster
      position="top-right"
      richColors
      closeButton
      expand
    />
  );
}

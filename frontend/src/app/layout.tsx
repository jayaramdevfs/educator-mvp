import "./globals.css";
import React from "react";
import { QueryProvider } from "@/components/providers/query-provider";
import { Toaster } from "@/components/providers/toast-provider";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <QueryProvider>
          {children}
          <Toaster />
        </QueryProvider>
      </body>
    </html>
  );
}

"use client";

import React, { useState } from "react";
import Link from "next/link";
import { TopNav, PublicFooter } from "@/components/layout";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Mail, ArrowLeft, Loader2, CheckCircle2 } from "lucide-react";
import { apiPost } from "@/lib/api/client";
import { toast } from "sonner";

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const [sent, setSent] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      await apiPost("/api/auth/forgot-password", { email });
      setSent(true);
    } catch {
      toast.error("Failed to send reset email. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-950">
      <TopNav />
      <main className="flex min-h-[calc(100vh-4rem)] items-center justify-center px-4">
        <div className="w-full max-w-md">
          <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-8 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
            {sent ? (
              <div className="text-center">
                <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-green-500/10">
                  <CheckCircle2 className="h-7 w-7 text-green-400" />
                </div>
                <h1 className="text-xl font-bold text-slate-100">Check your email</h1>
                <p className="mt-2 text-sm text-slate-400">
                  If an account exists with <span className="text-slate-200">{email}</span>,
                  you&apos;ll receive a password reset link shortly.
                </p>
                <Link href="/login-new">
                  <Button
                    variant="ghost"
                    className="mt-6 text-purple-400 hover:text-purple-300"
                  >
                    <ArrowLeft className="mr-2 h-4 w-4" />
                    Back to Sign In
                  </Button>
                </Link>
              </div>
            ) : (
              <>
                <h1 className="text-xl font-bold text-slate-100">Forgot password?</h1>
                <p className="mt-2 text-sm text-slate-400">
                  Enter your email and we&apos;ll send you a reset link.
                </p>

                <form onSubmit={handleSubmit} className="mt-6 space-y-4">
                  <div>
                    <label htmlFor="email" className="mb-1.5 block text-sm font-medium text-slate-300">
                      Email address
                    </label>
                    <div className="relative">
                      <Mail className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-500" />
                      <Input
                        id="email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="you@example.com"
                        required
                        className="border-slate-700 bg-slate-800/50 pl-9 text-slate-200"
                      />
                    </div>
                  </div>

                  <Button
                    type="submit"
                    disabled={loading}
                    className="w-full bg-gradient-to-r from-purple-600 to-pink-500 text-white hover:from-purple-500 hover:to-pink-400"
                  >
                    {loading ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : null}
                    Send Reset Link
                  </Button>
                </form>

                <div className="mt-4 text-center">
                  <Link
                    href="/login-new"
                    className="text-sm text-purple-400 hover:text-purple-300"
                  >
                    <ArrowLeft className="mr-1 inline h-3 w-3" />
                    Back to Sign In
                  </Link>
                </div>
              </>
            )}
          </div>
        </div>
      </main>
      <PublicFooter />
    </div>
  );
}

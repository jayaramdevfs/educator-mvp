"use client";

import React, { useState, Suspense } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import Link from "next/link";
import { TopNav, PublicFooter } from "@/components/layout";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Lock, ArrowLeft, Loader2, CheckCircle2 } from "lucide-react";
import { apiPost } from "@/lib/api/client";
import { toast } from "sonner";

function ResetPasswordForm() {
  const searchParams = useSearchParams();
  const router = useRouter();
  const token = searchParams.get("token") ?? "";

  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      toast.error("Passwords do not match.");
      return;
    }
    if (newPassword.length < 6) {
      toast.error("Password must be at least 6 characters.");
      return;
    }
    setLoading(true);
    try {
      await apiPost("/api/auth/reset-password", { token, newPassword });
      setSuccess(true);
    } catch {
      toast.error("Failed to reset password. The link may have expired.");
    } finally {
      setLoading(false);
    }
  };

  if (!token) {
    return (
      <div className="text-center">
        <h1 className="text-xl font-bold text-slate-100">Invalid Reset Link</h1>
        <p className="mt-2 text-sm text-slate-400">
          This password reset link is invalid or has expired.
        </p>
        <Link href="/forgot-password">
          <Button
            variant="ghost"
            className="mt-4 text-purple-400 hover:text-purple-300"
          >
            Request a new link
          </Button>
        </Link>
      </div>
    );
  }

  if (success) {
    return (
      <div className="text-center">
        <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-green-500/10">
          <CheckCircle2 className="h-7 w-7 text-green-400" />
        </div>
        <h1 className="text-xl font-bold text-slate-100">Password Reset!</h1>
        <p className="mt-2 text-sm text-slate-400">
          Your password has been changed. You can now sign in with your new password.
        </p>
        <Link href="/login-new">
          <Button className="mt-6 bg-gradient-to-r from-purple-600 to-pink-500 text-white hover:from-purple-500 hover:to-pink-400">
            Sign In
          </Button>
        </Link>
      </div>
    );
  }

  return (
    <>
      <h1 className="text-xl font-bold text-slate-100">Set New Password</h1>
      <p className="mt-2 text-sm text-slate-400">
        Enter your new password below.
      </p>

      <form onSubmit={handleSubmit} className="mt-6 space-y-4">
        <div>
          <label htmlFor="newPassword" className="mb-1.5 block text-sm font-medium text-slate-300">
            New Password
          </label>
          <div className="relative">
            <Lock className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-500" />
            <Input
              id="newPassword"
              type="password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              required
              minLength={6}
              className="border-slate-700 bg-slate-800/50 pl-9 text-slate-200"
            />
          </div>
        </div>

        <div>
          <label htmlFor="confirmPassword" className="mb-1.5 block text-sm font-medium text-slate-300">
            Confirm Password
          </label>
          <div className="relative">
            <Lock className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-500" />
            <Input
              id="confirmPassword"
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
              minLength={6}
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
          Reset Password
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
  );
}

export default function ResetPasswordPage() {
  return (
    <div className="min-h-screen bg-slate-950">
      <TopNav />
      <main className="flex min-h-[calc(100vh-4rem)] items-center justify-center px-4">
        <div className="w-full max-w-md">
          <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-8 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
            <Suspense fallback={<div className="text-slate-400">Loading...</div>}>
              <ResetPasswordForm />
            </Suspense>
          </div>
        </div>
      </main>
      <PublicFooter />
    </div>
  );
}

"use client";

import React, { useState } from "react";
import { useAuthStore } from "@/store/auth-store";
import { apiPut } from "@/lib/api/client";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { User, Lock, Mail, Loader2, CheckCircle2 } from "lucide-react";
import { toast } from "sonner";

export default function SettingsPage() {
  const user = useAuthStore((state) => state.user);

  const [name, setName] = useState(user?.email?.split("@")[0] ?? "");
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [saving, setSaving] = useState(false);
  const [changingPassword, setChangingPassword] = useState(false);

  const handleSaveProfile = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      await apiPut("/api/learner/profile", { name });
      toast.success("Profile updated successfully!");
    } catch {
      toast.error("Failed to update profile.");
    } finally {
      setSaving(false);
    }
  };

  const handleChangePassword = async (e: React.FormEvent) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      toast.error("Passwords do not match.");
      return;
    }
    if (newPassword.length < 6) {
      toast.error("Password must be at least 6 characters.");
      return;
    }
    setChangingPassword(true);
    try {
      await apiPut("/api/learner/password", {
        currentPassword,
        newPassword,
      });
      toast.success("Password changed successfully!");
      setCurrentPassword("");
      setNewPassword("");
      setConfirmPassword("");
    } catch {
      toast.error("Failed to change password. Check your current password.");
    } finally {
      setChangingPassword(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-950 via-purple-950/20 to-slate-950">
      <div className="mx-auto max-w-2xl px-4 py-8 sm:px-6">
        <div className="mb-8">
          <h1 className="text-2xl font-bold text-slate-100">Profile & Settings</h1>
          <p className="mt-1 text-sm text-slate-400">Manage your account details.</p>
        </div>

        {/* Profile Section */}
        <form onSubmit={handleSaveProfile} className="mb-8 rounded-xl border border-slate-800 bg-slate-900/60 p-6">
          <h2 className="mb-4 flex items-center gap-2 text-lg font-semibold text-slate-200">
            <User className="h-5 w-5 text-purple-400" />
            Profile Information
          </h2>

          <div className="space-y-4">
            <div>
              <label className="mb-1.5 block text-sm font-medium text-slate-300">
                Email
              </label>
              <div className="flex items-center gap-2 rounded-lg border border-slate-700 bg-slate-800/50 px-3 py-2 text-sm text-slate-400">
                <Mail className="h-4 w-4" />
                {user?.email ?? "—"}
              </div>
              <p className="mt-1 text-xs text-slate-500">Email cannot be changed.</p>
            </div>

            <div>
              <label htmlFor="name" className="mb-1.5 block text-sm font-medium text-slate-300">
                Display Name
              </label>
              <Input
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="border-slate-700 bg-slate-800/50 text-slate-200"
              />
            </div>

            <Button
              type="submit"
              disabled={saving}
              className="bg-gradient-to-r from-purple-600 to-pink-500 text-white hover:from-purple-500 hover:to-pink-400"
            >
              {saving ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : null}
              Save Changes
            </Button>
          </div>
        </form>

        {/* Password Section */}
        <form onSubmit={handleChangePassword} className="rounded-xl border border-slate-800 bg-slate-900/60 p-6">
          <h2 className="mb-4 flex items-center gap-2 text-lg font-semibold text-slate-200">
            <Lock className="h-5 w-5 text-purple-400" />
            Change Password
          </h2>

          <div className="space-y-4">
            <div>
              <label htmlFor="currentPassword" className="mb-1.5 block text-sm font-medium text-slate-300">
                Current Password
              </label>
              <Input
                id="currentPassword"
                type="password"
                value={currentPassword}
                onChange={(e) => setCurrentPassword(e.target.value)}
                className="border-slate-700 bg-slate-800/50 text-slate-200"
                required
              />
            </div>

            <div>
              <label htmlFor="newPassword" className="mb-1.5 block text-sm font-medium text-slate-300">
                New Password
              </label>
              <Input
                id="newPassword"
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                className="border-slate-700 bg-slate-800/50 text-slate-200"
                required
                minLength={6}
              />
            </div>

            <div>
              <label htmlFor="confirmPassword" className="mb-1.5 block text-sm font-medium text-slate-300">
                Confirm New Password
              </label>
              <Input
                id="confirmPassword"
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className="border-slate-700 bg-slate-800/50 text-slate-200"
                required
                minLength={6}
              />
            </div>

            <Button
              type="submit"
              disabled={changingPassword}
              className="bg-gradient-to-r from-purple-600 to-pink-500 text-white hover:from-purple-500 hover:to-pink-400"
            >
              {changingPassword ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : null}
              Change Password
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}

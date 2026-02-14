"use client";

import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { BookOpen, User, LogOut, Settings, Menu, X, Shield, GraduationCap, Bell as BellIcon, Award } from "lucide-react";
import Link from "next/link";
import { useRouter, usePathname } from "next/navigation";
import { useAuthStore } from "@/store/auth-store";
import { Button } from "@/components/ui/button";
import { NotificationBell } from "@/components/learner/notification-bell";

export function TopNav() {
  const router = useRouter();
  const pathname = usePathname();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);

  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const user = useAuthStore((state) => state.user);
  const logout = useAuthStore((state) => state.logout);

  const handleLogout = () => {
    logout();
    router.push("/login-new");
  };

  const getRoleLinks = () => {
    if (!user) return [];

    if (user.roles.includes("ADMIN")) {
      return [
        { href: "/admin", label: "Admin Dashboard", icon: Shield },
        { href: "/admin/courses", label: "Courses", icon: BookOpen },
        { href: "/admin/users", label: "Users", icon: User },
      ];
    }

    if (user.roles.includes("INSTRUCTOR")) {
      return [
        { href: "/instructor", label: "Instructor Dashboard", icon: GraduationCap },
        { href: "/instructor/analytics", label: "Analytics", icon: BookOpen },
      ];
    }

    return [
      { href: "/learner/dashboard", label: "Dashboard", icon: BookOpen },
      { href: "/learner/notifications", label: "Notifications", icon: BellIcon },
      { href: "/learner/certificates", label: "Certificates", icon: Award },
    ];
  };

  const roleLinks = getRoleLinks();

  return (
    <nav className="sticky top-0 z-50 border-b border-slate-800 bg-slate-900/80 backdrop-blur-xl">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div className="flex h-16 items-center justify-between">
          {/* Logo */}
          <Link href="/" className="flex items-center gap-2 group">
            <motion.div
              className="flex h-10 w-10 items-center justify-center rounded-lg bg-gradient-to-br from-purple-400 to-pink-400 shadow-lg shadow-purple-500/30"
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
            >
              <BookOpen className="h-5 w-5 text-white" />
            </motion.div>
            <span className="bg-gradient-to-r from-purple-300 to-pink-200 bg-clip-text text-lg font-bold text-transparent transition-all group-hover:from-purple-200 group-hover:to-pink-100">
              Educator
            </span>
          </Link>

          {/* Desktop Navigation */}
          {isAuthenticated && (
            <div className="hidden md:flex items-center gap-1">
              {roleLinks.map((link) => {
                const Icon = link.icon;
                const isActive = pathname === link.href;

                return (
                  <Link key={link.href} href={link.href}>
                    <motion.div
                      className={`flex items-center gap-2 rounded-lg border-b-2 px-3 py-2 text-sm font-medium transition-colors ${
                        isActive
                          ? "border-purple-400 bg-purple-500/10 text-purple-400"
                          : "border-transparent text-slate-300 hover:bg-slate-800/60 hover:text-purple-300"
                      }`}
                      whileHover={{ scale: 1.02 }}
                      whileTap={{ scale: 0.98 }}
                    >
                      <Icon className="h-4 w-4" />
                      {link.label}
                    </motion.div>
                  </Link>
                );
              })}
            </div>
          )}

          {/* Right side */}
          <div className="flex items-center gap-4">
            {isAuthenticated ? (
              <>
                {/* Notification Bell (Desktop) */}
                <div className="hidden md:block">
                  <NotificationBell />
                </div>

                {/* User Menu (Desktop) */}
                <div className="relative hidden md:block">
                  <motion.button
                    className="flex items-center gap-2 rounded-lg border border-slate-700 bg-slate-800 px-3 py-2 text-sm font-medium text-slate-300 transition-colors hover:border-purple-500/30 hover:bg-slate-700 hover:text-purple-200"
                    onClick={() => setIsUserMenuOpen(!isUserMenuOpen)}
                    whileHover={{ scale: 1.02 }}
                    whileTap={{ scale: 0.98 }}
                  >
                    <User className="h-4 w-4" />
                    {user?.email.split("@")[0]}
                  </motion.button>

                  <AnimatePresence>
                    {isUserMenuOpen && (
                      <>
                        {/* Backdrop */}
                        <div
                          className="fixed inset-0 z-40"
                          onClick={() => setIsUserMenuOpen(false)}
                        />

                        {/* Dropdown */}
                        <motion.div
                          initial={{ opacity: 0, y: -10 }}
                          animate={{ opacity: 1, y: 0 }}
                          exit={{ opacity: 0, y: -10 }}
                          className="absolute right-0 z-50 mt-2 w-48 rounded-lg border border-slate-700 bg-slate-800 py-2 shadow-xl shadow-purple-500/10"
                        >
                          <div className="border-b border-slate-700 px-4 py-2">
                            <p className="text-xs text-slate-400">Signed in as</p>
                            <p className="truncate text-sm font-medium text-slate-200">
                              {user?.email}
                            </p>
                          </div>

                          <Link href="/learner/settings">
                            <button
                              className="flex w-full items-center gap-2 px-4 py-2 text-sm text-slate-300 transition-colors hover:bg-slate-700 hover:text-purple-200"
                              onClick={() => setIsUserMenuOpen(false)}
                            >
                              <Settings className="h-4 w-4" />
                              Settings
                            </button>
                          </Link>

                          <button
                            className="flex w-full items-center gap-2 px-4 py-2 text-sm text-red-400 transition-colors hover:bg-slate-700"
                            onClick={handleLogout}
                          >
                            <LogOut className="h-4 w-4" />
                            Sign Out
                          </button>
                        </motion.div>
                      </>
                    )}
                  </AnimatePresence>
                </div>

                {/* Mobile Menu Button */}
                <button
                  className="flex h-10 w-10 items-center justify-center rounded-lg text-slate-300 transition-colors hover:bg-slate-800/60 hover:text-purple-300 md:hidden"
                  onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
                >
                  {isMobileMenuOpen ? (
                    <X className="h-5 w-5" />
                  ) : (
                    <Menu className="h-5 w-5" />
                  )}
                </button>
              </>
            ) : (
              <div className="flex items-center gap-2">
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => router.push("/login-new")}
                  className="text-slate-300 hover:bg-slate-800/60 hover:text-purple-300"
                >
                  Sign In
                </Button>
                <Button
                  size="sm"
                  onClick={() => router.push("/register")}
                  className="bg-gradient-to-r from-purple-600 to-purple-500 text-white shadow-lg shadow-purple-500/30 hover:from-purple-500 hover:to-purple-400"
                >
                  Get Started
                </Button>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Mobile Menu */}
      <AnimatePresence>
        {isMobileMenuOpen && isAuthenticated && (
          <motion.div
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: "auto" }}
            exit={{ opacity: 0, height: 0 }}
            className="border-t border-slate-800 md:hidden"
          >
            <div className="px-4 py-4 space-y-2">
              {roleLinks.map((link) => {
                const Icon = link.icon;
                const isActive = pathname === link.href;

                return (
                  <Link key={link.href} href={link.href}>
                    <div
                      className={`flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium ${
                        isActive
                          ? "bg-purple-500/10 text-purple-400"
                          : "text-slate-300 hover:bg-slate-800/60 hover:text-purple-300"
                      }`}
                      onClick={() => setIsMobileMenuOpen(false)}
                    >
                      <Icon className="h-4 w-4" />
                      {link.label}
                    </div>
                  </Link>
                );
              })}

              <div className="border-t border-slate-800 pt-2 mt-2">
                <Link href="/learner/settings">
                  <div
                    className="flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium text-slate-300 transition-colors hover:bg-slate-800/60 hover:text-purple-300"
                    onClick={() => setIsMobileMenuOpen(false)}
                  >
                    <Settings className="h-4 w-4" />
                    Settings
                  </div>
                </Link>

                <button
                  className="flex w-full items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium text-red-400"
                  onClick={() => {
                    setIsMobileMenuOpen(false);
                    handleLogout();
                  }}
                >
                  <LogOut className="h-4 w-4" />
                  Sign Out
                </button>
              </div>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </nav>
  );
}

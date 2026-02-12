"use client";

import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { BookOpen, User, LogOut, Settings, Menu, X, Shield, GraduationCap } from "lucide-react";
import Link from "next/link";
import { useRouter, usePathname } from "next/navigation";
import { useAuthStore } from "@/store/auth-store";
import { Button } from "@/components/ui/button";

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
      { href: "/learner/courses", label: "My Courses", icon: BookOpen },
      { href: "/learner/certificates", label: "Certificates", icon: GraduationCap },
    ];
  };

  const roleLinks = getRoleLinks();

  return (
    <nav className="sticky top-0 z-50 border-b border-slate-800/50 bg-slate-900/95 backdrop-blur-xl">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div className="flex h-16 items-center justify-between">
          {/* Logo */}
          <Link href="/" className="flex items-center gap-2 group">
            <motion.div
              className="flex h-10 w-10 items-center justify-center rounded-lg bg-gradient-to-br from-purple-600 to-purple-500 shadow-lg shadow-purple-500/30"
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
            >
              <BookOpen className="h-5 w-5 text-white" />
            </motion.div>
            <span className="text-lg font-bold bg-gradient-to-r from-purple-200 to-pink-200 bg-clip-text text-transparent group-hover:from-purple-300 group-hover:to-pink-300 transition-all">
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
                      className={`flex items-center gap-2 rounded-lg px-3 py-2 text-sm font-medium transition-colors ${
                        isActive
                          ? "bg-purple-500/10 text-purple-300"
                          : "text-slate-400 hover:bg-slate-800 hover:text-slate-200"
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
                {/* User Menu (Desktop) */}
                <div className="relative hidden md:block">
                  <motion.button
                    className="flex items-center gap-2 rounded-lg border border-slate-700 bg-slate-800/50 px-3 py-2 text-sm font-medium text-slate-200 hover:bg-slate-800"
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
                          className="absolute right-0 z-50 mt-2 w-48 rounded-lg border border-slate-800 bg-slate-900 py-2 shadow-xl"
                        >
                          <div className="px-4 py-2 border-b border-slate-800">
                            <p className="text-xs text-slate-400">Signed in as</p>
                            <p className="text-sm font-medium text-slate-200 truncate">
                              {user?.email}
                            </p>
                          </div>

                          <Link href="/learner/settings">
                            <button
                              className="flex w-full items-center gap-2 px-4 py-2 text-sm text-slate-300 hover:bg-slate-800"
                              onClick={() => setIsUserMenuOpen(false)}
                            >
                              <Settings className="h-4 w-4" />
                              Settings
                            </button>
                          </Link>

                          <button
                            className="flex w-full items-center gap-2 px-4 py-2 text-sm text-red-400 hover:bg-slate-800"
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
                  className="md:hidden flex items-center justify-center h-10 w-10 rounded-lg text-slate-400 hover:bg-slate-800 hover:text-slate-200"
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
                  className="text-slate-400 hover:text-slate-200"
                >
                  Sign In
                </Button>
                <Button
                  size="sm"
                  onClick={() => router.push("/register")}
                  className="bg-gradient-to-r from-purple-600 to-purple-500 text-white hover:from-purple-500 hover:to-purple-400"
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
                          ? "bg-purple-500/10 text-purple-300"
                          : "text-slate-400"
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
                    className="flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium text-slate-400"
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

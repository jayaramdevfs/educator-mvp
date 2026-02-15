"use client";

import { useState } from "react";
import { motion } from "framer-motion";
import { Eye, EyeOff, LogIn } from "lucide-react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

const loginSchema = z.object({
  email: z.string().email("Enter a valid email address"),
  password: z
    .string()
    .min(8, "Password must be at least 8 characters")
    .regex(/[A-Z]/, "Password must include at least one uppercase letter")
    .regex(/\d/, "Password must include at least one number"),
});

type LoginFormValues = z.infer<typeof loginSchema>;

interface LoginFormProps {
  onEmailFocus: () => void;
  onPasswordChange: (hasValue: boolean) => void;
  onLoginClick: () => Promise<void>;
  isLoading: boolean;
}

export function LoginForm({
  onEmailFocus,
  onPasswordChange,
  onLoginClick,
  isLoading,
}: LoginFormProps) {
  const [showPassword, setShowPassword] = useState(false);

  const form = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    mode: "onBlur",
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const handleSubmit = form.handleSubmit(async () => {
    await onLoginClick();
  });

  return (
    <motion.div
      initial={{ opacity: 0, x: -40 }}
      animate={{ opacity: 1, x: 0 }}
      transition={{ duration: 0.8, delay: 0.3 }}
    >
      <Card className="border-amber-900/20 bg-gradient-to-br from-slate-900/95 via-slate-800/95 to-slate-900/95 backdrop-blur-xl">
        <CardHeader>
          <CardTitle className="text-2xl font-bold text-amber-50">
            Welcome to Educator
          </CardTitle>
          <CardDescription className="text-slate-400">
            Step into your learning sanctuary
          </CardDescription>
        </CardHeader>

        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-5" noValidate>
            {/* Email field */}
            <div className="space-y-2">
              <label className="block text-xs font-medium uppercase tracking-wider text-slate-400">
                Email
              </label>
              <Input
                type="email"
                placeholder="your@email.com"
                autoComplete="email"
                disabled={isLoading}
                className="h-11 border-slate-700 bg-slate-950/50 text-slate-100 placeholder:text-slate-600 focus:border-amber-600 focus:ring-amber-600/30"
                {...form.register("email")}
                onFocus={onEmailFocus}
              />
              {form.formState.errors.email && (
                <motion.p
                  initial={{ opacity: 0, y: -4 }}
                  animate={{ opacity: 1, y: 0 }}
                  className="text-sm text-red-400"
                >
                  {form.formState.errors.email.message}
                </motion.p>
              )}
            </div>

            {/* Password field */}
            <div className="space-y-2">
              <label className="block text-xs font-medium uppercase tracking-wider text-slate-400">
                Password
              </label>
              <div className="relative">
                <Input
                  type={showPassword ? "text" : "password"}
                  placeholder="••••••••"
                  autoComplete="current-password"
                  disabled={isLoading}
                  className="h-11 border-slate-700 bg-slate-950/50 pr-11 text-slate-100 placeholder:text-slate-600 focus:border-amber-600 focus:ring-amber-600/30"
                  {...form.register("password")}
                  onChange={(e) => {
                    form.register("password").onChange(e);
                    onPasswordChange(e.target.value.length > 0);
                  }}
                />
                <button
                  type="button"
                  className="absolute inset-y-0 right-0 inline-flex items-center pr-3 text-slate-500 hover:text-slate-300"
                  onClick={() => setShowPassword(!showPassword)}
                  aria-label={showPassword ? "Hide password" : "Show password"}
                >
                  {showPassword ? (
                    <EyeOff className="h-4 w-4" />
                  ) : (
                    <Eye className="h-4 w-4" />
                  )}
                </button>
              </div>
              {form.formState.errors.password && (
                <motion.p
                  initial={{ opacity: 0, y: -4 }}
                  animate={{ opacity: 1, y: 0 }}
                  className="text-sm text-red-400"
                >
                  {form.formState.errors.password.message}
                </motion.p>
              )}
            </div>

            {/* Submit button */}
            <motion.div whileTap={{ scale: 0.98 }}>
              <Button
                type="submit"
                disabled={isLoading}
                className="h-12 w-full bg-gradient-to-r from-amber-600 to-amber-700 font-semibold text-white shadow-lg shadow-amber-900/30 hover:from-amber-500 hover:to-amber-600"
              >
                {isLoading ? (
                  <span className="inline-flex items-center gap-2">
                    <motion.span
                      className="inline-block h-2 w-2 rounded-full bg-white"
                      animate={{ scale: [1, 1.4, 1] }}
                      transition={{
                        repeat: Infinity,
                        duration: 1,
                        ease: "easeInOut",
                      }}
                    />
                    Opening your library...
                  </span>
                ) : (
                  <span className="inline-flex items-center gap-2">
                    <LogIn className="h-4 w-4" />
                    Enter Library
                  </span>
                )}
              </Button>
            </motion.div>
          </form>

          <div className="mt-5 text-center text-xs text-slate-500">
            Need access? Contact your administrator
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}

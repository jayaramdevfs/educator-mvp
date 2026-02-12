"use client";

import { motion } from "framer-motion";
import { BookOpen, Award, Clock, TrendingUp } from "lucide-react";
import { AuthenticatedGuard } from "@/components/auth";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

export default function LearnerDashboardPage() {
  return (
    <AuthenticatedGuard>
      <div className="min-h-screen bg-gradient-to-br from-slate-950 via-blue-950 to-slate-900 p-6">
        <div className="mx-auto max-w-7xl">
          {/* Header */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="mb-8"
          >
            <h1 className="mb-2 bg-gradient-to-r from-blue-200 to-cyan-200 bg-clip-text text-4xl font-bold text-transparent">
              Your Dashboard
            </h1>
            <p className="text-slate-400">
              Welcome back! Continue your learning journey.
            </p>
          </motion.div>

          {/* Stats Grid */}
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
            <StatsCard
              icon={<BookOpen className="h-6 w-6 text-cyan-400" />}
              label="Enrolled Courses"
              value="5"
              delay={0}
            />
            <StatsCard
              icon={<Clock className="h-6 w-6 text-blue-400" />}
              label="Hours Learned"
              value="24"
              delay={0.1}
            />
            <StatsCard
              icon={<Award className="h-6 w-6 text-purple-400" />}
              label="Certificates"
              value="2"
              delay={0.2}
            />
            <StatsCard
              icon={<TrendingUp className="h-6 w-6 text-green-400" />}
              label="Progress"
              value="67%"
              delay={0.3}
            />
          </div>

          {/* Main Content */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.4 }}
            className="mt-8"
          >
            <Card className="border-slate-800 bg-slate-900/50 backdrop-blur-xl">
              <CardHeader>
                <CardTitle className="text-slate-100">Continue Learning</CardTitle>
                <CardDescription className="text-slate-400">
                  Pick up where you left off
                </CardDescription>
              </CardHeader>
              <CardContent>
                <p className="text-slate-400">
                  This is a protected learner dashboard. You can only see this if you&apos;re authenticated.
                </p>
              </CardContent>
            </Card>
          </motion.div>
        </div>
      </div>
    </AuthenticatedGuard>
  );
}

function StatsCard({
  icon,
  label,
  value,
  delay,
}: {
  icon: React.ReactNode;
  label: string;
  value: string;
  delay: number;
}) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay }}
    >
      <Card className="border-slate-800 bg-slate-900/50 backdrop-blur-xl">
        <CardContent className="p-6">
          <div className="flex items-center gap-4">
            <div className="rounded-lg bg-slate-800/50 p-3">{icon}</div>
            <div>
              <p className="text-sm text-slate-400">{label}</p>
              <p className="text-2xl font-bold text-slate-100">{value}</p>
            </div>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}

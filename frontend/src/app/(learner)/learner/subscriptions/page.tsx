"use client";

import React, { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { apiGet } from "@/lib/api/client";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import {
  CreditCard,
  Clock,
  CheckCircle2,
  XCircle,
  AlertCircle,
  Loader2,
  ShieldCheck,
  CalendarDays,
} from "lucide-react";

/* ---------- Types ---------- */

interface Subscription {
  subscriptionId: string;
  planId: string;
  status: string;
  startAt: string;
  expiresAt: string;
  remainingDays: number;
}

interface PaymentRecord {
  paymentId: string;
  planId: string;
  amount: number;
  status: string;
  provider: string;
  providerPaymentId: string;
  createdAt: string;
  completedAt: string | null;
}

type TabKey = "subscriptions" | "payments";

/* ---------- Helpers ---------- */

function formatDate(dateStr: string | null): string {
  if (!dateStr) return "—";
  return new Date(dateStr).toLocaleDateString("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
}

function formatCurrency(amount: number): string {
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
  }).format(amount);
}

function statusColor(status: string): string {
  switch (status.toUpperCase()) {
    case "ACTIVE":
      return "text-green-400 bg-green-500/10";
    case "EXPIRED":
      return "text-red-400 bg-red-500/10";
    case "COMPLETED":
      return "text-green-400 bg-green-500/10";
    case "INITIATED":
      return "text-amber-400 bg-amber-500/10";
    case "FAILED":
      return "text-red-400 bg-red-500/10";
    default:
      return "text-slate-400 bg-slate-500/10";
  }
}

function StatusIcon({ status }: { status: string }) {
  switch (status.toUpperCase()) {
    case "ACTIVE":
    case "COMPLETED":
      return <CheckCircle2 className="h-4 w-4" />;
    case "FAILED":
    case "EXPIRED":
      return <XCircle className="h-4 w-4" />;
    case "INITIATED":
      return <AlertCircle className="h-4 w-4" />;
    default:
      return <Clock className="h-4 w-4" />;
  }
}

/* ---------- Page ---------- */

export default function LearnerSubscriptionsPage() {
  const [activeTab, setActiveTab] = useState<TabKey>("subscriptions");

  const subsQuery = useQuery({
    queryKey: ["learner-subscriptions"],
    queryFn: () => apiGet<Subscription[]>("/api/learner/subscriptions/my"),
  });

  const paymentsQuery = useQuery({
    queryKey: ["learner-payment-history"],
    queryFn: () =>
      apiGet<PaymentRecord[]>("/api/learner/subscriptions/payments/history"),
    enabled: activeTab === "payments",
  });

  const tabs: { key: TabKey; label: string; icon: React.ReactNode }[] = [
    {
      key: "subscriptions",
      label: "My Subscriptions",
      icon: <ShieldCheck className="h-4 w-4" />,
    },
    {
      key: "payments",
      label: "Payment History",
      icon: <CreditCard className="h-4 w-4" />,
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-950 via-purple-950/30 to-slate-950">
      <div className="mx-auto max-w-6xl px-4 py-8 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="mb-8 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h1 className="bg-gradient-to-r from-purple-200 via-pink-200 to-purple-200 bg-clip-text text-3xl font-bold text-transparent">
              Subscriptions
            </h1>
            <p className="mt-1 text-slate-400">
              Manage your subscriptions and view payment history.
            </p>
          </div>
          <Link href="/pricing">
            <Button className="bg-gradient-to-r from-purple-600 to-pink-500 text-white shadow-lg shadow-purple-500/20 hover:from-purple-500 hover:to-pink-400">
              Browse Plans
            </Button>
          </Link>
        </div>

        {/* Tabs */}
        <div className="mb-6 flex gap-1 rounded-lg border border-slate-800 bg-slate-900/60 p-1 w-fit">
          {tabs.map((tab) => (
            <button
              key={tab.key}
              onClick={() => setActiveTab(tab.key)}
              className={`flex items-center gap-2 rounded-md px-4 py-2 text-sm font-medium transition-all ${
                activeTab === tab.key
                  ? "bg-purple-500/20 text-purple-300 shadow-sm"
                  : "text-slate-400 hover:text-slate-200"
              }`}
            >
              {tab.icon}
              {tab.label}
            </button>
          ))}
        </div>

        {/* Tab Content */}
        {activeTab === "subscriptions" && (
          <SubscriptionsTab
            data={subsQuery.data}
            isLoading={subsQuery.isLoading}
            isError={subsQuery.isError}
          />
        )}

        {activeTab === "payments" && (
          <PaymentsTab
            data={paymentsQuery.data}
            isLoading={paymentsQuery.isLoading}
            isError={paymentsQuery.isError}
          />
        )}
      </div>
    </div>
  );
}

/* ---------- Subscriptions Tab ---------- */

function SubscriptionsTab({
  data,
  isLoading,
  isError,
}: {
  data: Subscription[] | undefined;
  isLoading: boolean;
  isError: boolean;
}) {
  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-20">
        <Loader2 className="h-8 w-8 animate-spin text-purple-400" />
      </div>
    );
  }

  if (isError) {
    return (
      <div className="rounded-xl border border-red-500/30 bg-red-500/5 p-6 text-center text-red-300">
        Failed to load subscriptions. Please refresh the page.
      </div>
    );
  }

  const subscriptions = data ?? [];

  if (subscriptions.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center rounded-2xl border border-slate-800 bg-slate-900/50 px-6 py-16 text-center">
        <div className="mb-4 flex h-16 w-16 items-center justify-center rounded-2xl bg-purple-500/10">
          <ShieldCheck className="h-8 w-8 text-purple-400" />
        </div>
        <h2 className="mb-2 text-xl font-semibold text-slate-200">
          No active subscriptions
        </h2>
        <p className="mb-6 max-w-sm text-slate-400">
          Get started by purchasing a subscription plan.
        </p>
        <Link href="/pricing">
          <Button className="bg-gradient-to-r from-purple-600 to-pink-500 text-white shadow-lg shadow-purple-500/20 hover:from-purple-500 hover:to-pink-400">
            Browse Plans
          </Button>
        </Link>
      </div>
    );
  }

  return (
    <div className="grid gap-4 md:grid-cols-2">
      {subscriptions.map((sub) => {
        const totalDays = Math.max(
          Math.ceil(
            (new Date(sub.expiresAt).getTime() -
              new Date(sub.startAt).getTime()) /
              (1000 * 60 * 60 * 24),
          ),
          1,
        );
        const progressPct = Math.min(
          Math.max(
            ((totalDays - sub.remainingDays) / totalDays) * 100,
            0,
          ),
          100,
        );

        return (
          <div
            key={sub.subscriptionId}
            className="rounded-2xl border border-slate-800 bg-slate-900/60 p-5 shadow-xl shadow-purple-500/10 backdrop-blur-xl"
          >
            <div className="mb-3 flex items-center justify-between">
              <span
                className={`inline-flex items-center gap-1.5 rounded-full px-2.5 py-0.5 text-xs font-medium ${statusColor(sub.status)}`}
              >
                <StatusIcon status={sub.status} />
                {sub.status}
              </span>
              <span className="text-xs text-slate-500">
                {sub.remainingDays} day{sub.remainingDays !== 1 ? "s" : ""}{" "}
                remaining
              </span>
            </div>

            <div className="mb-4 flex items-center gap-3">
              <CalendarDays className="h-5 w-5 text-purple-400" />
              <div className="text-sm text-slate-300">
                <span>{formatDate(sub.startAt)}</span>
                <span className="mx-2 text-slate-600">-</span>
                <span>{formatDate(sub.expiresAt)}</span>
              </div>
            </div>

            {/* Progress bar */}
            <div className="space-y-1.5">
              <div className="flex justify-between text-xs text-slate-500">
                <span>Usage</span>
                <span>{Math.round(progressPct)}%</span>
              </div>
              <div className="h-2 w-full overflow-hidden rounded-full bg-slate-800">
                <div
                  className="h-full rounded-full bg-gradient-to-r from-purple-500 to-pink-500 transition-all"
                  style={{ width: `${progressPct}%` }}
                />
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
}

/* ---------- Payments Tab ---------- */

function PaymentsTab({
  data,
  isLoading,
  isError,
}: {
  data: PaymentRecord[] | undefined;
  isLoading: boolean;
  isError: boolean;
}) {
  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-20">
        <Loader2 className="h-8 w-8 animate-spin text-purple-400" />
      </div>
    );
  }

  if (isError) {
    return (
      <div className="rounded-xl border border-red-500/30 bg-red-500/5 p-6 text-center text-red-300">
        Failed to load payment history. Please refresh the page.
      </div>
    );
  }

  const payments = data ?? [];

  if (payments.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center rounded-2xl border border-slate-800 bg-slate-900/50 px-6 py-16 text-center">
        <div className="mb-4 flex h-16 w-16 items-center justify-center rounded-2xl bg-purple-500/10">
          <CreditCard className="h-8 w-8 text-purple-400" />
        </div>
        <h2 className="mb-2 text-xl font-semibold text-slate-200">
          No payments yet
        </h2>
        <p className="max-w-sm text-slate-400">
          Your payment history will appear here after your first purchase.
        </p>
      </div>
    );
  }

  return (
    <div className="overflow-hidden rounded-2xl border border-slate-800 bg-slate-900/60 backdrop-blur-xl">
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead>
            <tr className="border-b border-slate-800 text-left text-xs font-medium uppercase tracking-wider text-slate-500">
              <th className="px-5 py-3">Payment ID</th>
              <th className="px-5 py-3">Amount</th>
              <th className="px-5 py-3">Status</th>
              <th className="px-5 py-3">Provider</th>
              <th className="px-5 py-3">Created</th>
              <th className="px-5 py-3">Completed</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800/50">
            {payments.map((p) => (
              <tr
                key={p.paymentId}
                className="transition-colors hover:bg-slate-800/30"
              >
                <td className="whitespace-nowrap px-5 py-3 font-mono text-xs text-slate-400">
                  {p.paymentId.slice(0, 8)}...
                </td>
                <td className="whitespace-nowrap px-5 py-3 text-sm font-semibold text-slate-200">
                  {formatCurrency(p.amount)}
                </td>
                <td className="whitespace-nowrap px-5 py-3">
                  <span
                    className={`inline-flex items-center gap-1.5 rounded-full px-2.5 py-0.5 text-xs font-medium ${statusColor(p.status)}`}
                  >
                    <StatusIcon status={p.status} />
                    {p.status}
                  </span>
                </td>
                <td className="whitespace-nowrap px-5 py-3 text-sm text-slate-400">
                  {p.provider}
                </td>
                <td className="whitespace-nowrap px-5 py-3 text-sm text-slate-400">
                  {formatDate(p.createdAt)}
                </td>
                <td className="whitespace-nowrap px-5 py-3 text-sm text-slate-400">
                  {formatDate(p.completedAt)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

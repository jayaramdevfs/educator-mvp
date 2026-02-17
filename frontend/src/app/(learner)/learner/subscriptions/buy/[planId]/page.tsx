"use client";

import React, { useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { useQuery } from "@tanstack/react-query";
import { apiGet, apiPost } from "@/lib/api/client";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";
import {
  Loader2,
  ShieldCheck,
  Clock,
  CreditCard,
  CheckCircle2,
  ArrowLeft,
} from "lucide-react";
import Link from "next/link";

/* ---------- Types ---------- */

interface SubscriptionPlan {
  id: string;
  name: string;
  price: number;
  durationDays: number;
  enabled: boolean;
}

interface BuyResponse {
  provider: string;
  providerPaymentId: string;
  redirectUrl: string;
}

/* ---------- Helpers ---------- */

function formatPrice(price: number): string {
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
  }).format(price);
}

function formatDuration(days: number): string {
  if (days >= 365) {
    const years = Math.floor(days / 365);
    return years === 1 ? "1 Year" : `${years} Years`;
  }
  if (days >= 30) {
    const months = Math.floor(days / 30);
    return months === 1 ? "1 Month" : `${months} Months`;
  }
  return days === 1 ? "1 Day" : `${days} Days`;
}

/* ---------- Page ---------- */

export default function BuySubscriptionPage() {
  const params = useParams<{ planId: string }>();
  const router = useRouter();
  const planId = params.planId;

  const [isPurchasing, setIsPurchasing] = useState(false);
  const [purchaseComplete, setPurchaseComplete] = useState(false);

  const plansQuery = useQuery({
    queryKey: ["subscription-plans"],
    queryFn: () =>
      apiGet<SubscriptionPlan[]>("/api/public/subscriptions/plans"),
  });

  const plan = plansQuery.data?.find((p) => p.id === planId);

  async function handlePurchase() {
    if (!plan) return;

    setIsPurchasing(true);
    try {
      // Step 1: Initiate purchase
      const buyResult = await apiPost<BuyResponse>(
        `/api/learner/subscriptions/${planId}/buy`,
      );

      toast.success("Payment initiated successfully!");

      // Step 2: Simulate webhook callback (mock payment - no real gateway)
      await apiPost(
        `/api/payment/webhook?providerPaymentId=${encodeURIComponent(buyResult.providerPaymentId)}`,
      );

      toast.success("Payment confirmed! Subscription is now active.");
      setPurchaseComplete(true);

      // Step 3: Redirect after a short delay
      setTimeout(() => {
        router.push("/learner/subscriptions");
      }, 2000);
    } catch (err: unknown) {
      const message =
        err instanceof Error ? err.message : "Purchase failed. Please try again.";
      toast.error(message);
    } finally {
      setIsPurchasing(false);
    }
  }

  /* ---------- Loading state ---------- */
  if (plansQuery.isLoading) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gradient-to-b from-slate-950 via-purple-950/30 to-slate-950">
        <Loader2 className="h-8 w-8 animate-spin text-purple-400" />
      </div>
    );
  }

  /* ---------- Error / Not Found ---------- */
  if (plansQuery.isError || !plan) {
    return (
      <div className="min-h-screen bg-gradient-to-b from-slate-950 via-purple-950/30 to-slate-950">
        <div className="mx-auto max-w-2xl px-4 py-16 sm:px-6">
          <div className="flex flex-col items-center justify-center rounded-2xl border border-slate-800 bg-slate-900/50 px-6 py-16 text-center">
            <h2 className="mb-2 text-xl font-semibold text-slate-200">
              Plan not found
            </h2>
            <p className="mb-6 text-slate-400">
              The subscription plan you are looking for does not exist or is no
              longer available.
            </p>
            <Link href="/pricing">
              <Button className="bg-gradient-to-r from-purple-600 to-pink-500 text-white shadow-lg shadow-purple-500/20 hover:from-purple-500 hover:to-pink-400">
                Back to Plans
              </Button>
            </Link>
          </div>
        </div>
      </div>
    );
  }

  /* ---------- Success state ---------- */
  if (purchaseComplete) {
    return (
      <div className="min-h-screen bg-gradient-to-b from-slate-950 via-purple-950/30 to-slate-950">
        <div className="mx-auto max-w-2xl px-4 py-16 sm:px-6">
          <div className="flex flex-col items-center justify-center rounded-2xl border border-green-500/30 bg-slate-900/60 px-6 py-16 text-center backdrop-blur-xl">
            <div className="mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-green-500/10">
              <CheckCircle2 className="h-8 w-8 text-green-400" />
            </div>
            <h2 className="mb-2 text-2xl font-bold text-slate-100">
              Purchase Successful!
            </h2>
            <p className="mb-2 text-slate-400">
              Your subscription to <strong className="text-slate-200">{plan.name}</strong> is
              now active.
            </p>
            <p className="text-sm text-slate-500">
              Redirecting to your subscriptions...
            </p>
          </div>
        </div>
      </div>
    );
  }

  /* ---------- Main purchase view ---------- */
  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-950 via-purple-950/30 to-slate-950">
      <div className="mx-auto max-w-2xl px-4 py-8 sm:px-6 lg:px-8">
        {/* Back link */}
        <Link
          href="/pricing"
          className="mb-6 inline-flex items-center gap-1.5 text-sm text-slate-400 transition-colors hover:text-purple-300"
        >
          <ArrowLeft className="h-4 w-4" />
          Back to Plans
        </Link>

        <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-6 shadow-2xl shadow-purple-500/10 backdrop-blur-xl sm:p-8">
          {/* Gradient accent */}
          <div className="mb-6 h-1 w-full rounded-full bg-gradient-to-r from-purple-500 to-pink-500" />

          <h1 className="bg-gradient-to-r from-purple-200 via-pink-200 to-purple-200 bg-clip-text text-2xl font-bold text-transparent sm:text-3xl">
            Complete Your Purchase
          </h1>

          {/* Plan details */}
          <div className="mt-6 space-y-4 rounded-xl border border-slate-800 bg-slate-950/40 p-5">
            <div className="flex items-center gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-purple-500/10">
                <ShieldCheck className="h-5 w-5 text-purple-400" />
              </div>
              <div>
                <p className="text-lg font-semibold text-slate-100">
                  {plan.name}
                </p>
                <p className="text-sm text-slate-400">Subscription Plan</p>
              </div>
            </div>

            <div className="grid gap-3 sm:grid-cols-2">
              <div className="flex items-center gap-2 rounded-lg border border-slate-800 bg-slate-900/40 px-4 py-3">
                <CreditCard className="h-4 w-4 text-slate-500" />
                <div>
                  <p className="text-xs text-slate-500">Price</p>
                  <p className="text-lg font-bold text-white">
                    {formatPrice(plan.price)}
                  </p>
                </div>
              </div>
              <div className="flex items-center gap-2 rounded-lg border border-slate-800 bg-slate-900/40 px-4 py-3">
                <Clock className="h-4 w-4 text-slate-500" />
                <div>
                  <p className="text-xs text-slate-500">Duration</p>
                  <p className="text-lg font-bold text-white">
                    {formatDuration(plan.durationDays)}
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Purchase button */}
          <div className="mt-8">
            <Button
              onClick={handlePurchase}
              disabled={isPurchasing || !plan.enabled}
              className="w-full bg-gradient-to-r from-purple-600 to-pink-500 py-3 text-base font-semibold text-white shadow-lg shadow-purple-500/30 transition-all hover:from-purple-500 hover:to-pink-400 hover:shadow-purple-500/40 disabled:cursor-not-allowed disabled:opacity-50"
            >
              {isPurchasing ? (
                <span className="flex items-center justify-center gap-2">
                  <Loader2 className="h-4 w-4 animate-spin" />
                  Processing...
                </span>
              ) : (
                "Confirm Purchase"
              )}
            </Button>
            {!plan.enabled && (
              <p className="mt-2 text-center text-sm text-red-400">
                This plan is currently unavailable for purchase.
              </p>
            )}
          </div>

          <p className="mt-4 text-center text-xs text-slate-500">
            This is a mock payment. No real charge will be made.
          </p>
        </div>
      </div>
    </div>
  );
}

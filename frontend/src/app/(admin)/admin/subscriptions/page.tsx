"use client";

import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { apiGet, apiPost, apiPut } from "@/lib/api/client";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogFooter,
  DialogClose,
} from "@/components/ui/dialog";
import { CreditCard, ArrowLeft, Plus, ToggleLeft, ToggleRight } from "lucide-react";
import Link from "next/link";

interface SubscriptionPlan {
  id: string;
  name: string;
  durationDays: number;
  price: number;
  enabled: boolean;
  createdAt: string;
  updatedAt: string;
}

export default function SubscriptionsPage() {
  const queryClient = useQueryClient();
  const [dialogOpen, setDialogOpen] = useState(false);

  // Form state
  const [planName, setPlanName] = useState("");
  const [durationDays, setDurationDays] = useState(30);
  const [price, setPrice] = useState(0);

  const { data: plans, isLoading, isError } = useQuery<SubscriptionPlan[]>({
    queryKey: ["admin", "subscriptions"],
    queryFn: () => apiGet<SubscriptionPlan[]>("/api/admin/subscriptions"),
  });

  const createMutation = useMutation({
    mutationFn: (payload: { name: string; durationDays: number; price: number }) =>
      apiPost<SubscriptionPlan>("/api/admin/subscriptions", payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["admin", "subscriptions"] });
      setDialogOpen(false);
      resetForm();
    },
  });

  const enableMutation = useMutation({
    mutationFn: (planId: string) =>
      apiPut(`/api/admin/subscriptions/${planId}/enable`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["admin", "subscriptions"] });
    },
  });

  const disableMutation = useMutation({
    mutationFn: (planId: string) =>
      apiPut(`/api/admin/subscriptions/${planId}/disable`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["admin", "subscriptions"] });
    },
  });

  function resetForm() {
    setPlanName("");
    setDurationDays(30);
    setPrice(0);
  }

  function handleToggle(plan: SubscriptionPlan) {
    if (plan.enabled) {
      disableMutation.mutate(plan.id);
    } else {
      enableMutation.mutate(plan.id);
    }
  }

  const inputClass = "border-slate-700 bg-slate-800 text-slate-100";
  const labelClass = "mb-1 block text-sm text-slate-400";

  return (
    <main className="relative min-h-screen overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6 py-10">
      <div className="pointer-events-none absolute left-[8%] top-16 h-80 w-80 rounded-full bg-purple-500/20 blur-3xl" />
      <div className="pointer-events-none absolute right-[12%] top-10 h-72 w-72 rounded-full bg-pink-500/15 blur-3xl" />

      <div className="relative mx-auto max-w-5xl space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between rounded-3xl border border-slate-800 bg-slate-900/60 p-8 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
          <div className="flex items-center gap-4">
            <Link href="/admin">
              <Button variant="ghost" size="icon" className="text-slate-400 hover:text-slate-200">
                <ArrowLeft className="h-5 w-5" />
              </Button>
            </Link>
            <div>
              <h1 className="flex items-center gap-3 bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-3xl font-semibold tracking-tight text-transparent">
                <CreditCard className="h-8 w-8 text-purple-400" />
                Subscription Plans
              </h1>
              <p className="mt-1 text-slate-400">
                Manage subscription plans and pricing.
              </p>
            </div>
          </div>

          <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
            <DialogTrigger asChild>
              <Button className="bg-gradient-to-r from-purple-600 to-pink-600 text-white hover:from-purple-700 hover:to-pink-700">
                <Plus className="mr-2 h-4 w-4" />
                New Plan
              </Button>
            </DialogTrigger>
            <DialogContent className="border-slate-700 bg-slate-900 text-slate-100">
              <DialogHeader>
                <DialogTitle className="text-slate-100">Create Subscription Plan</DialogTitle>
              </DialogHeader>
              <div className="space-y-4 py-2">
                <div>
                  <label className={labelClass}>Plan Name</label>
                  <Input
                    value={planName}
                    onChange={(e) => setPlanName(e.target.value)}
                    placeholder="e.g. Monthly Pro"
                    className={inputClass}
                  />
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className={labelClass}>Duration (days)</label>
                    <Input
                      type="number"
                      value={durationDays}
                      onChange={(e) => setDurationDays(Number(e.target.value))}
                      className={inputClass}
                    />
                  </div>
                  <div>
                    <label className={labelClass}>Price</label>
                    <Input
                      type="number"
                      step="0.01"
                      value={price}
                      onChange={(e) => setPrice(Number(e.target.value))}
                      className={inputClass}
                    />
                  </div>
                </div>
              </div>
              <DialogFooter>
                <DialogClose asChild>
                  <Button variant="outline" className="border-slate-700 text-slate-300">
                    Cancel
                  </Button>
                </DialogClose>
                <Button
                  onClick={() =>
                    createMutation.mutate({ name: planName, durationDays, price })
                  }
                  disabled={!planName || createMutation.isPending}
                  className="bg-gradient-to-r from-purple-600 to-pink-600 text-white"
                >
                  {createMutation.isPending ? "Creating..." : "Create Plan"}
                </Button>
              </DialogFooter>
              {createMutation.isError && (
                <p className="text-sm text-red-400">
                  Error: {(createMutation.error as Error).message}
                </p>
              )}
            </DialogContent>
          </Dialog>
        </div>

        {/* Table */}
        <div className="overflow-hidden rounded-2xl border border-slate-800 bg-slate-900/60 backdrop-blur-xl">
          {isLoading ? (
            <div className="flex h-64 items-center justify-center">
              <div className="h-8 w-8 animate-spin rounded-full border-2 border-purple-500 border-t-transparent" />
            </div>
          ) : isError ? (
            <div className="flex h-64 items-center justify-center text-slate-400">
              Failed to load subscription plans.
            </div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow className="border-slate-800 hover:bg-transparent">
                  <TableHead className="text-slate-400">Name</TableHead>
                  <TableHead className="text-slate-400">Duration</TableHead>
                  <TableHead className="text-slate-400">Price</TableHead>
                  <TableHead className="text-slate-400">Status</TableHead>
                  <TableHead className="text-slate-400">Created</TableHead>
                  <TableHead className="text-right text-slate-400">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {(!plans || plans.length === 0) ? (
                  <TableRow className="border-slate-800">
                    <TableCell colSpan={6} className="h-24 text-center text-slate-500">
                      No subscription plans found.
                    </TableCell>
                  </TableRow>
                ) : (
                  plans.map((plan) => (
                    <TableRow key={plan.id} className="border-slate-800 text-slate-300">
                      <TableCell className="font-medium text-slate-200">
                        {plan.name}
                      </TableCell>
                      <TableCell>{plan.durationDays} days</TableCell>
                      <TableCell className="font-mono">
                        ${typeof plan.price === "number" ? plan.price.toFixed(2) : plan.price}
                      </TableCell>
                      <TableCell>
                        <Badge variant={plan.enabled ? "default" : "secondary"}>
                          {plan.enabled ? "Enabled" : "Disabled"}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-slate-500">
                        {plan.createdAt
                          ? new Date(plan.createdAt).toLocaleDateString()
                          : "--"}
                      </TableCell>
                      <TableCell className="text-right">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleToggle(plan)}
                          disabled={enableMutation.isPending || disableMutation.isPending}
                          className={
                            plan.enabled
                              ? "text-amber-400 hover:text-amber-300"
                              : "text-emerald-400 hover:text-emerald-300"
                          }
                        >
                          {plan.enabled ? (
                            <>
                              <ToggleRight className="mr-1 h-4 w-4" />
                              Disable
                            </>
                          ) : (
                            <>
                              <ToggleLeft className="mr-1 h-4 w-4" />
                              Enable
                            </>
                          )}
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          )}
        </div>
      </div>
    </main>
  );
}

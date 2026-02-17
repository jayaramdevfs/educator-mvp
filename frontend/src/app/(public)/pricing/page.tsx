import Link from "next/link";
import { PublicFooter, TopNav } from "@/components/layout";

interface SubscriptionPlan {
  id: string;
  name: string;
  price: number;
  durationDays: number;
  enabled: boolean;
}

const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL?.trim() || "http://localhost:8080";

async function fetchPlans(): Promise<SubscriptionPlan[]> {
  try {
    const response = await fetch(
      `${API_BASE_URL}/api/public/subscriptions/plans`,
      { cache: "no-store" },
    );

    if (!response.ok) {
      return [];
    }

    return (await response.json()) as SubscriptionPlan[];
  } catch {
    return [];
  }
}

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

export default async function PricingPage() {
  const plans = await fetchPlans();

  const enabledPlans = plans.filter((p) => p.enabled);
  const disabledPlans = plans.filter((p) => !p.enabled);

  return (
    <div className="min-h-screen bg-slate-950">
      <TopNav />
      <main className="relative overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6 py-10">
        {/* Background blurs */}
        <div className="pointer-events-none absolute left-[8%] top-16 h-96 w-96 rounded-full bg-purple-500/20 blur-3xl" />
        <div className="pointer-events-none absolute right-[10%] top-10 h-80 w-80 rounded-full bg-pink-500/15 blur-3xl" />

        <div className="relative mx-auto max-w-5xl">
          {/* Header */}
          <div className="mb-10 text-center">
            <h1 className="bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-4xl font-bold text-transparent sm:text-5xl">
              Subscription Plans
            </h1>
            <p className="mt-3 text-lg text-slate-400">
              Choose a plan that fits your learning goals.
            </p>
          </div>

          {plans.length === 0 ? (
            <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-12 text-center text-slate-400 backdrop-blur-xl">
              No subscription plans are available at the moment. Please check
              back later.
            </div>
          ) : (
            <>
              {/* Enabled Plans */}
              <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                {enabledPlans.map((plan) => (
                  <article
                    key={plan.id}
                    className="group relative flex flex-col rounded-2xl border border-slate-800 bg-slate-900/60 p-6 shadow-xl shadow-purple-500/10 backdrop-blur-xl transition-all hover:border-purple-500/40 hover:shadow-purple-500/20"
                  >
                    {/* Gradient accent */}
                    <div className="absolute inset-x-0 top-0 h-1 rounded-t-2xl bg-gradient-to-r from-purple-500 to-pink-500" />

                    <h2 className="mt-2 text-xl font-bold text-slate-100">
                      {plan.name}
                    </h2>

                    <div className="mt-4 flex items-baseline gap-1">
                      <span className="text-4xl font-extrabold text-white">
                        {formatPrice(plan.price)}
                      </span>
                    </div>

                    <p className="mt-2 text-sm text-slate-400">
                      {formatDuration(plan.durationDays)} access
                    </p>

                    <div className="mt-4 flex items-center gap-2">
                      <span className="inline-flex items-center rounded-full bg-green-500/10 px-2.5 py-0.5 text-xs font-medium text-green-400">
                        Available
                      </span>
                    </div>

                    <div className="mt-auto pt-6">
                      <Link
                        href={`/learner/subscriptions/buy/${plan.id}`}
                        className="block w-full rounded-lg bg-gradient-to-r from-purple-600 to-pink-500 px-4 py-3 text-center text-sm font-semibold text-white shadow-lg shadow-purple-500/30 transition-all hover:from-purple-500 hover:to-pink-400 hover:shadow-purple-500/40"
                      >
                        Buy Now
                      </Link>
                    </div>
                  </article>
                ))}
              </div>

              {/* Disabled Plans */}
              {disabledPlans.length > 0 && (
                <div className="mt-10">
                  <h2 className="mb-4 text-lg font-semibold text-slate-500">
                    Currently Unavailable
                  </h2>
                  <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                    {disabledPlans.map((plan) => (
                      <article
                        key={plan.id}
                        className="relative flex flex-col rounded-2xl border border-slate-800/50 bg-slate-900/30 p-6 opacity-60 backdrop-blur-xl"
                      >
                        <h2 className="text-xl font-bold text-slate-400">
                          {plan.name}
                        </h2>

                        <div className="mt-4 flex items-baseline gap-1">
                          <span className="text-4xl font-extrabold text-slate-500">
                            {formatPrice(plan.price)}
                          </span>
                        </div>

                        <p className="mt-2 text-sm text-slate-500">
                          {formatDuration(plan.durationDays)} access
                        </p>

                        <div className="mt-4 flex items-center gap-2">
                          <span className="inline-flex items-center rounded-full bg-slate-500/10 px-2.5 py-0.5 text-xs font-medium text-slate-500">
                            Unavailable
                          </span>
                        </div>

                        <div className="mt-auto pt-6">
                          <span className="block w-full cursor-not-allowed rounded-lg border border-slate-700 bg-slate-800/50 px-4 py-3 text-center text-sm font-semibold text-slate-500">
                            Not Available
                          </span>
                        </div>
                      </article>
                    ))}
                  </div>
                </div>
              )}
            </>
          )}
        </div>
      </main>
      <PublicFooter />
    </div>
  );
}

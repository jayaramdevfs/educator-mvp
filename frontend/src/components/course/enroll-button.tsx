"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { apiPost } from "@/lib/api/client";
import type { ApiClientError } from "@/lib/api/errors";

interface EnrollButtonProps {
    courseId: number;
}

export default function EnrollButton({ courseId }: EnrollButtonProps) {
    const router = useRouter();
    const queryClient = useQueryClient();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleEnroll = async () => {
        try {
            setLoading(true);
            setError(null);

            await apiPost(`/api/learner/enrollments/course/${courseId}`);

            // Invalidate the dashboard enrollments cache so it re-fetches
            queryClient.invalidateQueries({ queryKey: ["learner-enrollments"] });

            router.push("/learner/dashboard");
        } catch (err: unknown) {
            const apiError = err as ApiClientError;
            if (apiError?.status === 401) {
                router.push("/login-new");
            } else if (apiError?.status === 409) {
                setError("You are already enrolled in this course.");
            } else {
                setError("Failed to enroll. Please try again.");
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <button
                onClick={handleEnroll}
                disabled={loading}
                className="rounded-lg bg-gradient-to-r from-purple-600 to-purple-500 px-4 py-2 text-sm font-semibold text-white shadow-lg shadow-purple-500/30 transition-all hover:from-purple-500 hover:to-purple-400 disabled:opacity-50"
            >
                {loading ? "Enrolling..." : "Enroll Now"}
            </button>
            {error && (
                <p className="mt-2 text-sm text-red-400">{error}</p>
            )}
        </div>
    );
}

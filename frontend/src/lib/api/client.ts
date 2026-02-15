import axios, {
  AxiosHeaders,
  type AxiosError,
  type AxiosInstance,
  type AxiosRequestConfig,
  type InternalAxiosRequestConfig,
} from "axios";
import { normalizeApiError } from "@/lib/api/errors";
import {
  getAccessToken,
  setAccessToken,
} from "@/lib/api/token-storage";

export const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL?.trim() || "http://localhost:8080";

export const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 20_000,
  headers: {
    "Content-Type": "application/json",
  },
});

/**
 * Resolve the access token, checking token-storage first, then
 * falling back to the Zustand auth store (handles sessions created
 * before token-storage sync was added).
 */
function resolveAccessToken(): string | null {
  const stored = getAccessToken();
  if (stored) return stored;

  // Fallback: read directly from Zustand persisted localStorage
  if (typeof window === "undefined") return null;
  try {
    const raw = window.localStorage.getItem("educator-auth");
    if (!raw) return null;
    const parsed = JSON.parse(raw);
    const token = parsed?.state?.token as string | null;
    if (token) {
      // Sync it into token-storage so future reads are fast
      setAccessToken(token);
    }
    return token ?? null;
  } catch {
    return null;
  }
}

apiClient.interceptors.request.use((config) => {
  const token = resolveAccessToken();
  if (!token) {
    return config;
  }

  if (!config.headers) {
    config.headers = new AxiosHeaders();
  }

  const headers = config.headers as AxiosHeaders | Record<string, string>;
  if (headers instanceof AxiosHeaders) {
    headers.set("Authorization", `Bearer ${token}`);
  } else {
    headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

let isRefreshing = false;
let failedQueue: Array<{
  resolve: (value: unknown) => void;
  reject: (reason?: unknown) => void;
}> = [];

type RetryableRequestConfig = InternalAxiosRequestConfig & {
  _retry?: boolean;
};

const processQueue = (error: unknown = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(null);
    }
  });

  failedQueue = [];
};

// Public path prefixes where 401 should NOT trigger login redirect
const PUBLIC_PATH_PREFIXES = [
  "/catalog", "/courses", "/login", "/login-new",
  "/register", "/forgot-password", "/reset-password",
];

function isPublicPage(): boolean {
  if (typeof window === "undefined") return false;
  const path = window.location.pathname;
  if (path === "/") return true;
  return PUBLIC_PATH_PREFIXES.some((prefix) => path.startsWith(prefix));
}

apiClient.interceptors.response.use(
  (response) => response,
  async (error: unknown) => {
    const axiosError = error as AxiosError;
    const originalRequest = axiosError.config as RetryableRequestConfig | undefined;
    const status = axiosError.response?.status;

    // If 401 and not already retrying, attempt refresh
    if (status === 401 && originalRequest && !originalRequest._retry) {
      // If no token exists (unauthenticated user on public page), just reject silently
      const token = resolveAccessToken();
      if (!token) {
        return Promise.reject(normalizeApiError(error));
      }

      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then(() => apiClient(originalRequest))
          .catch((err) => Promise.reject(err));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const refreshResponse = await axios.post<{ token: string }>(
          `${API_BASE_URL}/api/auth/refresh`,
          { token },
          {
            headers: { "Content-Type": "application/json" },
          },
        );

        const newToken = refreshResponse.data.token;

        const { setAccessToken } = await import("@/lib/api/token-storage");
        setAccessToken(newToken);

        const { useAuthStore } = await import("@/store/auth-store");
        useAuthStore.getState().setToken(newToken);

        processQueue();

        return apiClient(originalRequest);
      } catch (refreshError) {
        processQueue(refreshError);

        const { clearAccessToken } = await import("@/lib/api/token-storage");
        const { useAuthStore } = await import("@/store/auth-store");

        clearAccessToken();
        useAuthStore.getState().logout();

        // Only redirect to login if on a protected page
        if (typeof window !== "undefined" && !isPublicPage()) {
          window.location.href = "/login-new";
        }

        return Promise.reject(normalizeApiError(refreshError));
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(normalizeApiError(error));
  },
);

export async function apiGet<TResponse>(
  url: string,
  config?: AxiosRequestConfig,
): Promise<TResponse> {
  const response = await apiClient.get<TResponse>(url, config);
  return response.data;
}

export async function apiPost<TResponse, TBody = unknown>(
  url: string,
  body?: TBody,
  config?: AxiosRequestConfig,
): Promise<TResponse> {
  const response = await apiClient.post<TResponse>(url, body, config);
  return response.data;
}

export async function apiPut<TResponse, TBody = unknown>(
  url: string,
  body?: TBody,
  config?: AxiosRequestConfig,
): Promise<TResponse> {
  const response = await apiClient.put<TResponse>(url, body, config);
  return response.data;
}

export async function apiDelete<TResponse = void>(
  url: string,
  config?: AxiosRequestConfig,
): Promise<TResponse> {
  const response = await apiClient.delete<TResponse>(url, config);
  return response.data;
}

import axios, { AxiosHeaders, type AxiosInstance, type AxiosRequestConfig } from "axios";
import { normalizeApiError } from "@/lib/api/errors";
import { getAccessToken } from "@/lib/api/token-storage";

export const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL?.trim() || "http://localhost:8080";

export const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 20_000,
  headers: {
    "Content-Type": "application/json",
  },
});

apiClient.interceptors.request.use((config) => {
  const token = getAccessToken();
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

apiClient.interceptors.response.use(
  (response) => response,
  (error: unknown) => Promise.reject(normalizeApiError(error)),
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


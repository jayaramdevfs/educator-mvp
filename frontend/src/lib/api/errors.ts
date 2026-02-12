import axios from "axios";
import type { ApiError } from "@/types";

export interface ApiClientError extends Error {
  status: number | null;
  code: string | null;
  path: string | null;
  isNetworkError: boolean;
  details?: unknown;
}

function isRecord(value: unknown): value is Record<string, unknown> {
  return typeof value === "object" && value !== null;
}

function isApiErrorPayload(value: unknown): value is ApiError {
  if (!isRecord(value)) {
    return false;
  }

  return (
    typeof value.message === "string" &&
    typeof value.status === "number" &&
    typeof value.error === "string"
  );
}

function createClientError(params: {
  message: string;
  status: number | null;
  code: string | null;
  path: string | null;
  isNetworkError: boolean;
  details?: unknown;
}): ApiClientError {
  const err = new Error(params.message) as ApiClientError;
  err.name = "ApiClientError";
  err.status = params.status;
  err.code = params.code;
  err.path = params.path;
  err.isNetworkError = params.isNetworkError;
  err.details = params.details;
  return err;
}

export function normalizeApiError(error: unknown): ApiClientError {
  if (axios.isAxiosError(error)) {
    const status = error.response?.status ?? null;
    const payload = error.response?.data;
    const isNetworkError = !error.response;

    if (isApiErrorPayload(payload)) {
      return createClientError({
        message: payload.message,
        status: payload.status,
        code: payload.code,
        path: payload.path,
        isNetworkError,
        details: payload,
      });
    }

    return createClientError({
      message: error.message || "Request failed",
      status,
      code: null,
      path: null,
      isNetworkError,
      details: payload,
    });
  }

  if (error instanceof Error) {
    return createClientError({
      message: error.message,
      status: null,
      code: null,
      path: null,
      isNetworkError: false,
    });
  }

  return createClientError({
    message: "Unknown error",
    status: null,
    code: null,
    path: null,
    isNetworkError: false,
    details: error,
  });
}


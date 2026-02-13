import { describe, expect, it } from "vitest";
import { normalizeApiError } from "@/lib/api/errors";

describe("normalizeApiError", () => {
  it("maps structured API payload from axios errors", () => {
    const error = {
      isAxiosError: true,
      message: "Request failed",
      response: {
        status: 400,
        data: {
          message: "Validation failed",
          status: 400,
          error: "Bad Request",
          code: "VALIDATION_ERROR",
          path: "/api/auth/login",
        },
      },
    };

    const normalized = normalizeApiError(error);

    expect(normalized.name).toBe("ApiClientError");
    expect(normalized.message).toBe("Validation failed");
    expect(normalized.status).toBe(400);
    expect(normalized.code).toBe("VALIDATION_ERROR");
    expect(normalized.path).toBe("/api/auth/login");
    expect(normalized.isNetworkError).toBe(false);
  });

  it("maps axios errors with non-standard payloads", () => {
    const error = {
      isAxiosError: true,
      message: "Internal Server Error",
      response: {
        status: 500,
        data: { detail: "failure" },
      },
    };

    const normalized = normalizeApiError(error);

    expect(normalized.message).toBe("Internal Server Error");
    expect(normalized.status).toBe(500);
    expect(normalized.code).toBeNull();
    expect(normalized.path).toBeNull();
    expect(normalized.isNetworkError).toBe(false);
  });

  it("marks axios errors without response as network errors", () => {
    const error = {
      isAxiosError: true,
      message: "Network Error",
      response: undefined,
    };

    const normalized = normalizeApiError(error);

    expect(normalized.message).toBe("Network Error");
    expect(normalized.status).toBeNull();
    expect(normalized.isNetworkError).toBe(true);
  });

  it("maps plain Error objects", () => {
    const normalized = normalizeApiError(new Error("Something went wrong"));

    expect(normalized.message).toBe("Something went wrong");
    expect(normalized.status).toBeNull();
    expect(normalized.code).toBeNull();
    expect(normalized.path).toBeNull();
    expect(normalized.isNetworkError).toBe(false);
  });

  it("maps unknown values to default error response", () => {
    const normalized = normalizeApiError({ random: "value" });

    expect(normalized.message).toBe("Unknown error");
    expect(normalized.status).toBeNull();
    expect(normalized.code).toBeNull();
    expect(normalized.path).toBeNull();
    expect(normalized.isNetworkError).toBe(false);
    expect(normalized.details).toEqual({ random: "value" });
  });

  it("keeps unknown primitive as error details", () => {
    const normalized = normalizeApiError("boom");

    expect(normalized.message).toBe("Unknown error");
    expect(normalized.details).toBe("boom");
  });
});


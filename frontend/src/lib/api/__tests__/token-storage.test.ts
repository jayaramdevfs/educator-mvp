import { beforeEach, describe, expect, it, vi } from "vitest";
import {
  clearAccessToken,
  getAccessToken,
  getTokenStorageKey,
  setAccessToken,
} from "@/lib/api/token-storage";

describe("token storage", () => {
  beforeEach(() => {
    vi.restoreAllMocks();
    clearAccessToken();
    window.localStorage.removeItem(getTokenStorageKey());
  });

  it("returns the configured token storage key", () => {
    expect(getTokenStorageKey()).toBe("educator.access_token");
  });

  it("stores token in memory and localStorage", () => {
    setAccessToken("abc.def.ghi");

    expect(getAccessToken()).toBe("abc.def.ghi");
    expect(window.localStorage.getItem(getTokenStorageKey())).toBe("abc.def.ghi");
  });

  it("clears token from memory and localStorage", () => {
    setAccessToken("abc.def.ghi");

    clearAccessToken();

    expect(getAccessToken()).toBeNull();
    expect(window.localStorage.getItem(getTokenStorageKey())).toBeNull();
  });

  it("removes persisted token when setAccessToken receives null", () => {
    setAccessToken("abc.def.ghi");

    setAccessToken(null);

    expect(getAccessToken()).toBeNull();
    expect(window.localStorage.getItem(getTokenStorageKey())).toBeNull();
  });

  it("returns token from localStorage when memory cache is empty", () => {
    window.localStorage.setItem(getTokenStorageKey(), "persisted-token");

    const token = getAccessToken();

    expect(token).toBe("persisted-token");
  });

  it("returns null when localStorage getItem throws", () => {
    vi.spyOn(Storage.prototype, "getItem").mockImplementation(() => {
      throw new Error("blocked");
    });

    const token = getAccessToken();

    expect(token).toBeNull();
  });

  it("does not throw when localStorage setItem fails", () => {
    vi.spyOn(Storage.prototype, "setItem").mockImplementation(() => {
      throw new Error("blocked");
    });

    expect(() => setAccessToken("abc.def.ghi")).not.toThrow();
  });
});


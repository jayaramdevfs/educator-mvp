import { beforeEach, describe, expect, it } from "vitest";
import { clearAccessToken } from "@/lib/api/token-storage";
import { useAuthStore } from "@/store/auth-store";

describe("auth store", () => {
  beforeEach(() => {
    clearAccessToken();
    useAuthStore.setState({
      status: "anonymous",
      token: null,
      user: null,
      hydrated: false,
      isAuthenticated: false,
    });
  });

  it("hydrates anonymous state when no token is present", () => {
    useAuthStore.getState().hydrate();
    const state = useAuthStore.getState();
    expect(state.hydrated).toBe(true);
    expect(state.isAuthenticated).toBe(false);
    expect(state.user).toBeNull();
  });

  it("logs in and maps user from token claims", async () => {
    await useAuthStore.getState().login({
      email: "tester@educator.local",
      password: "TestPass123!",
    });

    const state = useAuthStore.getState();
    expect(state.status).toBe("authenticated");
    expect(state.isAuthenticated).toBe(true);
    expect(state.token).toBeTruthy();
    expect(state.user?.email).toBe("tester@educator.local");
    expect(state.user?.roles).toEqual(["ADMIN"]);
  });
});


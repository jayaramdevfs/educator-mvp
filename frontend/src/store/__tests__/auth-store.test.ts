import { beforeEach, describe, expect, it } from "vitest";
import { useAuthStore } from "@/store/auth-store";

describe("auth store", () => {
  beforeEach(() => {
    useAuthStore.setState({
      token: null,
      user: null,
      isAuthenticated: false,
    });
  });

  it("starts with anonymous state", () => {
    const state = useAuthStore.getState();
    expect(state.isAuthenticated).toBe(false);
    expect(state.user).toBeNull();
    expect(state.token).toBeNull();
  });

  it("login sets token, user, and isAuthenticated", () => {
    useAuthStore.getState().login("test-token", {
      id: 1,
      email: "tester@educator.local",
      roles: ["STUDENT"],
    });

    const state = useAuthStore.getState();
    expect(state.isAuthenticated).toBe(true);
    expect(state.token).toBe("test-token");
    expect(state.user?.email).toBe("tester@educator.local");
    expect(state.user?.roles).toEqual(["STUDENT"]);
  });

  it("logout clears all auth state", () => {
    useAuthStore.getState().login("test-token", {
      id: 1,
      email: "tester@educator.local",
      roles: ["ADMIN"],
    });

    useAuthStore.getState().logout();

    const state = useAuthStore.getState();
    expect(state.isAuthenticated).toBe(false);
    expect(state.token).toBeNull();
    expect(state.user).toBeNull();
  });

  it("setToken updates only the token", () => {
    useAuthStore.getState().login("old-token", {
      id: 1,
      email: "tester@educator.local",
      roles: ["STUDENT"],
    });

    useAuthStore.getState().setToken("new-token");

    const state = useAuthStore.getState();
    expect(state.token).toBe("new-token");
    expect(state.user?.email).toBe("tester@educator.local");
  });

  it("setUser updates user and sets isAuthenticated", () => {
    useAuthStore.getState().setUser({
      id: 2,
      email: "admin@educator.local",
      roles: ["ADMIN"],
    });

    const state = useAuthStore.getState();
    expect(state.isAuthenticated).toBe(true);
    expect(state.user?.email).toBe("admin@educator.local");
  });
});

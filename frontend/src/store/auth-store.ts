import { create } from "zustand";
import { apiPost } from "@/lib/api";
import { clearAccessToken, getAccessToken, setAccessToken } from "@/lib/api/token-storage";
import { mapTokenToAuthUser, type AuthUser } from "@/lib/auth/jwt";
import type { LoginRequest, LoginResponse } from "@/types";

export type AuthStatus = "anonymous" | "loading" | "authenticated";

export interface AuthState {
  status: AuthStatus;
  token: string | null;
  user: AuthUser | null;
  hydrated: boolean;
  isAuthenticated: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => void;
  hydrate: () => void;
  setToken: (token: string | null) => void;
}

function deriveUser(token: string | null): AuthUser | null {
  if (!token) {
    return null;
  }
  return mapTokenToAuthUser(token);
}

export const useAuthStore = create<AuthState>((set) => ({
  status: "anonymous",
  token: null,
  user: null,
  hydrated: false,
  isAuthenticated: false,

  setToken: (token) => {
    if (token) {
      setAccessToken(token);
    } else {
      clearAccessToken();
    }

    const user = deriveUser(token);
    set({
      token,
      user,
      isAuthenticated: Boolean(token),
      status: token ? "authenticated" : "anonymous",
    });
  },

  hydrate: () => {
    const token = getAccessToken();
    const user = deriveUser(token);
    set({
      token,
      user,
      hydrated: true,
      isAuthenticated: Boolean(token),
      status: token ? "authenticated" : "anonymous",
    });
  },

  login: async (credentials) => {
    set({ status: "loading" });
    try {
      const response = await apiPost<LoginResponse, LoginRequest>("/api/auth/login", credentials);
      const token = response.token;
      const user = deriveUser(token);
      setAccessToken(token);
      set({
        token,
        user,
        isAuthenticated: true,
        status: "authenticated",
        hydrated: true,
      });
    } catch (error) {
      set({
        status: "anonymous",
        token: null,
        user: null,
        isAuthenticated: false,
      });
      throw error;
    }
  },

  logout: () => {
    clearAccessToken();
    set({
      status: "anonymous",
      token: null,
      user: null,
      isAuthenticated: false,
      hydrated: true,
    });
  },
}));

export const useAuthStatus = () => useAuthStore((state) => state.status);
export const useAuthUser = () => useAuthStore((state) => state.user);
export const useIsAuthenticated = () => useAuthStore((state) => state.isAuthenticated);
export const useAuthHydrated = () => useAuthStore((state) => state.hydrated);
export const useAuthActions = () =>
  useAuthStore((state) => ({
    login: state.login,
    logout: state.logout,
    hydrate: state.hydrate,
    setToken: state.setToken,
  }));


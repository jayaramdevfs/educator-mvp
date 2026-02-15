import { create } from "zustand";
import { persist } from "zustand/middleware";
import {
  setAccessToken,
  clearAccessToken,
} from "@/lib/api/token-storage";

interface User {
  id: number;
  email: string;
  roles: string[];
}

interface AuthState {
  token: string | null;
  user: User | null;
  isAuthenticated: boolean;

  login: (token: string, user: User) => void;
  logout: () => void;
  setUser: (user: User) => void;
  setToken: (token: string) => void;
}

export const useAuthStore = create<AuthState>()(
    persist(
        (set) => ({
          token: null,
          user: null,
          isAuthenticated: false,

          login: (token, user) => {
            setAccessToken(token);
            set({
              token,
              user,
              isAuthenticated: true,
            });
          },

          logout: () => {
            clearAccessToken();
            set({
              token: null,
              user: null,
              isAuthenticated: false,
            });
          },

          setUser: (user) => {
            set({
              user,
              isAuthenticated: true,
            });
          },

          setToken: (token) => {
            setAccessToken(token);
            set({ token });
          },
        }),
        {
          name: "educator-auth",
        }
    )
);

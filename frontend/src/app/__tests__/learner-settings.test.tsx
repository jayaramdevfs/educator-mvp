import { describe, expect, it, vi, beforeEach } from "vitest";
import { render, screen } from "@testing-library/react";
import SettingsPage from "@/app/(learner)/learner/settings/page";

vi.mock("next/navigation", () => ({
  useRouter: () => ({ push: vi.fn() }),
  usePathname: () => "/learner/settings",
}));

const mockAuthState = {
  isAuthenticated: true,
  user: { id: 1, email: "student@test.com", roles: ["STUDENT"] },
  token: "mock-token",
  login: vi.fn(),
  logout: vi.fn(),
  setUser: vi.fn(),
  setToken: vi.fn(),
};

vi.mock("@/store/auth-store", () => ({
  useAuthStore: (selector?: (state: typeof mockAuthState) => unknown) =>
    selector ? selector(mockAuthState) : mockAuthState,
}));

describe("Settings Page (F3.11)", () => {
  beforeEach(() => vi.clearAllMocks());

  it("renders profile heading", () => {
    render(<SettingsPage />);
    expect(screen.getByText("Profile & Settings")).toBeInTheDocument();
  });

  it("displays user email (read-only)", () => {
    render(<SettingsPage />);
    expect(screen.getByText("student@test.com")).toBeInTheDocument();
  });

  it("renders display name input pre-filled", () => {
    render(<SettingsPage />);
    const nameInput = screen.getByLabelText("Display Name") as HTMLInputElement;
    expect(nameInput.value).toBe("student");
  });

  it("renders change password section", () => {
    render(<SettingsPage />);
    expect(screen.getByLabelText("Current Password")).toBeInTheDocument();
    expect(screen.getByLabelText("New Password")).toBeInTheDocument();
    expect(screen.getByLabelText("Confirm New Password")).toBeInTheDocument();
  });

  it("renders save changes button", () => {
    render(<SettingsPage />);
    expect(screen.getByText("Save Changes")).toBeInTheDocument();
  });
});

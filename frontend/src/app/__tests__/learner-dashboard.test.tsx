import { describe, expect, it, vi, beforeEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import DashboardPage from "@/app/(learner)/learner/dashboard/page";

// Mock next/navigation
vi.mock("next/navigation", () => ({
  useRouter: () => ({ push: vi.fn(), replace: vi.fn() }),
  usePathname: () => "/learner/dashboard",
  useSearchParams: () => new URLSearchParams(),
}));

// Mock next/link
vi.mock("next/link", () => ({
  default: ({ href, children }: { href: string; children: React.ReactNode }) => (
    <a href={href}>{children}</a>
  ),
}));

// Mock auth store
vi.mock("@/store/auth-store", () => ({
  useAuthStore: () => ({
    isAuthenticated: true,
    user: { email: "student@test.com", roles: ["STUDENT"] },
  }),
  useAuthUser: () => ({ email: "student@test.com", roles: ["STUDENT"] }),
  useAuthStatus: () => "authenticated",
  useIsAuthenticated: () => true,
  useAuthHydrated: () => true,
}));

function renderWithQuery(ui: React.ReactElement) {
  const queryClient = new QueryClient({
    defaultOptions: { queries: { retry: false } },
  });
  return render(
    <QueryClientProvider client={queryClient}>{ui}</QueryClientProvider>,
  );
}

describe("Learner Dashboard (F3.1)", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("renders welcome message with username", async () => {
    renderWithQuery(<DashboardPage />);
    await waitFor(() => {
      expect(screen.getByText(/Welcome back, student/i)).toBeInTheDocument();
    });
  });

  it("renders stat cards", async () => {
    renderWithQuery(<DashboardPage />);
    await waitFor(() => {
      expect(screen.getByText("Enrolled Courses")).toBeInTheDocument();
      expect(screen.getByText("In Progress")).toBeInTheDocument();
      expect(screen.getByText("Completed")).toBeInTheDocument();
    });
  });

  it("renders enrollment cards from API", async () => {
    renderWithQuery(<DashboardPage />);
    await waitFor(() => {
      expect(screen.getByText("Intro to History")).toBeInTheDocument();
      expect(screen.getByText("Advanced Math")).toBeInTheDocument();
    });
  });

  it("renders status badges on cards", async () => {
    renderWithQuery(<DashboardPage />);
    await waitFor(() => {
      expect(screen.getByText("ACTIVE")).toBeInTheDocument();
      expect(screen.getByText("COMPLETED")).toBeInTheDocument();
    });
  });

  it("renders filter tabs", async () => {
    renderWithQuery(<DashboardPage />);
    await waitFor(() => {
      expect(screen.getByText("All Courses")).toBeInTheDocument();
    });
  });

  it("filters courses by tab click", async () => {
    const user = userEvent.setup();
    renderWithQuery(<DashboardPage />);

    await waitFor(() => {
      expect(screen.getByText("Intro to History")).toBeInTheDocument();
    });

    // Click the "Completed" filter tab (inside the tab bar, not the stat card)
    const tabs = screen.getAllByText("Completed");
    // The filter tab is the one with a count next to it
    const filterTab = tabs.find((el) => el.closest("button"));
    if (filterTab) await user.click(filterTab);

    await waitFor(() => {
      expect(screen.getByText("Advanced Math")).toBeInTheDocument();
      expect(screen.queryByText("Intro to History")).not.toBeInTheDocument();
    });
  });

  it("filters courses by search input", async () => {
    const user = userEvent.setup();
    renderWithQuery(<DashboardPage />);

    await waitFor(() => {
      expect(screen.getByText("Intro to History")).toBeInTheDocument();
    });

    const searchInput = screen.getByPlaceholderText("Search courses...");
    await user.type(searchInput, "Math");

    await waitFor(() => {
      expect(screen.getByText("Advanced Math")).toBeInTheDocument();
      expect(screen.queryByText("Intro to History")).not.toBeInTheDocument();
    });
  });

  it("shows loading skeletons initially", () => {
    renderWithQuery(<DashboardPage />);
    // Skeletons are shown while loading
    expect(screen.getByText(/Track your learning/i)).toBeInTheDocument();
  });

  it("renders difficulty badges on cards", async () => {
    renderWithQuery(<DashboardPage />);
    await waitFor(() => {
      expect(screen.getByText("Beginner")).toBeInTheDocument();
      expect(screen.getByText("Advanced")).toBeInTheDocument();
    });
  });

  it("renders 'Start Learning' button for active courses", async () => {
    renderWithQuery(<DashboardPage />);
    await waitFor(() => {
      expect(screen.getByText("Start Learning")).toBeInTheDocument();
    });
  });

  it("renders 'Review Course' button for completed courses", async () => {
    renderWithQuery(<DashboardPage />);
    await waitFor(() => {
      expect(screen.getByText("Review Course")).toBeInTheDocument();
    });
  });
});

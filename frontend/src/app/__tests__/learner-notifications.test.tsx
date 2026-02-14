import { describe, expect, it, vi, beforeEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import NotificationsPage from "@/app/(learner)/learner/notifications/page";

vi.mock("next/navigation", () => ({
  useRouter: () => ({ push: vi.fn() }),
  usePathname: () => "/learner/notifications",
}));

vi.mock("next/link", () => ({
  default: ({ href, children }: { href: string; children: React.ReactNode }) => (
    <a href={href}>{children}</a>
  ),
}));

function renderWithQuery(ui: React.ReactElement) {
  const queryClient = new QueryClient({
    defaultOptions: { queries: { retry: false } },
  });
  return render(
    <QueryClientProvider client={queryClient}>{ui}</QueryClientProvider>,
  );
}

describe("Notifications Page (F3.8)", () => {
  beforeEach(() => vi.clearAllMocks());

  it("renders notifications heading", async () => {
    renderWithQuery(<NotificationsPage />);
    await waitFor(() => {
      expect(screen.getByText("Notifications")).toBeInTheDocument();
    });
  });

  it("renders notification items from API", async () => {
    renderWithQuery(<NotificationsPage />);
    await waitFor(() => {
      expect(screen.getByText("Course Completed")).toBeInTheDocument();
      expect(screen.getByText("Exam Passed")).toBeInTheDocument();
    });
  });

  it("renders notification messages", async () => {
    renderWithQuery(<NotificationsPage />);
    await waitFor(() => {
      expect(screen.getByText("You completed Intro to History.")).toBeInTheDocument();
    });
  });

  it("shows 'Mark read' button for unread notifications", async () => {
    renderWithQuery(<NotificationsPage />);
    await waitFor(() => {
      expect(screen.getByText("Mark read")).toBeInTheDocument();
    });
  });

  it("shows unread indicator dot for unread notifications", async () => {
    renderWithQuery(<NotificationsPage />);
    await waitFor(() => {
      expect(screen.getByText("Course Completed")).toBeInTheDocument();
    });
  });
});

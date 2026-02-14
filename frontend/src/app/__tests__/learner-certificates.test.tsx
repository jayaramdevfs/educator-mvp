import { describe, expect, it, vi, beforeEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import CertificatesPage from "@/app/(learner)/learner/certificates/page";

vi.mock("next/navigation", () => ({
  useRouter: () => ({ push: vi.fn() }),
  usePathname: () => "/learner/certificates",
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

describe("Certificates Page (F3.10)", () => {
  beforeEach(() => vi.clearAllMocks());

  it("renders certificates heading", async () => {
    renderWithQuery(<CertificatesPage />);
    await waitFor(() => {
      expect(screen.getByText("My Certificates")).toBeInTheDocument();
    });
  });

  it("renders certificate cards from API", async () => {
    renderWithQuery(<CertificatesPage />);
    await waitFor(() => {
      expect(screen.getByText("Course #102")).toBeInTheDocument();
    });
  });

  it("renders certificate status badge", async () => {
    renderWithQuery(<CertificatesPage />);
    await waitFor(() => {
      expect(screen.getByText("Issued")).toBeInTheDocument();
    });
  });

  it("renders download button for issued certificate", async () => {
    renderWithQuery(<CertificatesPage />);
    await waitFor(() => {
      expect(screen.getByText("Download Certificate")).toBeInTheDocument();
    });
  });

  it("renders generation date for certificate", async () => {
    renderWithQuery(<CertificatesPage />);
    await waitFor(() => {
      expect(screen.getByText(/Generated/)).toBeInTheDocument();
    });
  });
});

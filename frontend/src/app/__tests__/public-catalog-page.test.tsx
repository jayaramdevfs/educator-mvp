import { beforeEach, describe, expect, it, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import CatalogPage from "@/app/(public)/catalog/page";

vi.mock("next/link", () => ({
  default: ({ href, children }: { href: string; children: React.ReactNode }) => (
    <a href={href}>{children}</a>
  ),
}));

vi.mock("@/components/layout", () => ({
  TopNav: () => <div data-testid="top-nav" />,
  PublicFooter: () => <div data-testid="public-footer" />,
}));

describe("public catalog page", () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it("renders course cards and hierarchy chips from API", async () => {
    const fetchMock = vi
      .fn()
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({
          content: [
            {
              id: 11,
              titleEn: "History Basics",
              descriptionEn: "Learn the fundamentals",
              difficulty: "BEGINNER",
              status: "PUBLISHED",
              estimatedDurationMinutes: 45,
            },
          ],
          pageNumber: 0,
          pageSize: 20,
          totalElements: 1,
          totalPages: 1,
          last: true,
        }),
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({
          content: [{ id: 1, nameEn: "History" }],
          pageNumber: 0,
          pageSize: 20,
          totalElements: 1,
          totalPages: 1,
          last: true,
        }),
      });
    vi.stubGlobal("fetch", fetchMock);

    render(await CatalogPage({ searchParams: Promise.resolve({ q: "history" }) }));

    expect(screen.getByText("Course Catalog")).toBeInTheDocument();
    expect(screen.getByText("History Basics")).toBeInTheDocument();
    expect(screen.getByText("History")).toBeInTheDocument();
    expect(screen.getByText(/Page\s+1\s+of\s+1/i)).toBeInTheDocument();
  });

  it("renders empty-state when course API returns no data", async () => {
    const fetchMock = vi
      .fn()
      .mockResolvedValueOnce({
        ok: false,
      })
      .mockResolvedValueOnce({
        ok: false,
      });
    vi.stubGlobal("fetch", fetchMock);

    render(await CatalogPage({ searchParams: Promise.resolve({}) }));

    expect(screen.getByText("No courses found with current filters.")).toBeInTheDocument();
  });
});

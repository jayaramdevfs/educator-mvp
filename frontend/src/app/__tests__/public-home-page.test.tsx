import { beforeEach, describe, expect, it, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import PublicHomePage from "@/app/(public)/page";

vi.mock("next/link", () => ({
  default: ({ href, children }: { href: string; children: React.ReactNode }) => (
    <a href={href}>{children}</a>
  ),
}));

vi.mock("@/components/layout", () => ({
  TopNav: () => <div data-testid="top-nav" />,
  PublicFooter: () => <div data-testid="public-footer" />,
}));

describe("public home page", () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it("renders dynamic homepage sections from API response", async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      json: async () => ({
        content: [
          {
            section: {
              id: "sec-1",
              title: "Featured Courses",
              position: "TOP",
            },
            blocks: [{ id: "blk-1", blockType: "COURSE" }],
          },
        ],
      }),
    });
    vi.stubGlobal("fetch", fetchMock);

    render(await PublicHomePage());

    expect(screen.getByText("Sprint 9 public experience starts here.")).toBeInTheDocument();
    expect(screen.getByText("Featured Courses")).toBeInTheDocument();
    expect(screen.getByText("COURSE")).toBeInTheDocument();
  });

  it("renders fallback state when API is unavailable", async () => {
    const fetchMock = vi.fn().mockRejectedValue(new Error("ECONNREFUSED"));
    vi.stubGlobal("fetch", fetchMock);

    render(await PublicHomePage());

    expect(screen.getByText("No homepage sections are published yet.")).toBeInTheDocument();
  });
});


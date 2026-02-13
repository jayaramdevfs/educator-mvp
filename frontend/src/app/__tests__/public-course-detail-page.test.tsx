import { beforeEach, describe, expect, it, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import CourseDetailPage from "@/app/(public)/courses/[courseId]/page";

vi.mock("next/link", () => ({
  default: ({ href, children }: { href: string; children: React.ReactNode }) => (
    <a href={href}>{children}</a>
  ),
}));

vi.mock("next/navigation", () => ({
  notFound: vi.fn(),
}));

vi.mock("@/components/layout", () => ({
  TopNav: () => <div data-testid="top-nav" />,
  PublicFooter: () => <div data-testid="public-footer" />,
}));

describe("public course detail page", () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it("renders course details and lesson tree from API", async () => {
    const fetchMock = vi
      .fn()
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({
          id: 99,
          titleEn: "Advanced History",
          descriptionEn: "Deep historical analysis",
          difficulty: "ADVANCED",
          status: "PUBLISHED",
          languageCode: "en",
          estimatedDurationMinutes: 120,
          hierarchyNode: { id: 1, nameEn: "History" },
        }),
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({
          content: [
            {
              id: 501,
              orderIndex: 1,
              depthLevel: 0,
              children: [],
            },
          ],
        }),
      });
    vi.stubGlobal("fetch", fetchMock);

    render(await CourseDetailPage({ params: Promise.resolve({ courseId: "99" }) }));

    expect(screen.getByText("Advanced History")).toBeInTheDocument();
    expect(screen.getByText("Structured lesson hierarchy for this course.")).toBeInTheDocument();
    expect(screen.getByText("Lesson #501")).toBeInTheDocument();
  });

  it("renders no-lessons fallback when lesson tree API fails", async () => {
    const fetchMock = vi
      .fn()
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({
          id: 100,
          titleEn: "Course Without Lessons",
          descriptionEn: "No lessons yet",
          difficulty: "BEGINNER",
          status: "PUBLISHED",
          languageCode: "en",
          estimatedDurationMinutes: 30,
        }),
      })
      .mockResolvedValueOnce({
        ok: false,
      });
    vi.stubGlobal("fetch", fetchMock);

    render(await CourseDetailPage({ params: Promise.resolve({ courseId: "100" }) }));

    expect(screen.getByText("No lessons are available yet.")).toBeInTheDocument();
  });
});


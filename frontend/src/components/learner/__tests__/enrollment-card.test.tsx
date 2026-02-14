import { describe, expect, it, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import EnrollmentCard from "@/components/learner/enrollment-card";

vi.mock("next/link", () => ({
  default: ({ href, children }: { href: string; children: React.ReactNode }) => (
    <a href={href}>{children}</a>
  ),
}));

describe("EnrollmentCard Component", () => {
  const baseProps = {
    enrollmentId: 1,
    courseId: 101,
    title: "Test Course",
    description: "A test course description",
    difficulty: "BEGINNER",
    estimatedDurationMinutes: 60,
    progressPercentage: 50,
    status: "ACTIVE" as const,
    enrolledAt: "2025-12-01T10:00:00",
  };

  it("renders course title", () => {
    render(<EnrollmentCard {...baseProps} />);
    expect(screen.getByText("Test Course")).toBeInTheDocument();
  });

  it("renders description", () => {
    render(<EnrollmentCard {...baseProps} />);
    expect(screen.getByText("A test course description")).toBeInTheDocument();
  });

  it("renders difficulty badge as Beginner", () => {
    render(<EnrollmentCard {...baseProps} />);
    expect(screen.getByText("Beginner")).toBeInTheDocument();
  });

  it("renders ACTIVE status badge", () => {
    render(<EnrollmentCard {...baseProps} />);
    expect(screen.getByText("ACTIVE")).toBeInTheDocument();
  });

  it("renders 'Resume Learning' for active course with progress", () => {
    render(<EnrollmentCard {...baseProps} progressPercentage={50} />);
    expect(screen.getByText("Resume Learning")).toBeInTheDocument();
  });

  it("renders 'Start Learning' for active course with no progress", () => {
    render(<EnrollmentCard {...baseProps} progressPercentage={0} />);
    expect(screen.getByText("Start Learning")).toBeInTheDocument();
  });

  it("renders 'Review Course' for completed course", () => {
    render(<EnrollmentCard {...baseProps} status="COMPLETED" />);
    expect(screen.getByText("Review Course")).toBeInTheDocument();
  });

  it("renders 'View Details' for dropped course", () => {
    render(<EnrollmentCard {...baseProps} status="DROPPED" />);
    expect(screen.getByText("View Details")).toBeInTheDocument();
  });

  it("renders progress bar with percentage", () => {
    render(<EnrollmentCard {...baseProps} progressPercentage={75} />);
    expect(screen.getByText("75%")).toBeInTheDocument();
  });

  it("renders estimated duration", () => {
    render(<EnrollmentCard {...baseProps} />);
    expect(screen.getByText("60 min")).toBeInTheDocument();
  });

  it("links to correct course page", () => {
    render(<EnrollmentCard {...baseProps} />);
    const link = screen.getByRole("link");
    expect(link).toHaveAttribute("href", "/learner/courses/101");
  });

  it("renders enrolled date", () => {
    render(<EnrollmentCard {...baseProps} />);
    expect(screen.getByText(/Enrolled/)).toBeInTheDocument();
  });
});

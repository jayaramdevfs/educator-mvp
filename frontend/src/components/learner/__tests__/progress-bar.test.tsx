import { describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import { ProgressBar } from "@/components/learner/progress-bar";

describe("ProgressBar Component", () => {
  it("renders progress percentage label", () => {
    render(<ProgressBar value={65} />);
    expect(screen.getByText("65%")).toBeInTheDocument();
  });

  it("renders 0% for zero progress", () => {
    render(<ProgressBar value={0} />);
    expect(screen.getByText("0%")).toBeInTheDocument();
  });

  it("renders 100% for full progress", () => {
    render(<ProgressBar value={100} />);
    expect(screen.getByText("100%")).toBeInTheDocument();
  });

  it("clamps value above 100 to 100", () => {
    render(<ProgressBar value={150} />);
    expect(screen.getByText("100%")).toBeInTheDocument();
  });

  it("clamps negative value to 0", () => {
    render(<ProgressBar value={-10} />);
    expect(screen.getByText("0%")).toBeInTheDocument();
  });

  it("hides label when showLabel is false", () => {
    render(<ProgressBar value={50} showLabel={false} />);
    expect(screen.queryByText("50%")).not.toBeInTheDocument();
    expect(screen.queryByText("Progress")).not.toBeInTheDocument();
  });

  it("renders 'Progress' label by default", () => {
    render(<ProgressBar value={50} />);
    expect(screen.getByText("Progress")).toBeInTheDocument();
  });
});

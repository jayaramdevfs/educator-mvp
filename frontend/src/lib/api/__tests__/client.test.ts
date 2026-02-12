import { describe, expect, it } from "vitest";
import { apiGet } from "@/lib/api/client";

describe("api client", () => {
  it("returns typed payload from msw handler", async () => {
    const data = await apiGet<{ ok: boolean; source: string }>("/api/test/ping");
    expect(data.ok).toBe(true);
    expect(data.source).toBe("msw");
  });
});


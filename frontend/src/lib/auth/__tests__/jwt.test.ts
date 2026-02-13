import { describe, expect, it } from "vitest";
import { mapTokenToAuthUser, parseJwtClaims } from "@/lib/auth/jwt";

function toBase64Url(value: string): string {
  return Buffer.from(value, "utf-8")
    .toString("base64")
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=+$/g, "");
}

function createToken(payload: Record<string, unknown>): string {
  const header = toBase64Url(JSON.stringify({ alg: "HS256", typ: "JWT" }));
  const body = toBase64Url(JSON.stringify(payload));
  return `${header}.${body}.signature`;
}

describe("jwt helpers", () => {
  it("parses claims from a valid token", () => {
    const token = createToken({ sub: "student@example.com", roles: ["STUDENT"] });

    const claims = parseJwtClaims(token);

    expect(claims?.sub).toBe("student@example.com");
    expect(claims?.roles).toEqual(["STUDENT"]);
  });

  it("returns null for malformed token format", () => {
    expect(parseJwtClaims("invalid-token")).toBeNull();
  });

  it("returns null for invalid json payload", () => {
    const header = toBase64Url(JSON.stringify({ alg: "HS256", typ: "JWT" }));
    const invalidPayload = toBase64Url("{not-valid-json");
    const token = `${header}.${invalidPayload}.signature`;

    expect(parseJwtClaims(token)).toBeNull();
  });

  it("maps valid token to auth user", () => {
    const token = createToken({ sub: "student@example.com", roles: ["STUDENT", "ADMIN"] });

    const user = mapTokenToAuthUser(token);

    expect(user).toEqual({
      email: "student@example.com",
      roles: ["STUDENT", "ADMIN"],
    });
  });

  it("filters out unknown roles when mapping auth user", () => {
    const token = createToken({ sub: "student@example.com", roles: ["STUDENT", "SUPER_ADMIN"] });

    const user = mapTokenToAuthUser(token);

    expect(user).toEqual({
      email: "student@example.com",
      roles: ["STUDENT"],
    });
  });

  it("returns empty roles when token has no roles claim", () => {
    const token = createToken({ sub: "student@example.com" });

    const user = mapTokenToAuthUser(token);

    expect(user).toEqual({
      email: "student@example.com",
      roles: [],
    });
  });

  it("returns null when sub is missing", () => {
    const token = createToken({ roles: ["STUDENT"] });

    expect(mapTokenToAuthUser(token)).toBeNull();
  });

  it("returns null for unparsable token when mapping user", () => {
    expect(mapTokenToAuthUser("bad-token")).toBeNull();
  });
});


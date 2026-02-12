import { http, HttpResponse } from "msw";

export const handlers = [
  http.get("http://localhost:8080/api/test/ping", () => {
    return HttpResponse.json({ ok: true, source: "msw" });
  }),
  http.post("http://localhost:8080/api/auth/login", async () => {
    return HttpResponse.json({
      token:
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZXJAZWR1Y2F0b3IubG9jYWwiLCJyb2xlcyI6WyJBRE1JTiJdLCJpYXQiOjE3Nzc3NzAwMDAsImV4cCI6MTc3Nzc3MzYwMH0.signature",
    });
  }),
];


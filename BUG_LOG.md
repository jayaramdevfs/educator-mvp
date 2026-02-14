# Bug Log

> Tracks bugs found and resolved across sprints.

---

## Sprint 10 Stabilization (2026-02-14)

### BUG-001: Login bypasses auth store, uses wrong token key
- **Severity:** Critical
- **Found:** Sprint 10 stabilization
- **Root Cause:** `login-new/page.tsx` used raw `apiClient.post()` and stored token with `localStorage.setItem("token")`, while the auth store uses `"educator.access_token"`. Also read `response.data.roles` but backend only returns `{ token }`.
- **Fix:** Replaced with `useAuthStore.login()` which stores token correctly and derives roles from JWT.
- **Status:** RESOLVED

### BUG-002: Student login redirects to non-existent /dashboard
- **Severity:** Critical
- **Found:** Sprint 10 stabilization
- **Root Cause:** Login page defaulted to `router.push("/dashboard")` for students, but student dashboard lives at `/learner/dashboard` (inside `(learner)` route group).
- **Fix:** Changed redirect to `/learner/dashboard` in login page and AuthGuard fallback.
- **Status:** RESOLVED

### BUG-003: Registration page not wired to backend
- **Severity:** High
- **Found:** Sprint 10 stabilization
- **Root Cause:** Registration page had `// TODO: Integrate with auth service` comment and used `setTimeout(resolve, 2000)` to simulate registration.
- **Fix:** Replaced with `apiClient.post("/api/auth/register", { email, password })` with error handling for duplicate emails (409) and generic failures.
- **Status:** RESOLVED

### BUG-004: Login input fields invisible on dark background
- **Severity:** Medium
- **Found:** Sprint 10 stabilization
- **Root Cause:** Login `<Input>` components used default shadcn styling (`bg-transparent`, `text-foreground`) which resolved to dark text on dark background. Register page had explicit overrides, but login page did not.
- **Fix:** Added `className="h-12 border-slate-700 bg-slate-950/50 text-slate-100 placeholder:text-slate-600 focus:border-purple-500 focus:ring-purple-500/30"` to login inputs.
- **Status:** RESOLVED

### BUG-005: Dropdown/popover colors jarring (light on dark)
- **Severity:** Medium
- **Found:** Sprint 10 stabilization
- **Root Cause:** CSS design tokens (`tokens.css`) defined light-theme values (`--popover: #fffdf8`, `--card: #fffdf8`, etc.) but the entire app uses dark-themed layouts (`bg-slate-950`). Shadcn components using `bg-popover` rendered with cream backgrounds.
- **Fix:** Overhauled all design tokens to dark palette: `--popover: #1e293b`, `--card: #1e293b`, `--border: #334155`, `--input: #334155`, etc.
- **Status:** RESOLVED

### BUG-006: CertificateServiceTest NPE
- **Severity:** High (CI blocker)
- **Found:** Sprint 10 stabilization
- **Root Cause:** `CertificateService` gained a 3rd constructor dependency (`NotificationPersistenceService`) in Sprint 10A, but the test only mocked 2 dependencies. `@InjectMocks` created the service, but `notificationPersistenceService` was null, causing NPE in `generate()`.
- **Fix:** Added `@Mock private NotificationPersistenceService notificationPersistenceService;` to the test.
- **Status:** RESOLVED

### BUG-007: LearnerNotificationServiceTest failures (6 errors, 1 failure)
- **Severity:** High (CI blocker)
- **Found:** Sprint 10 stabilization
- **Root Cause:** Service `markRead()` was refactored in Sprint 10A to use `findByIdAndUserId(notificationId, userId)` for DB-level ownership validation. Tests still mocked the old `findById(notificationId)` method. Also, the idempotent guard (return early if already read) was added but the test expected `save()` to be called.
- **Fix:** Updated all mocks to use `findByIdAndUserId`. Updated "belongs to different user" tests (now returns empty Optional). Updated "already read" test to verify `never().save()`.
- **Status:** RESOLVED

### BUG-008: AuthGuard redirects student to /learner (404)
- **Severity:** Medium
- **Found:** Sprint 10 stabilization
- **Root Cause:** AuthGuard line 66 used `"/learner"` as student fallback, but there's no page at that path — dashboard is at `/learner/dashboard`.
- **Fix:** Changed to `"/learner/dashboard"`.
- **Status:** RESOLVED

---

## Summary

| Sprint | Bugs Found | Bugs Resolved | Open |
|---|---|---|---|
| Sprint 10 | 8 | 8 | 0 |
| **Total** | **8** | **8** | **0** |

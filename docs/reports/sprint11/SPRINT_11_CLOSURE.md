# Sprint 11 — Full Closure Report

**Date:** 2026-02-17
**Sprint:** 11 — Admin Experience, Subscriptions & Payment
**Status:** CLOSED

---

## Objective

Complete the Admin Experience layer, implement Subscription and Payment backend, wire the full frontend for admin, instructor, learner subscriptions, and public pricing, and fix the exam access control bug.

---

## Backend Deliverables

### New Controllers

| Controller | Path | Purpose |
|---|---|---|
| `PublicExamController` | `/api/public/exams` | Anonymous exam browsing |
| `PublicSubscriptionController` | `/api/public/subscriptions/plans` | Public plan listing for pricing page |

### Subscription & Payment Architecture

| Feature | Detail |
|---|---|
| Subscription plan types | COURSE_ONLY, EXAM_ONLY, COURSE_EXAM_BUNDLE |
| Payment lifecycle | Initiate → providerPaymentId (mock) → Webhook confirm → Activate |
| Subscription activation | Auto-enroll in attached courses on activation |
| Subscription retrieval | `GET /api/learner/subscriptions/my` |
| Payment history | `GET /api/learner/payments/history` |
| Access control | Subscription-gated exam and course access integrated |

### Bug Fix — Exam Access Control

**Root cause:** `ExamAttemptService.startAttempt()` was calling `UUID.getMostSignificantBits()` to derive a `Long` user ID. This produces a random Long that never matches any real `User.id` (a DB auto-increment Long), causing `AccessControlService.canAccessExam()` to always return false (403 denied).

**Fix:** `StudentExamController` now passes the authenticated user's email. `ExamAttemptService` resolves the real `Long` PK via `userRepository.findByEmail(email)`.

**Files changed:**
- `backend/.../exam/controller/StudentExamController.java` — pass email to service
- `backend/.../exam/service/ExamAttemptService.java` — add `UserRepository` dep, resolve Long via email, add slf4j logging
- `backend/.../security/SecurityConfig.java` — permit public exam/subscription routes
- `backend/.../exam/service/ExamAttemptServiceTimerShuffleTest.java` — updated 3 test methods for new 3-arg signature

---

## Frontend Deliverables

### New Pages

| Route | File | Description |
|---|---|---|
| `/admin/courses` | `admin/courses/page.tsx` | Admin course CRUD management |
| `/admin/exams` | `admin/exams/page.tsx` | Admin exam management |
| `/admin/homepage` | `admin/homepage/page.tsx` | Homepage CMS (sections, banners) |
| `/admin/subscriptions` | `admin/subscriptions/page.tsx` | Subscription plan CRUD |
| `/admin/users` | `admin/users/page.tsx` | User management and role assignment |
| `/learner/subscriptions` | `learner/subscriptions/page.tsx` | Plan cards with purchase CTA |
| `/learner/subscriptions/buy/[planId]` | `learner/subscriptions/buy/page.tsx` | Plan purchase flow |
| `/pricing` | `(public)/pricing/page.tsx` | Public pricing comparison page |

### Updated Pages

| Route | Change |
|---|---|
| `/admin` | Enhanced dashboard with quick-action navigation cards |
| `/instructor` | Full instructor dashboard — course listing, exam management |
| `/learner` | Added subscription entry point and plan status |
| `/learner/courses/[courseId]/exam` | Fixed exam start to pass email; resolved 403 access-denied |
| `/` (homepage) | CMS-driven sections wired |
| Top nav | Subscription and pricing links added |

---

## Security Configuration Updates

| Route Pattern | Access |
|---|---|
| `/api/public/exams/**` | `permitAll` |
| `/api/public/subscriptions/**` | `permitAll` |
| `/api/student/exams/*/start` | Authenticated (was broken — now fixed) |

---

## Test Results

| Suite | Tests | Result |
|---|---|---|
| `QuestionBankTest` | 3 | PASS |
| `ExamAttemptServiceTimerShuffleTest` | 5 | PASS |
| `Sprint9EndpointIntegrationTest` | 2 | PASS |
| `LearnerNotificationServiceTest` | 8 | PASS |
| `ProfileServiceTest` | 10 | PASS |
| `CertificateRepositoryDataJpaTest` | 2 | PASS |
| `CourseRepositoryDataJpaTest` | 3 | PASS |
| `EnrollmentRepositoryDataJpaTest` | 1 | PASS |
| `ExamRepositoriesDataJpaTest` | 3 | PASS |
| `HierarchyNodeRepositoryDataJpaTest` | 1 | PASS |
| `HomepageSectionRepositoryDataJpaTest` | 2 | PASS |
| `LessonRepositoryDataJpaTest` | 3 | PASS |
| `NotificationRepositoryDataJpaTest` | 2 | PASS |
| `UserRepositoryDataJpaTest` | 1 | PASS |
| `JwtUtilTest` | 5 | PASS |
| `SecurityIntegrationTest` | 7 | PASS |
| **Total** | **155** | **BUILD SUCCESS** |

### Frontend Build

```
25 routes generated — all clean
○ Static / ƒ Dynamic — no errors
```

---

## Sprint 11 E2E Validated Flows (Newman)

| Flow | Status |
|---|---|
| Login Student → 200 | PASS |
| Buy Subscription → 200 | PASS |
| Webhook Confirmation → 200 | PASS |
| Validate ACTIVE subscription (remainingDays > 0) → 200 | PASS |
| Payment History retrieval → 200 | PASS |

Full HTML report: `docs/reports/sprint11/Sprint11_E2E_Report.html`

---

## Known Limitations / Sprint 12 Inputs

- Payment is mock (no real payment gateway integrated)
- Webhook does not validate HMAC signature (recommended for production)
- Duplicate subscription purchase prevention not enforced
- Instructor course/exam edit flows are UI stubs — full CRUD wiring in Sprint 12
- Admin pages make API calls but some create/update/delete actions are pending full backend wiring for new fields

---

## Credentials (for testing)

| Role | Email | Password |
|---|---|---|
| Admin | jayaramadmin@educate.com | Rama@1994 |
| Student | (register via `/register`) | — |

---

## Next Sprint: Sprint 12

**Focus:** Instructor experience (course authoring, lesson management, exam publishing), admin CRUD completion, Polish & integration (error states, loading states, empty states), full E2E regression.

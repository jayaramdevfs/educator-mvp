# Sprint 11 — Backend Closure Report

**Date:** 2026-02-17
**Status:** CLOSED

---

## Objective

Implement and validate:

- Subscription Plans (COURSE_ONLY, EXAM_ONLY, COURSE_EXAM_BUNDLE)
- Purchase Flow (initiate → providerPaymentId → webhook → activate)
- Payment Webhook (mock confirmation)
- Subscription Activation (auto-enroll in attached courses)
- Subscription Retrieval API (`/api/learner/subscriptions/my`)
- Payment History API (`/api/learner/payments/history`)
- Public Exam and Subscription browsing endpoints
- Exam access control bug fix

---

## Architecture Summary

### Authentication

- JWT-based stateless security
- `CustomUserDetails` injection via `JwtAuthenticationFilter`
- Role-based access control (STUDENT, INSTRUCTOR, ADMIN)

### Subscription Types

| Type | Grants access to |
|---|---|
| COURSE_ONLY | Attached courses |
| EXAM_ONLY | Attached exams |
| COURSE_EXAM_BUNDLE | Attached courses + exams |

### Payment Lifecycle

1. Student calls `POST /api/learner/subscriptions/buy/{planId}`
2. System generates `providerPaymentId` (mock UUID)
3. Payment record created with status `PENDING`
4. Webhook at `POST /api/payment/webhook` confirms payment
5. Payment status → `SUCCESS`
6. Subscription record created with status `ACTIVE`
7. Student auto-enrolled in attached courses
8. Validated via `GET /api/learner/subscriptions/my`

---

## New Endpoints

| Method | Path | Auth | Description |
|---|---|---|---|
| `GET` | `/api/public/exams` | Public | Browse published exams |
| `GET` | `/api/public/subscriptions/plans` | Public | List active subscription plans |
| `POST` | `/api/learner/subscriptions/buy/{planId}` | Student | Initiate subscription purchase |
| `GET` | `/api/learner/subscriptions/my` | Student | Get active subscription |
| `POST` | `/api/payment/webhook` | Public | Payment gateway confirmation |
| `GET` | `/api/learner/payments/history` | Student | Payment transaction history |

---

## Bug Fix — Exam Access Control

**Symptom:** `POST /api/student/exams/{examId}/start` always returned 403 (access denied) for valid enrolled students.

**Root cause:** `ExamAttemptService.startAttempt(UUID userId, ...)` called `userId.getMostSignificantBits()` to cast to a `Long`. This is bitwise extraction — not the real database PK. The resulting Long never matched any `User.id`, so `AccessControlService.canAccessExam()` always denied.

**Fix:**
- `StudentExamController` extracts `email` from `Authentication` and passes it to `startAttempt()`
- `ExamAttemptService` resolves the real `Long` PK via `userRepository.findByEmail(email).orElseThrow()`
- Added `UserRepository` dependency injection to `ExamAttemptService`
- Added slf4j logging for access attempt details

**Files changed:**
- `StudentExamController.java`
- `ExamAttemptService.java`
- `ExamAttemptServiceTimerShuffleTest.java` (updated 3 tests for new method signature)

---

## E2E Automation (Newman)

**Tool:** Newman v6.2.2 + `newman-reporter-htmlextra`

| Flow | HTTP Status | Result |
|---|---|---|
| Login Student | 200 | PASS |
| Buy Subscription | 200 | PASS |
| Webhook Confirmation | 200 | PASS |
| Validate ACTIVE subscription (`remainingDays > 0`) | 200 | PASS |
| Payment History | 200 | PASS |

HTML report: `Sprint11_E2E_Report.html`

---

## Bugs Resolved

| # | Bug | Cause | Fix |
|---|---|---|---|
| 1 | Invalid BCrypt hash | Manual SQL seeding | Re-registered users via API |
| 2 | `CustomUserDetails` null in controller | JWT filter stored String principal | Updated `JwtAuthenticationFilter` to inject `CustomUserDetails` |
| 3 | Webhook 401 | SecurityConfig did not permit webhook URL | Added `permitAll` matcher for `/api/payment/webhook` |
| 4 | Newman reporter failure | Deprecated `htmlextra` version | Migrated to `newman-reporter-htmlextra@3.x` |
| 5 | Exam start always 403 | `UUID.getMostSignificantBits()` never matches Long PK | Pass email from controller; resolve via `userRepository.findByEmail()` |

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

---

## Remaining / Sprint 12 Inputs

- Real payment gateway integration (Razorpay / Stripe)
- Webhook HMAC signature validation
- Duplicate purchase prevention
- Webhook idempotency guard
- Expired subscription handling
- CI Newman run integrated into GitHub Actions

# 🚀 Sprint 5 Handover — Enrollment & Progress Tracking

**Sprint:** 5  
**Status:** CLOSED & SIGNED OFF  
**Handover To:** Sprint 6  
**Go-Live Impact:** Foundational (Learner Core)

---

## 🎯 Sprint Objective

Sprint 5 introduced **learner-facing functionality** focused on:

- Course enrollment
- Lesson consumption
- Progress tracking
- Strict separation between admin, learner, and public concerns

The sprint explicitly avoided:
- Monetization
- Subscriptions
- Exams
- Notifications
- UI polish

---

## ✅ Scope Delivered (Verified)

### 1️⃣ Learner Enrollment
- Enroll in course
- Enrollment lifecycle:
  - ACTIVE
  - COMPLETED (reserved for future sprint)
- Duplicate enrollment prevention
- Enrollment restricted to PUBLISHED courses
- Enrollment bound strictly to authenticated learner

---

### 2️⃣ Lesson Consumption & Progress Tracking
- Lesson start tracking
- Lesson completion tracking
- Progress persisted per learner per lesson
- Idempotent progress updates
- Progress does NOT mutate:
  - Course structure
  - Lesson hierarchy

---

### 3️⃣ Learner Read-Only APIs
- Learner course overview
- Learner lesson list access
- Learner lesson content access
- Public lesson tree access
- Public vs enrolled-only access enforced

---

### 4️⃣ Security & Access Control
- JWT-based stateless authentication verified
- URL-pattern–based authorization enforced
- Admin APIs isolated
- Learner APIs protected
- Public APIs accessible without authentication
- No method-level security used

---

## 🧪 Verification & Testing

- Manual Postman testing completed
- Positive paths verified:
  - Enrollment
  - Progress tracking
- Negative paths verified:
  - Unauthorized access
  - Draft course enrollment
  - Invalid endpoints
- State-dependent behaviors validated using direct DB inspection

Sprint 5 test coverage is **complete and acceptable** for its scope.

---

## 🗄️ Database State

### Snapshot Created
- `db/snapshots/sprint5_baseline_data.sql`

### Snapshot Contents
- users
- roles
- user_roles
- courses
- lessons
- enrollments
- lesson_progress

### Snapshot Status
- Verified
- Restorable using `--disable-triggers`
- Locked as baseline for Sprint 6

---

## 🔒 Locked Decisions (Carry Forward)

- Lesson hierarchy uses `path + depthLevel`
- Soft delete (`is_deleted`) only
- Deterministic ordering enforced
- Roles ≠ Subscriptions
- Learners cannot modify content structure
- Progress tracking is additive and immutable

---

## ❌ Explicitly Not Included in Sprint 5

- Exams
- Course completion logic
- Certificates
- Notifications
- Subscriptions / payments
- UI/UX enhancements
- Mobile applications

These items are **intentionally deferred**.

---

## 🚀 Handover to Sprint 6

Sprint 6 will build upon Sprint 5 by introducing:

- Exams & completion logic
- Homepage configuration
- Subscription definitions
- Notification persistence
- Web + mobile (Android) UI foundations

Sprint 6 must use:
- Sprint 5 codebase
- Sprint 5 DB snapshot as baseline

No Sprint 6 work may begin without:
- File Impact Checklist approval
- Sprint 6 master prompt confirmation

---

## ✍️ Sign-Off

Sprint 5 is **complete, stable, and approved**.

All objectives achieved.  
All constraints respected.  
Safe to proceed to Sprint 6.

---

End of Sprint 5 Handover.

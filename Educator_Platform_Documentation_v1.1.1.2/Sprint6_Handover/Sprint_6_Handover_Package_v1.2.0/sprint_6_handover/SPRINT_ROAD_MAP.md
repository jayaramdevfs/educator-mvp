# 🧭 Educator Platform — Sprint Roadmap (Sprint 1 → Sprint 8)

This document defines the **end-to-end sprint roadmap** for the Educator Platform.
It is the **single planning reference** for what is built, when, and why.

This roadmap is **LOCKED** unless explicitly revised and approved.

---

## 🟢 Sprint 1 — Project Foundation & Core Setup (COMPLETED)

### Goal
Establish the technical foundation and project structure.

### Key Deliverables
- Spring Boot project setup
- PostgreSQL + JPA/Hibernate configuration
- Base package structure
- Core domain modeling foundations
- Audit fields (`created_at`, `updated_at`)
- Soft delete strategy (`is_deleted`)
- Deterministic ordering fields

### Status
✅ Completed  
🔒 Locked

---

## 🟢 Sprint 2 — Security & Access Control (COMPLETED)

### Goal
Implement secure, scalable authentication and authorization.

### Key Deliverables
- JWT-based stateless authentication
- URL-pattern–based authorization
- Public API separation (`/api/public/**`)
- Admin API isolation (`/api/admin/**`)
- Learner API protection (`/api/learner/**`)
- Removal of method-level security

### Status
✅ Completed  
🔒 Locked

---

## 🟢 Sprint 3 — Content Authoring & Hierarchy (COMPLETED)

### Goal
Enable admin-driven course and lesson creation with infinite hierarchy.

### Key Deliverables
- Course creation (Admin)
- Lesson creation (Admin)
- Infinite lesson hierarchy
- `path + depthLevel` hierarchy model
- Order control using `order_index`
- Public lesson tree access

### Status
✅ Completed  
🔒 Locked

---

## 🟢 Sprint 4 — Public Content Access & Stability (COMPLETED)

### Goal
Stabilize content access and verify hierarchy integrity.

### Key Deliverables
- Public lesson-tree APIs
- Course publish / draft lifecycle
- Hierarchy verification
- Security regression checks
- Persistence validation

### Status
✅ Completed  
🔒 Locked

---

## 🟢 Sprint 5 — Enrollment & Progress Tracking (COMPLETED)

### Goal
Enable learner participation without modifying content structure.

### Key Deliverables
- Course enrollment (Learner)
- Enrollment lifecycle (ACTIVE)
- Lesson progress tracking
- Idempotent progress updates
- Learner read-only APIs
- Business rule validation using DB state
- Sprint 5 database snapshot

### Status
✅ Completed  
🔒 Locked  
📦 Baseline Snapshot: `sprint5_baseline_data.sql`

---

## 🟡 Sprint 6 — Foundation, Configuration & UI Skeletons (PLANNED)

### Goal
Build **completion truth**, **platform configuration**, and **UI foundations**
across backend, web, and mobile.

### Key Deliverables
#### Backend
- Exam ↔ Course binding (one exam per course)
- Exam lifecycle (DRAFT / PUBLISHED / ARCHIVED)
- MCQ-based exams (deterministic, non-AI)
- Exam attempts & evaluation
- Course completion logic
- Admin-configurable homepage sections
- Subscription plan definitions (admin only)
- Notification persistence (DB + logs)
- Certificate data model
- Rule-based content & exam automation foundations

#### Frontend (Web)
- Admin dashboard shell
- Instructor dashboard shell
- Learner application shell
- Homepage driven by admin sections

#### Mobile
- React Native setup
- Android application foundation
- iOS-compatible codebase (build deferred)

### Explicit Exclusions
- Payments
- Subscription purchase
- Notification delivery (email/SMS)
- Certificates (PDF)
- Exam contests
- Native mobile SDK development

### Status
🟡 Planned  
🔒 Scope Locked

---

## 🔴 Sprint 7 — Go-Live Sprint (NON-NEGOTIABLE)

### Goal
Deliver a **production-ready platform** and go live.

### Key Deliverables
#### Backend
- Subscription purchase & entitlement enforcement
- Certificate generation (PDF) + verification endpoint
- Notification delivery:
  - In-app (mandatory)
  - Email (optional)
- Final security hardening
- Production configuration
- Sprint 7 database snapshot

#### Frontend (Web)
- Full UI/UX polish
- Learner dashboard
- Admin operational screens
- Certificate download UI
- Subscription purchase UI

#### Mobile
- Android app polish & release
- App versioning & release readiness
- iOS build deferred but code compatible

### Go-Live Definition
Users must be able to:
1. Register / log in
2. Enroll in courses
3. Complete lessons
4. Take and pass exams (if applicable)
5. Complete courses
6. Receive certificates
7. Receive notifications

### Status
🔴 Planned  
🚀 Platform MUST be LIVE at sprint end

---

## 🔵 Sprint 8 — Post-Launch Expansion (DEFERRED)

### Goal
Increase engagement and platform reach after launch.

### Key Deliverables
- Exam competitions / contests
- Time-bound competitive exams
- Leaderboards & rankings
- Contest rules engine
- Prize pool logic (requires payments)
- iOS application build & App Store submission (optional)

### Dependencies
- Sprint 7 completion
- Monetization readiness
- Apple Developer account

### Status
🟦 Deferred  
❌ Not required for initial launch

---

## 🔒 Global Roadmap Rules

- Roles ≠ Subscriptions (never mix)
- No AI or paid APIs during development sprints
- No schema-breaking changes without migration
- Database snapshot required at end of every sprint
- Any roadmap change must be:
  - Explicit
  - Documented
  - Approved

---

End of Sprint Roadmap.

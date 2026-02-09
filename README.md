# Educator Platform — Backend
## Sprint 6 README (Authoritative)

This document captures the **state of the Educator Platform backend up to the end of Sprint 6**.
It supersedes the Sprint 5 README and acts as the **official handover document into Sprint 7**.

---

## 📌 Project Status (as of Sprint 6)

- **Current Sprint:** Sprint 6 — ✅ Completed & Exit-Verified  
- **Next Sprint:** Sprint 7 — Monetization & Deployment  
- **Codebase State:** Clean, compiling, verified  
- **Sprint Discipline:** Strictly followed (no scope creep)

Sprint 6 focused on **foundations, correctness, and readiness**, not feature rush.

---

## 🧠 Core Principles (Locked)

- Strict sprint-by-sprint execution
- Stateless JWT-based authentication
- URL-pattern–based authorization only
- Clear separation of:
  - Admin
  - Instructor
  - Learner
  - Public APIs
- Deterministic, NON-AI backend logic
- PostgreSQL + JPA/Hibernate only
- Soft deletes and audit fields everywhere
- **Language governance:** User may speak Telugu, system responses are English-only

---

## 🏗️ Technology Stack

- Java 17
- Spring Boot
- Spring Security (JWT, Stateless)
- JPA / Hibernate
- PostgreSQL
- Maven
- Docker (local infra)

---

## 🔐 Security Model (Stable)

| API Pattern | Access |
|------------|--------|
| `/api/public/**` | Open |
| `/api/auth/**` | Authentication |
| `/api/admin/**` | ADMIN only |
| `/api/learner/**` | Authenticated STUDENT |

❌ No session-based auth  
❌ No method-level security  

All Sprint 6 APIs are **contract-frozen**.

---

## 👥 Users & Roles

Supported roles:
- `ROLE_ADMIN`
- `ROLE_INSTRUCTOR`
- `ROLE_STUDENT`

Rules:
- ADMIN implicitly has instructor privileges
- Roles are **NOT** used for monetization
- Monetization logic deferred to Sprint 7

---

## 📚 Courses — NON-AI Course Engine (Sprint 6)

Sprint 6 introduced a **rule-based Course Engine**.

Capabilities:
- Deterministic course structure validation
- Admin-defined sequencing rules
- Visibility & enable/disable rules
- Pre-publish validation hooks

❌ No AI generation  
❌ No personalization  

---

## 📘 Lessons — Hierarchy + Media Readiness

Lesson hierarchy remains **path + depthLevel** (locked).

Lesson types:
- TEXT
- VIDEO (reference-only)
- PDF (reference-only)
- EXAM

Media readiness added:
- Metadata-only media references
- External / internal source flags
- No uploads
- No storage or streaming

---

## 🧪 Exams — NON-AI Exam Engine (Sprint 6)

Exam engine is now **deterministic and auditable**.

Supports:
- MCQ-based exams
- One exam per course
- Exam lifecycle:
  - DRAFT
  - PUBLISHED
  - ARCHIVED
- Deterministic question selection
- Fixed scoring & pass rules

### Deferred Exam Enhancements (Sprint 7)
- Negative marking (configurable)
- Student-optional negative marking
- Answer explanations post-exam

---

## 🧾 Enrollment, Completion & Certificates

### Completion Engine
- Lesson completion thresholds
- Exam pass/fail rules
- Enrollment state transitions:
  - ACTIVE → COMPLETED

### Certificates
- Certificate data model created
- Eligibility rules defined
- ❌ No PDF generation (deferred)

---

## 🔔 Notifications (Sprint 6)

- Notification persistence layer (DB-only)
- Supported events:
  - COURSE_COMPLETED
  - EXAM_PASSED
  - EXAM_FAILED
  - CERTIFICATE_ELIGIBLE
- Read / unread tracking
- User association

❌ No delivery (email/SMS/push)

---

## 🏠 Homepage Architecture (Sprint 6 Major Deliverable)

Homepage is now **fully dynamic and admin-controlled**.

Model:
- HomepageSection (container)
- SectionLayout (behavior)
- SectionBlock (content unit)
- BlockConfig (dynamic config)

Characteristics:
- Deterministic ordering
- Mixed content (courses, exams, banners, CTAs)
- Frontend-agnostic
- No CSS or UI logic in backend

---

## 💼 Monetization — Design Locked (Sprint 6)

### Platform-First Monetization Model
- Platform collects all payments
- Admin controls final pricing
- Instructor may suggest price only
- Revenue sharing is policy-driven
- Historical transactions immutable

❌ No implementation in Sprint 6  
➡️ Implemented in Sprint 7

---

## 🗄️ Database State & Snapshots

Snapshots stored under:
```
backend/db/snapshots/
```

- `sprint5_baseline_data.sql` — Historical reference
- `sprint6_baseline.sql` — **Deployment baseline**

Sprint 6 snapshot:
- Schema-only
- No data
- Docker-generated
- Exit-verified

---

## 🚀 Deployment Plan

- ❌ No deployment in Sprint 6
- ✅ **Deployment planned in Sprint 7 (Web)**
- Mobile apps deferred post Sprint 7

Sprint 6 ensures **deployment readiness**, not execution.

---

## 🗺️ Sprint Roadmap (Updated)

🟩 Sprint 1 — Foundation & Architecture  
🟩 Sprint 2 — Authentication & Authorization  
🟩 Sprint 3 — Course & Lesson Domain  
🟩 Sprint 4 — Security Stabilization & Public APIs  
🟩 Sprint 5 — Enrollment & Progress  
🟩 Sprint 6 — Foundations, Engines & Readiness  

🟨 Sprint 7 — Monetization & Deployment  
🟨 Sprint 8 — Observability, Analytics, Enhancements  
🟨 Sprint 9+ — Mobile Applications

---

## 📦 Handover to Sprint 7

Sprint 7 will start with:
- Stable, frozen APIs
- Deterministic NON-AI engines
- Deployment-ready DB schema
- Locked monetization design
- Mandatory Exit & Handover documentation process

---

## ✅ Sprint 6 Closure Statement

Sprint 6 is **EXIT-COMPLETE, STABLE, and VERIFIED**.

This README is the **final authoritative backend document**
approved for:
- Git commit
- GitHub publication
- Sprint 7 entry

---

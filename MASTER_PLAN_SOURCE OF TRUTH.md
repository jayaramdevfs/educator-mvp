# EDUCATOR PLATFORM -- MASTER PLAN

> **AUTHORITY:** This document is the **single source of truth** for the Educator Platform.
> It consolidates and supersedes the Technical Design Document (TDD v1.0), Sprint 6 README,
> Architectural Handover Review, and Full Product Execution Plan.
> All development, deployment, and operational decisions reference this document.

| Field | Value |
|---|---|
| **Version** | 2.0 |
| **Date** | 2026-02-10 |
| **Baseline** | Sprint 1 through Sprint 6 (Complete, Contract-Frozen) |
| **Forward Scope** | Sprint 7 through Sprint 14 + Post-Production |
| **Status** | Active -- Living Document |
| **Supersedes** | TDD v1.0, README Sprint 6, Handover Review, Full Product Execution Plan |

### Document Governance

- **Part I** (Historical Baseline) is **immutable**. It records what exists as of Sprint 6.
- **Part II** (Current State) is **immutable**. It declares system health at time of writing.
- **Parts III-V** are **living sections**. They are updated as sprints progress.
- **Amendments** follow the process defined in Part V.

---

## TABLE OF CONTENTS

**PART I -- HISTORICAL BASELINE (READ-ONLY)**
- [1. Sprint 1-6 Summary](#1-sprint-1-6-summary)
- [2. Technology Stack](#2-technology-stack)
- [3. Architecture](#3-architecture)
- [4. Implemented Modules](#4-implemented-modules)
- [5. Domain Model & Database](#5-domain-model--database)
- [6. API Endpoint Reference](#6-api-endpoint-reference)
- [7. Business Logic Engines](#7-business-logic-engines)
- [8. Partially Implemented Features](#8-partially-implemented-features)
- [9. Intentionally Deferred Features](#9-intentionally-deferred-features)
- [10. Architectural Decisions & Constraints](#10-architectural-decisions--constraints)

**PART II -- CURRENT STATE DECLARATION (READ-ONLY)**
- [11. System Health Declaration](#11-system-health-declaration)
- [12. Known Gaps & Deviations](#12-known-gaps--deviations)

**PART III -- FORWARD EXECUTION PLAN**
- [13. Frontend Architecture](#13-frontend-architecture)
- [14. Design System](#14-design-system)
- [15. Screen Inventory & Role-Based Flows](#15-screen-inventory--role-based-flows)
- [16. Sprint 7 -- Local Setup & Foundation](#16-sprint-7----local-setup--foundation)
- [17. Sprint 8 -- Security Hardening & Backend Fixes](#17-sprint-8----security-hardening--backend-fixes)
- [18. Sprint 9 -- Backend Completion & API Polish](#18-sprint-9----backend-completion--api-polish)
- [19. Sprint 10 -- Student Experience](#19-sprint-10----student-experience)
- [20. Sprint 11 -- Admin Experience](#20-sprint-11----admin-experience)
- [21. Sprint 12 -- Instructor, Polish & Integration](#21-sprint-12----instructor-polish--integration)
- [22. Sprint 13 -- Testing, Performance & Pre-Deploy](#22-sprint-13----testing-performance--pre-deploy)
- [23. Sprint 14 -- Production Deployment & Launch](#23-sprint-14----production-deployment--launch)

**PART IV -- DEPLOYMENT & POST-PRODUCTION**
- [24. Security Hardening Checklist](#24-security-hardening-checklist)
- [25. CI/CD Pipeline](#25-cicd-pipeline)
- [26. Infrastructure & Environments](#26-infrastructure--environments)
- [27. Monitoring & Observability](#27-monitoring--observability)
- [28. Backup & Rollback](#28-backup--rollback)
- [29. Production Readiness Checklist](#29-production-readiness-checklist)
- [30. Post-Launch Operations](#30-post-launch-operations)

**PART V -- ONGOING FEATURE EVOLUTION & SPRINT AMENDMENTS**
- [31. Amendment Process](#31-amendment-process)
- [32. Feature Entry Template](#32-feature-entry-template)
- [33. Feature Status Lifecycle](#33-feature-status-lifecycle)
- [34. Sprint Amendment Log](#34-sprint-amendment-log)
- [35. Change Log](#35-change-log)

**APPENDICES**
- [A. Complete Enum Reference](#appendix-a-complete-enum-reference)
- [B. File Inventory](#appendix-b-file-inventory)
- [C. Technology Reference](#appendix-c-technology-reference)
- [D. Risk Register](#appendix-d-risk-register)
- [E. Non-Functional Requirements](#appendix-e-non-functional-requirements)
- [F. API-to-Screen Mapping](#appendix-f-api-to-screen-mapping)

---

# PART I -- HISTORICAL BASELINE (READ-ONLY)

> This section records the state of the Educator Platform at the end of Sprint 6.
> It is **immutable** and must not be modified. Any corrections are recorded as amendments in Part V.

---

## 1. Sprint 1-6 Summary

The Educator Platform is a Learning Management System (LMS) backend built with Java 17 and Spring Boot 4.0.2.

| Sprint | Focus | Key Deliverables |
|---|---|---|
| 1 | Foundation & Architecture | Project setup, Spring Boot scaffolding |
| 2 | Authentication & Authorization | JWT auth, role system, security config |
| 3 | Course & Lesson Domain | Course CRUD, lesson hierarchy, path-based ordering |
| 4 | Security Stabilization & Public APIs | URL-pattern auth, public endpoints, API hardening |
| 5 | Enrollment & Progress | Enrollment lifecycle, lesson progress tracking |
| 6 | Foundations, Engines & Readiness | Rule engines, exams, homepage CMS, notifications, automation framework, media references, DB snapshot |

**Result:** 119 Java source files across 15+ modules. 23 database tables. 30+ REST API endpoints. All APIs contract-frozen.

---

## 2. Technology Stack

| Component | Technology | Version |
|---|---|---|
| Language | Java | 17 (LTS) |
| Framework | Spring Boot | 4.0.2 |
| Security | Spring Security + JWT (JJWT) | 0.11.5 |
| ORM | JPA / Hibernate | Managed |
| Database | PostgreSQL | 15 |
| Build Tool | Maven | Wrapper |
| Code Generation | Lombok | 1.18.32 |
| Local Infra | Docker | Latest |
| Timezone | UTC | Enforced globally |

---

## 3. Architecture

### Layered Architecture

```
REST Controllers        -- HTTP endpoints
Service Layer           -- Business logic, orchestration
Engine Layer            -- Pure rule evaluation (stateless)
Repository Layer (JPA)  -- Data access, custom queries
PostgreSQL Database     -- Persistent storage
```

### Package Structure

```
com.educator/
  auth/                  # Authentication (login, register)
  automation/            # Automation rules & event logs
  certificate/           # Certificate entity & status
  completion/            # Course completion records
  course/                # Course entity, service, controller
    lesson/              # Lesson hierarchy, tree APIs
  engine/                # Stateless rule engines
    completion/          # Completion, eligibility, entitlement
    course/              # Course validation, templates
    exam/                # Exam blueprint, question selection
  enrollment/            # Enrollment & lesson progress
  exam/                  # Exam entity, attempts, grading
  hierarchy/             # Category taxonomy (tree)
  homepage/              # Dynamic homepage CMS
  importer/              # Data import abstractions
  lesson/                # Lesson type enums
  media/                 # Media reference metadata
  notification/          # Notification persistence
  roles/                 # Role entity & initialization
  security/              # JWT filter, security config
  users/                 # User entity & repository
```

### Authentication Flow

```
Client                    Server
  |-- POST /api/auth/register --> Create user (STUDENT role)
  |-- POST /api/auth/login -----> Validate credentials, generate JWT
  |<-- { token: "eyJ..." } ------|
  |-- GET /api/learner/... -----> JWT filter validates, authorizes by URL pattern
  |<-- Response -----------------|
```

**JWT Configuration:** HMAC-SHA256, 1-hour expiry, email subject, roles claim. CSRF disabled (stateless API).

### URL-Pattern Authorization

| URL Pattern | Access Level |
|---|---|
| `/api/auth/**` | Public (no auth) |
| `/api/public/**` | Public (no auth) |
| `/api/learner/**` | Authenticated |
| `/api/admin/**` | ADMIN role required |
| `/api/instructor/**` | Authenticated |
| `/api/student/**` | Authenticated |
| `OPTIONS /**` | Permitted (CORS preflight) |

### Roles

| Role | Constant | Description |
|---|---|---|
| `ROLE_STUDENT` | `Role.STUDENT` | Default role on registration |
| `ROLE_INSTRUCTOR` | `Role.INSTRUCTOR` | Course/exam management |
| `ROLE_ADMIN` | `Role.ADMIN` | Full platform control |

---

## 4. Implemented Modules

### 4.1 Auth Module (5 files)

Registration (STUDENT auto-assigned, BCrypt), login with JWT, CustomUserDetails adapter, 6 security components (SecurityConfig, JwtAuthenticationFilter, JwtUtil, CustomUserDetailsService, CustomUserDetails, PasswordConfig).

### 4.2 Course Module (6 files)

Course entity (15 fields, Long PK), lifecycle DRAFT -> PUBLISHED -> ARCHIVED, soft-delete, optimistic locking (@Version), 3 custom repository queries, admin CRUD controller.

### 4.3 Lesson Module (9 files)

Self-referential hierarchy (parent/children), path-based ordering, unlimited nesting, cascade soft-delete, 5 custom repository queries, LessonTreeResponse DTO, 4 controllers (admin write, admin query, public list, public tree).

### 4.4 Hierarchy/Taxonomy Module (5 files)

Self-referencing category tree, slug uniqueness, max-depth-10, cycle detection on reparenting, soft-delete with restore, optimistic locking, 5 custom repository queries, admin and public controllers.

### 4.5 Enrollment Module (9 files)

User-Course enrollment with status lifecycle (ACTIVE -> COMPLETED / DROPPED), LessonProgress per-lesson tracking (idempotent markCompleted), ownership guards, unique constraints (user_id, course_id), 4+3 custom repository queries.

### 4.6 Exam Module (15 files)

UUID-based entities, one exam per course (unique courseId), MCQ with questions/options, ExamAttempt lifecycle (IN_PROGRESS -> EVALUATED), auto-grading on submit, max attempts enforcement, CourseCompletion created on pass. 3 controllers (admin, instructor, student). 2+3 repository queries.

### 4.7 Completion & Certificate Module (4 files, should be 5)

CourseCompletion entity (UUID PK) with courseId, userId, examAttemptId. Certificate entity (UUID PK) with status lifecycle (GENERATED -> ISSUED -> REVOKED -> EXPIRED). CourseCompletionRepository present. **CertificateRepository missing** (the 1 missing file out of 120).

### 4.8 Homepage CMS Module (17 files)

HomepageSection (container), SectionBlock (content), SectionLayout (behavior), BlockConfig (dynamic config). 4 enums (SectionPosition, BlockType, LayoutType, BlockTargetType). Admin creation service, public query service (readOnly transaction). Admin and public controllers + 3 DTOs.

### 4.9 Notification Module (8 files)

Notification entity (UUID PK) with userId, type, title, message, read, status. NotificationPersistenceService (persist with PERSISTED status, read=false). NotificationTriggerMapper for event-to-type mapping. **Persistence-only -- no delivery mechanism.**

### 4.10 Automation Module (5 files)

AutomationRule entity (UUID PK) with trigger type + JSON action payload. AutomationEventLog for audit trail. 5 trigger types defined. **Framework-only -- no execution engine, no event listener, no action dispatcher.**

### 4.11 Media Module (5 files)

MediaReference entity (UUID PK) -- metadata-only (lessonId, type, source, URL). MediaValidationService validates VIDEO/PDF lessons have source URL. **No file upload, storage, or streaming.**

### 4.12 Import Module (6 files)

AbstractImportService base class, ImportContext, ImportResult, ImportError. Enums: ImportEntityType (COURSE, EXAM), ImportSourceType (DOCX, PDF, CSV). **Abstractions only -- no concrete parsers.**

### 4.13 Users & Roles Module (5 files)

User entity (Long PK, email unique, BCrypt password, ManyToMany EAGER roles). Role entity (Long PK, name unique). RoleInitializer seeds STUDENT/INSTRUCTOR/ADMIN on startup (idempotent).

### 4.14 Engine Classes (10 files)

All engines are pure stateless functions with no Spring dependencies.

| Engine | Purpose |
|---|---|
| CompletionRuleEngine | isCourseCompleted(lessons, total, examPassed) |
| CertificateEligibilityResolver | isEligible(completed, passed) |
| EnrollmentStateResolver | resolve(completed) -> ACTIVE/COMPLETED |
| EntitlementPolicy | Certificate + lifetime access flags |
| CourseRuleValidator | Module ordering + lesson count validation |
| CourseTemplate | Template definition (modules, ordering, min lessons) |
| CourseStructureRule | Rule definition (code, description, severity) |
| CourseValidationResult | Accumulates violations, checks publishability |
| ExamRuleEvaluator | Blueprint vs question bank validation |
| DeterministicQuestionSelector | Topic->Difficulty->ID sorted selection |

---

## 5. Domain Model & Database

### Entity Relationship Overview

```
User --(M:N)-- Role
  |
  |-- Enrollment --(N:1)-- Course --(N:1)-- HierarchyNode (self-ref)
  |       |
  |       +-- LessonProgress --(N:1)-- Lesson (self-ref parent/children)
  |
  |-- ExamAttempt --(ref)-- Exam --(1:1 by courseId)-- Course
  |       |
  |       +-- ExamAttemptAnswer
  |
  |-- CourseCompletion
  |-- Certificate
  +-- Notification

Exam --(1:N)-- ExamQuestion --(1:N)-- ExamOption
HomepageSection --(1:N)-- SectionBlock --(1:1)-- BlockConfig
HomepageSection --(1:1)-- SectionLayout
AutomationRule (standalone)
AutomationEventLog (standalone)
MediaReference (standalone, linked by lessonId UUID)
```

### Entity Summary

| Entity | PK Type | Key Fields |
|---|---|---|
| User | Long | email (unique), password (BCrypt), roles (M:N EAGER) |
| Role | Long | name (unique), constants: STUDENT/INSTRUCTOR/ADMIN |
| Course | Long | hierarchyNode (FK), titleEn, status, difficulty, isDeleted, version (@Version) |
| Lesson | Long | course (FK), parentLesson (self-ref), path, depthLevel, type, orderIndex, isDeleted |
| HierarchyNode | Long | parent (self-ref), slug (unique), nameEn, sortOrder, version, isDeleted, max depth 10 |
| Enrollment | Long | user (FK), course (FK), status, enrolledAt; unique(user_id, course_id) |
| LessonProgress | Long | enrollment (FK), lesson (FK), completed, completedAt; unique(enrollment_id, lesson_id) |
| Exam | UUID | courseId (unique), title, passPercentage, maxAttempts, timeLimitMinutes, shuffleQuestions, shuffleOptions, status |
| ExamQuestion | UUID | examId, questionText, displayOrder, explanation |
| ExamOption | UUID | questionId, optionText, correct, displayOrder |
| ExamAttempt | UUID | examId, userId, status, totalQuestions, correctAnswers, scorePercentage, passed |
| ExamAttemptAnswer | UUID | attemptId, questionId, selectedOptionId |
| CourseCompletion | UUID | courseId, userId, examAttemptId; unique(course_id, user_id) |
| Certificate | UUID | courseId, userId, courseCompletionId, status; unique(course_id, user_id) |
| Notification | UUID | userId, type, title, message, status, read |
| AutomationRule | UUID | triggerType, actionPayload (JSON), enabled |
| AutomationEventLog | UUID | triggerType, referenceId, eventPayload |
| MediaReference | UUID | lessonId, mediaType, sourceType, sourceUrl |
| HomepageSection | UUID | title, position, orderIndex, enabled |
| SectionBlock | UUID | sectionId, blockType, orderIndex, enabled |
| BlockConfig | UUID | blockId, title, subtitle, imageUrl, targetType, targetId, clickable |
| SectionLayout | UUID | sectionId, layoutType, autoScroll, scrollIntervalSeconds |

### Database Tables (23)

users, roles, user_roles, hierarchy_nodes, courses, lessons, enrollments, lesson_progress, exams, exam_questions, exam_options, exam_attempts, exam_attempt_answers, course_completions, certificates, notifications, automation_rules, automation_event_logs, media_references, homepage_sections, section_blocks, section_layouts, block_configs

### Unique Constraints

| Table | Constraint Columns |
|---|---|
| users | email |
| roles | name |
| hierarchy_nodes | slug |
| exams | course_id |
| enrollments | (user_id, course_id) |
| lesson_progress | (enrollment_id, lesson_id) |
| course_completions | (course_id, user_id) |
| certificates | (course_id, user_id) |

### Indexes

| Index | Table | Column(s) |
|---|---|---|
| idx_course_hierarchy | courses | hierarchy_node_id |
| idx_course_status | courses | status |
| idx_course_difficulty | courses | difficulty |
| idx_hierarchy_parent | hierarchy_nodes | parent_id |
| idx_hierarchy_slug | hierarchy_nodes | slug (UNIQUE) |
| idx_lesson_course | lessons | course_id |
| idx_lesson_order | lessons | order_index |
| idx_lesson_type | lessons | type |

### State Machines

**Course:** DRAFT -> PUBLISHED -> ARCHIVED (soft delete at any state)
**Exam:** DRAFT -> PUBLISHED -> ARCHIVED
**Enrollment:** ACTIVE -> COMPLETED (via completion engine) | ACTIVE -> DROPPED (user action)
**ExamAttempt:** IN_PROGRESS -> EVALUATED (on submit + auto-grade)
**LessonProgress:** (not started) -> STARTED -> COMPLETED (idempotent)

---

## 6. API Endpoint Reference

### Authentication (`/api/auth`) -- Public

| Method | Path | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user (STUDENT role) |
| POST | `/api/auth/login` | Login, receive JWT |

### Public (`/api/public`, `/api/hierarchy`)

| Method | Path | Description |
|---|---|---|
| GET | `/api/public/lessons/course/{courseId}` | Get lessons for a course |
| GET | `/api/public/lesson-tree/course/{courseId}` | Get hierarchical lesson tree |
| GET | `/api/public/homepage` | Get dynamic homepage |
| GET | `/api/hierarchy/roots` | Get root hierarchy nodes |
| GET | `/api/hierarchy/{parentId}/children` | Get child hierarchy nodes |

### Learner (`/api/learner`) -- Authenticated

| Method | Path | Description |
|---|---|---|
| POST | `/api/learner/enrollments/course/{courseId}` | Enroll in course |
| DELETE | `/api/learner/enrollments/{enrollmentId}` | Drop enrollment |
| GET | `/api/learner/enrollments` | List my enrollments |
| POST | `/api/learner/progress/enrollment/{id}/start` | Mark lesson started |
| POST | `/api/learner/progress/enrollment/{id}/lesson/{lessonId}/complete` | Mark lesson completed |

### Admin (`/api/admin`) -- ADMIN Role

| Method | Path | Description |
|---|---|---|
| POST | `/api/admin/courses` | Create course (DRAFT) |
| POST | `/api/admin/courses/{id}/publish` | Publish course |
| POST | `/api/admin/courses/{id}/archive` | Archive course |
| DELETE | `/api/admin/courses/{id}` | Soft delete course |
| GET | `/api/admin/courses/active` | List all active courses |
| POST | `/api/admin/lessons/course/{courseId}` | Add lesson to course |
| DELETE | `/api/admin/lessons/{lessonId}` | Delete lesson (cascade) |
| GET | `/api/admin/lesson-queries/course/{courseId}` | Get ordered lessons |
| POST | `/api/admin/exams` | Create exam |
| POST | `/api/admin/exams/{examId}/publish` | Publish exam |
| POST | `/api/admin/exams/{examId}/archive` | Archive exam |
| POST | `/api/admin/hierarchy` | Create hierarchy node |
| PUT | `/api/admin/hierarchy/{id}` | Update hierarchy node |
| PUT | `/api/admin/hierarchy/{id}/move` | Move hierarchy node |
| DELETE | `/api/admin/hierarchy/{id}` | Soft delete hierarchy node |
| PUT | `/api/admin/hierarchy/{id}/restore` | Restore hierarchy node |
| POST | `/api/admin/homepage/sections` | Create homepage section |
| POST | `/api/admin/homepage/blocks` | Create section block |

### Instructor (`/api/instructor`) -- Authenticated

| Method | Path | Description |
|---|---|---|
| GET | `/api/instructor/exams/{examId}` | Get exam by ID |

### Student Exam (`/api/student`) -- Authenticated

| Method | Path | Description |
|---|---|---|
| POST | `/api/student/exams/{examId}/start` | Start exam attempt |
| POST | `/api/student/exams/attempts/{attemptId}/submit` | Submit + auto-grade |

---

## 7. Business Logic Engines

### CompletionRuleEngine
```
isCourseCompleted(lessonsCompleted, totalLessons, examPassed) -> boolean
Rules: totalLessons > 0 AND lessonsCompleted >= totalLessons AND examPassed == true
```

### CertificateEligibilityResolver
```
isEligibleForCertificate(courseCompleted, examPassed) -> boolean
Rule: courseCompleted AND examPassed
```

### EnrollmentStateResolver
```
resolve(completed) -> EnrollmentState
Mapping: completed ? COMPLETED : ACTIVE
```

### CourseRuleValidator
```
validate(template, actualModules, lessonsPerModule) -> CourseValidationResult
Rule 1: Module ordering validation (ERROR, BLOCKING)
Rule 2: Lesson count per module validation (WARNING, NON-BLOCKING)
```

### ExamRuleEvaluator
```
evaluate(blueprint, questionBank) -> EvaluationResult
Rules: Course binding match, sufficient questions total/per-difficulty/per-topic
```

### DeterministicQuestionSelector
```
select(blueprint, questionBank) -> List<Question>
Sort by Topic -> Difficulty -> QuestionID, then select by distribution
```

---

## 8. Partially Implemented Features

| Feature | What Exists | What Is Missing |
|---|---|---|
| CertificateRepository | TDD specifies it; entity + enum exist | **Repository file missing entirely** |
| Automation execution | Rule entity + event log + trigger types | No event listener, no rule matcher, no action dispatcher |
| Notification delivery | Complete persistence layer | No email/SMS/push integration |
| Exam timer | `timeLimitMinutes` field on Exam entity | No server-side enforcement, no auto-submit |
| Question shuffling | `shuffleQuestions`/`shuffleOptions` flags | No shuffle logic in exam delivery |
| Answer explanations | `explanation` field on ExamQuestion | No API to serve post-exam |
| Enrollment auto-completion | Comment "// trigger auto-complete later" | Not wired to trigger on all-lessons-done |
| ExamType usage | ExamType enum (MCQ, PDF, HYBRID) | Not referenced in Exam entity or services |
| SecurityConfig completeness | URL patterns work via catch-all | `/api/instructor/**` and `/api/student/**` not explicit; STATELESS not declared |
| Data import | Abstract framework + enums | No concrete CSV/DOCX/PDF parsers |

---

## 9. Intentionally Deferred Features

### 9.1 Originally Planned for Sprint 7 (Monetization)

| # | Feature | Current State |
|---|---|---|
| 1 | Payment gateway (Razorpay/Stripe) | Not started |
| 2 | Subscription/pricing model | Design locked (platform-first) |
| 3 | Revenue sharing system | Policy defined |
| 4 | Purchase history & receipts | Not started |
| 5 | Instructor pricing suggestions | Role exists, no pricing capability |

### 9.2 Notification & Communication

| # | Feature | Current State |
|---|---|---|
| 6 | Email notification delivery | Persistence only |
| 7 | SMS notification delivery | Persistence only |
| 8 | Push notification delivery | Persistence only |
| 9 | Notification preferences | Not started |
| 10 | Real-time notifications (WebSocket) | Not started |

### 9.3 Certificate System

| # | Feature | Current State |
|---|---|---|
| 11 | PDF certificate generation | Entity + eligibility resolver exist |
| 12 | Certificate verification URL | Not started |
| 13 | Certificate revocation workflow | Status enum has REVOKED |
| 14 | Certificate expiry management | Fields exist |

### 9.4 Exam Enhancements

| # | Feature | Current State |
|---|---|---|
| 15 | Multi-type exams (PDF, HYBRID) | ExamType enum exists, never used |
| 16 | Question bank management API | QuestionBank engine exists |
| 17 | Blueprint-based exam generation | Blueprint + Selector exist |
| 18 | Negative exam marking | No field, no scoring logic |

### 9.5 Content & Course Enhancements

| # | Feature | Current State |
|---|---|---|
| 19 | Course prerequisites | Not started |
| 20 | Course reviews/ratings | Not started |
| 21 | Lesson content versioning | Not started |
| 22 | Course cloning/templates | CourseTemplate engine exists (validation only) |

### 9.6 Data Import & Media

| # | Feature | Current State |
|---|---|---|
| 23 | CSV/DOCX/PDF import | Abstract framework defined |
| 24 | File upload service | Metadata references only |
| 25 | Video streaming | External URL references only |

### 9.7 Mobile & Platform

| # | Feature | Current State |
|---|---|---|
| 26 | Mobile API optimization | Standard REST APIs |
| 27 | Offline content support | Not started |
| 28 | Mobile-specific auth (biometric) | JWT only |
| 29 | Web animation/scroll stack portability | `framer-motion` (web-capable), `lenis` (web-only); native apps require mobile-specific motion/scroll libs |

### 9.8 Explicit Exclusions

| Feature | Reason |
|---|---|
| AI/ML personalization | System is deterministic by design |
| AI content generation | All content is human-authored |
| Social features | Not in product scope |
| Multi-tenancy | Single-tenant architecture |
| GraphQL API | REST-only by design |

---

## 10. Architectural Decisions & Constraints

### Core Design Principles

| Decision | Rationale |
|---|---|
| Stateless JWT authentication | No server-side sessions; horizontal scalability |
| URL-pattern authorization only | Simpler than method-level; all security in one place |
| Soft deletes everywhere | Audit trail, data recovery, no permanent data loss |
| Deterministic, NON-AI logic | Auditable, testable, predictable outcomes |
| PostgreSQL + JPA/Hibernate only | Single source of truth, no caching layers |
| Optimistic locking (@Version) | Safe concurrent writes on Course, HierarchyNode |
| UUID for Sprint 6 entities | Exam, certificate, notification |
| Bigint for Sprint 1-5 entities | User, Course, Lesson, Enrollment |
| One exam per course | Enforced at service layer via unique courseId |
| Path-based lesson hierarchy | Efficient ordering and traversal |
| Metadata-only media references | No file handling; external URLs only |
| English-only system responses | Language governance |

### Key Constraints

- No session-based authentication
- No method-level `@PreAuthorize` annotations
- No hard deletes in any entity
- No AI, ML, or personalization in business logic
- No file uploads or binary storage
- Sprint 6 APIs are contract-frozen
- Strict sprint discipline -- no scope creep

### Error Handling Patterns

| Exception Type | Usage |
|---|---|
| IllegalArgumentException | Validation failures (duplicate email, bad input) |
| IllegalStateException | Business rule violations (missing role, bad state) |
| NoSuchElementException | Entity not found (via orElseThrow) |

### Transaction Patterns

- `@Transactional` on service write methods
- `@Transactional(readOnly=true)` for query-only methods
- Optimistic locking via `@Version` on Course, HierarchyNode

### Validation Rules

**Enrollment:** Course must exist, be PUBLISHED, not deleted, no duplicate enrollment. Only owner can drop.
**Lesson Hierarchy:** No child on deleted, no self-reparent, no circular hierarchy, depth = parent.depth + 1.
**Hierarchy Node:** Unique slug, max depth 10, no self-reparent, no cycles (descendant check).
**Exam:** One per course, attempt must be IN_PROGRESS to submit, maxAttempts enforced.
**Course:** Cannot publish archived or deleted courses.

### Idempotent Operations

- LessonProgress.markCompleted() -- sets completed only if not already
- Enrollment.touchLastAccessed() -- always updates to current time
- Role initialization -- orElseGet creates only if not exists

### Ownership Guards

| Service | Method | Guard |
|---|---|---|
| EnrollmentService | dropEnrollment() | user.id == enrollment.user.id |
| LessonProgressService | markLessonStarted() | user.id == enrollment.user.id |
| LessonProgressService | markLessonCompleted() | user.id == enrollment.user.id |

---

# PART II -- CURRENT STATE DECLARATION (READ-ONLY)

> This section declares the health and operational state of the system at the time of writing.
> It exists to **prevent unnecessary rework**. The backend is functional.

---

## 11. System Health Declaration

| Dimension | Status |
|---|---|
| **Build** | Compiles successfully (`mvn clean compile`) |
| **Startup** | Starts correctly on localhost:8080 |
| **Database** | Connects to PostgreSQL via Docker (localhost:5432/educator) |
| **IDE** | Runs successfully in IDE with healthy application status |
| **APIs** | All 30+ existing endpoints respond correctly |
| **Role Seeding** | RoleInitializer seeds STUDENT/INSTRUCTOR/ADMIN on startup |
| **Docker** | Docker Compose brings up PostgreSQL correctly |
| **Schema** | Hibernate DDL auto-update synchronizes schema on startup |

**No corrective action is required for existing code. The backend is in a stable continuation state.**

---

## 12. Known Gaps & Deviations

### 12.1 Missing File (1 of 120)

`certificate/repository/CertificateRepository.java` -- TDD Sections 8.7 and 14.16 specify this should exist. The Certificate entity and CertificateStatus enum exist. Only the repository interface is missing. **Fix: 15 minutes.**

### 12.2 Security Gaps (Development-Only Configuration)

These are expected development-environment defaults. They are addressed in Sprint 8 (Security Hardening).

| Gap | Severity | Resolution Sprint |
|---|---|---|
| Hardcoded JWT secret (`educator-secret-key-change-later`) | CRITICAL for production | Sprint 8 |
| Hardcoded DB credentials in application.yml | CRITICAL for production | Sprint 8 |
| `hibernate.ddl-auto: update` | CRITICAL for production | Sprint 8 |
| Empty `application-prod.yml` | CRITICAL for production | Sprint 8 |
| `show-sql: true` | HIGH for production | Sprint 8 |
| No CORS configuration | HIGH (blocks frontend) | Sprint 8 |
| No @ControllerAdvice exception handler | MEDIUM | Sprint 8 |
| No input validation (@Valid) | MEDIUM | Sprint 8 |
| No rate limiting on auth endpoints | MEDIUM | Sprint 8 |
| SessionCreationPolicy.STATELESS not explicit | MEDIUM | Sprint 8 |
| No security headers (HSTS, X-Frame-Options) | MEDIUM | Sprint 8 |
| StudentExamController uses @RequestParam userId | MEDIUM (security bug) | Sprint 8 |

### 12.3 Infrastructure Gaps

| Gap | Resolution Sprint |
|---|---|
| No frontend (zero frontend code) | Sprint 7-12 |
| No tests (1 context-load test only) | Sprint 8-13 |
| No CI/CD pipeline | Sprint 9 |
| No Docker production configuration | Sprint 13 |
| No cloud infrastructure | Sprint 13-14 |
| No monitoring/alerting | Sprint 12-13 |
| No database migrations (Flyway) | Sprint 9 |

---

# PART III -- FORWARD EXECUTION PLAN

> This section defines the sprint-by-sprint plan for reaching production deployment.
> It starts after Sprint 6. All tasks are forward-looking.

---

## 13. Frontend Architecture

### Chosen Stack

| Choice | Reason |
|---|---|
| **Next.js 15 (App Router)** | File-based routing, SSR for public pages (SEO), API route proxying, image optimization |
| **TypeScript** | Type safety against 30+ API response shapes with mixed Long/UUID PKs |
| **Tailwind CSS** | Utility-first, no CSS-in-JS overhead, excellent responsive design |
| **shadcn/ui** | Component source code you own; built on Radix UI (accessible); fully customizable |
| **React Hook Form + Zod** | Form handling with schema validation mirroring backend rules |
| **TanStack Query (React Query)** | Server state management; caching, refetching, loading/error states |
| **Zustand** | Minimal client state (auth token, user session, sidebar) |
| **Lucide Icons** | Clean, consistent, tree-shakeable |
| **Framer Motion** | Declarative UI motion with spring physics for web interactions |
| **GSAP** | Timeline-driven orchestration for advanced entrance/background animation |
| **Lenis** | Smooth scrolling for web UX (DOM-based runtime) |

### Cross-Platform Compatibility Note (Added 2026-02-12)

- `Lenis` is web-only and should not be treated as a portable dependency for native mobile runtimes.
- Planned Android/iOS apps should reuse platform-agnostic modules (API contracts, DTO types, auth rules, business logic), while animation/scroll behavior should use native mobile libraries.

### Project Structure

```
frontend/
  app/                          # Next.js App Router
    (public)/                   # Public layout group
      page.tsx                  # Homepage
      courses/
        page.tsx                # Course catalog
        [courseId]/page.tsx     # Course detail
      login/page.tsx
      register/page.tsx
      reset-password/page.tsx
    (learner)/                  # Authenticated learner layout
      dashboard/page.tsx
      learn/[enrollmentId]/page.tsx
      exams/
        [examId]/start/page.tsx
        [attemptId]/take/page.tsx
        [attemptId]/results/page.tsx
      notifications/page.tsx
      certificates/page.tsx
      profile/page.tsx
    (admin)/                    # Admin layout
      dashboard/page.tsx
      courses/
        page.tsx
        new/page.tsx
        [courseId]/
          edit/page.tsx
          lessons/page.tsx
      exams/
        page.tsx
        [examId]/edit/page.tsx
      taxonomy/page.tsx
      homepage/page.tsx
      users/page.tsx
    (instructor)/               # Instructor layout
      dashboard/page.tsx
      analytics/page.tsx
    layout.tsx                  # Root layout
  components/
    ui/                         # shadcn/ui components
    shared/                     # Cross-role shared components
    public/                     # Public-specific components
    learner/                    # Learner-specific components
    admin/                      # Admin-specific components
    instructor/                 # Instructor-specific components
  lib/
    api/                        # API client, typed endpoints
    auth/                       # JWT management, auth context
    hooks/                      # Custom React hooks
    types/                      # TypeScript types matching backend entities
    utils/                      # Formatting, validation helpers
    constants/                  # Enums, config, routes
  styles/
    globals.css                 # Tailwind base + custom tokens
  public/
    images/
    fonts/
```

---

## 14. Design System

### Color Palette

| Token | Color | Usage |
|---|---|---|
| primary | #2563EB (Blue 600) | Buttons, links, active states |
| primary-hover | #1D4ED8 (Blue 700) | Hover on primary elements |
| secondary | #64748B (Slate 500) | Secondary text, borders |
| success | #16A34A (Green 600) | Pass, complete, published badges |
| warning | #D97706 (Amber 600) | Draft badges, caution states |
| danger | #DC2626 (Red 600) | Fail, delete, error states |
| background | #FFFFFF | Main background |
| surface | #F8FAFC (Slate 50) | Card backgrounds, sidebars |
| border | #E2E8F0 (Slate 200) | Borders, dividers |
| text-primary | #0F172A (Slate 900) | Headings, body text |
| text-secondary | #64748B (Slate 500) | Labels, descriptions |
| text-muted | #94A3B8 (Slate 400) | Placeholders, disabled |

### Typography

| Element | Font | Size | Weight |
|---|---|---|---|
| H1 (Page title) | Inter | 30px / 1.875rem | 700 (Bold) |
| H2 (Section title) | Inter | 24px / 1.5rem | 600 (Semibold) |
| H3 (Card title) | Inter | 20px / 1.25rem | 600 |
| Body | Inter | 16px / 1rem | 400 (Regular) |
| Body small | Inter | 14px / 0.875rem | 400 |
| Caption / Meta | Inter | 12px / 0.75rem | 500 (Medium) |
| Button | Inter | 14px / 0.875rem | 500 |
| Code | JetBrains Mono | 14px | 400 |

### Spacing, Radius, Shadows

**Spacing:** 8px grid -- 4, 8, 12, 16, 20, 24, 32, 40, 48, 64, 80, 96

| Element | Radius |
|---|---|
| Buttons | 8px (rounded-lg) |
| Cards | 12px (rounded-xl) |
| Inputs | 8px (rounded-lg) |
| Badges | 9999px (rounded-full) |
| Modals | 16px (rounded-2xl) |

| Level | Usage |
|---|---|
| shadow-sm | Cards, inputs |
| shadow | Dropdowns, popovers |
| shadow-lg | Modals, overlays |

### UX Principles

| Principle | Implementation |
|---|---|
| Clarity over cleverness | Clear labels; no icon-only destructive buttons; confirmation dialogs |
| Progressive disclosure | Lesson tree on click; exam rules before start; collapsible admin sections |
| Immediate feedback | Optimistic UI on lesson completion; toast on save; skeleton loaders |
| Accessible by default | Radix UI (WCAG 2.1 AA); keyboard navigable; ARIA labels; contrast >= 4.5:1 |
| Role-appropriate density | Public: spacious; Student: content-first; Admin: data-dense |
| Error prevention | Validation on blur; disabled until valid; inline errors below fields |
| Navigation consistency | Top nav always visible; breadcrumbs on nested pages; sidebar for admin |
| Mobile-first responsive | All screens designed mobile-first, enhanced for tablet and desktop |

---

## 15. Screen Inventory & Role-Based Flows

### Public (Unauthenticated) -- 7 screens

| Screen | Description |
|---|---|
| Landing / Homepage | Dynamic CMS-driven homepage |
| Course Catalog | Browse categories, list courses per category |
| Course Detail | Course info, lesson outline tree, enroll CTA |
| Login | Email + password, redirect to dashboard |
| Register | Email + password, validation, redirect to login |
| Password Reset | Request reset, email token, set new password |
| 404 / Error Pages | Branded error pages |

### Student / Learner (Authenticated) -- 10 screens

| Screen | Description |
|---|---|
| Student Dashboard | Enrolled courses grid with progress bars |
| Course Learning View | Sidebar (lesson tree), main area (content), progress |
| Lesson Content Display | Render TEXT / VIDEO / DOCUMENT |
| Exam Start Page | Rules, instructions, timer info, start button |
| Exam Taking | Questions, radio options, navigation, timer, submit |
| Exam Results | Score, pass/fail, question breakdown, explanations |
| Exam Attempt History | Past attempts with scores and dates |
| Notifications | List with read/unread state |
| Certificates | Earned certificates with download |
| Profile / Settings | Name, email, password change |

### Admin -- 12 screens

| Screen | Description |
|---|---|
| Admin Dashboard | Metrics: courses, enrollments, pass rates |
| Course Management | Table with status badges, CRUD actions |
| Course Editor | Form for all course fields, hierarchy selector |
| Lesson Editor | Tree drag-drop interface, add/edit/delete/reorder |
| Lesson Content Editor | Rich text for TEXT, URL for VIDEO/DOCUMENT |
| Exam Management | Table with status, create/publish/archive |
| Exam Editor | Config form, question builder, option editor |
| Taxonomy Manager | Tree editor, drag-drop reorder |
| Homepage CMS | Section builder, block configurator, layout picker |
| User Management | User list, role assignment, search |
| Notification Management | View/create notifications |
| Admin Settings | Platform settings |

### Instructor -- 3 screens

| Screen | Description |
|---|---|
| Instructor Dashboard | Assigned courses, exam performance summary |
| Exam Analytics | Pass rate, avg score, question difficulty heatmap |
| Student Performance | Enrollment stats, completion rates |

### Shared Components -- 15+

Navigation Bar, Sidebar, Auth Guard, JWT Token Manager, API Client (Axios), Data Table, Tree Component, Rich Text Editor, Modal/Dialog, Toast/Notification, Form Components, Loading States, Empty States, Breadcrumbs, Status Badges.

### Role-Based Flows

**Public:** Landing -> Browse Categories -> Course List -> Course Detail -> Login/Register -> Enroll
**Student:** Dashboard -> Resume Course -> Lesson Tree + Content -> Mark Complete -> Take Exam -> Results -> Certificate
**Admin:** Dashboard -> Manage Courses/Lessons/Exams/Taxonomy/Homepage/Users
**Instructor:** Dashboard -> Exam Analytics -> Student Performance

---

## 16. Sprint 7 -- Local Setup & Foundation

**Objective:** Establish development environment, verify backend, scaffold frontend, set up tooling.

### Backend Tasks

| # | Task | Details |
|---|---|---|
| B0.1 | Verify backend builds and starts | `mvn clean compile`, `mvn spring-boot:run` |done
| B0.2 | Create local PostgreSQL database | Docker compose with `educator` database |done
| B0.3 | Run application and verify startup | Confirm RoleInitializer seeds roles, verify endpoints |done
| B0.4 | Test all existing APIs with Postman | Create full API collection |COMPLETED
| B0.5 | Create `CertificateRepository.java` | `public interface CertificateRepository extends JpaRepository<Certificate, UUID> {}` |COMPLETED
| B0.6 | Document all API response shapes | Save as TypeScript types reference (`docs/B0.6_API_RESPONSE_SHAPES.md`) |COMPLETED

### Frontend Tasks

| # | Task | Details |
|---|---|---|
| F0.1 | Initialize Next.js 15 project | `npx create-next-app@latest frontend --typescript --tailwind --app --src-dir` |COMPLETED
| F0.2 | Install core dependencies | shadcn/ui, @tanstack/react-query, zustand, react-hook-form, zod, axios, lucide-react |COMPLETED
| F0.3 | Configure Tailwind design tokens | Custom colors, typography, spacing from Design System |COMPLETED
| F0.4 | Set up project structure | app/, components/, lib/, styles/ per plan |COMPLETED
| F0.5 | Create TypeScript types | All backend entity types, enum types, API request/response types (`frontend/src/types/*`) |COMPLETED
| F0.6 | Create API client | Axios instance with base URL, JWT interceptor, error handler (`frontend/src/lib/api/*`) |COMPLETED
| F0.7 | Create auth store (Zustand) | Token storage, user state, login/logout actions (`frontend/src/store/auth-store.ts`) |COMPLETED
| F0.8 | Create route layout groups | (public), (learner), (admin), (instructor) with placeholder pages |COMPLETED
| F0.9 | Install shadcn/ui components | Button, Input, Card, Dialog, Table, Badge, Toast, Dropdown, Tabs |COMPLETED

### Testing Tasks

| # | Task | Details |
|---|---|---|
| T0.1 | Set up backend test infrastructure | Verify `mvn test` runs existing context-load test | done
| T0.2 | Set up frontend test infrastructure | Install vitest, @testing-library/react, msw |COMPLETED

### Deployment Tasks

| # | Task | Details |
|---|---|---|
| D0.1 | Create `docker-compose.yml` for local dev | PostgreSQL 15, backend, frontend |done
| D0.2 | Initialize Git branching strategy | main (production), develop (integration), feature branches (`docs/D0.2_GIT_BRANCHING_STRATEGY.md`) |COMPLETED

### Exit Criteria

- [x] Backend starts on localhost:8080, all endpoints respond
- [x] All APIs tested via Postman with documented response shapes
- [x] CertificateRepository.java exists, backend compiles
- [x] Frontend scaffolded on localhost:3000 with placeholder pages
- [x] TypeScript types match backend entities
- [x] API client can call backend
- [x] Design tokens configured in Tailwind
- [x] shadcn/ui base components installed
- [x] Docker compose brings up full local environment

### Sprint 7 Handoff Artifacts (2026-02-12)

- `SPRINT_7_FINAL_HANDOFF.md` -- final Sprint 7 closure status, completed work, validation evidence, and Sprint 8 start point.
- `docs/B0.6_API_RESPONSE_SHAPES.md` -- backend-verified response shape reference from Newman execution.
- `docs/D0.2_GIT_BRANCHING_STRATEGY.md` -- branch model (`main`, `develop`, `feature/*`, `hotfix/*`) and workflow.
- `postman/Educator_MVP_Backend_Aligned.postman_collection.json` -- backend-aligned API test suite.
- `postman/Educator_MVP_Backend_Aligned.postman_environment.json` -- environment used for backend-aligned verification.
- Latest backend-aligned verification snapshot: `40 requests`, `111 assertions`, `0 failed`.
- Frontend validation snapshot: `npm run lint`, `npx tsc --noEmit`, `npm test`, `npm run build` all passing.

---

## 17. Sprint 8 -- Security Hardening & Backend Fixes

**Objective:** Fix all critical security vulnerabilities, add exception handling, validation, CORS. Make backend production-safe.

### Backend Tasks

| # | Task | Details |
|---|---|---|
| B1.1 | Externalize JWT secret | `${JWT_SECRET:educator-secret-key-change-later}` |COMPLETED
| B1.2 | Externalize DB credentials | `${DB_HOST}`, `${DB_USERNAME}`, `${DB_PASSWORD}` |COMPLETED
| B1.3 | Populate application-prod.yml | `ddl-auto: validate`, `show-sql: false`, env vars, pool config |COMPLETED
| B1.4 | Add CORS configuration | CorsConfigurer bean; allow frontend origin |COMPLETED
| B1.5 | Set SessionCreationPolicy.STATELESS | Explicit in SecurityConfig |COMPLETED
| B1.6 | Add explicit URL patterns | `/api/instructor/**`, `/api/student/**` as authenticated |COMPLETED
| B1.7 | Fix StudentExamController | Replace @RequestParam userId with @AuthenticationPrincipal |COMPLETED
| B1.8 | Create @ControllerAdvice GlobalExceptionHandler | 400, 404, 409, 403, 500 with structured JSON |COMPLETED
| B1.9 | Add spring-boot-starter-validation | Maven dependency + @Valid on all @RequestBody parameters |COMPLETED
| B1.10 | Add validation annotations to DTOs | @NotBlank @Email, @Size(min=8), validate all request DTOs |COMPLETED
| B1.11 | Add security headers | HSTS, X-Frame-Options DENY, X-Content-Type-Options nosniff |COMPLETED
| B1.12 | Improve JWT filter error response | JSON error on 401 |COMPLETED
| B1.13 | Add password strength validation | Min 8 chars, 1 uppercase, 1 digit |COMPLETED
| B1.14 | Add rate limiting on auth | 10 req/min per IP on /api/auth/** |COMPLETED

### Frontend Tasks

| # | Task | Details |
|---|---|---|
| F1.1 | Build Login page | Email + password, validation, JWT storage, role-based redirect (`/login` with Framer Motion + GSAP) |COMPLETED
| F1.2 | Build Register page | Confirm password, password strength indicator |COMPLETED
| F1.3 | Build Auth Guard component | Check JWT validity, redirect to login |COMPLETED
| F1.4 | Build role-based route protection | Admin routes require ADMIN; learner routes require auth |COMPLETED
| F1.5 | Build JWT refresh mechanism | Intercept 401, attempt refresh, redirect on failure |COMPLETED
| F1.6 | Build top navigation bar | Logo, role-aware nav links, user menu dropdown |COMPLETED
| F1.7 | Build error boundary | Global error boundary with fallback UI |COMPLETED
| F1.8 | Build toast notification system | Success, error, warning, info with auto-dismiss |COMPLETED

### Testing Tasks

| # | Task | Details |
|---|---|---|
| T1.1 | Unit tests for AuthService | Register (happy, duplicate email), authenticate (happy, wrong password) |COMPLETED
| T1.2 | Unit tests for JwtUtil | Generate, validate (valid, expired, tampered), extract |COMPLETED
| T1.3 | Integration test: register + login | POST /register -> POST /login -> verify JWT -> use on protected endpoint |COMPLETED
| T1.4 | Security test: role enforcement | ADMIN-only rejects STUDENT, unauthenticated rejected |COMPLETED
| T1.5 | Frontend tests for auth flow | Login render, validation, redirect, error display |

### Exit Criteria

- [x] All secrets externalized; backend starts with env vars
- [x] application-prod.yml complete
- [x] CORS preflight succeeds
- [x] GlobalExceptionHandler returns proper status codes with JSON
- [x] All DTOs validated; invalid requests return 400
- [x] StudentExamController uses @AuthenticationPrincipal
- [x] Rate limiting active on auth
- [x] Login and register pages functional and styled
- [x] Auth guard protects all non-public routes
- [x] 15+ backend tests, 5+ frontend tests passing

**Sprint 8 Status:** ✅ COMPLETED (All tasks 100% done)

---

## 18. Sprint 9 -- Backend Completion & API Polish

**Objective:** Complete all missing backend APIs, add pagination, wire partially-implemented features, build public pages.

### Backend Tasks

| # | Task | Details |
|---|---|---|
| B2.1 | Add pagination to all list endpoints | Pageable parameter, Page<T>, default 20, max 100 |
| B2.2 | Add User profile endpoint | GET/PUT /api/learner/profile |
| B2.3 | Add password change endpoint | PUT /api/learner/profile/password |
| B2.4 | Add password reset flow | POST /api/auth/reset-request, POST /api/auth/reset-confirm |
| B2.5 | Wire exam timer enforcement | Check startedAt + timeLimitMinutes on submit; scheduled auto-expire |
| B2.6 | Wire question shuffle | Randomize order in ExamAttemptService.start() |
| B2.7 | Add answer explanations API | GET /api/student/exams/attempts/{attemptId}/review (EVALUATED only) |
| B2.8 | Wire enrollment auto-completion | After marking lesson complete, check all done + exam passed |
| B2.9 | Add exam attempt history API | GET /api/student/exams/{examId}/attempts |
| B2.10 | Add notification list/read API | GET /api/learner/notifications, PUT .../notifications/{id}/read |
| B2.11 | Add unread notification count | GET /api/learner/notifications/unread-count |
| B2.12 | Add certificate service | Create, issue, revoke certificates |
| B2.13 | Add certificate API | GET /api/learner/certificates, GET /api/admin/certificates/{id} |
| B2.14 | Add course search & filter | GET /api/public/courses/search?q=&difficulty=&status= |
| B2.15 | Add admin user management API | GET /api/admin/users, PUT /api/admin/users/{id}/roles |
| B2.16 | Add admin dashboard stats API | GET /api/admin/stats |
| B2.17 | Set up Flyway | spring-boot-starter-flyway, V1__baseline.sql, remove ddl-auto: update |
| B2.18 | Add Spring Actuator | /actuator/health, readiness, liveness |
| B2.19 | Add refresh token endpoint | POST /api/auth/refresh |

### Frontend Tasks

| # | Task | Details |
|---|---|---|
| F2.1 | Build Homepage (dynamic CMS) | Fetch /api/public/homepage, render sections/blocks |
| F2.2 | Build Course Catalog page | Hierarchy tree, course cards, search, filters |
| F2.3 | Build Course Detail page | Course info, lesson tree, difficulty badge, enroll button |
| F2.4 | Build 404 and error pages | Branded illustration, "Go Home" button |
| F2.5 | Build footer | Copyright, links, responsive |

### Testing Tasks

| # | Task | Details |
|---|---|---|
| T2.1 | Unit tests for all new services | Certificate, pagination, profile, password |
| T2.2 | Unit tests for exam timer + shuffle | Timer enforcement, shuffle randomization |
| T2.3 | Integration tests for all new endpoints | Every new API from B2.1-B2.19 |
| T2.4 | Engine tests (100% coverage) | All 10 engine classes |
| T2.5 | Repository tests | All custom query methods |
| T2.6 | Frontend tests for public pages | Homepage, catalog, course detail |

### Deployment Tasks

| # | Task | Details |
|---|---|---|
| D2.1 | Create initial CI pipeline | GitHub Actions: mvn clean test, npm run lint && npm run test |

### Exit Criteria

- [ ] All list endpoints paginated
- [ ] User profile and password management work
- [ ] Exam timer enforcement works
- [ ] Question shuffle works
- [ ] Answer explanations visible after exam
- [ ] Enrollment auto-completes
- [ ] Notification APIs work
- [ ] Certificate CRUD works
- [ ] Course search returns filtered paginated results
- [ ] Admin user management and stats APIs work
- [ ] Flyway manages schema migrations
- [ ] Actuator health endpoint responds
- [ ] Homepage renders dynamic CMS content
- [ ] Course catalog with search and filters
- [ ] CI pipeline runs tests on push
- [ ] 60+ backend tests, 15+ frontend tests

---

## 19. Sprint 10 -- Student Experience

**Objective:** Build the complete student journey end-to-end.

### Backend Tasks

| # | Task | Details |
|---|---|---|
| B3.1 | Auto-notification on course completion | CourseCompletion -> COURSE_COMPLETED notification |
| B3.2 | Auto-notification on exam pass/fail | Evaluation -> EXAM_PASSED or EXAM_FAILED notification |
| B3.3 | Auto-certificate on completion | CourseCompletion + exam passed -> Certificate (GENERATED) |
| B3.4 | Add negative exam marking | negativeMarkPercentage field, scoring logic update |
| B3.5 | Fix API issues from frontend integration | Buffer for response shape fixes |

### Frontend Tasks

| # | Task | Details |
|---|---|---|
| F3.1 | Build Student Dashboard | Enrolled courses grid, progress bars, status badges, "Resume" |
| F3.2 | Build Course Learning View | Collapsible lesson tree sidebar, content area, "Mark Complete" |
| F3.3 | Build lesson content renderers | TEXT: HTML; VIDEO: iframe; DOCUMENT: PDF link/viewer |
| F3.4 | Build Exam Start Page | Rules, timer info, attempts remaining, "Start" |
| F3.5 | Build Exam Taking Page | Questions, radio options, navigation, timer, submit + confirm |
| F3.6 | Build Exam Results Page | Score card, pass/fail, question breakdown, explanations |
| F3.7 | Build Exam Attempt History | Table of past attempts |
| F3.8 | Build Notifications Page | List with read/unread styling, mark-as-read |
| F3.9 | Build Notification Bell (nav) | Unread count badge, dropdown top 5, "View All" |
| F3.10 | Build Certificates Page | Certificate cards, download button |
| F3.11 | Build Profile / Settings Page | Display email, edit name, change password |
| F3.12 | Build enrollment flow | Enroll -> success -> redirect to learning view |
| F3.13 | Build drop enrollment flow | "Drop Course" with confirmation |
| F3.14 | Build password reset pages | Request reset form, set new password form |

### Testing Tasks

| # | Task | Details |
|---|---|---|
| T3.1 | Integration test: full student journey | Register -> login -> browse -> enroll -> lessons -> exam -> results -> certificate |
| T3.2 | Frontend component tests | Dashboard, learning view, exam taking, results |
| T3.3 | Exam edge cases | Timer expiry, max attempts, shuffle correctness |
| T3.4 | Notification trigger tests | Auto-created on completion, exam pass/fail |
| T3.5 | Accessibility audit | Screen reader, keyboard navigation, color contrast |

### Exit Criteria

- [ ] Student can register, login, browse, enroll, complete lessons, take exam, see results
- [ ] Exam timer counts down and enforces limit
- [ ] Notifications appear on completion and exam results
- [ ] Certificates appear after completion
- [ ] Profile and password management work
- [ ] All student screens responsive (mobile, tablet, desktop)
- [ ] Accessibility audit passes (WCAG 2.1 AA)
- [ ] 100+ backend tests, 40+ frontend tests

---

## 20. Sprint 11 -- Admin Experience

**Objective:** Build the complete admin panel.

### Backend Tasks

| # | Task | Details |
|---|---|---|
| B4.1 | Add course update endpoint | PUT /api/admin/courses/{id} |
| B4.2 | Add lesson update endpoint | PUT /api/admin/lessons/{id} |
| B4.3 | Add lesson reorder endpoint | PUT /api/admin/lessons/reorder |
| B4.4 | Add exam question CRUD API | POST/PUT/DELETE questions and options |
| B4.5 | Add exam option CRUD API | POST/PUT/DELETE options |
| B4.6 | Add homepage section update/delete | PUT/DELETE /api/admin/homepage/sections/{id} |
| B4.7 | Add homepage block update/delete/reorder | PUT/DELETE/reorder blocks |
| B4.8 | Add block config CRUD | POST/PUT block config |
| B4.9 | Add section layout CRUD | POST/PUT section layout |
| B4.10 | Add instructor analytics API | GET /api/instructor/exams/{examId}/analytics |

### Frontend Tasks

| # | Task | Details |
|---|---|---|
| F4.1 | Build Admin Dashboard | Metrics cards, recent activity |
| F4.2 | Build Admin Sidebar | Collapsible with section groups |
| F4.3 | Build Course Management table | Data table: title, status badge, difficulty, actions |
| F4.4 | Build Course Creation/Edit form | All fields, hierarchy selector, auto-save draft |
| F4.5 | Build Lesson Editor | Tree with drag-drop, add root/child, inline edit |
| F4.6 | Build Lesson Content Editor | Rich text (TipTap) for TEXT; URL input for VIDEO/DOCUMENT |
| F4.7 | Build Exam Management table | Title, course link, status, question count |
| F4.8 | Build Exam Editor | Config form + question builder |
| F4.9 | Build Question Builder | Add questions + options, correct flag toggle, reorder |
| F4.10 | Build Taxonomy Manager | Tree editor, create/edit/move/delete/restore, drag-drop |
| F4.11 | Build Homepage CMS | Section list, block configurator, layout picker |
| F4.12 | Build User Management | User table, search, role assignment modal |
| F4.13 | Build confirmation dialogs | Publish, archive, delete (with entity name), drop (with warning) |

### Testing Tasks

| # | Task | Details |
|---|---|---|
| T4.1 | Integration tests for all new admin APIs | Course update, lesson CRUD, exam question CRUD, homepage CRUD |
| T4.2 | Frontend tests for admin forms | Course form, exam config, question builder |
| T4.3 | End-to-end: course lifecycle | Create -> add lessons -> create exam -> add questions -> publish |
| T4.4 | Test drag-drop | Lesson reorder, taxonomy move, block reorder |

### Exit Criteria

- [ ] Admin can create, edit, publish, archive, delete courses
- [ ] Admin can add, edit, reorder, delete lessons with drag-drop
- [ ] Admin can create exams with questions/options, publish, archive
- [ ] Admin can manage taxonomy tree
- [ ] Admin can build homepage
- [ ] Admin can manage users and roles
- [ ] Admin dashboard shows metrics
- [ ] All admin screens responsive
- [ ] 140+ backend tests, 70+ frontend tests

---

## 21. Sprint 12 -- Instructor, Polish & Integration

**Objective:** Build instructor screens, polish all UI/UX, achieve cohesive product feel.

### Backend Tasks

| # | Task | Details |
|---|---|---|
| B5.1 | Add instructor course assignment | Link instructors to courses |
| B5.2 | Add instructor student list API | GET /api/instructor/courses/{courseId}/students |
| B5.3 | Add bulk lesson operations | POST /api/admin/lessons/course/{courseId}/bulk |
| B5.4 | Add structured logging | JSON log format, correlation ID, request/response filter |
| B5.5 | Optimize N+1 queries | Lesson tree flat + in-memory; enrollment JOIN FETCH |
| B5.6 | Add OpenAPI/Swagger | springdoc-openapi-starter-webmvc-ui |

### Frontend Tasks

| # | Task | Details |
|---|---|---|
| F5.1 | Build Instructor Dashboard | Assigned courses, stats per course |
| F5.2 | Build Exam Analytics Page | Pass rate pie, score distribution, question difficulty heatmap |
| F5.3 | Build Student Performance Page | Enrollment count, completion rate, avg score |
| F5.4 | Add loading skeletons | Every data-fetching page |
| F5.5 | Add empty states | Every list/table/grid with illustration + CTA |
| F5.6 | Add error states | API error -> friendly message + retry button |
| F5.7 | Add transitions & animations | Page fade, sidebar toggle, toast slide-in |
| F5.8 | Responsive audit | Test at 375px, 768px, 1024px, 1440px |
| F5.9 | Accessibility audit | Fix all axe-core violations, test with screen readers |
| F5.10 | Build breadcrumbs | All nested pages |
| F5.11 | Build global search | Command-K overlay for admin |
| F5.12 | Build favicon, meta tags, OG tags | SEO for public pages |

### Testing Tasks

| # | Task | Details |
|---|---|---|
| T5.1 | E2E test suite (Playwright) | 10 critical tests covering all roles |
| T5.2 | E2E: Public flow | Homepage -> catalog -> detail -> register -> login |
| T5.3 | E2E: Student flow | Login -> enroll -> lessons -> exam -> results |
| T5.4 | E2E: Admin flow | Login -> create course -> lessons -> exam -> publish |
| T5.5 | Performance test | Lighthouse: Performance > 90, Accessibility > 95 |
| T5.6 | Cross-browser testing | Chrome, Firefox, Safari, Edge |

### Exit Criteria

- [ ] Instructor dashboard and analytics functional
- [ ] Every page has loading, error, and empty states
- [ ] All screens responsive at 4 breakpoints
- [ ] Accessibility audit passes (0 axe-core violations)
- [ ] Lighthouse scores meet targets
- [ ] OpenAPI documentation at /swagger-ui
- [ ] 10 E2E tests passing
- [ ] Cross-browser passes
- [ ] 160+ backend, 90+ frontend, 10 E2E tests

---

## 22. Sprint 13 -- Testing, Performance & Pre-Deploy

**Objective:** Comprehensive test coverage, performance optimization, staging deployment.

### Backend Tasks

| # | Task | Details |
|---|---|---|
| B6.1 | Increase unit test coverage to 80%+ | All services, edge cases, error paths |
| B6.2 | Load test key endpoints | 100 concurrent users, target < 200ms p95 |
| B6.3 | Add database indexes for new queries | Course search, notification lookup, certificate lookup |
| B6.4 | Connection pool tuning | HikariCP: max=20, min=5, idle-timeout=600s |
| B6.5 | Add request size limits | Max body 10MB |
| B6.6 | Security penetration test | JWT forgery, SQL injection, XSS, role escalation |
| B6.7 | Fix remaining bugs | Buffer for integration issues |

### Frontend Tasks

| # | Task | Details |
|---|---|---|
| F6.1 | Performance optimization | Code splitting, lazy load admin, optimize images |
| F6.2 | Bundle size audit | @next/bundle-analyzer, tree-shake unused |
| F6.3 | SEO optimization | Next.js metadata API, sitemap.xml, robots.txt |
| F6.4 | PWA considerations | Web manifest, offline awareness |
| F6.5 | Fix remaining frontend bugs | Buffer |

### Deployment Tasks

| # | Task | Details |
|---|---|---|
| D6.1 | Create production Dockerfile (backend) | Multi-stage: Maven build -> JRE-slim runtime |
| D6.2 | Create production Dockerfile (frontend) | Multi-stage: npm build -> standalone Next.js |
| D6.3 | Set up staging environment | Cloud VM/container, managed PostgreSQL, env vars |
| D6.4 | Set up CI/CD pipeline (full) | Build -> test -> lint -> package -> push -> deploy staging |
| D6.5 | Set up SSL/TLS | Let's Encrypt or cloud-managed |
| D6.6 | Set up domain + DNS | Custom domain -> staging |
| D6.7 | Set up database backup strategy | Daily automated, 30-day retention, test restore |
| D6.8 | Create production env vars | Document every required env var |
| D6.9 | Set up monitoring | Actuator health polling, basic alerting |

### Exit Criteria

- [ ] Backend coverage >= 80%
- [ ] Load test passes: < 200ms p95 at 100 concurrent
- [ ] Security scan: no critical/high findings
- [ ] Staging running on HTTPS with custom domain
- [ ] CI/CD deploys to staging on merge to develop
- [ ] Database backups automated and restore tested
- [ ] Frontend bundle < 250KB gzipped
- [ ] Lighthouse Performance > 90
- [ ] All E2E tests pass on staging
- [ ] Zero known critical bugs

---

## 23. Sprint 14 -- Production Deployment & Launch

**Objective:** Deploy to production, verify, monitor, launch.

### Deployment Tasks

| # | Task | Details |
|---|---|---|
| D7.1 | Provision production infrastructure | Production-grade compute, managed PostgreSQL HA |
| D7.2 | Deploy backend to production | Docker image -> container service |
| D7.3 | Deploy frontend to production | Vercel or Docker -> CDN-backed hosting |
| D7.4 | Run Flyway migrations on production | Baseline + all migration scripts |
| D7.5 | Configure production env vars | JWT_SECRET (256-bit), DB credentials, CORS frontend URL |
| D7.6 | Set up production monitoring | Health check polling, error rate alerts, latency alerts |
| D7.7 | Set up log aggregation | Centralized logging (CloudWatch, ELK, or Loki) |
| D7.8 | Create admin user | Register via API, assign ADMIN role in DB |
| D7.9 | Seed initial data | Hierarchy nodes, initial courses |
| D7.10 | DNS cutover | Point production domain to infrastructure |

### Testing Tasks

| # | Task | Details |
|---|---|---|
| T7.1 | Production smoke test | Register, login, browse, enroll, complete, exam |
| T7.2 | Admin smoke test | Login, create course, add lessons, create exam, publish |
| T7.3 | Performance validation | p95 < 200ms on production |
| T7.4 | SSL verification | HTTPS, HSTS, no mixed content |
| T7.5 | Security headers verification | securityheaders.com A+ score |

### Exit Criteria (LAUNCH GATE)

- [ ] Production live on custom domain with HTTPS
- [ ] All smoke tests pass on production
- [ ] Monitoring and alerting active
- [ ] Logs flowing to centralized system
- [ ] Admin user exists and can manage platform
- [ ] p95 latency < 200ms
- [ ] Security headers A+
- [ ] Database backups verified on production
- [ ] Rollback procedure documented and tested
- [ ] Incident response runbook created

---

# PART IV -- DEPLOYMENT & POST-PRODUCTION

---

## 24. Security Hardening Checklist

| Requirement | Details | Sprint |
|---|---|---|
| JWT secret | 256-bit random, from env var | 8 |
| DB credentials | From env vars or secrets manager | 8 |
| HTTPS | TLS certificate, force HTTP -> HTTPS | 13 |
| CORS | Whitelist frontend origin only | 8 |
| Rate limiting | Auth: 10 req/min per IP | 8 |
| Security headers | HSTS, X-Frame-Options DENY, X-Content-Type-Options nosniff, CSP | 8 |
| Input validation | @Valid on all DTOs, sanitize all input | 8 |
| XSS prevention | Content-Security-Policy, output encoding | 8 |
| CSRF | Disabled (stateless) -- already done | -- |
| Password policy | Min 8 chars, 1 number, 1 uppercase | 8 |
| JWT refresh tokens | Silent refresh, revocation on logout | 9 |
| Sensitive data logging | Mask passwords, tokens, PII | 12 |
| Session policy | STATELESS explicit | 8 |

---

## 25. CI/CD Pipeline

### Pipeline Stages

```
Push to branch
  -> Lint (backend + frontend)
  -> Build (mvn clean package, npm run build)
  -> Test (mvn test, npm run test)
  -> Package (Docker images)
  -> Push images to registry
  -> Deploy to staging (on develop merge)
  -> Deploy to production (on main merge, manual gate)
```

### GitHub Actions Configuration

- Trigger: push to any branch, PR to develop/main
- Backend: Java 17, Maven, PostgreSQL service container
- Frontend: Node.js LTS, npm
- Docker: Multi-stage builds, push to container registry
- Environments: staging (auto-deploy), production (manual approval)

---

## 26. Infrastructure & Environments

### Environment Matrix

| Environment | Purpose | Database | Secrets | Deploy Trigger |
|---|---|---|---|---|
| Local | Development | Docker PostgreSQL | .env file | Manual |
| Staging | Pre-production testing | Managed PostgreSQL | Env vars | Merge to develop |
| Production | Live users | Managed PostgreSQL HA | Secrets manager | Merge to main + approval |

### Docker Configuration

**Backend Dockerfile (Multi-stage):**
- Stage 1: Maven build (Java 17 + Maven)
- Stage 2: JRE-slim runtime, copy JAR, expose 8080

**Frontend Dockerfile (Multi-stage):**
- Stage 1: Node.js build (npm install + npm run build)
- Stage 2: Standalone Next.js or nginx

### Required Environment Variables

| Variable | Description | Example |
|---|---|---|
| JWT_SECRET | 256-bit random key | (generated) |
| DB_HOST | PostgreSQL host | localhost / RDS endpoint |
| DB_PORT | PostgreSQL port | 5432 |
| DB_NAME | Database name | educator |
| DB_USERNAME | Database user | educator |
| DB_PASSWORD | Database password | (secret) |
| FRONTEND_URL | Frontend origin for CORS | https://app.educator.com |
| SPRING_PROFILES_ACTIVE | Active profile | prod |

---

## 27. Monitoring & Observability

| Requirement | Tool/Approach |
|---|---|
| Structured logging | JSON format, correlation IDs, request tracing |
| Health endpoints | Spring Actuator: /health, /health/readiness, /health/liveness |
| Metrics | Micrometer -> Prometheus-compatible endpoint |
| Error tracking | Sentry or equivalent |
| Request logging | Method, path, status, duration, user ID (masked) |
| Alerting | Health check failures, error rate spikes, latency thresholds |

---

## 28. Backup & Rollback

### Database Backup Strategy

- Automated daily PostgreSQL backups
- 30-day retention
- Point-in-time recovery enabled (managed DB)
- Restore tested quarterly

### Application Rollback

- Docker images tagged with git commit SHA
- Rollback = deploy previous image tag
- Database rollback via Flyway undo migrations (or restore from backup)
- Zero-downtime rolling deployment with health check gates

### Rollback Procedure

1. Identify failing deployment via monitoring
2. Trigger rollback in CI/CD (deploy previous image)
3. Verify health checks pass
4. If DB migration involved, run Flyway undo or restore from backup
5. Post-mortem within 24 hours

---

## 29. Production Readiness Checklist

### Backend

- [ ] All 30+ API endpoints respond correctly
- [ ] All endpoints have @Valid input validation
- [ ] GlobalExceptionHandler returns structured JSON (never stack traces)
- [ ] JWT secret 256-bit random from env var
- [ ] DB credentials from env vars
- [ ] hibernate.ddl-auto: validate in production
- [ ] show-sql: false in production
- [ ] CORS configured for production domain only
- [ ] Rate limiting on auth endpoints
- [ ] Security headers present
- [ ] Flyway manages schema changes
- [ ] Actuator health endpoints exposed
- [ ] Structured JSON logging with correlation IDs
- [ ] Pagination on all list endpoints
- [ ] Exam timer enforcement works
- [ ] Question shuffle works
- [ ] Certificate auto-creation on completion
- [ ] Notification auto-creation on events
- [ ] Password reset flow works

### Frontend

- [ ] All 32 screens built, styled, responsive
- [ ] Auth flows work (login, register, password reset)
- [ ] Student: browse -> enroll -> learn -> exam -> results -> certificate
- [ ] Admin: manage courses, lessons, exams, taxonomy, homepage, users
- [ ] Instructor: view analytics
- [ ] Every page has loading skeleton, error state, empty state
- [ ] Toast notifications on all actions
- [ ] Breadcrumbs on all nested pages
- [ ] Auth guard on all protected routes
- [ ] JWT auto-refresh or clean expiry
- [ ] Mobile responsive at 375px, 768px, 1024px, 1440px
- [ ] Accessibility: 0 axe-core violations, keyboard navigable
- [ ] Lighthouse: Performance > 90, Accessibility > 95
- [ ] Bundle < 250KB gzipped
- [ ] SEO: meta tags, OG tags, sitemap on public pages

### Testing

- [ ] Backend unit coverage >= 80%
- [ ] Engine coverage = 100%
- [ ] All API endpoints have integration tests
- [ ] 10+ E2E tests covering all roles
- [ ] Security scan: 0 critical/high
- [ ] Load test: p95 < 200ms at 100 concurrent
- [ ] Cross-browser tested (Chrome, Firefox, Safari, Edge)

### Infrastructure

- [ ] Production on custom domain with HTTPS
- [ ] CI/CD: push -> test -> build -> deploy
- [ ] Flyway manages migrations
- [ ] Daily database backups with tested restore
- [ ] Monitoring: health checks, error rate, latency alerts
- [ ] Centralized log aggregation
- [ ] Rollback procedure documented and tested
- [ ] All secrets in env vars or secrets manager
- [ ] Zero hardcoded credentials in source

### Documentation

- [ ] API docs via OpenAPI/Swagger
- [ ] Environment variable reference
- [ ] Deployment runbook
- [ ] Incident response runbook

---

## 30. Post-Launch Operations

### First 48 Hours

| Task | Priority |
|---|---|
| Monitor error rates and latency | P0 |
| Fix production-only bugs immediately | P0 |
| Verify database backups are running | P0 |
| Check log aggregation is working | P1 |

### First 2 Weeks

| Task | Priority |
|---|---|
| Set up database performance monitoring | P1 |
| Implement email notification delivery (SendGrid/SES) | P1 |
| Implement PDF certificate generation | P1 |
| Add payment gateway (Razorpay/Stripe) | P1 |
| Add subscription model | P1 |

### Ongoing

- Weekly security patch review
- Monthly dependency update
- Quarterly backup restore test
- Quarterly performance baseline
- Continuous monitoring and alerting

---

# PART V -- ONGOING FEATURE EVOLUTION & SPRINT AMENDMENTS

> This section defines the industry-standard process for adding new features,
> documenting changes, and maintaining this document as a living source of truth.

---

## 31. Amendment Process

### When to Create an Amendment

1. **New feature proposed** that was not in the original plan
2. **Existing feature modified** in scope, priority, or sprint assignment
3. **Feature deferred** from its original sprint
4. **Sprint scope changed** (tasks added, removed, or moved between sprints)
5. **Architectural decision changed** that affects multiple modules
6. **Bug discovered** that requires plan adjustment

### How to Submit an Amendment

1. Create a Feature Entry using the template in Section 32
2. Set status to `PROPOSED`
3. Add the entry to Section 34 (Sprint Amendment Log)
4. Review and approve (update status to `APPROVED`)
5. Assign to a sprint
6. Update relevant sprint section in Part III if tasks are affected
7. Record in Section 35 (Change Log)

### Rules

- Amendments **never modify Part I or Part II** (historical baseline is immutable)
- Amendments to Part III update sprint task lists in-place
- Every amendment is recorded in the Change Log with date and reason
- Feature scope changes require explicit justification

---

## 32. Feature Entry Template

```
### FE-[NNN]: [Feature Title]

| Field | Value |
|---|---|
| ID | FE-[NNN] |
| Title | [Short descriptive title] |
| Status | PROPOSED / APPROVED / IN_PROGRESS / IMPLEMENTED / DEFERRED |
| Priority | P0 / P1 / P2 / P3 |
| Sprint | [Target sprint number or "Backlog"] |
| Proposed By | [Name or role] |
| Proposed Date | [YYYY-MM-DD] |
| Approved Date | [YYYY-MM-DD or pending] |
| Implemented Date | [YYYY-MM-DD or pending] |

**Description:**
[What the feature does, why it is needed]

**Acceptance Criteria:**
- [ ] [Criterion 1]
- [ ] [Criterion 2]

**Dependencies:**
[Other features, APIs, or modules this depends on]

**Impact:**
[Files, modules, or sprints affected]

**Notes:**
[Additional context, alternatives considered, risks]
```

---

## 33. Feature Status Lifecycle

```
PROPOSED
  |
  v
APPROVED -----------> DEFERRED (with reason and target sprint)
  |                       |
  v                       v
IN_PROGRESS          PROPOSED (re-enters cycle when ready)
  |
  v
IMPLEMENTED
```

| Status | Meaning |
|---|---|
| PROPOSED | Feature idea submitted, awaiting review |
| APPROVED | Reviewed and accepted for implementation |
| IN_PROGRESS | Active development underway |
| IMPLEMENTED | Code complete, tested, merged |
| DEFERRED | Postponed to a future sprint with documented reason |

### Priority Definitions

| Priority | Definition |
|---|---|
| P0 | Blocks production deployment or creates security vulnerability |
| P1 | Required for complete user experience; must be in current sprint |
| P2 | Important enhancement; can be deferred one sprint |
| P3 | Nice-to-have; backlog candidate |

---

## 34. Sprint Amendment Log

> Add new feature entries here as they are proposed. Keep in chronological order.

*No amendments recorded yet. This section will be populated as development progresses.*

---

## 35. Change Log

| Date | Version | Change | Reason |
|---|---|---|---|
| 2026-02-10 | 2.0 | Initial Master Plan created | Consolidation of TDD v1.0, Handover Review, Execution Plan, README into single source of truth |

---

# APPENDICES

---

## Appendix A: Complete Enum Reference

| Enum | Values |
|---|---|
| CourseStatus | DRAFT, PUBLISHED |
| CourseDifficulty | BEGINNER, INTERMEDIATE, ADVANCED |
| LessonType | TEXT, VIDEO, DOCUMENT (DB); TEXT, VIDEO, PDF, EXAM (lesson enum) |
| EnrollmentStatus | ACTIVE, COMPLETED, DROPPED |
| ExamStatus | DRAFT, PUBLISHED, ARCHIVED |
| AttemptStatus | IN_PROGRESS, SUBMITTED, EVALUATED, EXPIRED |
| ExamType | MCQ, PDF, HYBRID |
| CertificateStatus | GENERATED, ISSUED, REVOKED, EXPIRED |
| AutomationTriggerType | COURSE_COMPLETED, EXAM_PASSED, CERTIFICATE_GENERATED, SUBSCRIPTION_ACTIVATED, SUBSCRIPTION_EXPIRED |
| NotificationType | COURSE_COMPLETED, EXAM_PASSED, EXAM_FAILED, CERTIFICATE_ELIGIBLE |
| NotificationStatus | PERSISTED, ACKNOWLEDGED |
| NotificationTrigger | COURSE_COMPLETED, EXAM_PASSED, EXAM_FAILED, CERTIFICATE_ELIGIBLE |
| MediaType | VIDEO, PDF |
| MediaSourceType | EXTERNAL, INTERNAL |
| BlockType | COURSE, EXAM, IMAGE, VIDEO, TEXT, CTA |
| BlockTargetType | COURSE, EXAM, EXTERNAL |
| LayoutType | HORIZONTAL_SCROLL, VERTICAL_LIST, GRID, CAROUSEL, BANNER_ROTATOR |
| SectionPosition | TOP, LEFT, CENTER, RIGHT, BOTTOM |
| ImportEntityType | COURSE, EXAM |
| ImportSourceType | DOCX, PDF, CSV |

---

## Appendix B: File Inventory

### File Count Summary

| Category | Count |
|---|---|
| Java source files | 119 (120 with CertificateRepository) |
| Entity classes | 23 |
| Enum types | 19 |
| DTO classes | 8 |
| Service classes | 12 |
| Controller classes | 15 |
| Repository interfaces | 16 (17 with CertificateRepository) |
| Engine classes | 10 |
| Import framework | 6 |
| Database tables | 23 |

### Module File Distribution

| Module | Files |
|---|---|
| auth/ | 5 (AuthController, AuthService, LoginRequest, RegisterRequest, JwtResponse) |
| security/ | 6 (SecurityConfig, JwtAuthenticationFilter, JwtUtil, CustomUserDetailsService, CustomUserDetails, PasswordConfig) |
| users/ | 2 (User, UserRepository) |
| roles/ | 3 (Role, RoleRepository, RoleInitializer) |
| course/ | 6 (Course, CourseStatus, CourseDifficulty, CourseRepository, CourseController, CourseService) |
| course/lesson/ | 9 (Lesson, LessonType, LessonRepository, LessonService, 4 controllers, LessonTreeResponse) |
| hierarchy/ | 5 (HierarchyNode, Repository, Service, 2 controllers) |
| enrollment/ | 9 (Enrollment, EnrollmentStatus, LessonProgress, 2 repos, 2 services, 2 controllers) |
| exam/ | 15 (5 entities, 3 enums, 2 repos, 2 services, 3 controllers) |
| completion/ | 2 (CourseCompletion, Repository) |
| certificate/ | 2-3 (Certificate, CertificateStatus, +Repository when created) |
| engine/completion/ | 4 (CompletionRuleEngine, CertificateEligibilityResolver, EnrollmentStateResolver, EntitlementPolicy) |
| engine/course/ | 4 (CourseRuleValidator, CourseTemplate, CourseStructureRule, CourseValidationResult) |
| engine/exam/ | 4 (ExamBlueprint, QuestionBank, ExamRuleEvaluator, DeterministicQuestionSelector) |
| notification/ | 8 (Notification, 3 enums, Repository, 2 services, DTO) |
| automation/ | 5 (2 entities, enum, 2 repos) |
| homepage/ | 17 (4 entities, 4 enums, 2 repos, 2 services, 2 controllers, 3 DTOs) |
| media/ | 5 (MediaReference, 2 enums, Repository, ValidationService) |
| importer/ | 6 (AbstractImportService, ImportContext, ImportResult, ImportError, 2 enums) |
| test/ | 1 (EducatorApplicationTests) |

---

## Appendix C: Technology Reference

| Layer | Technology | Version |
|---|---|---|
| Backend Language | Java | 17 |
| Backend Framework | Spring Boot | 4.0.2 |
| ORM | Hibernate / JPA | Managed |
| Database | PostgreSQL | 15 |
| Auth | JWT (JJWT) | 0.11.5 |
| Build | Maven | Wrapper |
| Migration | Flyway | Latest (Sprint 9+) |
| Frontend Framework | Next.js | 15 |
| Frontend Language | TypeScript | 5.x |
| Styling | Tailwind CSS | 4.x |
| Component Library | shadcn/ui (Radix) | Latest |
| State (Server) | TanStack React Query | 5.x |
| State (Client) | Zustand | 5.x |
| Forms | React Hook Form + Zod | Latest |
| Icons | Lucide React | Latest |
| Animation (Web) | Framer Motion | 12.x |
| Animation Orchestration (Web) | GSAP | 3.x |
| Smooth Scroll (Web) | Lenis | 1.x |
| Charts | Recharts | Latest |
| Rich Text | TipTap | Latest |
| E2E Testing | Playwright | Latest |
| Unit Testing (FE) | Vitest + Testing Library | Latest |
| Unit Testing (BE) | JUnit 5 + Mockito | Managed |
| CI/CD | GitHub Actions | -- |
| Containerization | Docker | Latest |

---

## Appendix D: Risk Register

| # | Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|---|
| 1 | Mixed PK types (Long vs UUID) cause join issues | Medium | Medium | Use UUID for new entities; document mapping |
| 2 | Entity serialization exposes passwords | High | Critical | @JsonIgnore on User.password; verify no entity leaks |
| 3 | N+1 queries degrade performance | Medium | High | Flat query + in-memory tree; JOIN FETCH |
| 4 | Frontend bundle grows too large | Medium | Medium | Code-split admin; lazy-load heavy components |
| 5 | CORS misconfiguration blocks frontend | Medium | High | Test with production domain on staging |
| 6 | Schema migration failures | Low | Critical | Test migrations on copy of production data |
| 7 | JWT token stolen via XSS | Medium | Critical | CSP header; httpOnly cookie option; short expiry |
| 8 | Exam auto-grade edge cases | Medium | Medium | 100% engine test coverage; manual verification |

---

## Appendix E: Non-Functional Requirements

### Performance

| Requirement | Target |
|---|---|
| API response time (p95) | < 200ms |
| Homepage load | < 1.5 seconds |
| Database connection pool | HikariCP (max 20, min 5) |
| N+1 prevention | Lesson tree in-memory from flat query |
| Pagination | All lists (default 20, max 100) |
| Frontend bundle | < 250KB gzipped initial |
| Image optimization | WebP with fallback, lazy loading |
| Static assets | CDN with cache-busting hashes |

### Testing Targets

| Type | Target |
|---|---|
| Backend unit (services) | 80%+ line coverage |
| Engine tests | 100% coverage |
| Integration tests (API) | Every endpoint |
| Security tests | Auth flows |
| Frontend unit | Components |
| Frontend integration | User flows |
| E2E | 10+ critical paths |

---

## Appendix F: API-to-Screen Mapping

### Auth Module

| API | Screen | UX |
|---|---|---|
| POST /api/auth/register | Register Page | Inline validation, disabled until valid, generic error messages |
| POST /api/auth/login | Login Page | "Remember me", "Forgot password?", generic credential error |

### Course Module

| API | Screen | UX |
|---|---|---|
| GET /api/admin/courses/active | Admin Course List | Sortable, searchable, filterable, paginated |
| POST /api/admin/courses | Course Creation Form | All fields, hierarchy dropdown, "Save as Draft" |
| POST .../publish | Course List (inline) | Badge amber DRAFT -> green PUBLISHED |
| POST .../archive | Course List (inline) | Warning about enrolled students |
| DELETE .../courses/{id} | Course List (inline) | Red button, explicit title in dialog |

### Lesson Module

| API | Screen | UX |
|---|---|---|
| GET /api/public/lesson-tree/... | Course Detail, Learning View | Expandable tree, completion icons |
| POST /api/admin/lessons/... | Lesson Editor | Modal with type selector, auto-order |
| DELETE /api/admin/lessons/{id} | Lesson Editor | Cascade warning with child count |

### Enrollment Module

| API | Screen | UX |
|---|---|---|
| POST .../enrollments/course/{id} | Course Detail | Button changes to "Continue Learning" |
| GET .../enrollments | Student Dashboard | Cards with progress bar, "Resume" button |
| DELETE .../enrollments/{id} | Dashboard or Course View | Warning: "You will lose all progress" |
| POST .../progress/.../complete | Learning View | Optimistic update; confetti on last lesson |

### Exam Module

| API | Screen | UX |
|---|---|---|
| POST .../exams/{id}/start | Exam Start Page | Display pass%, attempts remaining, timer info |
| POST .../attempts/{id}/submit | Exam Taking -> Results | Confirm modal: "Cannot change after submitting" |

### Homepage Module

| API | Screen | UX |
|---|---|---|
| GET /api/public/homepage | Landing Page | Sections by position; blocks by layout type |
| POST .../sections | Homepage CMS | Visual grid for positions; preview button |
| POST .../blocks | Homepage CMS | Block type selector with preview; drag-drop |

---

*End of Master Plan*
*Version 2.0 -- Single Source of Truth*
*Created: 2026-02-10*

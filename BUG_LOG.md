# BUG LOG — Educator Platform

**Project:** Educator Platform (International-grade LMS)
**Version:** 2.0
**Last Updated:** 2026-02-10
**Governance:** Sprint-controlled, append-only
**Tracking:** This document (archive) + GitHub Issues (when available)

---

## Governance Rules

- This file is **append-only** — bugs are never deleted or rewritten
- Bugs found in **closed sprints are NOT fixed retroactively**
- Fixes are always scheduled into a **future sprint**
- Each bug has a clear status, resolution sprint, and no ambiguity
- Historical entries (Sprint 1-6) are **immutable**

---

## Bug Severity Levels

| Level | Description | Response Time |
|-------|-------------|---------------|
| **Critical** | System down, data loss, security breach | Immediate |
| **High** | Major feature broken, significant user impact | Within 24 hours |
| **Medium** | Feature partially working, workaround available | Within 1 week |
| **Low** | Minor issue, cosmetic problem | Next sprint |

---

## Bug Status Types

| Icon | Status | Meaning |
|------|--------|---------|
| 🟢 | Resolved | Fix implemented and tested |
| 🔵 | Closed | Verified in production/staging |
| ⏳ | Planned | Scheduled for a future sprint |
| 🟡 | In Progress | Actively being worked on |
| 🔴 | Open | Identified, not yet assigned |

---

# PART A — HISTORICAL BUGS (Sprint 1-6, IMMUTABLE)

> All bugs below were discovered and resolved within their respective sprints.
> This section is frozen and must not be modified.

---

## Sprint 1 — Foundation & Architecture

| ID | Title | Severity | Category | Root Cause | Resolution | Status |
|----|-------|----------|----------|------------|------------|--------|
| S1-B1 | PostgreSQL Timezone Error | Critical | Database | PostgreSQL rejected deprecated `Asia/Calcutta` timezone | Forced JVM/JDBC/Hibernate/Jackson to UTC | 🟢 Resolved |
| S1-B2 | Hibernate Dialect Detection Failure | High | ORM | Hibernate auto-detection failed for PostgreSQL | Explicitly configured `PostgreSQLDialect` in `application.yml` | 🟢 Resolved |
| S1-B3 | Spring Security Default Login Page | Medium | Security | Form login + HTTP Basic enabled by default | Disabled `formLogin` and `httpBasic` in SecurityFilterChain | 🟢 Resolved |

---

## Sprint 2 — Authentication & Authorization

| ID | Title | Severity | Category | Root Cause | Resolution | Status |
|----|-------|----------|----------|------------|------------|--------|
| S2-B1 | 403 on Admin Hierarchy Endpoints | High | Security | `/api/admin/hierarchy/**` not in SecurityFilterChain permits | Temporarily `permitAll()` (proper fix in Sprint 4) | 🟢 Resolved |

---

## Sprint 3.1 — Course & Lesson Domain

| ID | Title | Severity | Category | Root Cause | Resolution | Status |
|----|-------|----------|----------|------------|------------|--------|
| S3.1-B1 | Hibernate ExceptionInInitializerError | High | ORM | Non-null fields without default values | Added `@Builder.Default` and sensible defaults | 🟢 Resolved |
| S3.1-B2 | Lombok Incompatibility with JDK 17 | Medium | Build | Old Lombok version incompatible with Java 17 | Updated Lombok to 1.18.30 | 🟢 Resolved |
| S3.1-B3 | Illegal Manual Assignment of JPA IDs | Medium | ORM | Manual `setId()` on `IDENTITY` generation entities | Removed all manual ID assignments | 🟢 Resolved |
| S3.1-B4 | Package Path Mismatches | Low | Build | Package declarations didn't match directory structure | Corrected package declarations and reorganized files | 🟢 Resolved |
| S3.1-B5 | Missing Setters in Domain Entities | Medium | ORM | `@Getter` without `@Setter` on mutable entities | Added `@Setter` or used `@Data` | 🟢 Resolved |
| S3.1-B6 | Lesson Service Compilation Failures | Medium | Service | Entity refactoring not reflected in service layer | Updated LessonService to match current entity definitions | 🟢 Resolved |

---

## Sprint 3.2 — Course & Lesson APIs

| ID | Title | Severity | Category | Root Cause | Resolution | Status |
|----|-------|----------|----------|------------|------------|--------|
| S3.2-B1 | Potential Duplication of LessonService Logic | Low | Service | Unclear handover from Sprint 3.1 | Reviewed existing implementation; no changes needed | 🟢 Resolved |

---

## Sprint 4 — Security Stabilization & Public APIs

| ID | Title | Severity | Category | Root Cause | Resolution | Status |
|----|-------|----------|----------|------------|------------|--------|
| S4-B1 | Admin APIs Returned 403 | Critical | Security | JwtAuthenticationFilter missing from SecurityFilterChain | Added filter with `addFilterBefore` | 🟢 Resolved |
| S4-B2 | Lesson Admin APIs Always 403 | High | Security | No UserDetailsService configured for `@PreAuthorize` | Introduced CustomUserDetailsService | 🟢 Resolved |
| S4-B3 | Conflicting Authorization Checks | High | Security | Mixed URL-based + method-based authorization | Removed all `@PreAuthorize`; unified to URL-pattern only | 🟢 Resolved |
| S4-B4 | Lesson Creation 500 Error | High | Database | Missing `@CreationTimestamp`/`@UpdateTimestamp` on Lesson | Added Hibernate timestamp annotations | 🟢 Resolved |

---

## Sprint 5 — Enrollment & Progress

| ID | Title | Severity | Category | Root Cause | Resolution | Status |
|----|-------|----------|----------|------------|------------|--------|
| S5-B1 | 403 on Public Lesson-Tree API | Medium | API | Mismatch between tested URL and controller mapping | Corrected endpoint usage | 🟢 Resolved |
| S5-B2 | Enrollment Blocked for Draft Courses | Low | API | Course status was `DRAFT` | Verified as correct business rule (not a bug) | 🟢 Resolved |
| S5-B3 | JWT Filter Testing Inconsistencies | Low | Security | Misunderstanding of security chain during manual testing | Validated final config; behavior confirmed correct | 🟢 Resolved |

**Database Actions:**
- Created verified snapshot: `db/snapshots/sprint5_baseline_data.sql`
- Snapshot locked as baseline for Sprint 6

---

## Sprint 6 — Exam, Certificate, Media, Automation Domains

| ID | Title | Severity | Category | Root Cause | Resolution | Status |
|----|-------|----------|----------|------------|------------|--------|
| S6-B1 | JPA Enum Mapping Error (MediaSourceType) | High | ORM | `MediaSourceType` defined as class instead of enum | Converted to proper `enum` | 🟢 Resolved |
| S6-B2 | MediaReference Table Not Created | Medium | Database | Blocked by S6-B1 enum mapping error | Fixed after S6-B1; table generated on restart | 🟢 Resolved |
| S6-B3 | pg_dump Socket Connection Failure | Medium | DevOps | Docker pg_dump used Unix socket instead of TCP | Forced TCP with `pg_dump -h localhost` | 🟢 Resolved |
| S6-B4 | Package Name Mismatch in Exam Controller | Low | Build | Typo: `contoller` vs `controller` | Corrected package path | 🟢 Resolved |

**Warnings (non-blocking):**
- Lombok suggestions ("Class may use @Getter/@Setter", "Method is never used") — expected for domain entities and placeholder engines. No action required.

**Sprint 6 Quality Status:** CLEAN — no open bugs at exit.

---

# PART B — GOVERNANCE BUG TRACKER (Sprint 6+ Discoveries)

> Bugs discovered during Sprint 6 review that require fixes in future sprints.
> These follow the formal governance table format.

---

### BUG-001 — Missing CertificateRepository

| Field | Value |
|-------|-------|
| **ID** | BUG-001 |
| **Title** | Missing `CertificateRepository` interface |
| **Discovered In** | Sprint 6 |
| **Severity** | MEDIUM |
| **Type** | Code Omission |
| **Description** | Certificate entity and status enum exist, but the JPA repository interface was missing, preventing persistence operations. |
| **Impact** | Certificate module incomplete at persistence layer |
| **Root Cause** | File omission during Sprint 6 implementation |
| **Resolution Sprint** | Sprint 7 |
| **Fix Status** | ✅ FIXED |
| **Fix Summary** | Added `CertificateRepository extends JpaRepository<Certificate, UUID>` |
| **Verified In** | Sprint 7 — API sanity + compile check |

---

### BUG-002 — Hardcoded JWT Secret

| Field | Value |
|-------|-------|
| **ID** | BUG-002 |
| **Title** | JWT secret hardcoded in configuration |
| **Discovered In** | Sprint 6 |
| **Severity** | CRITICAL (Production) |
| **Type** | Security Misconfiguration |
| **Description** | JWT secret is hardcoded in `application.yml`, unsafe for production usage. |
| **Impact** | Token compromise risk in production |
| **Root Cause** | Development-only configuration |
| **Resolution Sprint** | Sprint 8 |
| **Fix Status** | ⏳ PLANNED |
| **Sprint 8 Task** | B1.1 — Externalize JWT secret to `${JWT_SECRET}` |

---

### BUG-003 — Hardcoded Database Credentials

| Field | Value |
|-------|-------|
| **ID** | BUG-003 |
| **Title** | Database credentials hardcoded in config files |
| **Discovered In** | Sprint 6 |
| **Severity** | CRITICAL (Production) |
| **Type** | Security Misconfiguration |
| **Description** | DB username/password present in `application.yml`. |
| **Impact** | Credential leakage risk |
| **Root Cause** | Local development defaults |
| **Resolution Sprint** | Sprint 8 |
| **Fix Status** | ⏳ PLANNED |
| **Sprint 8 Task** | B1.2 — Externalize DB credentials to `${DB_HOST}`, `${DB_USERNAME}`, `${DB_PASSWORD}` |

---

### BUG-004 — No Global Exception Handling

| Field | Value |
|-------|-------|
| **ID** | BUG-004 |
| **Title** | Missing `@ControllerAdvice` for structured error responses |
| **Discovered In** | Sprint 6 |
| **Severity** | MEDIUM |
| **Type** | Error Handling |
| **Description** | Exceptions return raw stack traces or inconsistent responses. |
| **Impact** | Poor API consumer experience |
| **Resolution Sprint** | Sprint 8 |
| **Fix Status** | ⏳ PLANNED |
| **Sprint 8 Task** | B1.8 — Create GlobalExceptionHandler with structured JSON (400, 404, 409, 403, 500) |

---

### BUG-005 — No Input Validation on Request DTOs

| Field | Value |
|-------|-------|
| **ID** | BUG-005 |
| **Title** | Missing `@Valid` and constraint annotations on DTOs |
| **Discovered In** | Sprint 6 |
| **Severity** | MEDIUM |
| **Type** | Validation |
| **Description** | Request DTOs lack validation annotations. Invalid data may reach service layer. |
| **Impact** | Data integrity risk |
| **Resolution Sprint** | Sprint 8 |
| **Fix Status** | ⏳ PLANNED |
| **Sprint 8 Task** | B1.9 + B1.10 — Add `spring-boot-starter-validation`, `@Valid` on all `@RequestBody`, constraint annotations on DTOs |

---

### BUG-006 — Missing Explicit Stateless Session Policy

| Field | Value |
|-------|-------|
| **ID** | BUG-006 |
| **Title** | `SessionCreationPolicy.STATELESS` not explicitly configured |
| **Discovered In** | Sprint 6 |
| **Severity** | MEDIUM |
| **Type** | Security Configuration |
| **Description** | Stateless policy is implied but not explicitly set in SecurityConfig. |
| **Impact** | Ambiguity in security posture |
| **Resolution Sprint** | Sprint 8 |
| **Fix Status** | ⏳ PLANNED |
| **Sprint 8 Task** | B1.5 — Set `SessionCreationPolicy.STATELESS` explicitly in SecurityConfig |

---

# PART C — SPRINT 7 BUG LOG

**Sprint:** 7 — Local Setup & Foundation
**Status:** CLOSED (Immutable) — 2026-02-10
**Bugs Found:** 0 new
**Bugs Fixed:** 1 (BUG-001)

### Summary

Sprint 7 was a setup and verification sprint. No new bugs were discovered. The only code change was the governance-approved fix of BUG-001 (CertificateRepository).

| Action | Detail |
|--------|--------|
| BUG-001 fixed | `CertificateRepository.java` added — single file, no logic changes |
| Backend verified | `mvn clean compile` + `mvn spring-boot:run` — clean |
| APIs sanity-checked | All 30+ endpoints responded correctly via Postman |
| DB snapshot created | Sprint 6 snapshot preserved for rollback safety |
| Frontend scaffolded | Next.js 15 + TypeScript + Tailwind — no logic, placeholders only |
| No regressions | Zero unauthorized changes to Sprint 1-6 code |

---

# PART D — SPRINT 8 BUG-TO-TASK MAPPING

> This section maps all open bugs to their Sprint 8 resolution tasks.
> Use this as your Sprint 8 checklist.

### Open Bugs Entering Sprint 8

| Bug ID | Title | Severity | Sprint 8 Task(s) | Category |
|--------|-------|----------|-------------------|----------|
| BUG-002 | Hardcoded JWT Secret | CRITICAL | B1.1 | Security |
| BUG-003 | Hardcoded DB Credentials | CRITICAL | B1.2 | Security |
| BUG-004 | No Global Exception Handling | MEDIUM | B1.8 | Error Handling |
| BUG-005 | No Input Validation on DTOs | MEDIUM | B1.9, B1.10 | Validation |
| BUG-006 | Missing Stateless Session Policy | MEDIUM | B1.5 | Security |

### Additional Sprint 8 Security Tasks (Not from Bug Log)

These are security hardening items from the MASTER_PLAN, not tracked as bugs:

| Task | Description | Priority |
|------|-------------|----------|
| B1.3 | Populate `application-prod.yml` (ddl-auto: validate, show-sql: false, env vars) | CRITICAL |
| B1.4 | Add CORS configuration (allow frontend origin) | HIGH |
| B1.6 | Add explicit URL patterns for `/api/instructor/**`, `/api/student/**` | HIGH |
| B1.7 | Fix StudentExamController — replace `@RequestParam userId` with `@AuthenticationPrincipal` | HIGH |
| B1.11 | Add security headers (HSTS, X-Frame-Options, X-Content-Type-Options) | MEDIUM |
| B1.12 | Improve JWT filter error response (JSON on 401) | MEDIUM |
| B1.13 | Add password strength validation (min 8 chars, 1 uppercase, 1 digit) | MEDIUM |
| B1.14 | Add rate limiting on `/api/auth/**` (10 req/min per IP) | MEDIUM |

### Sprint 8 Execution Priority

1. **CRITICAL (Do First)**
   - B1.1 — Externalize JWT secret
   - B1.2 — Externalize DB credentials
   - B1.3 — Populate application-prod.yml

2. **HIGH (Do Second)**
   - B1.4 — CORS configuration
   - B1.5 — Explicit STATELESS session policy
   - B1.6 — URL pattern authorization
   - B1.7 — Fix StudentExamController security bug

3. **MEDIUM (Do Third)**
   - B1.8 — GlobalExceptionHandler
   - B1.9 + B1.10 — Input validation
   - B1.11 — Security headers
   - B1.12 — JWT filter error response
   - B1.13 — Password strength validation
   - B1.14 — Rate limiting

---

# PART E — BUG STATISTICS

## Overall Summary (Sprint 1-7)

| Sprint | Bugs Found | Critical | High | Medium | Low | Resolved |
|--------|-----------|----------|------|--------|-----|----------|
| Sprint 1 | 3 | 1 | 1 | 1 | 0 | 3 |
| Sprint 2 | 1 | 0 | 1 | 0 | 0 | 1 |
| Sprint 3.1 | 6 | 0 | 2 | 3 | 1 | 6 |
| Sprint 3.2 | 1 | 0 | 0 | 0 | 1 | 1 |
| Sprint 4 | 4 | 1 | 3 | 0 | 0 | 4 |
| Sprint 5 | 3 | 0 | 0 | 1 | 2 | 3 |
| Sprint 6 | 4 | 0 | 1 | 2 | 1 | 4 |
| Sprint 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| **Total** | **22** | **2** | **8** | **7** | **5** | **22** |

## Governance Bugs (Discovered Sprint 6, Tracked Forward)

| Status | Count | Details |
|--------|-------|---------|
| ✅ Fixed | 1 | BUG-001 (Sprint 7) |
| ⏳ Planned for Sprint 8 | 5 | BUG-002 through BUG-006 |
| 🔴 Open/Unscheduled | 0 | — |

## Bug Categories (All Sprints)

| Category | Count | Percentage |
|----------|-------|------------|
| Security / Auth | 7 | 31.8% |
| ORM / Hibernate | 6 | 27.3% |
| Build / Compilation | 3 | 13.6% |
| Database | 3 | 13.6% |
| Service Layer | 2 | 9.1% |
| DevOps | 1 | 4.5% |

## Key Insights

1. **Security** was the dominant bug category (31.8%), particularly JWT and authorization config
2. **100% in-sprint resolution rate** — all runtime bugs resolved within their discovery sprint
3. **Zero data loss** across all 22 bugs
4. **Sprint 7 was clean** — zero new bugs (verification-only sprint)
5. **5 governance bugs** remain open heading into Sprint 8 (all security/validation)
6. Sprint 8 is designed specifically to close all remaining governance bugs

---

## Governance Declaration

- Sprint 1-7 are **CLOSED (Immutable)**
- All Sprint 1-7 bugs are **resolved**
- 5 governance bugs (BUG-002 through BUG-006) are **scheduled for Sprint 8**
- No silent fixes allowed — all changes must be logged
- Next update: **Sprint 8 exit**

---

**END OF BUG LOG — Pre-Sprint 8**

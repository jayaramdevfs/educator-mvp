# SPRINT 7 – API VERIFICATION BUG LOG
Project: Educator MVP Backend
Phase: Sprint 7 API Verification & Stabilization
Status: STABLE (Post-Fix)

---

## 1. OVERVIEW

This document records all technical issues encountered during API verification,
their root causes, applied fixes, and current stability status.

This log reflects the state after full API testing using the provided
Postman Collection.

---

## 2. BUG LOG SUMMARY TABLE

| # | Issue | Root Cause | Fix Applied | Status |
|---|-------|------------|-------------|--------|
| 1 | 403 on Admin Exam Creation | JWT authority confusion | Verified ROLE_ADMIN in token | Closed |
| 2 | UUID ↔ Long mismatch (Exam.courseId) | Entity type inconsistency | Standardized to Long | Closed |
| 3 | PostgreSQL casting error (uuid → bigint) | Schema not aligned with entity | Dropped & recreated exam tables | Closed |
| 4 | Duplicate CourseCompletion class | Wrong package location | Removed duplicate file | Closed |
| 5 | GenericGenerator deprecated | Hibernate 6.5 change | Replaced with @UuidGenerator | Closed |
| 6 | 403 on /questions endpoint | Endpoint not implemented | Confirmed not in scope (Sprint 11) | Deferred |
| 7 | Publish exam allowed without questions | Missing business validation | Deferred to Sprint 11 | Deferred |

---

## 3. DETAILED BUG ANALYSIS

---

### BUG 1 – 403 on Admin Exam Creation

**Symptom**
POST /api/admin/exams → 403

**Investigation**
- Verified JWT payload
- Confirmed ROLE_ADMIN present
- Confirmed SecurityConfig correct

**Root Cause**
GlobalExceptionHandler mapped IllegalArgumentException/IllegalStateException to 403.
Real failure was deeper (schema mismatch).

**Resolution**
After schema fix (Bug #3), issue resolved.

Status: CLOSED

---

### BUG 2 – UUID vs Long mismatch (Exam.courseId)

**Original State**
Course.id → Long
Exam.courseId → UUID

**Problem**
Type mismatch caused cascading errors in:
- ExamService
- ExamAttemptService
- CourseCompletion

**Fix**
Standardized:

- Course.id → Long
- Exam.courseId → Long
- CourseCompletion.courseId → Long

This removed structural inconsistency.

Status: CLOSED

Risk: None (Monolith architecture safe)

---

### BUG 3 – PostgreSQL casting failure

**Error**
column "course_id" cannot be cast automatically to type bigint

**Cause**
Hibernate attempted schema migration from UUID → BIGINT.

**Fix**
Dropped exam-related tables:

- exams
- exam_attempts
- exam_attempt_answers
- exam_questions
- exam_options
- course_completions

Restarted application.
Hibernate recreated schema correctly.

Status: CLOSED

---

### BUG 4 – Duplicate CourseCompletion class

**Cause**
File placed in wrong package:
com.educator.completion.repository instead of entity.

**Fix**
Removed duplicate.
Corrected file path.

Status: CLOSED

---

### BUG 5 – GenericGenerator Deprecated

**Cause**
Hibernate 6.5 deprecates @GenericGenerator.

**Fix**
Replaced with:

    @GeneratedValue
    @UuidGenerator

Status: CLOSED

---

### BUG 6 – Missing Question CRUD API

**Observation**
No endpoint for:
POST /api/admin/exams/{examId}/questions

**Cause**
Question CRUD scheduled for Sprint 11 (per Master Plan).

**Decision**
Do NOT implement now.
Deferred to Sprint 11.

Status: DEFERRED (Planned Feature)

---

### BUG 7 – Publish Exam Without Questions

**Observation**
Exam can be published with 0 questions.

**Cause**
No validation in publishExam().

**Decision**
Add validation in Sprint 11 when Question CRUD is implemented.

Status: DEFERRED (Business Rule Enhancement)

---

## 4. FILES MODIFIED DURING STABILIZATION

- Exam.java
- ExamRepository.java
- ExamAttemptService.java
- CourseCompletion.java
- CourseCompletionRepository.java
- SecurityConfig (validated, no change required)

---

## 5. CURRENT STABILITY ASSESSMENT

Backend is now:

- Structurally consistent
- Schema aligned
- ID mapping stable
- JWT authorization verified
- Exam lifecycle functional

No blocking defects remain.

---

## 6. RISK STATUS

| Area | Risk |
|------|------|
| Course ID Long | Low |
| Exam UUID | Stable |
| Schema | Clean |
| Question Module | Pending Implementation |
| Security | Verified |

---

END OF BUG LOG

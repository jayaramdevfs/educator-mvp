# SPRINT 7 – FULL COLLECTION-BASED API VERIFICATION REPORT
Project: Educator MVP
Collection: Educator MVP – Full API Collection (33 endpoints)
Verification Mode: Manual + Postman Execution
Status: Stable Core / Partial Coverage

---

# 1. COLLECTION STRUCTURE (13 FOLDERS)

1. Authentication
2. Public Endpoints
3. Admin – Hierarchy
4. Admin – Course Management
5. Admin – Lesson Management
6. Admin – Exam Management
7. Admin – Homepage CMS
8. Instructor Endpoints
9. Learner – Enrollment
10. Learner – Progress
11. Student – Exams
12. E2E Workflow Tests
13. Negative Tests

---

# 2. FOLDER-BY-FOLDER VERIFICATION STATUS

---

## 1️⃣ Authentication

Endpoints:
- Register User
- Register Admin
- Login (Student)
- Login (Admin)

Status: ✅ VERIFIED

Notes:
- JWT token generation confirmed
- ROLE_ADMIN and STUDENT roles verified in payload
- Environment token auto-save working
- SecurityConfig aligned

---

## 2️⃣ Public Endpoints

Status: ⚠ PARTIALLY VERIFIED

Verified:
- Public course listing
- Public homepage endpoints (basic)

Not fully tested:
- All combinations of filters

---

## 3️⃣ Admin – Hierarchy

Endpoints:
- Create Category
- Update Category
- Get Tree
- Delete / Restore

Status: ⚠ PARTIALLY VERIFIED

Verified:
- Create hierarchy node
- Basic structure retrieval

Not Verified:
- Reparenting edge cases
- Depth validation
- Cycle detection tests

---

## 4️⃣ Admin – Course Management

Endpoints:
- Create Course
- Publish Course
- Archive Course
- List Courses

Status: ✅ VERIFIED

Verified:
- Course creation
- course_id environment variable capture
- Publish lifecycle
- BIGINT ID consistency stable

Schema Stable: YES

---

## 5️⃣ Admin – Lesson Management

Endpoints:
- Create Lesson
- Publish Lesson
- Reorder
- Tree Retrieval

Status: ⚠ PARTIALLY VERIFIED

Verified:
- Basic lesson creation

Not Verified:
- Reorder edge cases
- Deep hierarchy operations
- Soft delete restore

---

## 6️⃣ Admin – Exam Management

Endpoints:
- Create Exam
- Publish Exam
- Archive Exam

Status: ✅ VERIFIED

Verified:
- UUID generation fixed
- courseId BIGINT alignment
- DRAFT → PUBLISHED → ARCHIVED lifecycle

Not Implemented:
- Question CRUD (Sprint 11 scope)
- Publish validation (min questions)

Structural Status: STABLE

---

## 7️⃣ Admin – Homepage CMS

Endpoints:
- Create Section
- Create Block

Status: ⚠ PARTIALLY VERIFIED

Verified:
- Section creation
- Block creation

Not Verified:
- Update / delete operations
- Reorder
- Layout CRUD

---

## 8️⃣ Instructor Endpoints

Endpoints:
- GET /api/instructor/exams/{examId}

Status: ⚠ PARTIALLY VERIFIED

Verified:
- Basic retrieval working

Not Verified:
- Analytics endpoint (Sprint 11 scope)

---

## 9️⃣ Learner – Enrollment

Endpoints:
- Enroll in course

Status: ✅ VERIFIED

Verified:
- Enrollment success
- Ownership guard working
- Unique constraint enforced

---

## 🔟 Learner – Progress

Endpoints:
- Start progress
- Complete lesson

Status: ✅ VERIFIED

Verified:
- Lesson completion
- Idempotent markComplete
- Enrollment linkage working

---

## 1️⃣1️⃣ Student – Exams

Endpoints:
- Start Attempt
- Submit Attempt

Status: ⚠ PARTIALLY VERIFIED

Verified:
- Attempt creation
- Auto-grading
- CourseCompletion created on pass

Not Verified:
- Shuffle logic (Sprint 9 task)
- Timer enforcement (Sprint 9 task)
- Review endpoint (Sprint 9 task)

---

## 1️⃣2️⃣ E2E Workflow Tests

Status: ❌ NOT EXECUTED FULLY

Steps Not Fully Verified:
- Full student journey
- Complete chain automation
- Multi-step chaining

Reason:
Manual API-level verification prioritized.

Pending:
Full E2E execution required.

---

## 1️⃣3️⃣ Negative Tests

Status: ❌ NOT FULLY VERIFIED

Test Cases Not Executed:
- Invalid login
- Duplicate email
- Unauthorized admin access
- Role mismatch tests

Pending:
Full negative validation sweep required.

---

# 3. OVERALL COLLECTION COVERAGE SUMMARY

| Folder | Status |
|--------|--------|
| Authentication | ✅ Complete |
| Public | ⚠ Partial |
| Admin Hierarchy | ⚠ Partial |
| Admin Course | ✅ Complete |
| Admin Lesson | ⚠ Partial |
| Admin Exam | ✅ Complete |
| Admin Homepage | ⚠ Partial |
| Instructor | ⚠ Partial |
| Learner Enrollment | ✅ Complete |
| Learner Progress | ✅ Complete |
| Student Exams | ⚠ Partial |
| E2E | ❌ Not Run |
| Negative | ❌ Not Run |

---

# 4. CURRENT SYSTEM STATE

Backend:
- Stable
- Schema aligned
- No casting issues
- UUID generation clean
- Course ID BIGINT consistent

Exam Module:
- Lifecycle working
- Question CRUD deferred (Sprint 11)
- Publish validation pending

Security:
- Verified
- Role enforcement working

---

# 5. PENDING WORK BEFORE SPRINT 8

1. Execute all E2E folder tests
2. Execute all Negative Tests
3. Verify:
   - Unauthorized access scenarios
   - Duplicate constraints
   - Validation errors
4. Confirm 400/401/403 mappings from GlobalExceptionHandler

---

# 6. COURSE ID STRATEGY (IMPORTANT)

Current:

Course.id = Long (BIGINT)
Exam.courseId = Long
CourseCompletion.courseId = Long

This deviates from early TDD UUID idea.

Impact:
- No structural conflict
- Stable in monolith architecture
- No migration required
- Safe for future sprints

Decision:
Freeze ID strategy.
Do NOT refactor again.

---

# 7. FINAL STATUS

Core API Routing: STABLE  
Schema: CLEAN  
Authentication: VERIFIED  
Exam Lifecycle: VERIFIED  
Question CRUD: DEFERRED  
E2E & Negative: PENDING EXECUTION  

Sprint 7 API verification is PARTIALLY COMPLETE.

Full completion requires executing:
- Folder 12 (E2E)
- Folder 13 (Negative Tests)

---

END OF REPORT

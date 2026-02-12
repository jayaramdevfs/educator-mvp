# SPRINT 7 – API EXECUTION STATUS
Reference: Educator MVP FULL API (Handwritten Sheet)
Source: Postman Collection Execution Notes

========================================================
1. AUTHENTICATION
========================================================

1. Register User → 200 ✔
2. Register Admin → 200 ✔
3. Login – Student → 200 ✔
4. Login – Admin → 200 ✔

Status: COMPLETE

--------------------------------------------------------

========================================================
2. PUBLIC ENDPOINTS
========================================================

1. Home → 200 ✔
2. LessonCourse → 200 ✔
3. Tree for Course → 200 ✔
4. Hierarchy Root → 403 ❌
5. Hierarchy Child → 403 ❌

Issue:
Hierarchy endpoints returning 403 under Public.

Action Required:
Check:
- SecurityConfig route rules
- Public controller mapping

Status: PARTIAL

--------------------------------------------------------

========================================================
3. ADMIN – HIERARCHY MANAGEMENT
========================================================

1. Create HN (Root) → 403 ❌
2. Create HN (Child) → 403 ❌
3. Update HN → 200 ✔
4. Move HN → 403 ❌
5. Delete HN → 403 ❌
6. Restore HN → 403 ❌

Observation:
Update works but Create/Delete/Move fail.

Likely Cause:
Authorization issue or wrong token active.

Status: FAILED / NEEDS RE-VERIFICATION

--------------------------------------------------------

========================================================
4. ADMIN – COURSE MANAGEMENT
========================================================

1. Create → 200 ✔
2. Publish → 200 ✔
3. Archive → 200 ✔
4. Get Active → 200 ✔
5. Delete (Soft) → Not verified ⚠

Status: STABLE

--------------------------------------------------------

========================================================
5. ADMIN – LESSON MANAGEMENT
========================================================

1. Text → 200 ✔
2. Video → 200 ✔
3. Document → 200 ✔
4. Get Lesson → 200 ✔
5. Delete (Soft) → Not verified ⚠

Status: STABLE (Soft delete pending check)

--------------------------------------------------------

========================================================
6. ADMIN – EXAM
========================================================

1. Create → 200 ✔
2. Publish → 200 ✔
3. Archive → 200 ✔

Note:
Question CRUD not implemented (by design)

Status: STABLE

--------------------------------------------------------

========================================================
7. ADMIN – HOMEPAGE CMS
========================================================

1. Create HS → 200 ✔
2. Create SB → 403 ❌

Likely Cause:
Role or request body issue.

Status: PARTIAL

--------------------------------------------------------

========================================================
8. INSTRUCTOR ENDPOINTS
========================================================

1. Get Exam Details → Not clearly verified ⚠

Note:
Instructor login not clearly tested.

Status: NEEDS TEST

--------------------------------------------------------

========================================================
9. LEARNER – ENROLL
========================================================

1. Enroll → 403 ❌
2. My Enrolls → 200 ✔
3. Drop Enroll → 200 ✔

Observation:
Enroll initially failing earlier due to role mismatch.
Needs clean re-test.

Status: PARTIAL

--------------------------------------------------------

========================================================
10. LEARNER – PROGRESS
========================================================

1. Start Enrollment Progress → 200 ✔
2. Complete Lesson → 200 ✔

Status: STABLE

--------------------------------------------------------

========================================================
11. STUDENT – EXAM
========================================================

1. Start → Not clearly recorded ⚠
2. Submit → Not clearly recorded ⚠

Note:
Needs fresh test with:
- Valid exam_id
- Valid enrollment

Status: NEEDS FULL TEST

--------------------------------------------------------

========================================================
12. E2E WORKFLOW TESTS
========================================================

12.1 Full Student Flow

1. Register → 200 ✔
2. Login → 200 ✔
3. Browse Hierarchy → 403 ❌
4. Enroll → 200 ✔
5. Start → 403 ❌
6. Complete → 403 ❌

Status: FAILED FLOW

--------------------------------------------------------

12.2 Admin Workflow

1. Login → 200 ✔
2. Create Category → 200 ✔
3. Create Course → 200 ✔
4. Add Lesson → 201 ✔
5. Publish Course → 200 ✔

Status: STABLE

--------------------------------------------------------

========================================================
13. NEGATIVE TESTS
========================================================

1. Login invalid creds → 403 ✔
2. Register duplicate email → 403 ✔
3. Access admin endpoint without auth → 403 ✔
4. Access admin endpoint with wrong role → 403 ✔
5. Get non-existent lesson → 403 ✔
6. Create course without required fields → 403 ✔

Status: PASSED

--------------------------------------------------------

========================================================
OVERALL STATUS SUMMARY
========================================================

Stable Modules:
✔ Authentication
✔ Course Management
✔ Lesson Management
✔ Exam Lifecycle
✔ Learner Progress
✔ Negative Tests

Partially Working:
⚠ Public Hierarchy
⚠ Admin Hierarchy
⚠ Homepage CMS
⚠ Enrollment (needs clean run)
⚠ Student Exam

Failed Flow:
❌ Full Student E2E

--------------------------------------------------------

NEXT PRIORITY ORDER FOR FIXING:

1. Fix Public Hierarchy (403)
2. Fix Admin Hierarchy (403)
3. Fix Homepage Create SB (403)
4. Re-test Enrollment
5. Re-run Full Student E2E
6. Verify Student Exam flow

--------------------------------------------------------

END OF DOCUMENT

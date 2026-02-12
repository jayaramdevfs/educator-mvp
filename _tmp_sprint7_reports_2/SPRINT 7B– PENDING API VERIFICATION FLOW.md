# SPRINT 7 – EXACT COLLECTION CLICK FLOW GUIDE
Collection: Educator MVP – Full API Collection
Mode: Manual Step-by-Step Execution
Purpose: Backend Full Verification

------------------------------------------------------------
IMPORTANT:
Follow this order strictly.
Do NOT jump folders.
Do NOT skip steps.
------------------------------------------------------------

============================================================
STEP 0 – RESET ENVIRONMENT
============================================================

1. Open Postman
2. Select environment
3. Clear variables:
   - jwt_token
   - admin_token
   - student_token
   - course_id
   - exam_id
   - attempt_id

------------------------------------------------------------

============================================================
FOLDER 1: Authentication
============================================================

▶ 1. Register User
   Click → Register User
   Body: default
   Expect:
     - 200 OK
     - User created

▶ 2. Register Admin
   Click → Register Admin
   Expect:
     - 200 OK

▶ 3. Login (Admin)
   Click → Login (Admin)
   Expect:
     - 200 OK
     - jwt_token updated
     - admin_token updated

▶ 4. Login (Student)
   Click → Login (Student)
   Expect:
     - 200 OK
     - jwt_token updated
     - student_token updated

⚠ After this:
Re-login as ADMIN before going to Admin folders.

------------------------------------------------------------

============================================================
FOLDER 2: Public Endpoints
============================================================

▶ GET Active Courses
   Expect:
     - 200 OK
     - List (empty or populated)

▶ GET Homepage
   Expect:
     - 200 OK
     - Sections array

If fails → Note: Public endpoints issue

------------------------------------------------------------

============================================================
FOLDER 3: Admin – Hierarchy
============================================================

⚠ Ensure ADMIN token active

▶ Create Category
   Expect 200
   Save returned ID manually if needed

▶ Create Child Category
   Expect 200

▶ Get Tree
   Expect 200
   Confirm nested structure

▶ Delete Category
   Expect 200

▶ Restore Category
   Expect 200

------------------------------------------------------------

============================================================
FOLDER 4: Admin – Course Management
============================================================

▶ Create Course
   Expect:
     - 200 OK
     - course_id saved in environment

▶ Get Course By ID
   Expect 200

▶ Publish Course
   Expect:
     - 200
     - status = PUBLISHED

▶ Archive Course
   Expect:
     - 200
     - status = ARCHIVED

------------------------------------------------------------

============================================================
FOLDER 5: Admin – Lesson Management
============================================================

▶ Create Lesson
   Expect 200

▶ Create Sub-Lesson
   Expect 200

▶ Reorder Lessons
   Expect 200

▶ Delete Lesson
   Expect 200

▶ Restore Lesson
   Expect 200

------------------------------------------------------------

============================================================
FOLDER 6: Admin – Exam Management
============================================================

▶ Create Exam
   Expect:
     - 200
     - exam_id saved

▶ Publish Exam
   Expect:
     - 200
     - status = PUBLISHED

▶ Archive Exam
   Expect:
     - 200
     - status = ARCHIVED

⚠ Note:
Question CRUD not implemented (expected)

------------------------------------------------------------

============================================================
FOLDER 7: Admin – Homepage CMS
============================================================

▶ Create Section
   Expect 200

▶ Create Block
   Expect 200

▶ Disable Section (if exists)
   Expect 200

Then go to:
Public → GET Homepage
Verify section visibility

------------------------------------------------------------

============================================================
FOLDER 8: Instructor Endpoints
============================================================

▶ GET Instructor Exam Details
   Expect:
     - 200
     - Exam details

If 403 → Role issue

------------------------------------------------------------

============================================================
FOLDER 9: Learner – Enrollment
============================================================

⚠ Login as STUDENT first

▶ Enroll in Course
   Expect:
     - 200
     - enrollment_id returned

------------------------------------------------------------

============================================================
FOLDER 10: Learner – Progress
============================================================

▶ Start Progress
   Expect 200

▶ Complete Lesson
   Expect 200

------------------------------------------------------------

============================================================
FOLDER 11: Student – Exams
============================================================

▶ Start Attempt
   Expect:
     - 200
     - attempt_id saved

▶ Submit Attempt
   Expect:
     - 200
     - scorePercentage
     - passed true/false

▶ Try second attempt (if maxAttempts > 1)
   Expect allowed

▶ Exceed maxAttempts
   Expect error

------------------------------------------------------------

============================================================
FOLDER 12: E2E Workflow Tests
============================================================

Run each request sequentially.
Expect:
All steps return 200.

If any step fails:
Write:
  Folder Name
  Request Name
  Status Code

------------------------------------------------------------

============================================================
FOLDER 13: Negative Tests
============================================================

▶ Invalid Login
   Expect 401

▶ Duplicate Registration
   Expect 409

▶ Access Admin without token
   Expect 401

▶ Access Admin with student token
   Expect 403

If any returns 200 → Security issue.

------------------------------------------------------------

============================================================
FINAL CHECKLIST
============================================================

Mark ✔ or ✖ for each folder:

[ ] Authentication
[ ] Public
[ ] Admin Hierarchy
[ ] Admin Course
[ ] Admin Lesson
[ ] Admin Exam
[ ] CMS
[ ] Instructor
[ ] Enrollment
[ ] Progress
[ ] Student Exam
[ ] E2E
[ ] Negative Tests

------------------------------------------------------------

END OF DOCUMENT

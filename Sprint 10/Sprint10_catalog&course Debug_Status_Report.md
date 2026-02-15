# Sprint 10 -- System Debug & Stability Report

## Project Context

System: Spring Boot (Backend) + PostgreSQL + Next.js (Frontend)\
Sprint Level: Up to Sprint 10\
Environment: Docker (PostgreSQL running), Localhost testing

------------------------------------------------------------------------

# 1. Current System Status

## 1.1 Database State

### Courses

-   12 courses confirmed as `PUBLISHED`
-   Courses exist and are valid
-   Course IDs are BIGINT (e.g., 1, 3, 4, etc.)

### Lessons

-   Lessons exist for published courses
-   Some lessons marked `is_deleted = true`
-   Active lessons correctly retrieved via lesson-tree API

### Exams

-   Exams exist for multiple courses
-   Exam questions were initially missing
-   Questions manually inserted successfully
-   Each question has 4 options
-   Question-option mapping confirmed

### Enrollments

-   Enrollment table contains valid records
-   Student enrollments persist correctly in DB

------------------------------------------------------------------------

# 2. Catalog Visibility Issue (Resolved)

## Problem

-   Students were seeing DRAFT courses instead of PUBLISHED
-   Public homepage showed inconsistent results

## Root Cause

Catalog endpoint was not enforcing role-based filtering correctly.

## Fix Applied

Backend updated: - Role-aware filtering added in
`CoursePublicController.searchCourses()` - Non-privileged users forced
to `PUBLISHED` - Admin & Instructor retain extended visibility

## Result

-   Draft courses no longer visible to students
-   Catalog now shows only PUBLISHED courses

Status: ✅ FIXED

------------------------------------------------------------------------

# 3. Homepage Dynamic Sections Issue

## Problem

Homepage displayed only TEXT blocks. No course cards rendered. Student
could not interact with homepage courses.

## Root Cause

`section_blocks` table had only `TEXT` block types. No `COURSE` blocks
configured. Additionally: `block_configs.target_id` used UUID
`courses.id` uses BIGINT Data type mismatch created linking issues.

## Fix Applied

-   Inserted COURSE block into section
-   Added block_config entry
-   Identified ID type mismatch problem

## Current Status

Homepage now contains COURSE block Course linking partially configured
Further refinement required for stable mapping

Status: ⚠ PARTIALLY FIXED

------------------------------------------------------------------------

# 4. Enrollment Flow

## Current Behavior

-   Student can enroll in published courses
-   Enrollment stored in DB
-   EnrollmentService validates status == PUBLISHED

## Remaining Concern

Homepage must correctly link to valid course IDs to ensure smooth
student experience.

Status: ✅ FUNCTIONAL (Catalog-based flow)

------------------------------------------------------------------------

# 5. Exam Flow

## Current State

-   Exams exist
-   Questions inserted manually
-   Options inserted correctly
-   Question count verified via SQL

## To Verify Next

-   Student exam attempt creation
-   exam_attempts table updates
-   exam_attempt_answers persistence
-   Result calculation logic

Status: ⚠ READY FOR FINAL VERIFICATION

------------------------------------------------------------------------

# 6. Architectural Observations

1.  CMS (Homepage) system is separate from Catalog.
2.  Publishing a course does NOT auto-add it to homepage.
3.  Block system requires correct block_config linkage.
4.  ID type inconsistency (UUID vs BIGINT) is a structural weakness.
5.  Role-based filtering now correctly implemented.

------------------------------------------------------------------------

# 7. What Is Fully Stable

-   Database integrity
-   Course publishing
-   Role-based filtering
-   Catalog visibility
-   Enrollment persistence
-   Exam question structure

------------------------------------------------------------------------

# 8. What Still Needs Hardening

-   Homepage COURSE block mapping
-   Course ID vs UUID alignment
-   Complete exam attempt testing
-   End-to-end student journey validation

------------------------------------------------------------------------

# 9. Recommended Next Controlled Plan

1.  Standardize course ID mapping (UUID vs BIGINT)
2.  Cleanly configure homepage COURSE blocks
3.  Perform full student flow test:
    -   Login
    -   Catalog
    -   Enroll
    -   Lessons
    -   Exam
    -   Submit
    -   Verify result persistence
4.  Mark Sprint 10 as STABLE only after full E2E validation

------------------------------------------------------------------------

# Final Status

Sprint 10 Core Systems: 85% Stable\
Remaining Work: CMS linkage stabilization + full exam validation

Development can safely continue with structured stabilization steps.

# Changelog - Educator Platform

All notable changes to the Educator Platform will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.2.0] - 2026-02-08 - Sprint 5 Completed

### Sprint Focus
Enrollment & Progress Tracking - Enabling learner participation

### Added
- **Enrollment System**
  - Learner course enrollment API
  - Enrollment lifecycle (ACTIVE status)
  - Duplicate enrollment prevention
  - Published-course-only enrollment restriction
  
- **Progress Tracking**
  - Lesson start tracking
  - Lesson completion tracking
  - Idempotent progress updates
  - Per-learner, per-lesson progress persistence

- **Learner APIs**
  - Learner course overview endpoint
  - Learner lesson list access
  - Learner lesson content access
  - Public lesson tree access (unauthenticated)

- **Database Tables**
  - `enrollments` table
  - `lesson_progress` table

- **Documentation**
  - SPRINT_5_HANDOVER.md
  - SPRINT_ROAD_MAP.md
  - SPRINT_6_SUPPORTING_DOCUMENTATION.md
  - Database snapshot: `sprint5_baseline_data.sql`

### Security
- URL-pattern authorization verified end-to-end
- JWT stateless authentication validated
- Admin/Learner/Public separation enforced

### Testing
- Manual Postman testing completed
- Positive and negative path verification
- State-dependent behavior validated via DB inspection

### Changed
- Enrollment restricted to PUBLISHED courses only
- Progress tracking implemented as additive (non-mutating)

### Technical Details
- Security model: URL-based, not method-based
- Progress updates: Idempotent by design
- Soft delete: Continues to be enforced

### Known Limitations
- Course completion logic deferred to Sprint 6
- Exam integration deferred to Sprint 6
- Certificate generation deferred to Sprint 7

---

## [1.1.2] - 2026-02-07 - Sprint 4 Completed

### Sprint Focus
Infinite Lesson Hierarchy + Admin Course APIs

### Added
- **Infinite Lesson Hierarchy**
  - Parent-child lesson relationships (unlimited nesting)
  - Materialized path pattern implementation (`/course/1/lesson/8/lesson/12`)
  - Depth-level calculation for hierarchical queries
  - Recursive subtree deletion with soft delete

- **Admin Course Management APIs**
  - List courses with pagination
  - Get single course by ID
  - Update course details
  - Delete course (soft delete)
  - Publish course (DRAFT → PUBLISHED)
  - Archive course (PUBLISHED → ARCHIVED)

- **Hierarchy-Aware Lesson APIs**
  - Create child lesson
  - Get children of a lesson
  - Update lesson details
  - Delete lesson subtree

- **Database Schema Updates**
  - `parent_lesson_id` column added to lessons
  - `path` column for materialized path storage
  - `depth_level` column for hierarchy queries

### Security
- URL-based admin authorization enforced (`/api/admin/**`)
- Global JWT filter with public endpoint exceptions
- SecurityFilterChain stabilized

### Bug Fixes
- Fixed JWT filter application inconsistencies
- Fixed path calculation for nested lessons
- Fixed soft delete cascading for subtrees
- Fixed timestamp handling in PostgreSQL

### Changed
- Enforced course status transitions (DRAFT → PUBLISHED → ARCHIVED)
- Added pagination support to course listing

### Removed
- Method-level security annotations (replaced with URL-based)

---

## [1.1.1] - 2026-02-06 - Sprint 3.2 Completed

### Sprint Focus
Lesson APIs (Admin & Public)

### Added
- Admin lesson creation API
- Admin lesson management APIs
- Public lesson browsing APIs
- Lesson type support (TEXT, VIDEO, DOCUMENT)

---

## [1.1.0] - 2026-02-05 - Sprint 3.1 Completed

### Sprint Focus
Course & Lesson Domain

### Added
- Course entity and repository
- Lesson entity and repository
- Course lifecycle states (DRAFT, PUBLISHED, ARCHIVED)
- Difficulty levels (BEGINNER, INTERMEDIATE, ADVANCED)
- Multi-language support foundation

---

## [1.0.1] - 2026-02-04 - Sprint 2 Completed

### Sprint Focus
Infinite Hierarchy System

### Added
- Infinite category/subcategory nesting
- Slug-based SEO-friendly routing
- Soft delete with full audit trail
- `hierarchy_nodes` table

---

## [1.0.0] - 2026-02-03 - Sprint 1 Completed

### Sprint Focus
Authentication & Security Foundation

### Added
- Spring Boot 4.0.2 project initialization
- PostgreSQL 15 database integration
- JWT-based stateless authentication
- BCrypt password hashing
- Role-based access control (ADMIN, INSTRUCTOR, STUDENT)
- User registration and login APIs
- `users`, `roles`, `user_roles` tables

### Security
- JWT token generation and validation
- Role-based route protection
- Stateless session management
- CSRF disabled for REST APIs

### Bug Fixes
- PostgreSQL timezone configuration
- JWT secret key configuration
- BCrypt encoder bean initialization

---

## Sprint Progression Summary

| Version | Sprint | Status | Key Milestone | Date |
|---------|--------|--------|---------------|------|
| 1.2.0 | Sprint 5 | ✅ Completed | Enrollment & Progress | 2026-02-08 |
| 1.1.2 | Sprint 4 | ✅ Completed | Infinite Hierarchy | 2026-02-07 |
| 1.1.1 | Sprint 3.2 | ✅ Completed | Lesson APIs | 2026-02-06 |
| 1.1.0 | Sprint 3.1 | ✅ Completed | Course & Lesson Domain | 2026-02-05 |
| 1.0.1 | Sprint 2 | ✅ Completed | Hierarchy System | 2026-02-04 |
| 1.0.0 | Sprint 1 | ✅ Completed | Authentication | 2026-02-03 |

---

## Upcoming Releases

### [1.3.0] - Sprint 6 (Planned)
Foundation, Configuration & UI Skeletons
- Exams & completion logic
- Homepage sections
- Subscription definitions
- Notification persistence
- Certificate data model
- Web + Mobile UI foundations

### [2.0.0] - Sprint 7 (Planned)
Go-Live Release
- Subscription purchase & enforcement
- Certificate generation (PDF)
- Notification delivery
- Full UI/UX polish
- Production deployment
- Platform goes LIVE

### [2.1.0] - Sprint 8 (Deferred)
Post-Launch Expansion
- Exam competitions
- Leaderboards
- iOS application build
- Contest prize pools

---

## Bug Resolution Tracking

### Total Bugs by Sprint
- **Sprint 1:** 4 bugs (all resolved)
- **Sprint 2:** 3 bugs (all resolved)
- **Sprint 3.1:** 2 bugs (all resolved)
- **Sprint 3.2:** 3 bugs (all resolved)
- **Sprint 4:** 4 bugs (all resolved)
- **Sprint 5:** 4 issues identified and resolved

### Resolution Rate
- **15/15 bugs resolved (100% resolution rate)**
- **0 open bugs**

---

## Governance & Quality

### Documentation Coverage
- ✅ Technical Design Document (TDD) - 100% updated
- ✅ Sprint Handover Documents - Complete for all sprints
- ✅ Bug Log - Comprehensive history maintained
- ✅ README - Current and accurate
- ✅ Database State - Fully documented

### Testing Approach
- **Manual API Testing:** Postman/curl after each feature
- **Security Testing:** 401/403 response validation
- **Database Constraints:** Validation via Hibernate
- **Regression Testing:** Previous sprint features retested
- **Automated Tests:** Planned for Sprint 7

### Sprint Discipline
- ✅ Strict scope enforcement
- ✅ No mid-sprint changes
- ✅ Complete documentation per sprint
- ✅ Database snapshots maintained
- ✅ Cross-document alignment verified

---

**Last Updated:** February 8, 2026  
**Maintained By:** Educator Platform Team  
**Next Review:** Sprint 6 Completion

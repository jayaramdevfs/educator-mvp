# Project Handover Document

**Project Name:** Educator Platform  
**Version:** 1.1.2  
**Last Updated:** February 7, 2026  
**Document Owner:** Development Team  
**Status:** Active Development - Sprint 4 Completed

---

## Table of Contents

- [Document Ecosystem Overview](#document-ecosystem-overview)
1. [Executive Summary](#1-executive-summary)
2. [Project Overview](#2-project-overview)
3. [Source of Truth](#3-source-of-truth)
4. [Architecture & Technology Stack](#4-architecture--technology-stack)
5. [Roles & Permissions](#5-roles--permissions)
6. [Core Features & Capabilities](#6-core-features--capabilities)
7. [Locked Architectural Decisions](#7-locked-architectural-decisions)
8. [Development Process & Sprint Model](#8-development-process--sprint-model)
9. [Sprint History & Status](#9-sprint-history--status)
10. [Current System State](#10-current-system-state)
11. [Known Issues & Constraints](#11-known-issues--constraints)
12. [Testing Strategy](#12-testing-strategy)
13. [Deployment & DevOps](#13-deployment--devops)
14. [Documentation & Resources](#14-documentation--resources)
15. [Team & Contacts](#15-team--contacts)
16. [Communication Protocols](#16-communication-protocols)
17. [Onboarding Checklist](#17-onboarding-checklist)
18. [Future Roadmap](#18-future-roadmap)

---

## Document Ecosystem Overview

This Project Handover Document is part of a comprehensive documentation ecosystem. All documents work together to provide complete project knowledge transfer.

### Document Hierarchy

```
PROJECT_HANDOVER.md (This Document)
├── Provides high-level overview and current state
├── References all other project documents
├── Updated after Sprint 4 (February 7, 2026)
└── Version: 1.1.1

Educator_TDD_v1_1_2.md (Technical Design Document)
├── Single source of truth for all technical decisions
├── Architecture, features, and sprint specifications
├── Updated: February 7, 2026
└── Current Status: Sprint 4 Completed, Sprint 5 Ready

SPRINT_HANDOVER Documents (Per Sprint)
├── SPRINT_1_HANDOVER.md ✅
├── SPRINT_2_HANDOVER.md ✅
├── SPRINT_3_1_HANDOVER.md ✅
├── SPRINT_3_2_HANDOVER.md ✅
├── SPRINT_4_HANDOVER.md ✅ (Latest)
├── Contains: Feature tracking, Git commits, Bug entries
├── Includes: Verification checklists, Next sprint prompt
└── Created at end of each sprint (mandatory)

BUG_LOG.md
├── Append-only bug tracking across all sprints
├── 15 total bugs (All resolved - 100% resolution rate)
├── Sprint breakdown: S1(3), S2(1), S3.1(6), S3.2(1), S4(4)
├── Updated: February 7, 2026
└── Version: 1.1.1

README.md
├── Quick start guide and setup instructions
├── Updated with Sprint 4 changes
└── Current features and how to run
```

### Document Relationships

- **Educator_TDD_v1_1_2.md** defines WHAT to build and WHY
- **Sprint Handovers** document WHAT WAS built and HOW
- **BUG_LOG.md** tracks PROBLEMS encountered and SOLUTIONS (15 bugs, 100% resolved)
- **PROJECT_HANDOVER.md** (this document) provides OVERVIEW and CONTEXT
- **README.md** explains HOW TO RUN the project

### Mandatory Documentation Updates After Each Sprint

1. Update **Educator_TDD_v1_1_2.md** with any architectural changes
2. Create **SPRINT_X_HANDOVER.md** with all sprint deliverables
3. Append bugs to **BUG_LOG.md** (never delete history)
4. Update **PROJECT_HANDOVER.md** (this document) with progress
5. Update **README.md** with setup changes

---

## 1. Executive Summary

### Project Goal
Build a full-scale, international-level e-learning platform (Udemy/Coursera/Unacademy class) with comprehensive course management, multi-language support, and role-based access control. This is NOT an MVP - it's designed to be production-ready and scalable from day one.

### Current Status
- **Phase:** Development
- **Completion:** ~57% (4 of 7 sprints completed)
- **Last Major Milestone:** Sprint 4 - Infinite Lesson Hierarchy + Admin Course APIs
- **Next Major Milestone:** Sprint 5 - Enrollment + Progress Tracking

### Key Metrics
| Metric | Value | Target |
|--------|-------|--------|
| Sprints Completed | 4 | 7 |
| API Endpoints | 28 | 65+ |
| Database Tables | 5 | 20+ |
| Code Coverage | TBD | 80% |

### Critical Success Factors
1. Maintain stability through sprint-based development
2. Adhere to locked architectural decisions
3. Complete comprehensive documentation after each sprint
4. Zero-cost infrastructure until revenue generation
5. International-grade UI/UX and scalability

---

## 2. Project Overview

### Project Name
**Educator Platform** - EdTech Solution

### Project Description
Educator Platform is a comprehensive e-learning solution that enables:
- **Course Creation & Management**: Instructors create structured courses with infinite lesson hierarchy
- **Exam System**: Timed exams with multiple question types (MCQ, True/False, Essay)
- **Multi-Language Support**: First-class language feature, not an afterthought
- **Subscription Management**: Multiple subscription plans with flexible monetization
- **Progress Tracking**: Detailed learner progress and completion tracking
- **Role-Based Access**: Admin, Instructor, and Student roles with fine-grained permissions
- **Cross-Platform**: Web (React), Mobile (React Native - planned)

### Business Objectives
1. Create a competitive alternative to Udemy, Coursera, and Unacademy
2. Support international expansion with built-in multi-language capabilities
3. Enable flexible monetization through subscription plans
4. Provide superior course organization through infinite hierarchy
5. Deliver international-grade user experience

### Target Users/Audience
- **Primary Users:** 
  - Students seeking online education
  - Instructors creating and selling courses
  - Platform administrators managing the ecosystem
- **Secondary Users:** Educational institutions, corporate training departments
- **Geographic Scope:** Global (multi-language support)
- **Expected Scale:** 100K+ users, 10K+ courses, 1M+ enrollments

### Competitive Landscape
Similar platforms/competitors:
- **Udemy**: Key differentiator - Better course hierarchy, instructor automation tools
- **Coursera**: Key differentiator - More flexible pricing, infinite lesson nesting
- **Unacademy**: Key differentiator - Superior multi-language implementation, better exam system

### Unique Value Proposition
- **Infinite Hierarchy**: Unlimited course/lesson nesting vs. fixed 3-4 levels
- **Language-First Design**: Language selection before course discovery
- **Rule-Based Automation**: AI-free automation for exam and content generation
- **Admin-Controlled Instructor Features**: Platform owner controls instructor capabilities
- **Zero-Cost Start**: Free infrastructure until revenue generation

---

## 3. Source of Truth

### Primary Documentation
All design, architecture, rules, and constraints are documented in:

📄 **Educator Technical Design Document (TDD) v1.1.1**
- **Location:** `EDUCATOR_TDD_v1_1_1.md`
- **Last Updated:** February 7, 2026
- **Owner:** Development Team
- **Purpose:** Single source of truth for all technical decisions

### Documentation Hierarchy
1. **Technical Design Document (TDD) v1.1.1** - Architecture, features, sprint planning
2. **Sprint Handover Documents** - Sprint-by-sprint progress (`SPRINT_X_HANDOVER.md`)
3. **Bug Log** - Historical bug tracking (`BUG_LOG.md`)
4. **Project Handover** - This document (high-level overview)
5. **README.md** - Setup and running instructions
6. **API Documentation** - Endpoint specifications (in TDD)

### Rules for Source of Truth
- ✅ All decisions must align with TDD
- ✅ Any architectural changes require TDD update
- ✅ New features must be approved and documented in TDD before implementation
- ✅ Sprint handover documents must reference TDD sections
- ❌ No silent changes or undocumented decisions
- ❌ No verbal-only agreements
- ❌ No deviation from locked decisions without explicit approval

---

## 4. Architecture & Technology Stack

### System Architecture

#### High-Level Architecture
```
                    FRONTEND LAYER
Web App (React)              Mobile App (React Native - Planned)
- Browser-based              - iOS + Android
- Desktop/Tablet             - Native feel
      |                            |
      |       HTTPS/REST           |
      |                            |
            BACKEND API LAYER
Spring Boot REST API
- JWT Authentication
- Role-based Authorization (URL-based)
- Rule-Based Automation Service
- Subscription Validation
     |            |            |
PostgreSQL   File Storage  Cache(Caffeine)
```

#### Architecture Patterns
- **Layered Architecture**: Entity → Repository → Service → Controller separation
- **URL-Based Security**: Security enforced at SecurityFilterChain level, not controller
- **Materialized Path Pattern**: Hierarchical data (lessons) stored with path strings
- **Soft Delete Pattern**: All entities use is_deleted flag, never hard delete
- **JWT Stateless Authentication**: No server-side sessions

### Technology Stack

#### Backend
| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| Language | Java | 17 | Primary backend language |
| Framework | Spring Boot | 4.0.2 | Web framework |
| ORM | JPA/Hibernate | 6.x | Database abstraction |
| Database | PostgreSQL | 15 | Primary data store |
| Build Tool | Maven | 3.9.x | Dependency management |
| Authentication | JWT | jjwt 0.11.5 | Stateless security |
| Security | Spring Security | 6 | Authorization framework |

#### Frontend
| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| Framework | React | 18+ | UI framework |
| Build Tool | Vite | 5+ | Fast dev server |
| Routing | React Router | 6+ | SPA navigation |
| State Management | Context API | - | Simple state management |
| HTTP Client | Axios | 1.x | API communication |
| Styling | CSS Modules / Tailwind | 3.x | UI styling |

#### Mobile (Planned - Sprint 7+)
| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| Framework | React Native | 0.73+ | Cross-platform mobile |
| Navigation | React Navigation | 6+ | App navigation |
| State | Context API | - | Shared with web |
| HTTP Client | Axios | 1.x | Same API client as web |

#### DevOps & Infrastructure
| Component | Technology | Cost | Purpose |
|-----------|-----------|------|---------|
| Version Control | GitHub | Free | Code repository |
| Backend Hosting | Railway (free tier) | Free | Application hosting (post Sprint 7) |
| Database Hosting | Railway PostgreSQL | Free | Database hosting (post Sprint 7) |
| Frontend Hosting | Vercel | Free | Web hosting (post Sprint 7) |
| Development DB | Docker PostgreSQL | Free | Local development |

### Package Structure

#### Backend Package Structure
```
com.educator/
├── auth/              (Authentication — Sprint 1)
├── security/          (JWT, SecurityConfig — Sprint 1, 4)
├── users/             (User entity — Sprint 1)
├── roles/             (Roles: STUDENT/INSTRUCTOR/ADMIN — Sprint 1)
├── hierarchy/         (Infinite hierarchy — Sprint 2)
├── course/            (Course domain — Sprint 3.1)
│   └── lesson/        (Lesson domain — Sprint 3.2, 4)
├── enrollment/        (Enrollment + Progress — Sprint 5)
├── subscription/      (Subscription plans — Sprint 6)
├── sections/          (Homepage sections — Sprint 6)
├── exam/              (Exam system — Sprint 6)
├── settings/          (Platform settings — Sprint 6)
├── automation/        (Rule-based automation — Sprint 6)
└── EducatorApplication.java
```

#### Frontend Structure (Sprint 7)
```
src/
├── api/           (API client and services)
├── components/    (Reusable UI components)
├── pages/         (Page components)
├── contexts/      (React contexts for auth, etc.)
├── hooks/         (Custom React hooks)
├── utils/         (Utility functions)
└── App.jsx        (Root component)
```

---

## 5. Roles & Permissions

### User Roles

#### Admin
**Capabilities:**
- Create, modify, publish, archive, delete ANY course or exam
- Enable or disable instructor feature platform-wide
- Perform everything an instructor can do
- Override any restriction
- Manage platform settings
- Manage subscription plans
- Manage homepage sections
- Review automation-generated content
- View all users, enrollments, progress

**Restrictions:**
- None - Admin has super-power role

**Access Level:** Full Access

**Implementation:** URL pattern `/api/admin/**` requires `ADMIN` role

#### Instructor
**Capabilities:**
- Create courses (when feature enabled by admin)
- Create lessons within their courses
- Create exams and attach to courses
- Create standalone exams (if allowed by admin)
- Use rule-based automation tools (when enabled)
- View their own course analytics
- Manage their course content lifecycle (draft → publish → archive)

**Restrictions:**
- Can only manage their own content (not other instructors' courses)
- Feature can be completely disabled by admin
- Cannot access admin functions
- Cannot manage users or platform settings
- Subject to admin-controlled restrictions

**Access Level:** Restricted (own content only, feature toggle controlled by admin)

**Implementation:** `/api/instructor/**` endpoints (Sprint 6+), requires platform setting check

#### Student
**Capabilities:**
- Select language first, then browse courses/exams
- Enroll in published courses
- Track progress through lessons
- Take exams (timed, with instant results for MCQ/True-False)
- Purchase subscription plans
- View their enrollments and progress
- Complete lessons and courses
- View exam results and history
- Manage their profile

**Restrictions:**
- Cannot create any content (courses, lessons, exams)
- Cannot access admin or instructor features
- Can only view published content
- Can only enroll in courses allowed by subscription plan
- Cannot modify course structure or content

**Access Level:** Read-only for content, full access to personal data

**Implementation:** `/api/learner/**` endpoints require authentication, no special role

### Permission Matrix

| Feature/Action | Admin | Instructor | Student |
|---------------|-------|------------|---------|
| Create Course | ✅ | ✅ (if enabled) | ❌ |
| Publish Course | ✅ | ✅ (own courses) | ❌ |
| Delete Any Course | ✅ | ❌ | ❌ |
| Create Lesson | ✅ | ✅ (own courses) | ❌ |
| Create Exam | ✅ | ✅ (if enabled) | ❌ |
| Take Exam | ✅ | ✅ | ✅ |
| Enroll in Course | ✅ | ✅ | ✅ |
| Manage Subscriptions (Admin) | ✅ | ❌ | ❌ |
| Purchase Subscription | ✅ | ✅ | ✅ |
| Manage Platform Settings | ✅ | ❌ | ❌ |
| Enable/Disable Instructors | ✅ | ❌ | ❌ |
| View All Users | ✅ | ❌ | ❌ |
| Manage Hierarchy | ✅ | ❌ | ❌ |
| Configure Homepage Sections | ✅ | ❌ | ❌ |

---

## 6. Core Features & Capabilities

### Feature 1: Infinite Hierarchy
**Status:** ✅ Completed  
**Sprint:** Sprint 2  
**Description:** Platform supports unlimited nesting of categories and organizational structures. No fixed depth limits.

**Key Components:**
- HierarchyNode entity with self-referencing parent
- Slug-based routing for SEO
- Sort ordering for deterministic display
- Soft delete support

**User Stories:**
- As an admin, I can create unlimited category levels for course organization
- As a student, I can browse courses through intuitive category trees

### Feature 2: Course Management
**Status:** ✅ Completed  
**Sprint:** Sprint 3.1, Sprint 4  
**Description:** Complete course lifecycle management with status transitions, difficulty levels, language support, and admin CRUD operations.

**Key Components:**
- Course entity with hierarchy attachment
- Status lifecycle: DRAFT → PUBLISHED → ARCHIVED
- Difficulty levels: BEGINNER, INTERMEDIATE, ADVANCED
- Language code support
- Admin CRUD APIs

**User Stories:**
- As an admin, I can create courses and manage their lifecycle
- As an instructor, I can create and publish courses (Sprint 6+)
- As a student, I can browse published courses by language

### Feature 3: Infinite Lesson Hierarchy
**Status:** ✅ Completed  
**Sprint:** Sprint 3.2, Sprint 4  
**Description:** Lessons support unlimited nesting (lesson → sub-lesson → sub-sub-lesson → ∞) using materialized path pattern.

**Key Components:**
- Lesson entity with parent_lesson_id
- Materialized path storage (e.g., "/1/2/5")
- Depth level calculation
- Lesson types: TEXT, VIDEO, DOCUMENT
- Subtree deletion with soft delete

**User Stories:**
- As an instructor, I can create nested lesson structures with unlimited depth
- As a student, I can navigate through hierarchical lessons
- As an admin, I can manage lesson hierarchy

### Feature 4: JWT Authentication
**Status:** ✅ Completed  
**Sprint:** Sprint 1, Sprint 4  
**Description:** Stateless JWT-based authentication with role-based authorization.

**Key Components:**
- User registration and login
- JWT token generation and validation
- Role system: STUDENT, INSTRUCTOR, ADMIN
- JwtAuthenticationFilter integration
- Password hashing with BCrypt

**User Stories:**
- As a user, I can register and login securely
- As a user, I receive a JWT token for authenticated requests
- As an admin, I have role-based access to admin features

### Feature 5: URL-Based Security
**Status:** ✅ Completed  
**Sprint:** Sprint 4  
**Description:** All authorization enforced at SecurityFilterChain level using URL patterns, not controller annotations.

**Key Components:**
- SecurityFilterChain with pattern-based authorization
- `/api/admin/**` requires ADMIN role
- `/api/public/**` open to all
- `/api/learner/**` requires authentication
- No @PreAuthorize annotations

**User Stories:**
- As a developer, I have centralized security configuration
- As a system, I enforce consistent authorization across all endpoints

### Feature 6: Enrollment & Progress Tracking
**Status:** ⏳ Planned  
**Sprint:** Sprint 5  
**Description:** Students can enroll in courses and track lesson-by-lesson progress with auto-completion.

**Key Components:**
- Enrollment entity
- Lesson progress tracking
- Auto-complete enrollment when all lessons done
- Progress percentage calculation

**User Stories:**
- As a student, I can enroll in published courses
- As a student, I can track which lessons I've completed
- As a student, I can see my overall course progress percentage

### Feature 7: Subscription Plans
**Status:** ⏳ Planned  
**Sprint:** Sprint 6 (Admin), Sprint 7 (Student)  
**Description:** Flexible subscription plans with 6 plan types and course/exam attachment.

**Key Components:**
- Subscription plans: MONTHLY, QUARTERLY, ANNUAL, LIFETIME, TRIAL, PAY_PER_COURSE
- Plan items (attach courses/exams)
- Subscription validation on enrollment
- Admin plan management
- Student purchase flow

**User Stories:**
- As an admin, I can create subscription plans with different pricing
- As a student, I can purchase a subscription to access courses
- As a student, I am blocked from enrolling without valid subscription

### Feature 8: Exam System
**Status:** ⏳ Planned  
**Sprint:** Sprint 6  
**Description:** Timed exams with multiple question types, instant auto-grading, and pass/fail scoring.

**Key Components:**
- Exam entity with questions
- Question types: MCQ, True/False, Essay
- Timed exam attempts
- Instant auto-grading (MCQ, True/False)
- Essay manual review
- Pass/fail based on passing score

**User Stories:**
- As an instructor, I can create exams with various question types
- As a student, I can take timed exams
- As a student, I receive instant results for objective questions

### Feature 9: Platform Settings
**Status:** ⏳ Planned  
**Sprint:** Sprint 6  
**Description:** Admin-controlled platform settings including instructor feature toggle.

**Key Components:**
- Platform settings entity (key-value store)
- Instructor feature enable/disable
- Admin always has instructor power
- Settings API endpoints

**User Stories:**
- As an admin, I can enable or disable instructor registration
- As an admin, I can control whether existing instructors can create content
- As an admin, I always retain instructor capabilities regardless of toggle

### Feature 10: Homepage Sections
**Status:** ⏳ Planned  
**Sprint:** Sprint 6  
**Description:** Admin-configurable homepage sections for dynamic content display.

**Key Components:**
- Sections entity with section types
- Section items (attach courses/exams)
- Reorderable sections
- Public API for section rendering

**User Stories:**
- As an admin, I can create homepage sections like "Featured Courses"
- As an admin, I can reorder sections for optimal display
- As a student, I see dynamic homepage content configured by admin

### Feature 11: Rule-Based Automation
**Status:** ⏳ Planned  
**Sprint:** Sprint 6  
**Description:** Non-AI automation for exam generation and content fetching using templates and rules.

**Key Components:**
- Automation jobs tracking
- Automation templates
- Content review queue
- Admin approval workflow

**User Stories:**
- As an instructor, I can auto-generate exam questions from topic templates
- As an admin, I review automation-generated content before publishing
- As an instructor, I can fetch and structure course content using predefined rules

### Feature 12: React Frontend
**Status:** ⏳ Planned  
**Sprint:** Sprint 7  
**Description:** Complete React-based web interface consuming all backend APIs.

**Key Components:**
- React 18 + Vite
- Authentication pages
- Public course/exam browsing
- Student dashboard
- Admin dashboard
- Instructor dashboard

**User Stories:**
- As a user, I can interact with the platform through a modern web interface
- As a student, I have a dashboard showing my enrollments and progress
- As an admin, I have comprehensive management tools

### Feature Summary

| Feature | Status | Priority | Sprint | Completion % |
|---------|--------|----------|--------|--------------|
| Authentication | ✅ Complete | High | 1 | 100% |
| Infinite Hierarchy | ✅ Complete | High | 2 | 100% |
| Course Management | ✅ Complete | High | 3.1 | 100% |
| Lesson APIs | ✅ Complete | High | 3.2 | 100% |
| Lesson Hierarchy | ✅ Complete | High | 4 | 100% |
| Security Stabilization | ✅ Complete | Critical | 4 | 100% |
| Enrollment | ⏳ Planned | High | 5 | 0% |
| Progress Tracking | ⏳ Planned | High | 5 | 0% |
| Subscriptions (Admin) | ⏳ Planned | High | 6 | 0% |
| Exam System | ⏳ Planned | High | 6 | 0% |
| Platform Settings | ⏳ Planned | Medium | 6 | 0% |
| Homepage Sections | ⏳ Planned | Medium | 6 | 0% |
| Automation | ⏳ Planned | Medium | 6 | 0% |
| User Profile | ⏳ Planned | Medium | 6 | 0% |
| Subscriptions (Student) | ⏳ Planned | High | 7 | 0% |
| React Frontend | ⏳ Planned | High | 7 | 0% |
| Deployment | ⏳ Planned | High | 7 | 0% |

---

## 7. Locked Architectural Decisions

### Decision 1: JWT Stateless Authentication
**Made In:** Sprint 1  
**Decision:** All authentication uses JWT tokens. No server-side sessions. Token contains user ID and roles.  
**Rationale:** Stateless design enables horizontal scaling, works seamlessly with mobile apps, simplifies deployment.  
**Impact:** Cannot invalidate tokens before expiry (except via token blacklist, which defeats stateless purpose). Logout is client-side only.  
**Status:** 🔒 LOCKED

**Related Components:**
- AuthService, JwtAuthenticationFilter, SecurityConfig
- All authenticated endpoints

### Decision 2: URL-Based Authorization Pattern
**Made In:** Sprint 4  
**Decision:** All admin endpoints use URL pattern `/api/admin/**` with role-based authorization at SecurityFilterChain level. No controller-level `@PreAuthorize` annotations.  
**Rationale:** Centralized security configuration, consistent authorization model, easier to audit and maintain.  
**Impact:** All future admin features must follow this pattern. Cannot use method-level security. Security rules in one place (SecurityConfig).  
**Status:** 🔒 LOCKED

**Related Components:**
- SecurityConfig, all admin controllers

### Decision 3: Materialized Path for Lesson Hierarchy
**Made In:** Sprint 4  
**Decision:** Lesson hierarchy stored using materialized path pattern (e.g., "/1/2/5") with path string field. IDs in paths are immutable.  
**Rationale:** Simple to implement, efficient for subtree queries, adequate performance for EdTech scale, easier to understand than alternatives (closure table, nested sets).  
**Impact:** Lesson IDs must never change. Reparenting lessons (moving to different parent) is complex and deferred as future enhancement. Path recalculation for entire subtree would be needed.  
**Status:** 🔒 LOCKED

**Related Components:**
- Lesson entity, LessonService, lesson hierarchy queries

### Decision 4: Soft Delete Pattern
**Made In:** Sprint 1-4  
**Decision:** Never hard delete data. Always use `is_deleted` flag and filter in queries. Preserve referential integrity.  
**Rationale:** Data recovery, audit trail, referential integrity preservation, regulatory compliance potential.  
**Impact:** All queries must check `is_deleted=false`. Database size grows over time. Periodic archival process may be needed in future. Soft-deleted records remain in indexes.  
**Status:** 🔒 LOCKED

**Related Components:**
- All entities, all repositories, all service queries

### Decision 5: Course Status Lifecycle
**Made In:** Sprint 4  
**Decision:** Courses follow strict DRAFT → PUBLISHED → ARCHIVED status flow. No backward transitions. No skip-ahead transitions.  
**Rationale:** Clear content approval workflow, prevents accidental publication, supports admin review process, maintains quality control.  
**Impact:** Cannot publish directly from ARCHIVED. Cannot revert PUBLISHED to DRAFT. Must unpublish (archive) then delete if needed. Lifecycle is one-way only.  
**Status:** 🔒 LOCKED

**Related Components:**
- Course entity, CourseService, course status transition APIs

### Decision 6: Language-First Design
**Made In:** Sprint 1-3  
**Decision:** Language is a first-class feature. Courses have `languageCode`. Lessons inherit language from course. Exams have `languageCode`. Student flow: (1) Select Language → (2) Browse Courses/Exams.  
**Rationale:** International expansion requirement, not an afterthought, clean user experience, enables language-specific content discovery.  
**Impact:** All content must have language specified. Cannot mix languages within a course. UI must support language selection as first step. Translation handled externally or at presentation layer.  
**Status:** 🔒 LOCKED

**Related Components:**
- Course entity, Lesson entity, Exam entity (Sprint 6), student flow (Sprint 7)

### Decision 7: Zero-Cost Infrastructure
**Made In:** Sprint 1  
**Decision:** Use free tiers for all services until revenue starts. No paid APIs, databases, or hosting during development. Rule-based automation uses no external paid APIs.  
**Rationale:** Minimize upfront investment, validate product-market fit first, scalable pricing as revenue grows.  
**Impact:** Limited to free tier constraints (Railway, Vercel, etc.). Must monitor usage to avoid overages. May need to upgrade post-launch. No premium features from paid services.  
**Status:** 🔒 LOCKED

**Related Components:**
- All infrastructure, hosting, database, automation

### Locked Decisions Summary

| Decision | Sprint | Area | Locked Status |
|----------|--------|------|---------------|
| JWT Stateless Authentication | 1 | Security | 🔒 LOCKED |
| URL-Based Authorization | 4 | Security | 🔒 LOCKED |
| Materialized Path Hierarchy | 4 | Backend/Database | 🔒 LOCKED |
| Soft Delete Pattern | 1-4 | Backend/Database | 🔒 LOCKED |
| Course Status Lifecycle | 4 | Backend/Business Logic | 🔒 LOCKED |
| Language-First Design | 1-3 | Product/UX | 🔒 LOCKED |
| Zero-Cost Infrastructure | 1 | DevOps | 🔒 LOCKED |

---

## 8. Development Process & Sprint Model

### Sprint Structure

**Total Planned Sprints:** 7  
**Sprint Duration:** Variable (based on complexity)  
**Current Sprint:** Sprint 4 COMPLETED, Sprint 5 NEXT

### Sprint Process (MANDATORY)

#### Before Sprint Starts
- [x] Latest repository provided (GitHub access or ZIP)
- [x] README.md is up-to-date
- [x] Bug log is current
- [x] Previous sprint handover reviewed
- [ ] Sprint planning meeting completed
- [ ] Sprint backlog finalized
- [ ] Proactive feature proposals reviewed and approved (inspired by Udemy/Coursera/Unacademy)

#### During Sprint
- [ ] Regular progress updates
- [ ] Code committed to repository regularly
- [ ] Build maintained in stable state
- [ ] Documentation updated in parallel with code
- [ ] Bug log updated as issues arise
- [ ] No silent changes - confirm before major decisions

#### After Sprint Ends (DELIVERABLES)
The assistant MUST provide:
1. ✅ **Git Commit Summary** (one-line, clear, sprint-scoped)
2. ✅ **Git Commit Description** (multi-line: what/why/deferred)
3. ✅ **Updated README.md** (section text only in .MD format)
4. ✅ **Updated Bug Log** (append-only in .MD format)
5. ✅ **Sprint Handover Document** (complete `SPRINT_X_HANDOVER.md`)
6. ✅ **Updated Project Handover** (this document updated)
7. ✅ **Updated Instructions** (any new rules or constraints from sprint)
8. ✅ **Sprint Closure Confirmation** (official statement: "Sprint X is closed, stable, and approved for handover")

**User Responsibilities:**
- Copy provided content to repository
- Commit to GitHub
- Share updated repo ZIP/access for next sprint
- Review and approve sprint deliverables

### Branching Strategy
- **main/master:** Production-ready code (after Sprint 7)
- **develop:** Integration branch (current development)
- **feature/*:** Feature branches (as needed)
- **sprint-X:** Sprint-specific branches for major work

### Code Review Process
- Code changes reviewed before sprint closure
- Manual testing performed for all new features
- Integration testing with existing features
- Security validation for all protected endpoints
- Documentation review for completeness

---

## 9. Sprint History & Status

### Sprint 1: Authentication & Security Foundation
**Status:** ✅ COMPLETED  
**Date:** [Start Date] - [End Date]  
**Goal:** Establish authentication foundation and core entities

**Delivered:**
- User registration API (POST /api/auth/register)
- User login API (POST /api/auth/login)
- BCrypt password hashing
- JWT-based stateless authentication
- Role system: STUDENT, INSTRUCTOR, ADMIN (auto-seeded)
- Default role on registration: STUDENT
- Spring Security configuration
- PostgreSQL in Docker with UTC timezone config

**Key Decisions:**
- JWT stateless authentication chosen over session-based
- Role-based access control model defined
- Soft delete pattern established

**Bugs Resolved:**
- S1-B1: PostgreSQL timezone error
- S1-B2: Hibernate dialect detection failure
- S1-B3: Spring Security default login page

**Handover Document:** `SPRINT_1_HANDOVER.md`

---

### Sprint 2: Infinite Hierarchy Backend
**Status:** ✅ COMPLETED  
**Date:** [Start Date] - [End Date]  
**Goal:** Implement infinite hierarchy foundation for course organization

**Delivered:**
- Infinite self-referencing hierarchy with unlimited depth
- Slug-based identifiers for routing
- Localization-ready fields (nameEn, descriptionEn)
- Versioning, visibility flags, soft delete
- Audit fields (createdAt, updatedAt, createdBy)
- Deterministic ordering (sortOrder)
- Admin APIs for managing hierarchy nodes
- Public read-only APIs for browsing hierarchy

**Key Decisions:**
- Self-referencing hierarchy chosen over separate category/subcategory tables
- Slug-based routing for future SEO
- English-first with _en suffix for future i18n

**Bugs Resolved:**
- S2-B1: 403 Forbidden on admin hierarchy endpoints (temporary permitAll)

**Handover Document:** `SPRINT_2_HANDOVER.md`

---

### Sprint 3.1: Core Course & Lesson Domain Foundation
**Status:** ✅ COMPLETED  
**Date:** [Start Date] - [End Date]  
**Goal:** Establish course and lesson domain entities

**Delivered:**
- Course entity: hierarchy attachment, title/description, status lifecycle, difficulty, language, duration
- Lesson entity: course attachment, type (TEXT/VIDEO/DOCUMENT), content fields, soft delete
- CourseRepository and LessonRepository
- CourseService with business rule enforcement
- Initial admin CourseController

**Key Decisions:**
- Course status lifecycle: DRAFT → PUBLISHED → ARCHIVED
- Lesson types: TEXT, VIDEO, DOCUMENT
- Language code at course level

**Bugs Resolved:**
- S3.1-B1: Hibernate ExceptionInInitializerError
- S3.1-B2: Lombok incompatibility
- S3.1-B3: Illegal manual ID assignment
- S3.1-B4: Package path mismatches
- S3.1-B5: Missing setters
- S3.1-B6: Lesson service compilation failures

**Handover Document:** `SPRINT_3_1_HANDOVER.md`

---

### Sprint 3.2: Lesson APIs
**Status:** ✅ COMPLETED  
**Date:** [Start Date] - [End Date]  
**Goal:** Complete lesson API layer without destabilizing system

**Delivered:**
- Lesson creation APIs for Admin/Instructor
- Public lesson retrieval APIs for Students
- Deterministic lesson ordering using orderIndex
- Soft delete enforcement at service layer
- Lesson language inherits from Course

**Key Decisions:**
- Existing LessonService accepted as stable (no refactoring)
- Infinite lesson hierarchy intentionally deferred to Sprint 4
- Public APIs remain authentication-free

**Bugs Resolved:**
- S3.2-B1: Potential LessonService logic duplication (caught in planning)

**Handover Document:** `SPRINT_3_2_HANDOVER.md`

---

### Sprint 4: Infinite Lesson Hierarchy + Admin Course APIs
**Status:** ✅ COMPLETED  
**Date:** [Start Date] - February 7, 2026  
**Goal:** Infinite lesson hierarchy and security stabilization

**Delivered:**
- **Infinite Lesson Hierarchy:**
  - Parent-child lesson relationships with unlimited depth
  - Materialized path pattern (e.g., "/1/2/5")
  - Depth-level calculation
  - Nested lesson retrieval
  - Subtree deletion with soft delete

- **Admin Course Management APIs:**
  - GET /api/admin/courses (list with pagination)
  - GET /api/admin/courses/{id} (get single)
  - PUT /api/admin/courses/{id} (update)
  - DELETE /api/admin/courses/{id} (soft delete)
  - POST /api/admin/courses/{id}/publish (publish)
  - POST /api/admin/courses/{id}/archive (archive)

- **Admin Lesson Hierarchy APIs:**
  - POST /api/admin/lessons/{parentId}/children (create child)
  - GET /api/admin/lessons/{id}/children (get children)
  - PUT /api/admin/lessons/{id} (update)
  - DELETE /api/admin/lessons/{id} (delete subtree)

- **Security Stabilization:**
  - Added JwtAuthenticationFilter to SecurityFilterChain
  - URL-based admin protection (/api/admin/** requires ADMIN role)
  - Removed conflicting method security annotations
  - Unified authorization approach

**Key Decisions:**
- **Decision 4.1:** Admin endpoints must use URL-based security only
- **Decision 4.2:** Materialized path pattern for hierarchies
- **Decision 4.3:** Course status transitions enforced
- **Decision 4.4:** Lesson subtree operations use path LIKE pattern

**Bugs Resolved:**
- S4-B1: Admin APIs returned 403 (JWT not injected into SecurityContext)
- S4-B2: Lesson admin APIs 403 (UserDetailsService missing)
- S4-B3: Conflicting authorization checks (duplicate JWT + method security)
- S4-B4: Lesson creation failed with 500 (NOT NULL constraint on created_at)

**Handover Document:** `SPRINT_4_HANDOVER.md`

---

### Sprint 5: Enrollment + Progress Tracking
**Status:** ⏳ NEXT (Ready to Begin)  
**Date:** TBD  
**Goal:** Enable students to enroll in courses and track progress

**Planned Deliverables:**
- Enrollment domain (entity, repository, service, controller)
- Lesson progress tracking (entity, repository, service, controller)
- 2 new tables: enrollments, lesson_progress
- 9 new API endpoints:
  - 4 enrollment endpoints (/api/learner/enrollments/...)
  - 3 progress endpoints (/api/learner/progress/...)
  - 2 course browsing endpoints (/api/learner/courses/...)
- Security config update: /api/learner/** pattern requiring JWT authentication
- Auto-complete enrollment when all lessons done
- Progress percentage calculation

**Prerequisites:**
- Course domain stable ✅
- Lesson hierarchy complete ✅
- Security architecture finalized ✅

**Handover Document:** `SPRINT_5_HANDOVER.md` (to be created)

---

### Sprint 6: Exam System + Subscriptions + Platform Settings + Homepage Sections + Automation + User Profile
**Status:** ⏳ Planned  
**Date:** TBD  
**Goal:** Complete backend feature set before frontend

**Planned Deliverables:**
- Full exam system (create, take, grade, results)
- Subscription plans (admin management)
- Platform settings (instructor toggle)
- Homepage sections (admin-configurable)
- Rule-based automation (architecture + schema)
- User profile management
- ~9 new tables, ~36 new API endpoints

**Prerequisites:**
- Enrollment complete ✅ (after Sprint 5)

**Handover Document:** `SPRINT_6_HANDOVER.md` (to be created)

---

### Sprint 7: React Web Frontend + Testing + Deployment
**Status:** ⏳ Planned  
**Date:** TBD  
**Goal:** Complete web frontend and deploy to production

**Planned Deliverables:**
- React 18 + Vite web application
- Authentication pages
- Public pages (home, catalog)
- Student dashboard (enrollments, progress, exams, subscriptions, profile)
- Admin dashboard (all management features)
- Subscription purchase flow (student side)
- End-to-end testing
- Production deployment (Railway + Vercel)

**Prerequisites:**
- All backend features complete ✅ (after Sprint 6)

**Handover Document:** `SPRINT_7_HANDOVER.md` (to be created)

---

### Sprint Roadmap

| Sprint | Title | Status | Key Features | Planned Completion |
|--------|-------|--------|--------------|-------------------|
| 1 | Authentication & Security | ✅ Complete | Auth, JWT, Roles | Completed |
| 2 | Infinite Hierarchy | ✅ Complete | HierarchyNode, Slug routing | Completed |
| 3.1 | Course & Lesson Domain | ✅ Complete | Course/Lesson entities | Completed |
| 3.2 | Lesson APIs | ✅ Complete | Lesson CRUD APIs | Completed |
| 4 | Lesson Hierarchy + Admin APIs | ✅ Complete | Infinite lessons, Security | February 7, 2026 |
| 5 | Enrollment + Progress | ⏳ Next | Enrollment, Progress tracking | TBD |
| 6 | Exams + Subscriptions + Settings | ⏳ Planned | Exams, Plans, Sections, Automation | TBD |
| 7 | Frontend + Deployment | ⏳ Planned | React UI, Production deploy | TBD |

---

## 10. Current System State

### Build Status
- **Backend:** ✅ Stable (Sprint 4 completed)
- **Frontend:** ⏳ Not Started (Sprint 7)
- **Mobile:** ⏳ Not Started (Future)

### Database State
- **Tables:** 5 (users, roles, user_roles, hierarchy_nodes, courses, lessons)
- **Relationships:** 4 foreign keys (user_roles → users/roles, courses → hierarchy_nodes, lessons → courses, lessons → lessons)
- **Migration Version:** V4 (lesson hierarchy fields added)
- **Seed Data:** 3 roles seeded at startup (STUDENT, INSTRUCTOR, ADMIN)

### API State
- **Total Endpoints:** 28
- **Public Endpoints:** 4 (register, login, browse hierarchy, list root lessons)
- **Admin Endpoints:** 10 (hierarchy create, course CRUD + lifecycle, lesson hierarchy)
- **Learner Endpoints:** 0 (Sprint 5)
- **Authentication Method:** JWT (jjwt 0.11.5)

### Code Metrics
| Metric | Value |
|--------|-------|
| Backend Packages | 8 (auth, security, users, roles, hierarchy, course, lesson, main) |
| JPA Entities | 5 (User, Role, HierarchyNode, Course, Lesson) |
| Service Classes | 5 (AuthService, HierarchyService, CourseService, LessonService, UserDetailsService) |
| Controllers | 5 (AuthController, HierarchyController, AdminCourseController, AdminLessonController, PublicLessonController) |
| Lines of Code | TBD |
| Test Coverage | TBD |
| Technical Debt | Low |

### Current Capabilities
What the system can do right now:
1. **User Management:** Register, login, role assignment
2. **Authentication:** JWT-based stateless auth, role-based authorization
3. **Hierarchy Management:** Create unlimited category depth, slug-based routing
4. **Course Management:** Full admin CRUD, status lifecycle (DRAFT → PUBLISHED → ARCHIVED)
5. **Lesson Management:** Infinite lesson hierarchy with parent-child relationships
6. **Lesson Content:** TEXT, VIDEO, DOCUMENT lesson types
7. **Path-Based Queries:** Efficient subtree retrieval using materialized path
8. **Security:** URL-based authorization, JWT filter, role enforcement

### What's NOT Yet Implemented
1. **Enrollment:** Students enrolling in courses (Sprint 5)
2. **Progress Tracking:** Lesson completion tracking (Sprint 5)
3. **Subscription Plans:** Monetization and access control (Sprint 6, 7)
4. **Exam System:** Create and take exams (Sprint 6)
5. **Platform Settings:** Instructor feature toggle (Sprint 6)
6. **Homepage Sections:** Admin-configurable content sections (Sprint 6)
7. **Rule-Based Automation:** Exam/content generation (Sprint 6)
8. **User Profile:** Profile management, password change (Sprint 6)
9. **Public Course Browsing:** Student-facing course discovery (Sprint 5)
10. **Instructor APIs:** Instructor-specific endpoints (Sprint 6)
11. **React Frontend:** Web UI (Sprint 7)
12. **Mobile Apps:** iOS and Android (Future)
13. **Payment Gateway:** Razorpay/Stripe integration (Deferred)
14. **Certificates:** Course completion certificates (Deferred)
15. **Analytics Dashboard:** Platform metrics (Deferred)

---

## 11. Known Issues & Constraints

### Known Issues
| Issue ID | Description | Severity | Status | Workaround |
|----------|-------------|----------|--------|------------|
| None | Sprint 4 closed with no known issues | - | - | - |

**Note:** All bugs encountered in Sprint 1-4 have been resolved. See `BUG_LOG.md` for historical issues.

### Bug Statistics (From BUG_LOG.md)

**Overall Summary:**
- **Total Bugs:** 15
- **Resolved:** 15 (100% resolution rate)
- **Open:** 0
- **Critical:** 2 (both resolved)
- **High:** 7 (all resolved)
- **Medium:** 4 (all resolved)
- **Low:** 2 (both resolved)

**Sprint-wise Breakdown:**

| Sprint | Total | Critical | High | Medium | Low | Resolved |
|--------|-------|----------|------|--------|-----|----------|
| Sprint 1 | 3 | 1 | 1 | 1 | 0 | 3 ✅ |
| Sprint 2 | 1 | 0 | 1 | 0 | 0 | 1 ✅ |
| Sprint 3.1 | 6 | 0 | 2 | 3 | 1 | 6 ✅ |
| Sprint 3.2 | 1 | 0 | 0 | 0 | 1 | 1 ✅ |
| Sprint 4 | 4 | 1 | 3 | 0 | 0 | 4 ✅ |
| **Total** | **15** | **2** | **7** | **4** | **2** | **15** ✅ |

**Bug Categories:**

| Category | Count | Percentage |
|----------|-------|------------|
| Security | 5 | 33.3% |
| Backend/ORM | 5 | 33.3% |
| Database | 2 | 13.3% |
| Backend/Service | 2 | 13.3% |
| Backend/Build | 1 | 6.7% |

**Key Insights:**
- 100% resolution rate maintained across all sprints
- Security issues dominated (33.3%), all resolved in Sprint 4
- No bugs carried forward to next sprint
- Average resolution time: Within same sprint
- No data loss or corruption occurred from any bug

### Technical Constraints
1. **Lesson Reparenting Not Supported:** Moving a lesson to a different parent requires path recalculation for entire subtree. This is complex and deferred as future enhancement. **Impact:** Cannot reorganize lesson hierarchy post-creation.

2. **No Circular Hierarchy Prevention:** Database does not enforce acyclic graph constraint. Application logic prevents circular references via path validation, but not enforced at DB level. **Impact:** Requires careful service-layer validation.

3. **Admin-Only Course Management (Current State):** Public and learner endpoints for courses deferred to Sprint 5. Students cannot browse or enroll yet. **Impact:** No student-facing functionality until Sprint 5.

4. **No Bulk Operations:** No batch delete, batch publish, or batch update endpoints. **Impact:** Admins must perform operations one-by-one.

5. **Pagination Fixed Size:** Course listing uses default page size, not configurable via query params. **Impact:** Cannot adjust page size dynamically.

6. **JWT Token Cannot Be Invalidated:** Stateless design means tokens valid until expiry. No token blacklist. **Impact:** Logout is client-side only, compromised tokens valid until expiry.

7. **Free Tier Limitations:** Infrastructure limited to free tier constraints (Railway, Vercel). **Impact:** May hit limits post-launch, require upgrade.

### Business Constraints
1. **Zero-Cost Until Revenue:** Must use free tiers only. No paid services until platform generates revenue.

2. **Instructor Feature Controlled by Admin:** Instructor registration and content creation can be completely disabled by admin. **Impact:** Instructor capabilities subject to platform owner control.

3. **Language Selection Required:** Students must select language before browsing courses. **Impact:** Cannot browse all courses globally, language acts as filter.

### Technical Debt
| Debt Item | Impact | Priority | Planned Resolution |
|-----------|--------|----------|-------------------|
| Manual API Testing Only | No automated test suite | Medium | Sprint 7 (add E2E tests) |
| No Code Coverage Metrics | Unknown test coverage | Low | Sprint 7 (add coverage tools) |
| Temporary Security permitAll | Hierarchy endpoints temporarily open | Low | Remove after Sprint 6 (proper role enforcement) |
| No API Versioning | Breaking changes affect all clients | Low | Add /v1/ prefix in Sprint 7 |

---

## 12. Testing Strategy

### Testing Levels
1. **Unit Testing:** Not yet implemented - planned for Sprint 7
2. **Integration Testing:** Not yet implemented - planned for Sprint 7
3. **API Testing:** Manual testing using curl/Postman after each sprint
4. **End-to-End Testing:** Planned for Sprint 7 with React frontend
5. **Performance Testing:** Not planned for MVP

### Test Coverage Goals
- **Backend:** Target 80% (Sprint 7)
- **Frontend:** Target 70% (Sprint 7)
- **Current Coverage:** 0% (no automated tests yet)

### Testing Tools
- **Backend Unit Tests:** JUnit 5 + Mockito (planned)
- **API Testing:** Manual curl + Postman (current)
- **Frontend Tests:** Jest + React Testing Library (planned)
- **E2E Tests:** Playwright or Cypress (planned)

### How to Run Tests
```bash
# Backend tests (after Sprint 7)
cd backend
mvn test

# Frontend tests (after Sprint 7)
cd frontend
npm test

# E2E tests (after Sprint 7)
npm run test:e2e
```

**Current Testing Approach (Sprint 1-4):**
- Manual API testing after each feature
- Security endpoint testing (401, 403 responses)
- Database constraint validation
- Regression testing of previous sprint features
- Build verification (clean Maven build)

---

## 13. Deployment & DevOps

### Environments

#### Development
- **URL:** http://localhost:8080 (backend), http://localhost:5173 (frontend - Sprint 7)
- **Database:** Docker PostgreSQL on localhost:5432
- **Purpose:** Local development and testing

#### Staging/Testing
- **URL:** Not yet deployed
- **Database:** Not yet deployed
- **Purpose:** Pre-production testing (post Sprint 7)

#### Production
- **URL:** Not yet deployed (planned for Sprint 7)
- **Backend:** Railway free tier
- **Frontend:** Vercel free tier
- **Database:** Railway PostgreSQL free tier
- **Purpose:** Live production environment

### Deployment Process

#### Backend Deployment (Sprint 7)
1. Create production `application-prod.yml` with Railway database URL
2. Build JAR: `mvn clean package -DskipTests`
3. Deploy to Railway via GitHub integration
4. Configure environment variables (JWT_SECRET, DATABASE_URL)
5. Verify health check endpoint
6. Test API endpoints

#### Frontend Deployment (Sprint 7)
1. Build production bundle: `npm run build`
2. Configure API_URL environment variable (Railway backend URL)
3. Deploy to Vercel via GitHub integration
4. Configure CORS on backend for Vercel domain
5. Verify frontend loads and connects to API
6. Test authentication flow

#### Database Deployment (Sprint 7)
1. Create PostgreSQL instance on Railway
2. Run migration scripts
3. Seed initial data (roles)
4. Verify connections from backend
5. Configure backup strategy

### Configuration Management
- **Environment Variables:** 
  - Development: `.env` file (not committed)
  - Production: Platform environment variables (Railway, Vercel)
  - Variables: JWT_SECRET, DATABASE_URL, CORS_ORIGINS
- **Secrets Management:** Environment variables only, no secrets in code
- **Configuration Files:** 
  - `application.yml` (default)
  - `application-dev.yml` (development)
  - `application-prod.yml` (production)

### Monitoring & Logging
- **Application Monitoring:** Not yet implemented (planned post-launch)
- **Error Tracking:** Not yet implemented (Sentry planned post-launch)
- **Log Aggregation:** Not yet implemented (planned post-launch)
- **Performance Monitoring:** Not yet implemented (planned post-launch)

**Current Approach:** Spring Boot console logs, manual monitoring

---

## 14. Documentation & Resources

### Core Documentation

| Document | Location | Owner | Last Updated |
|----------|----------|-------|--------------|
| Technical Design Document | `EDUCATOR_TDD_v1_1_1.md` | Dev Team | February 7, 2026 |
| Bug Log | `BUG_LOG.md` | Dev Team | February 7, 2026 |
| Project Handover | `PROJECT_HANDOVER.md` (this file) | Dev Team | February 7, 2026 |
| README | `README.md` | Dev Team | February 7, 2026 |
| Sprint 1 Handover | `SPRINT_1_HANDOVER.md` | Dev Team | Sprint 1 End |
| Sprint 2 Handover | `SPRINT_2_HANDOVER.md` | Dev Team | Sprint 2 End |
| Sprint 3.1 Handover | `SPRINT_3_1_HANDOVER.md` | Dev Team | Sprint 3.1 End |
| Sprint 3.2 Handover | `SPRINT_3_2_HANDOVER.md` | Dev Team | Sprint 3.2 End |
| Sprint 4 Handover | `SPRINT_4_HANDOVER.md` | Dev Team | February 7, 2026 |

### Sprint Documentation

All sprint handover documents follow the Sprint Handover Template structure and include:
- Feature tracking (proposed, approved, deferred, rejected)
- Source of truth verification
- Git commit information
- README updates
- Bug log entries
- Sprint closure confirmation
- Project handover details
- Locked decisions
- Safe extension points
- Verification checklists
- Next sprint prompt

**Location:** Project root directory

| Sprint | Document | Status |
|--------|----------|--------|
| Sprint 1 | `SPRINT_1_HANDOVER.md` | ✅ Complete |
| Sprint 2 | `SPRINT_2_HANDOVER.md` | ✅ Complete |
| Sprint 3.1 | `SPRINT_3_1_HANDOVER.md` | ✅ Complete |
| Sprint 3.2 | `SPRINT_3_2_HANDOVER.md` | ✅ Complete |
| Sprint 4 | `SPRINT_4_HANDOVER.md` | ✅ Complete |
| Sprint 5 | `SPRINT_5_HANDOVER.md` | ⏳ To be created |
| Sprint 6 | `SPRINT_6_HANDOVER.md` | ⏳ To be created |
| Sprint 7 | `SPRINT_7_HANDOVER.md` | ⏳ To be created |

### Bug Tracking

**Bug Log:** `BUG_LOG.md`  
**Location:** Project root directory  
**Format:** Append-only, industry-standard template  
**Update Frequency:** After each sprint  
**Entries:** 15 total bugs (Sprint 1: 3, Sprint 2: 1, Sprint 3.1: 6, Sprint 3.2: 1, Sprint 4: 4)  
**Resolution Rate:** 100% (all bugs resolved)

### External Resources
- **Spring Boot Documentation:** https://spring.io/projects/spring-boot
- **React Documentation:** https://react.dev
- **PostgreSQL Documentation:** https://www.postgresql.org/docs/
- **JWT Best Practices:** https://tools.ietf.org/html/rfc7519
- **Material Design (UI Reference):** https://material.io

### API Documentation
API specifications are documented in the Technical Design Document (TDD) Section 7.

**Endpoint Count:** 28 (current), 65+ (planned)

---

## 15. Team & Contacts

### Core Team

#### Project Lead / Product Owner
- **Name:** [To be filled]
- **Email:** [To be filled]
- **Responsibilities:** Product vision, feature prioritization, stakeholder management
- **Availability:** [To be filled]

#### Lead Developer / Backend
- **Name:** [To be filled]
- **Email:** [To be filled]
- **Responsibilities:** Backend architecture, Sprint execution, code reviews, security
- **Availability:** [To be filled]

#### Frontend Developer
- **Name:** [To be filled]
- **Email:** [To be filled]
- **Responsibilities:** React frontend (Sprint 7), UI/UX implementation
- **Availability:** [To be filled]

#### AI Assistant (Development Support)
- **Name:** Claude (Anthropic AI Assistant)
- **Role:** Technical implementation, documentation, sprint deliverables
- **Responsibilities:** Code generation, architecture recommendations, documentation
- **Availability:** On-demand

### Stakeholders

| Name | Role | Email | Involvement Level |
|------|------|-------|-------------------|
| [To be filled] | Business Owner | [To be filled] | High |
| [To be filled] | End User Representative | [To be filled] | Medium |

### Escalation Path
1. **Level 1:** Developer (day-to-day issues, bug fixes)
2. **Level 2:** Lead Developer (architectural decisions, sprint planning)
3. **Level 3:** Project Lead (product decisions, priority changes, stakeholder issues)

---

## 16. Communication Protocols

### Primary Communication Channels
- **Daily Communication:** [To be filled - e.g., Slack, Teams, Email]
- **Code Reviews:** GitHub Pull Requests
- **Project Management:** [To be filled - e.g., Jira, Trello, GitHub Projects]
- **Documentation:** GitHub repository + Markdown files

### Communication Language
- **Primary Language:** English
- **Secondary Language (Optional):** Telugu (Roman script only, no Telugu script characters)
- **Code Comments:** English
- **Documentation:** English
- **Team Chat:** English or Roman Telugu (no Telugu script)

**Note:** From project establishment, bilingual support (English/Roman Telugu) is allowed, but Telugu script (అ ఆ ఇ ఈ) is prohibited in all project communications.

### Meeting Schedule
- **Sprint Planning:** Before each sprint starts
- **Sprint Review:** After sprint completion (deliverables review)
- **Sprint Retrospective:** After sprint completion (process improvement)
- **Daily Standups:** Not required (asynchronous development)

### Communication Rules
- ✅ Respond within reasonable timeframe (1-2 days)
- ✅ No silent changes - confirm before major decisions
- ✅ Always document decisions in appropriate documents (TDD, handover, etc.)
- ✅ Friendly, direct, no unnecessary verbosity
- ✅ No background assumptions - state everything explicitly
- ❌ No undocumented architectural changes
- ❌ No verbal-only agreements
- ❌ No Telugu script characters in any communication

---

## 17. Onboarding Checklist

### For New Team Members

#### Access & Accounts
- [ ] GitHub repository access granted
- [ ] Development environment credentials provided (if applicable)
- [ ] Project management tool access (if applicable)
- [ ] Communication channel access (Slack/Teams/etc.)
- [ ] Database credentials (development environment)

#### Setup
- [ ] Java 17 JDK installed
- [ ] Maven 3.9+ installed
- [ ] Docker installed (for PostgreSQL)
- [ ] IDE configured (IntelliJ IDEA / Eclipse / VS Code)
- [ ] Clone repository from GitHub
- [ ] Run `docker-compose up -d postgres` (start database)
- [ ] Run `mvn spring-boot:run` (start backend)
- [ ] Verify application starts on http://localhost:8080
- [ ] Test authentication endpoints with curl/Postman

#### Knowledge Transfer
- [ ] Read Technical Design Document (`EDUCATOR_TDD_v1_1_1.md`)
- [ ] Review all sprint handover documents (SPRINT_1 through SPRINT_4)
- [ ] Understand architecture and technology stack
- [ ] Review bug log (`BUG_LOG.md`) for historical context
- [ ] Understand locked architectural decisions (cannot be changed)
- [ ] Review this project handover document completely
- [ ] Understand sprint process and deliverables

#### Understanding Key Concepts
- [ ] Infinite hierarchy concept and implementation
- [ ] Materialized path pattern for lesson hierarchy
- [ ] JWT authentication flow
- [ ] URL-based security architecture
- [ ] Soft delete pattern
- [ ] Course status lifecycle
- [ ] Language-first design principle
- [ ] Role-based permissions (Admin/Instructor/Student)

#### First Tasks
- [ ] Review existing code structure
- [ ] Run all existing APIs and understand responses
- [ ] Read through one completed sprint handover in detail
- [ ] Fix a documentation typo or update (low-risk contribution)
- [ ] Add a small feature with guidance (next sprint)

---

## 18. Future Roadmap

### Short-Term (Sprint 5 - Next 1-2 Months)
1. **Enrollment System:** Enable students to enroll in published courses
2. **Progress Tracking:** Track lesson-by-lesson completion with auto-complete
3. **Learner APIs:** Create `/api/learner/**` endpoints for student features
4. **Course Browsing (Student):** Public course catalog filtered by language
5. **Security Update:** Add `/api/learner/**` pattern requiring JWT authentication

### Medium-Term (Sprint 6 - 2-4 Months)
1. **Exam System:** Create, take, and grade exams with multiple question types
2. **Subscription Plans (Admin):** Create and manage 6 subscription plan types
3. **Platform Settings:** Instructor feature toggle and key-value settings
4. **Homepage Sections:** Admin-configurable content sections for dynamic homepage
5. **Rule-Based Automation:** Template-based exam and content generation (non-AI)
6. **User Profile Management:** Profile viewing, editing, password change

### Medium-Term (Sprint 7 - 4-6 Months)
1. **React Frontend:** Complete web UI for all features
2. **Subscription Purchase (Student):** Student-facing subscription flow
3. **Production Deployment:** Deploy to Railway (backend) + Vercel (frontend)
4. **End-to-End Testing:** Automated test suite
5. **CORS Configuration:** Production-ready cross-origin setup
6. **Performance Optimization:** Frontend build optimization

### Long-Term (Post-Sprint 7 - 6-12 Months)
1. **React Native Mobile App:** iOS and Android applications
2. **Payment Gateway Integration:** Razorpay or Stripe for subscriptions
3. **Email Verification:** Email-based account verification
4. **Certificates & Achievements:** Course completion certificates
5. **Analytics Dashboard:** Platform metrics and reporting
6. **Video Upload:** Direct video upload vs. URL-based
7. **Offline Mode:** Mobile app offline content access
8. **Social Features:** Course reviews, ratings, discussions
9. **Instructor Analytics:** Course performance metrics for instructors
10. **Revenue Reports:** Financial dashboard for platform owner

### Deferred Features
| Feature | Reason Deferred | Potential Timeline |
|---------|----------------|-------------------|
| Course Versioning | Complex, unclear MVP requirements | Post-MVP (12+ months) |
| Lesson Reparenting | Complex path recalculation, edge cases | Post-MVP (12+ months) |
| Bulk Operations | Not MVP critical, optimization | Post-MVP (6-12 months) |
| Payment Gateway | Requires revenue model validation | Sprint 8+ |
| Email Verification | Not blocking for MVP | Sprint 8+ |
| Certificates | Not blocking for MVP | Sprint 8+ |
| Mobile Apps | Web-first approach | Sprint 8+ |
| Drag-Drop Reordering | UI enhancement, not core feature | Sprint 7+ (frontend) |

### Technical Improvements Planned
1. **Automated Testing:** Unit tests, integration tests, E2E tests (Sprint 7)
2. **Code Coverage:** Achieve 80% backend, 70% frontend coverage (Sprint 7)
3. **API Versioning:** Add `/v1/` prefix to all endpoints (Sprint 7)
4. **Performance Monitoring:** Integrate Sentry or similar (Post-launch)
5. **Database Indexing:** Optimize queries with proper indexes (Sprint 6-7)
6. **Caching Strategy:** Implement Caffeine cache for frequent queries (Sprint 6-7)
7. **Rate Limiting:** Protect APIs from abuse (Post-launch)
8. **Database Backup:** Automated backup strategy (Post-launch)

---

## Appendix: Document Ecosystem Reference

### Complete Documentation Structure

```
PROJECT DOCUMENTATION ECOSYSTEM
│
├── 📄 PROJECT_HANDOVER.md (This Document)
│   ├── Purpose: High-level overview, current state, onboarding
│   ├── Audience: New team members, stakeholders, management
│   ├── Last Updated: February 7, 2026
│   ├── Version: 1.1.1
│   └── Contains: 18 sections covering all aspects of the project
│
├── 📘 Educator_TDD_v1_1_2.md (Technical Design Document)
│   ├── Purpose: Single source of truth for all technical decisions
│   ├── Audience: Developers, architects, technical leads
│   ├── Last Updated: February 7, 2026
│   ├── Status: Sprint 4 Completed, Sprint 5 Ready
│   └── Contains: Architecture, features, API specs, database schema
│
├── 📋 SPRINT_HANDOVER Documents (Per Sprint)
│   ├── SPRINT_1_HANDOVER.md ✅
│   ├── SPRINT_2_HANDOVER.md ✅
│   ├── SPRINT_3_1_HANDOVER.md ✅
│   ├── SPRINT_3_2_HANDOVER.md ✅
│   ├── SPRINT_4_HANDOVER.md ✅ (Latest - February 7, 2026)
│   ├── Purpose: Complete sprint closure documentation
│   ├── Audience: Current and future developers, project continuity
│   ├── Created: At end of each sprint (mandatory)
│   ├── Template: SPRINT_HANDOVER_TEMPLATE.md (10 mandatory sections)
│   └── Contains:
│       ├── Feature tracking (proposed/approved/deferred/rejected)
│       ├── Git commit info (ready to copy)
│       ├── README updates (ready to paste)
│       ├── Bug log entries (ready to append)
│       ├── Sprint closure confirmation
│       ├── Locked decisions from sprint
│       ├── Verification checklists
│       ├── Next sprint prompt
│       └── Explicit state documentation
│
├── 🐛 BUG_LOG.md
│   ├── Purpose: Append-only bug tracking across all sprints
│   ├── Audience: Developers, QA, project historians
│   ├── Last Updated: February 7, 2026
│   ├── Version: 1.1.1
│   ├── Total Bugs: 15 (All resolved - 100% resolution rate)
│   ├── Format: Sprint-scoped bug IDs (S1-B1, S2-B1, etc.)
│   └── Contains:
│       ├── Bug details (description, severity, status)
│       ├── Root cause analysis
│       ├── Resolution details
│       ├── Prevention strategies
│       └── Bug statistics by sprint
│
└── 📖 README.md
    ├── Purpose: Quick start guide and setup instructions
    ├── Audience: All developers, new team members
    ├── Last Updated: February 7, 2026 (Sprint 4)
    └── Contains: How to run, current features, tech stack
```

### Document Cross-References

**How Documents Reference Each Other:**

```
Educator_TDD_v1_1_2.md (Source of Truth)
 ↓ defines features and architecture
 ↓
PROJECT_HANDOVER.md (Overview)
 ↓ references TDD sections
 ↓ links to sprint handovers
 ↓
SPRINT_X_HANDOVER.md (Sprint Details)
 ↓ verifies alignment with TDD
 ↓ provides bug entries
 ↓ updates project handover
 ↓
BUG_LOG.md (Bug Archive)
 ↓ receives bugs from sprint handovers
 ↓ provides historical bug context
 ↓ 15 total bugs, 100% resolved
 ↓
README.md (Quick Start)
 ↓ updated from sprint handovers
 ↓ reflects current state
```

### When to Use Which Document

| Need | Document | Section |
|------|----------|---------|
| Understand project goals | PROJECT_HANDOVER | Section 1-2 |
| Learn architecture | Educator_TDD_v1_1_2.md | Section 3 |
| See current features | PROJECT_HANDOVER | Section 6 |
| Find API endpoints | Educator_TDD_v1_1_2.md | Section 7 |
| Understand why a decision was made | PROJECT_HANDOVER | Section 7 (Locked Decisions) |
| See what was built in Sprint 4 | SPRINT_4_HANDOVER.md | All sections |
| Find a resolved bug | BUG_LOG.md | Sprint-specific sections |
| Set up development environment | README.md | Setup section |
| Onboard new team member | PROJECT_HANDOVER | Section 17 (Onboarding) |
| Prepare next sprint | SPRINT_4_HANDOVER.md | Section 9 (Next Sprint Prompt) |
| Bug statistics | BUG_LOG.md | Bug Statistics section |
| Sprint history | PROJECT_HANDOVER | Section 9 |

### Documentation Maintenance Checklist

**After EVERY Sprint:**
- [x] Create `SPRINT_X_HANDOVER.md` using template ✅ (Sprint 4 done)
- [x] Append bugs to `BUG_LOG.md` with sprint prefix ✅ (S4-B1 through S4-B4)
- [x] Update `PROJECT_HANDOVER.md` Section 9 (Sprint History) ✅
- [x] Update `PROJECT_HANDOVER.md` Section 10 (Current State) ✅
- [x] Update `README.md` with new features/setup changes ✅
- [x] Update `Educator_TDD_v1_1_2.md` if architectural changes ✅
- [x] Commit all documentation updates to repository ✅
- [x] Verify all cross-references are valid ✅

**Quality Check:**
- [x] All documents use consistent terminology
- [x] Version numbers are updated (1.1.1)
- [x] Dates are current (February 7, 2026)
- [x] No broken internal links
- [x] No outdated information
- [x] All checklists completed where applicable

### Current Documentation Status (Sprint 4)

**✅ Complete & Up-to-Date:**
- PROJECT_HANDOVER.md (this document) - v1.1.1
- Educator_TDD_v1_1_2.md - v1.1.1
- SPRINT_4_HANDOVER.md - Complete
- BUG_LOG.md - v1.1.1, 15 bugs documented
- README.md - Updated with Sprint 4 features

**📊 Statistics:**
- Total Documentation Files: 10+
- Sprint Handovers: 5 (all complete)
- Total Bugs Documented: 15 (100% resolved)
- Total API Endpoints: 28
- Total Database Tables: 5

---

## Version History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0.0 | Sprint 1 End | Dev Team | Initial project handover document |
| 1.0.1 | Sprint 2 End | Dev Team | Updated with Sprint 2 progress |
| 1.0.2 | Sprint 3.1 End | Dev Team | Updated with Sprint 3.1 progress |
| 1.0.3 | Sprint 3.2 End | Dev Team | Updated with Sprint 3.2 progress |
| 1.1.0 | February 7, 2026 | Dev Team | Major update following industry-standard template, Sprint 4 completion |
| 1.1.1 | February 7, 2026 | Dev Team | Added comprehensive details, locked decisions, roadmap |
| 1.1.2 | February 7, 2026 | Dev Team | Enhanced with Document Ecosystem Overview, detailed bug statistics, Appendix section |

---

**END OF PROJECT HANDOVER DOCUMENT**

**Note:** This document is part of a complete documentation ecosystem that includes:
- **Educator_TDD_v1_1_2.md** - Single source of truth for technical decisions
- **Sprint Handover Documents** (5 completed) - Mandatory per-sprint closure documentation
- **BUG_LOG.md** - Append-only bug tracking (15 bugs, 100% resolved)
- **README.md** - Quick start and setup guide

This document is updated after each sprint and whenever major decisions or changes occur. It serves as the primary knowledge transfer document for new team members, stakeholders, and anyone joining the project mid-stream.

For complete documentation structure and relationships, see **Appendix: Document Ecosystem Reference** above.

**Current Status:** Sprint 4 complete (February 7, 2026). Ready for Sprint 5.  
**Next Action:** Review this handover, prepare Sprint 5 environment, begin Sprint 5 implementation (Enrollment + Progress Tracking).

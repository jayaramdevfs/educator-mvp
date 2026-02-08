# Educator Platform - Technical Design Document

**Version:** 1.1.2  
**Date:** February 8, 2026  
**Project Type:** EdTech Solution (Web + Mobile + Cross-Platform)  
**Current Status:** Sprint 5 Completed — Sprint 5 Ready to Begin

---

## Table of Contents

1. [Project Vision & Scope](#1-project-vision--scope)
2. [Technology Stack](#2-technology-stack)
3. [Architecture Overview](#3-architecture-overview)
4. [Sprint Completion History](#4-sprint-completion-history)
5. [Feature Inventory](#5-feature-inventory)
6. [Database Schema](#6-database-schema)
7. [API Specification](#7-api-specification)
8. [Mobile Architecture](#8-mobile-architecture)
9. [Cross-Platform Strategy](#9-cross-platform-strategy)
10. [Deployment Strategy](#10-deployment-strategy)
11. [14 Binding Rules](#11-14-binding-rules)
12. [Locked Architectural Decisions](#12-locked-architectural-decisions)
13. [Sprint Roadmap (Planned Sprints)](#13-sprint-roadmap-planned-sprints)
14. [Project Scope Summary](#14-project-scope-summary)

---

## 1. Project Vision & Scope

### What We're Building

**Educator Platform** - A complete automation-powered EdTech solution competing with Udemy/Coursera/Unacademy

### Target Users

- **Students** - Consume courses, take exams, buy subscriptions
- **Instructors** - Create courses, leverage rule-based automation (when enabled by admin)
- **Admins** - Manage platform, users, hierarchy, subscriptions, control instructor feature

### Platforms

- **Web Application** (React + Vite)
- **Mobile App** (React Native - iOS + Android) — planned
- **Cross-Platform Logic** (Shared business logic between web and mobile)

### Core Capabilities

- Infinite customizable hierarchy (Category > Custom Levels > Courses/Exams)
- Multi-language support (English base, on-the-fly translation)
- Course management (lessons, videos, documents)
- Exam system (timed tests, instant results, MCQ/True-False/Essay)
- User management (students, instructors, admins)
- Subscription plans (6 plan types, admin-managed)
- Content delivery (streaming, downloads)
- Learner progress tracking & enrollment lifecycle
- Payment integration (subscriptions, one-time purchases)
- Analytics & reporting
- Certificates & achievements
- **Rule-based exam generation** (auto-create exams from topics using templates and rules)
- **Rule-based content fetching** (auto-fetch and structure course content using predefined rules)
- **Admin control over instructor feature** (enable/disable)
- Admin can always act as instructor (regardless of toggle)

### Zero-Cost Philosophy

- Free tiers for all services until revenue starts
- No paid APIs, databases, hosting during development
- Rule-based automation uses no external paid APIs
- Can add paid services after user base grows

---

## 2. Technology Stack

### Backend (Single API for Both Platforms)

| Component | Technology | Version | Why |
|-----------|-----------|---------|-----|
| Framework | Spring Boot | 4.0.2 | Production-ready, enterprise-grade |
| Language | Java | 17 | Type-safe, scalable |
| Database | PostgreSQL | 15 | Free, powerful, JSON support |
| ORM | JPA/Hibernate | - | Database abstraction |
| Authentication | JWT | jjwt 0.11.5 | Stateless auth for mobile/web |
| Security | Spring Security | 6 | Industry standard |
| Build | Maven | - | Dependency management |
| Automation | Rule-Based Engine | Internal | Exam generation, content automation (template + rule driven) |

### Web Frontend

| Component | Technology | Version | Why |
|-----------|-----------|---------|-----|
| Framework | React | 18+ | Component-based, large ecosystem |
| Build Tool | Vite | 5+ | Fast dev server, modern bundler |
| Routing | React Router | 6+ | SPA navigation |
| State | Context API | - | Start simple, scale if needed |
| HTTP Client | Axios | - | Request interceptors for JWT |
| Styling | CSS Modules / Tailwind | - | Scoped styles, utility-first |

### Mobile App

| Component | Technology | Version | Why |
|-----------|-----------|---------|-----|
| Framework | React Native | 0.73+ | Cross-platform iOS + Android |
| Navigation | React Navigation | 6+ | Native navigation feel |
| State | Context API | - | Code sharing with web |
| HTTP Client | Axios | - | Same API client as web |
| Local Storage | AsyncStorage | - | Offline data |

### DevOps & Deployment

| Component | Technology | Cost | When |
|-----------|-----------|------|------|
| Version Control | GitHub | Free | Active |
| Backend Hosting | Railway (free tier) | Free | Post Sprint 7 |
| Database Hosting | Railway PostgreSQL | Free | Post Sprint 7 |
| Web Hosting | Vercel | Free | Post Sprint 7 |
| Automation | Rule-Based Engine (internal) | Free | Sprint 6 (schema-ready) |

---

## 3. Architecture Overview

### System Architecture

```
                    FRONTEND LAYER
Web App (React)              Mobile App (React Native)
- Browser-based              - iOS + Android
- Desktop/Tablet             - Native feel
- Content Review UI          - Content Review UI
      |                            |
      |       HTTPS/REST           |
      |                            |
            BACKEND API LAYER
Spring Boot REST API
- JWT Authentication
- Role-based Authorization (URL-based)
- Rule-Based Automation Service (pluggable)
- Content Generation Queue (rule-driven)
- Platform Settings Service (Instructor Feature Toggle)
- Subscription Validation Service
     |            |            |            |
PostgreSQL   File Storage  Rule Engine  Cache(Caffeine)
```

### Rule-Based Content Pipeline

```
User Request → Rule Engine → Template Processing → Generate Content → Draft → Admin Review → Publish
```

### Backend Package Structure (Current)

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

---

## 4. Sprint Completion History

### Sprint 1 — Authentication & Security Foundation

**Status:** ✅ COMPLETED

#### Features Delivered

- User registration API (`POST /api/auth/register`)
- User login API (`POST /api/auth/login`)
- BCrypt password hashing
- JWT-based stateless authentication
- Role system: STUDENT, INSTRUCTOR, ADMIN (auto-seeded at startup)
- Default role on registration: STUDENT
- Spring Security: CSRF disabled, form login disabled, HTTP Basic disabled
- PostgreSQL running in Docker, UTC-safe timezone config

#### Database Tables Created

- `users` (id, email, password)
- `roles` (id, name)
- `user_roles` (user_id, role_id)

#### Bugs Resolved

- **S1-B1:** PostgreSQL timezone error ("Asia/Calcutta" rejected) — fixed with UTC override
- **S1-B2:** Hibernate dialect detection failure — fixed with explicit PostgreSQL dialect
- **S1-B3:** Spring Security default login page popup — fixed by disabling form login & HTTP Basic

---

### Sprint 2 — Infinite Hierarchy Backend

**Status:** ✅ COMPLETED

#### Features Delivered

- Infinite self-referencing hierarchy with unlimited depth
- Slug-based identifiers for stable routing and future SEO
- Localization-ready fields (English base: nameEn, descriptionEn)
- Versioning (@Version), visibility flags, soft delete
- Audit fields (createdAt, updatedAt, createdBy)
- Deterministic ordering (sortOrder)
- Admin APIs for managing hierarchy nodes
- Public read-only APIs for browsing hierarchy

#### Database Tables Created

- `hierarchy_nodes` (id, parent_id, slug, name_en, description_en, sort_order, version, is_published, is_visible, is_deleted, created_at, updated_at, created_by)

#### APIs Delivered

- `POST /api/admin/hierarchy` — Create node
- `GET /api/public/hierarchy` — Browse hierarchy tree

#### Bugs Resolved

- **S2-B1:** 403 Forbidden on admin hierarchy endpoints — fixed by permitting `/api/admin/hierarchy/**` temporarily

---

### Sprint 3.1 — Core Course & Lesson Domain Foundation

**Status:** ✅ COMPLETED

#### Features Delivered

- Course entity: hierarchy attachment, title/description (English base), status lifecycle (DRAFT/PUBLISHED/ARCHIVED), difficulty (BEGINNER/INTERMEDIATE/ADVANCED), language code, estimated duration, created-by role, soft delete, versioning, audit timestamps
- Lesson entity: course attachment, type (TEXT/VIDEO/DOCUMENT), text content, video URL, document URL, soft delete, ordering
- CourseRepository and LessonRepository with safe JPA queries
- CourseService with business rule enforcement
- Initial admin CourseController

#### Database Tables Created

- `courses` (id, hierarchy_node_id, title_en, description_en, status, difficulty, language_code, estimated_duration_minutes, created_by_role, is_archived, is_deleted, sort_order, version, created_at, updated_at)
- `lessons` (id, course_id, parent_lesson_id, path, depth_level, type, order_index, text_content, video_url, document_url, is_deleted, created_at, updated_at)

#### Bugs Resolved

- Hibernate ExceptionInInitializerError (non-null fields without defaults)
- Lombok incompatibility with unsupported JDK versions
- Illegal manual assignment of JPA entity IDs
- Package-path mismatches causing compilation failures
- Missing setters in domain entities

---

### Sprint 3.2 — Lesson APIs

**Status:** ✅ COMPLETED

#### Features Delivered

- Lesson creation APIs for Admin / Instructor
- Public lesson retrieval APIs for Students
- Deterministic lesson ordering using orderIndex
- Soft delete enforcement at service layer
- Lesson language inherits from Course

#### APIs Delivered

- `POST /api/admin/lessons/course/{courseId}` — Create lesson
- `DELETE /api/admin/lessons/{lessonId}` — Soft delete lesson
- `GET /api/public/lessons/course/{courseId}` — List root lessons

#### Bugs Resolved

- Potential duplication of LessonService logic — verified existing implementation and avoided unnecessary changes

---

### Sprint 4 — Infinite Lesson Hierarchy + Admin Course APIs

**Status:** ✅ COMPLETED

#### Features Delivered

##### 4.1 Infinite Lesson Hierarchy

- Parent-child relationships for lessons
- Path-based hierarchical storage (e.g., "/1/2/5")
- Depth-level calculation
- Nested lesson retrieval (get children of any lesson)
- Subtree deletion (soft delete lesson + descendants)

##### 4.2 Admin Course Management APIs

- `GET /api/admin/courses` — List all courses with pagination
- `GET /api/admin/courses/{courseId}` — Get single course
- `PUT /api/admin/courses/{courseId}` — Update course
- `DELETE /api/admin/courses/{courseId}` — Soft delete course
- `POST /api/admin/courses/{courseId}/publish` — Publish course
- `POST /api/admin/courses/{courseId}/archive` — Archive course

##### 4.3 Admin Lesson APIs (Hierarchy-Aware)

- `POST /api/admin/lessons/{parentLessonId}/children` — Create child lesson
- `GET /api/admin/lessons/{lessonId}/children` — Get direct children
- `DELETE /api/admin/lessons/{lessonId}` — Soft delete lesson + subtree
- `PUT /api/admin/lessons/{lessonId}` — Update lesson

##### 4.4 Security Configuration Update

- Added `/api/admin/**` pattern requiring `ADMIN` role
- JWT filter applied globally
- Public endpoints remain open
- Admin authentication enforced

#### New APIs

- 6 course admin endpoints
- 4 lesson hierarchy endpoints

#### Database Changes

- `lessons.parent_lesson_id` (foreign key)
- `lessons.path` (hierarchical path string)
- `lessons.depth_level` (integer depth)

---

## 5. Feature Inventory

### Sprint 5 — Enrollment + Progress Tracking

**Status:** 🟡 PLANNED (Ready to Begin)

#### Scope

##### 5.1 Enrollment Management

- Students enroll in courses
- Admin views all enrollments
- Student views their enrollments
- Unenroll from course
- Enrollment status tracking (ACTIVE / COMPLETED / DROPPED)

##### 5.2 Lesson Progress Tracking

- Track individual lesson completion
- Completion timestamp
- Auto-complete enrollment when all lessons done
- Progress percentage calculation
- Last accessed lesson tracking

##### 5.3 Enrollment APIs

- Browse published courses (learner-facing)
- Get single course with progress
- Enroll in course
- Unenroll from course
- Get my enrollments
- Get progress for a course

##### 5.4 Security Config Update

- Add `/api/learner/**` pattern requiring JWT authentication
- No changes to existing `/api/admin/**` or `/api/public/**` patterns

#### New Tables

- `enrollments`
- `lesson_progress`

#### New API Endpoints (~9)

- 4 enrollment endpoints (`/api/learner/enrollments/...`)
- 3 progress endpoints (`/api/learner/progress/...`)
- 2 course browsing endpoints (`/api/learner/courses/...`)

#### Constraints

- No changes to Sprint 4 security decisions
- No schema-breaking changes
- Learner APIs are read-only w.r.t. course/lesson structure
- Progress tracking is additive and non-destructive
- Enrollment as domain authorization (not role-based gating)

---

## 6. Database Schema

### Current Tables (Sprint 1-4)

#### users
```sql
id BIGSERIAL PRIMARY KEY
email VARCHAR(255) UNIQUE NOT NULL
password VARCHAR(255) NOT NULL
created_at TIMESTAMP
updated_at TIMESTAMP
```

#### roles
```sql
id BIGSERIAL PRIMARY KEY
name VARCHAR(50) UNIQUE NOT NULL
```

#### user_roles
```sql
user_id BIGINT REFERENCES users(id)
role_id BIGINT REFERENCES roles(id)
PRIMARY KEY (user_id, role_id)
```

#### hierarchy_nodes
```sql
id BIGSERIAL PRIMARY KEY
parent_id BIGINT REFERENCES hierarchy_nodes(id)
slug VARCHAR(255) UNIQUE NOT NULL
name_en VARCHAR(500) NOT NULL
description_en TEXT
sort_order INTEGER NOT NULL
version INTEGER
is_published BOOLEAN DEFAULT FALSE
is_visible BOOLEAN DEFAULT TRUE
is_deleted BOOLEAN DEFAULT FALSE
created_at TIMESTAMP
updated_at TIMESTAMP
created_by VARCHAR(255)
```

#### courses
```sql
id BIGSERIAL PRIMARY KEY
hierarchy_node_id BIGINT REFERENCES hierarchy_nodes(id)
title_en VARCHAR(500) NOT NULL
description_en TEXT
status VARCHAR(20) NOT NULL -- DRAFT, PUBLISHED, ARCHIVED
difficulty VARCHAR(20) -- BEGINNER, INTERMEDIATE, ADVANCED
language_code VARCHAR(10)
estimated_duration_minutes INTEGER
created_by_role VARCHAR(50)
is_archived BOOLEAN DEFAULT FALSE
is_deleted BOOLEAN DEFAULT FALSE
sort_order INTEGER
version INTEGER
created_at TIMESTAMP
updated_at TIMESTAMP
```

#### lessons
```sql
id BIGSERIAL PRIMARY KEY
course_id BIGINT NOT NULL REFERENCES courses(id)
parent_lesson_id BIGINT REFERENCES lessons(id)
path VARCHAR(500)
depth_level INTEGER
type VARCHAR(20) NOT NULL -- TEXT, VIDEO, DOCUMENT
order_index INTEGER NOT NULL
text_content TEXT
video_url VARCHAR(1000)
document_url VARCHAR(1000)
is_deleted BOOLEAN DEFAULT FALSE
created_at TIMESTAMP
updated_at TIMESTAMP
```

### Planned Tables (Sprint 5)

#### enrollments
```sql
id BIGSERIAL PRIMARY KEY
user_id BIGINT NOT NULL REFERENCES users(id)
course_id BIGINT NOT NULL REFERENCES courses(id)
status VARCHAR(20) NOT NULL -- ACTIVE, COMPLETED, DROPPED
enrolled_at TIMESTAMP
completed_at TIMESTAMP
last_accessed_at TIMESTAMP
UNIQUE (user_id, course_id)
```

#### lesson_progress
```sql
id BIGSERIAL PRIMARY KEY
enrollment_id BIGINT NOT NULL REFERENCES enrollments(id)
lesson_id BIGINT NOT NULL REFERENCES lessons(id)
completed BOOLEAN DEFAULT FALSE
completed_at TIMESTAMP
UNIQUE (enrollment_id, lesson_id)
```

### Planned Tables (Sprint 6)

#### subscription_plans
```sql
id BIGSERIAL PRIMARY KEY
name VARCHAR(255) NOT NULL
description TEXT
plan_type VARCHAR(50) NOT NULL -- MONTHLY, QUARTERLY, ANNUAL, LIFETIME, TRIAL, PAY_PER_COURSE
price DECIMAL(10,2) NOT NULL
duration_days INTEGER -- 0 for lifetime
is_active BOOLEAN DEFAULT TRUE
created_at TIMESTAMP
updated_at TIMESTAMP
```

#### subscription_plan_items
```sql
id BIGSERIAL PRIMARY KEY
plan_id BIGINT NOT NULL REFERENCES subscription_plans(id)
course_id BIGINT REFERENCES courses(id)
exam_id BIGINT -- Future reference
item_type VARCHAR(20) NOT NULL -- COURSE, EXAM
```

#### user_subscriptions
```sql
id BIGSERIAL PRIMARY KEY
user_id BIGINT NOT NULL REFERENCES users(id)
plan_id BIGINT NOT NULL REFERENCES subscription_plans(id)
status VARCHAR(20) NOT NULL -- ACTIVE, EXPIRED, CANCELLED
subscribed_at TIMESTAMP
expires_at TIMESTAMP
cancelled_at TIMESTAMP
```

#### sections
```sql
id BIGSERIAL PRIMARY KEY
name_en VARCHAR(255) NOT NULL
description_en TEXT
section_type VARCHAR(50) NOT NULL -- COURSES, EXAMS, MIXED, CURRENT_AFFAIRS
sort_order INTEGER NOT NULL
is_active BOOLEAN DEFAULT TRUE
created_at TIMESTAMP
updated_at TIMESTAMP
```

#### section_items
```sql
id BIGSERIAL PRIMARY KEY
section_id BIGINT NOT NULL REFERENCES sections(id)
content_id BIGINT NOT NULL
content_type VARCHAR(20) NOT NULL -- COURSE, EXAM
sort_order INTEGER NOT NULL
created_at TIMESTAMP
```

#### exams
```sql
id BIGSERIAL PRIMARY KEY
hierarchy_node_id BIGINT REFERENCES hierarchy_nodes(id)
course_id BIGINT REFERENCES courses(id)
title_en VARCHAR(500) NOT NULL
description_en TEXT
duration_minutes INTEGER NOT NULL
passing_score_percentage INTEGER NOT NULL
status VARCHAR(20) NOT NULL -- DRAFT, PUBLISHED, ARCHIVED
created_at TIMESTAMP
updated_at TIMESTAMP
```

#### questions
```sql
id BIGSERIAL PRIMARY KEY
exam_id BIGINT NOT NULL REFERENCES exams(id)
question_text TEXT NOT NULL
question_type VARCHAR(20) NOT NULL -- MCQ, TRUE_FALSE, ESSAY
points INTEGER NOT NULL
order_index INTEGER NOT NULL
```

#### question_options
```sql
id BIGSERIAL PRIMARY KEY
question_id BIGINT NOT NULL REFERENCES questions(id)
option_text TEXT NOT NULL
is_correct BOOLEAN DEFAULT FALSE
order_index INTEGER NOT NULL
```

#### exam_attempts
```sql
id BIGSERIAL PRIMARY KEY
user_id BIGINT NOT NULL REFERENCES users(id)
exam_id BIGINT NOT NULL REFERENCES exams(id)
started_at TIMESTAMP
submitted_at TIMESTAMP
score DECIMAL(5,2)
percentage DECIMAL(5,2)
passed BOOLEAN
```

#### exam_answers
```sql
id BIGSERIAL PRIMARY KEY
attempt_id BIGINT NOT NULL REFERENCES exam_attempts(id)
question_id BIGINT NOT NULL REFERENCES questions(id)
selected_option_id BIGINT REFERENCES question_options(id)
essay_answer TEXT
is_correct BOOLEAN
```

#### platform_settings
```sql
id BIGSERIAL PRIMARY KEY
setting_key VARCHAR(100) UNIQUE NOT NULL
setting_value TEXT NOT NULL
description TEXT
updated_at TIMESTAMP
```

#### automation_jobs
```sql
id BIGSERIAL PRIMARY KEY
job_type VARCHAR(50) NOT NULL -- EXAM_GENERATION, CONTENT_FETCH
status VARCHAR(20) NOT NULL -- PENDING, PROCESSING, COMPLETED, FAILED
input_params TEXT -- JSON
output_data TEXT -- JSON
created_at TIMESTAMP
completed_at TIMESTAMP
```

#### automation_templates
```sql
id BIGSERIAL PRIMARY KEY
template_name VARCHAR(255) NOT NULL
template_type VARCHAR(50) NOT NULL
template_content TEXT NOT NULL
is_active BOOLEAN DEFAULT TRUE
created_at TIMESTAMP
updated_at TIMESTAMP
```

#### content_review_queue
```sql
id BIGSERIAL PRIMARY KEY
content_type VARCHAR(50) NOT NULL -- EXAM, COURSE
content_id BIGINT NOT NULL
status VARCHAR(20) NOT NULL -- PENDING, APPROVED, REJECTED
submitted_at TIMESTAMP
reviewed_at TIMESTAMP
reviewed_by BIGINT REFERENCES users(id)
review_notes TEXT
```

---

## 7. API Specification

### Authentication APIs

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| POST | `/api/auth/register` | ❌ | Public | Register new user |
| POST | `/api/auth/login` | ❌ | Public | Login user |

### Hierarchy APIs

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| POST | `/api/admin/hierarchy` | ✅ | ADMIN | Create hierarchy node |
| GET | `/api/public/hierarchy` | ❌ | Public | Browse hierarchy tree |

### Course APIs (Admin)

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| POST | `/api/admin/courses` | ✅ | ADMIN | Create course |
| GET | `/api/admin/courses` | ✅ | ADMIN | List all courses (paginated) |
| GET | `/api/admin/courses/{id}` | ✅ | ADMIN | Get single course |
| PUT | `/api/admin/courses/{id}` | ✅ | ADMIN | Update course |
| DELETE | `/api/admin/courses/{id}` | ✅ | ADMIN | Soft delete course |
| POST | `/api/admin/courses/{id}/publish` | ✅ | ADMIN | Publish course |
| POST | `/api/admin/courses/{id}/archive` | ✅ | ADMIN | Archive course |

### Lesson APIs (Admin)

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| POST | `/api/admin/lessons/course/{courseId}` | ✅ | ADMIN | Create root lesson |
| POST | `/api/admin/lessons/{parentId}/children` | ✅ | ADMIN | Create child lesson |
| GET | `/api/admin/lessons/{id}/children` | ✅ | ADMIN | Get direct children |
| PUT | `/api/admin/lessons/{id}` | ✅ | ADMIN | Update lesson |
| DELETE | `/api/admin/lessons/{id}` | ✅ | ADMIN | Delete lesson + subtree |

### Lesson APIs (Public)

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| GET | `/api/public/lessons/course/{courseId}` | ❌ | Public | List root lessons |

### Enrollment APIs (Sprint 5 - Planned)

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| GET | `/api/learner/courses` | ✅ | Student | Browse published courses |
| GET | `/api/learner/courses/{id}` | ✅ | Student | Get course with progress |
| POST | `/api/learner/enrollments` | ✅ | Student | Enroll in course |
| DELETE | `/api/learner/enrollments/{id}` | ✅ | Student | Unenroll from course |
| GET | `/api/learner/enrollments` | ✅ | Student | Get my enrollments |

### Progress APIs (Sprint 5 - Planned)

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| POST | `/api/learner/progress` | ✅ | Student | Mark lesson complete |
| GET | `/api/learner/progress/course/{courseId}` | ✅ | Student | Get course progress |
| GET | `/api/learner/progress/enrollment/{enrollmentId}` | ✅ | Student | Get enrollment progress |

### Subscription APIs - Admin (Sprint 6 - Planned)

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| POST | `/api/admin/subscriptions/plans` | ✅ | ADMIN | Create plan |
| GET | `/api/admin/subscriptions/plans` | ✅ | ADMIN | List all plans |
| PUT | `/api/admin/subscriptions/plans/{id}` | ✅ | ADMIN | Update plan |
| DELETE | `/api/admin/subscriptions/plans/{id}` | ✅ | ADMIN | Delete plan |
| POST | `/api/admin/subscriptions/plans/{id}/items` | ✅ | ADMIN | Add item to plan |
| DELETE | `/api/admin/subscriptions/items/{id}` | ✅ | ADMIN | Remove item from plan |

### Subscription APIs - Learner (Sprint 7 - Planned)

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| GET | `/api/learner/subscriptions/plans` | ✅ | Student | Browse active plans |
| POST | `/api/learner/subscriptions` | ✅ | Student | Purchase/activate plan |
| GET | `/api/learner/subscriptions` | ✅ | Student | Get my subscriptions |
| DELETE | `/api/learner/subscriptions/{id}` | ✅ | Student | Cancel subscription |

### Section APIs - Admin (Sprint 6 - Planned)

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| POST | `/api/admin/sections` | ✅ | ADMIN | Create section |
| GET | `/api/admin/sections` | ✅ | ADMIN | List all sections |
| PUT | `/api/admin/sections/{id}` | ✅ | ADMIN | Update section |
| DELETE | `/api/admin/sections/{id}` | ✅ | ADMIN | Delete section |
| POST | `/api/admin/sections/{id}/items` | ✅ | ADMIN | Add item to section |

### Section APIs - Public (Sprint 6 - Planned)

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| GET | `/api/public/sections` | ❌ | Public | Get all active sections with items |

---

## 8. Mobile Architecture

### React Native App Structure (Planned)

```
mobile/
├── src/
│   ├── api/           (Axios client, same as web)
│   ├── auth/          (Auth context, JWT storage)
│   ├── navigation/    (React Navigation setup)
│   ├── screens/
│   │   ├── auth/      (Login, Register)
│   │   ├── student/   (Dashboard, Courses, Exams)
│   │   ├── admin/     (Admin dashboard)
│   │   └── common/    (Profile, Settings)
│   ├── components/    (Reusable UI components)
│   ├── hooks/         (Custom hooks)
│   └── utils/         (Helpers, constants)
├── android/
├── ios/
└── package.json
```

### Key Mobile Features

- Native iOS and Android builds
- Same API endpoints as web
- JWT token stored in AsyncStorage
- Offline lesson caching (future)
- Push notifications (future)
- Biometric authentication (future)

---

## 9. Cross-Platform Strategy

### Shared Code Approach

- **Business Logic:** Shared service layer (TypeScript)
- **API Client:** Same Axios configuration
- **Authentication:** Same JWT flow
- **State Management:** Same Context API patterns
- **Type Definitions:** Shared TypeScript interfaces

### Platform-Specific

- **Web:** React DOM, CSS Modules/Tailwind
- **Mobile:** React Native components, Native styling

---

## 10. Deployment Strategy

### Development Environment

- **Backend:** Local Spring Boot (port 8080)
- **Frontend:** Vite dev server (port 5173)
- **Database:** Docker PostgreSQL (port 5432)

### Production Environment (Post Sprint 7)

#### Backend

- **Platform:** Railway
- **Database:** Railway PostgreSQL
- **Environment:** Production profile
- **CORS:** Configured for Vercel domain
- **SSL:** Automatic HTTPS

#### Frontend

- **Platform:** Vercel
- **Build:** `vite build`
- **Environment Variables:** API_URL pointing to Railway
- **CDN:** Automatic via Vercel

### Deployment Checklist

- [ ] Production application-prod.yml configured
- [ ] CORS updated for production domains
- [ ] Environment variables set on Railway/Vercel
- [ ] Database migrations tested
- [ ] API endpoints verified
- [ ] Frontend build optimized
- [ ] Security headers configured
- [ ] Monitoring setup

---

## 11. 14 Binding Rules

### 1. Zero External Paid APIs
No paid services during development. Rule-based automation is internal only.

### 2. JWT Everywhere
All authenticated requests use JWT tokens. No sessions.

### 3. Soft Delete Only
Never hard delete data. Use `is_deleted` flags.

### 4. English First
All content fields use `_en` suffix. Multi-language is future extension.

### 5. Slug-Based Routing
Hierarchy nodes use slugs for stable URLs.

### 6. Role-Based URL Patterns
Security uses URL patterns (`/api/admin/**`, `/api/learner/**`, `/api/public/**`)

### 7. Subscription Validation
Enrollment requires active subscription (configurable per plan).

### 8. Admin Controls Instructor Feature
Platform setting toggles instructor capabilities. Admin always has access.

### 9. Infinite Hierarchy Depth
No artificial limits on hierarchy or lesson nesting.

### 10. Path-Based Lesson Storage
Lessons use materialized path pattern for efficient queries.

### 11. Deterministic Ordering
All lists use explicit `sort_order` or `order_index` fields.

### 12. Audit Everything
All entities have `created_at`, `updated_at`, `created_by` where applicable.

### 13. Status Lifecycle
Courses and exams use explicit status transitions (DRAFT → PUBLISHED → ARCHIVED).

### 14. Review Before Publish
Auto-generated content goes to review queue before publishing.

---

## 12. Locked Architectural Decisions

### Security Architecture (Sprint 4)
✅ **LOCKED** - Cannot be changed without major refactor

- JWT-based stateless authentication
- URL pattern-based authorization
- Three security tiers: Public, Learner (JWT required), Admin (ADMIN role required)
- No session management
- CSRF disabled for REST API

### Database Schema (Sprint 1-4)
✅ **LOCKED** - Core tables finalized

- PostgreSQL with JPA/Hibernate
- Soft delete pattern across all entities
- Audit fields standardized
- Path-based lesson hierarchy
- Self-referencing hierarchy_nodes

### API Structure (Sprint 1-4)
✅ **LOCKED** - Endpoint patterns established

- `/api/auth/**` - Authentication
- `/api/public/**` - Public access
- `/api/admin/**` - Admin only
- `/api/learner/**` - Authenticated students
- RESTful conventions
- JSON request/response

---

## 13. Sprint Roadmap (Planned Sprints)

### Sprint 6 — Exam System + Subscription Plans + Platform Settings + Homepage Sections + Rule-Based Automation + User Profile

**Status:** 🔵 PLANNED

#### Scope

##### 6.1 Full Exam System

- Admin creates exams (standalone or course-attached)
- Question types: MCQ, True/False, Essay
- Question options with correct answer marking
- Timed exams (duration in minutes)
- Exam lifecycle: DRAFT → PUBLISHED → ARCHIVED
- Student starts exam attempt (creates timed session)
- Student submits exam
- Instant auto-grading for MCQ and True/False
- Essay questions flagged for manual review
- Pass/fail based on passing score percentage
- Result display (score, percentage, passed, per-question breakdown)
- Subscription validation for exam access

##### 6.2 Subscription Plans — Admin Management

- Create subscription plans (MONTHLY / QUARTERLY / ANNUAL / LIFETIME / TRIAL / PAY_PER_COURSE)
- Plan items (courses/exams attached to plans)
- Plan lifecycle (ACTIVE / INACTIVE)
- Plan pricing and duration (days, 0 = lifetime)
- Admin CRUD operations for plans
- Attach/detach courses and exams to/from plans

##### 6.3 Admin-Configurable Homepage Sections

- Admin creates/updates/deletes homepage sections
- Section types: COURSES, EXAMS, MIXED, CURRENT_AFFAIRS
- Reorder sections using sort_order
- Attach courses and exams to sections
- Each section can contain multiple items
- Public API to fetch sections for homepage rendering
- Sections are presentation-layer aggregations (separate from hierarchy)

##### 6.4 Platform Settings

- Admin can enable/disable instructor feature
- Admin always has instructor privileges (regardless of toggle)
- Instructor registration blocked when feature disabled
- Existing instructors cannot create content when feature disabled
- Settings persistence in database (key-value store)
- Settings API endpoints

##### 6.5 Rule-Based Content Automation (Architecture + Schema)

- Automation job tracking entity (type, status, input params)
- Automation templates entity (reusable templates for exam/content generation)
- Content review queue entity (pending/approved/rejected workflow)
- Admin APIs for managing templates and review queue
- Pluggable automation service interface (can swap engine later)
- Stub implementation for now (returns template-based mock data)

##### 6.6 User Profile Management

- Profile viewing (get my profile)
- Profile editing (update name, email)
- Password change (old password + new password)
- Add firstName, lastName fields to User entity

##### 6.7 User Entity Enhancement

- Add firstName, lastName, enabled, accountStatus, lastLoginAt to User entity

#### New Tables

- `subscription_plans`
- `subscription_plan_items`
- `user_subscriptions`
- `sections`
- `section_items`
- `exams`
- `questions`
- `question_options`
- `exam_attempts`
- `exam_answers`
- `platform_settings`
- `automation_jobs`
- `automation_templates`
- `content_review_queue`

#### New API Endpoints (~36)

- 6 subscription admin endpoints (`/api/admin/subscriptions/...`)
- 5 section admin endpoints (`/api/admin/sections/...`)
- 1 section public endpoint (`/api/public/sections`)
- 9 exam admin endpoints (`/api/admin/exams/...`)
- 2 exam public endpoints (`/api/public/exams/...`)
- 4 exam learner endpoints (`/api/learner/exams/...`)
- 3 platform settings endpoints (`/api/admin/settings/...`)
- 4 automation admin endpoints (`/api/admin/automation/...`)
- 3 user profile endpoints (`/api/learner/profile/...`)

---

### Sprint 7 — React Web Frontend + Testing + Deployment

**Status:** 🔵 PLANNED

#### Scope

##### 7.1 React + Vite Web Application

- Project setup (React 18, Vite, React Router, Axios, Tailwind CSS)
- Axios interceptor for JWT token management
- Auth context for global authentication state

##### 7.2 Authentication Pages

- Login page
- Registration page
- JWT token storage and auto-refresh

##### 7.3 Public Pages

- Home page (platform overview)
- Course catalog (browse published courses)
- Exam list (browse published exams)

##### 7.4 Student Dashboard

- My enrollments (active, completed, dropped)
- Course detail page (with enrollment + progress)
- Lesson viewer (text, video, document)
- Exam list and exam detail
- Take exam page (timed, question navigation, submit)
- Exam result page (score, pass/fail, breakdown)
- My subscriptions (active, expired)
- Browse subscription plans + purchase/activate plan
- Subscription validation & enforcement
- Cancel subscription
- Profile page (view, edit, change password)
- Progress dashboard

##### 7.5 Admin Dashboard

- Admin overview/stats
- Course management (create, publish, archive, delete)
- Lesson management (create, hierarchy, reorder)
- Exam management (create, questions, publish)
- Hierarchy management (create, browse, reorder)
- Subscription plan management (create, items, activate/deactivate)
- Homepage section management (create, reorder, attach content)
- Platform settings (instructor feature toggle)
- Automation templates management
- Content review queue

##### 7.6 End-to-End Testing

- Full API verification (all endpoints)
- Frontend integration testing
- Security testing (JWT, unauthorized access)
- Enrollment + progress flow
- Subscription + exam flow
- Admin workflow testing

##### 7.7 Deployment Preparation

- Backend application-prod.yml for production configs
- Frontend build optimization (`vite build`)
- CORS configuration for production domains
- Environment variable setup
- Production PostgreSQL configuration
- Vercel deployment config (frontend)
- Railway deployment config (backend)

#### Deliverable

- Fully functional web application consuming all backend APIs
- Production-deployed backend + frontend
- All features tested and verified
- Platform ready for live users

---

## 14. Project Scope Summary

### What Will Be Delivered After Sprint 7

| Category | Features | Status |
|----------|----------|--------|
| Authentication | Register, login, JWT, roles, profile, password change | Sprint 1 + 6 |
| Hierarchy | Infinite depth, slug-based, admin CRUD, public browse | Sprint 2 |
| Courses | CRUD, lifecycle, difficulty, language, hierarchy attachment | Sprint 3.1 |
| Lessons | Infinite hierarchy, TEXT/VIDEO/DOCUMENT, path+depth, admin+public APIs | Sprint 3.2 + 4 |
| Security | JWT filter, URL-based admin auth, stateless sessions | Sprint 4 |
| Enrollment | Enroll/unenroll, progress tracking, auto-complete | Sprint 5 |
| Subscriptions | 6 plan types, admin config (Sprint 6), student purchase/enforcement (Sprint 7) | Sprint 6 + 7 |
| Homepage Sections | Admin-configurable content sections, reorderable, presentation layer | Sprint 6 |
| Exams | Create, MCQ/TF/Essay, timed, submit, auto-grade, results | Sprint 6 |
| Platform Settings | Instructor feature toggle, key-value settings | Sprint 6 |
| Automation | Rule-based templates, job tracking, content review queue | Sprint 6 |
| User Profile | View, edit, password change | Sprint 6 |
| Web Frontend | React + Vite, all admin + student pages, full UI | Sprint 7 |
| Deployment | Production-ready backend + frontend, live platform | Sprint 7 |

### What Is Deferred (Not in Sprint 5-7)

- React Native mobile app (separate phase)
- Actual payment gateway integration (Razorpay/Stripe)
- Email verification
- Certificates & achievements
- Offline mode
- Video/document file upload (currently URL-based)
- Analytics dashboard
- Revenue reports
- Drag-drop hierarchy reordering
- Social features (reviews, ratings, discussions)

### Total Project Metrics After Sprint 7

| Metric | Count |
|--------|-------|
| Backend Sprints | 7 (1-6) |
| Frontend Sprints | 1 (7) |
| Database Tables | ~20+ |
| API Endpoints | ~65+ |
| React Pages | ~25+ |
| Platforms | Web (React) |

---

## END OF TECHNICAL DESIGN DOCUMENT v1.1.1

**Current Status:** Sprint 4 complete. Sprint 5 ready to begin.  
**Next Action:** Review this document, then begin Sprint 5 implementation.

---

## 🔄 Sprint 5 Updates (Enrollment & Progress Tracking)

**Date:** February 8, 2026  
**Version:** 1.2.0

### New Entities Added

#### Enrollment Entity
```java
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status; // ACTIVE, COMPLETED
    
    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
```

#### Lesson Progress Entity
```java
@Entity
@Table(name = "lesson_progress")
public class LessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;
    
    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
```

### New API Endpoints (Sprint 5)

#### Learner Enrollment APIs
```
POST   /api/learner/enroll/{courseId}         - Enroll in course
GET    /api/learner/enrollments                - Get learner's enrollments
GET    /api/learner/courses/{courseId}         - Get enrolled course details
```

#### Learner Progress APIs
```
POST   /api/learner/progress/{lessonId}/start     - Mark lesson started
POST   /api/learner/progress/{lessonId}/complete  - Mark lesson completed
GET    /api/learner/progress/course/{courseId}    - Get course progress
```

#### Public APIs (Unauthenticated)
```
GET    /api/public/lesson-tree/course/{courseId}  - Browse lesson tree
```

### Business Rules (Sprint 5)

1. **Enrollment Restrictions**
   - Learners can only enroll in PUBLISHED courses
   - Duplicate enrollment prevention
   - Enrollment bound to authenticated learner

2. **Progress Tracking**
   - Progress updates are idempotent
   - Progress does NOT mutate course/lesson structure
   - Lesson completion requires prior enrollment
   - Progress persisted per learner per lesson

3. **Access Control**
   - Admin APIs: `/api/admin/**` (ADMIN role required)
   - Learner APIs: `/api/learner/**` (authenticated user required)
   - Public APIs: `/api/public/**` (no authentication required)

### Database Changes (Sprint 5)

#### New Tables
- `enrollments` - Links users to courses
- `lesson_progress` - Tracks lesson consumption

#### Table Relationships
- enrollments.user_id → users.id
- enrollments.course_id → courses.id
- lesson_progress.enrollment_id → enrollments.id
- lesson_progress.lesson_id → lessons.id

### Security Updates (Sprint 5)

- URL-pattern authorization verified end-to-end
- JWT stateless authentication validated
- Admin/Learner/Public separation enforced
- No method-level security (URL-based only)

### What's NOT in Sprint 5

- Course completion logic (deferred to Sprint 6)
- Exams (deferred to Sprint 6)
- Certificates (deferred to Sprint 7)
- Subscription enforcement (deferred to Sprint 7)

---

## 🎯 Sprint 6 Scope (Planned)

### Backend Features
- Exam domain (entity, lifecycle, MCQ evaluation)
- Course completion logic
- Homepage sections (admin-configurable)
- Subscription plan definitions
- Notification persistence (DB only)
- Certificate data model

### Frontend Features
- Admin dashboard shell (React)
- Instructor dashboard shell (React)
- Learner application shell (React + React Native)
- Homepage driven by admin sections

### Mobile Features
- React Native setup
- Android application foundation
- iOS-compatible codebase (build deferred)

---

**TDD Version History:**
- v1.0.0 - Sprint 1: Authentication
- v1.0.1 - Sprint 2: Hierarchy
- v1.1.0 - Sprint 3: Courses & Lessons
- v1.1.2 - Sprint 4: Infinite Hierarchy
- **v1.2.0 - Sprint 5: Enrollment & Progress** ← Current

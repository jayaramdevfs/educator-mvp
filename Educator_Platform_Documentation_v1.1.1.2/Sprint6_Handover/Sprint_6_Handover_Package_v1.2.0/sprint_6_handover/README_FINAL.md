# Educator Platform - EdTech Solution

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com)
[![Version](https://img.shields.io/badge/version-1.2.0-blue.svg)](CHANGELOG.md)

> A full-scale, international-level e-learning platform competing with Udemy, Coursera, and Unacademy. Built with automation-first approach and designed for global scale.

---

## 📋 Table of Contents

- [About The Project](#about-the-project)
- [Current Status](#current-status)
- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Project Structure](#project-structure)
- [Sprint History](#sprint-history)
- [Testing](#testing)
- [Deployment](#deployment)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

---

## 🎯 About The Project

**Educator Platform** is a comprehensive e-learning solution designed to compete with industry leaders like Udemy, Coursera, and Unacademy. Unlike typical MVPs, this platform is built production-ready from day one, with enterprise-grade architecture and international scalability.

### Why Educator Platform?

- **🌐 Global Scale**: Multi-language support, international-grade UI/UX
- **🤖 Automation-First**: Rule-based automation for exam generation and content structuring (no paid AI APIs)
- **♾️ Infinite Flexibility**: Unlimited hierarchy depth for course organization
- **💰 Zero-Cost Start**: Free tier infrastructure until revenue generation
- **📱 Cross-Platform**: Single backend for Web (React) + Mobile (React Native)
- **🔒 Enterprise Security**: JWT-based stateless authentication, role-based access control

---

## 📊 Current Status

**Version:** 1.2.0 (Sprint 5 Completed - February 8, 2026)

### Platform Maturity
- **Sprints Completed:** 5 of 7 (71%)
- **API Endpoints:** 35+ implemented
- **Database Tables:** 7 created
- **Test Coverage:** 0% (automated tests planned for Sprint 7)
- **Build Status:** ✅ Stable
- **Known Issues:** 0 (all bugs resolved)

### Current Capabilities
✅ User registration and authentication  
✅ JWT-based security  
✅ Role-based access control  
✅ Infinite hierarchy system  
✅ Course lifecycle management (DRAFT → PUBLISHED → ARCHIVED)  
✅ Infinite lesson hierarchy with materialized path  
✅ Admin CRUD operations for courses and lessons  
✅ Public browsing APIs  
✅ **Learner enrollment system** (Sprint 5)  
✅ **Lesson progress tracking** (Sprint 5)  
✅ **Idempotent progress updates** (Sprint 5)

### What's Next
🚧 **Sprint 6:** Foundation, Configuration & UI Skeletons (Backend + Web + Mobile)  
🚧 **Sprint 7:** Go-Live Sprint (NON-NEGOTIABLE - Platform MUST be production-live)

---

## ✨ Key Features

### ✅ Completed (Sprint 1-5)

#### Authentication & Security (Sprint 1)
- JWT-based stateless authentication
- Role-based access control (ADMIN, INSTRUCTOR, STUDENT)
- BCrypt password hashing
- URL-based security enforcement

#### Infinite Hierarchy System (Sprint 2)
- Unlimited category/subcategory nesting
- Slug-based SEO-friendly routing
- Soft delete with full audit trail

#### Course Management (Sprint 3-4)
- Complete course lifecycle (DRAFT → PUBLISHED → ARCHIVED)
- Difficulty levels (BEGINNER, INTERMEDIATE, ADVANCED)
- Multi-language support
- Admin CRUD operations

#### Lesson System (Sprint 3-4)
- Infinite lesson hierarchy (unlimited nesting depth)
- Materialized path pattern for efficient queries
- Lesson types: TEXT, VIDEO, DOCUMENT
- Subtree operations (create children, delete subtrees)

#### Enrollment & Progress Tracking (Sprint 5) **NEW**
- Learner course enrollment
- Enrollment lifecycle (ACTIVE → COMPLETED)
- Lesson-by-lesson progress tracking
- Idempotent progress updates
- Published-course-only enrollment restriction
- Per-learner, per-lesson progress persistence

#### Security Architecture (Sprint 1-5)
- JwtAuthenticationFilter integrated
- URL-based admin protection (`/api/admin/**`)
- Learner API protection (`/api/learner/**`)
- Public API access (`/api/public/**`)
- CSRF disabled for REST APIs
- Stateless session management

### 🚧 Planned (Sprint 6-7)

#### Sprint 6: Foundation, Configuration & UI Skeletons
**Backend Features:**
- Exams attached to courses
- Exam lifecycle management (Admin)
- MCQ-based exams with deterministic evaluation
- Exam attempts and pass/fail logic
- Course completion rules (lessons + exam)
- Admin-configurable homepage sections
- Subscription plan definitions (admin-only)
- Notification persistence (database-level)
- Certificate data model
- Rule-based (non-AI) content and exam automation foundations

**Frontend Features:**
- Admin dashboard shell (React)
- Instructor dashboard shell (React)
- Learner application shell (React)
- Homepage driven by admin sections

**Mobile Features:**
- React Native setup
- Android application foundation
- iOS-compatible codebase (build deferred)

#### Sprint 7: Go-Live Sprint (NON-NEGOTIABLE)
- Subscription purchase & entitlement enforcement
- Certificate generation (PDF) + verification endpoint
- Notification delivery (in-app + optional email)
- Full UI/UX polish (web + mobile)
- Production hardening and deployment
- **Platform MUST be LIVE at sprint end**

---

## 🛠 Technology Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17 | Primary backend language |
| **Spring Boot** | 4.0.2 | Web framework |
| **Spring Security** | 6 | Authentication & authorization |
| **Spring Data JPA** | - | Database abstraction |
| **Hibernate** | 6.x | ORM |
| **PostgreSQL** | 15 | Primary database |
| **JWT (jjwt)** | 0.11.5 | Token-based authentication |
| **Maven** | 3.9.x | Build & dependency management |

### Frontend (Sprint 6-7)
| Technology | Version | Purpose |
|------------|---------|---------|
| **React** | 18+ | Web UI framework |
| **Vite** | 5+ | Build tool & dev server |
| **React Router** | 6+ | SPA navigation |
| **Axios** | Latest | HTTP client |
| **Tailwind CSS** | 3.x | Utility-first styling |

### Mobile (Sprint 6-7)
| Technology | Version | Purpose |
|------------|---------|---------|
| **React Native** | 0.73+ | Cross-platform mobile |
| **React Navigation** | 6+ | App navigation |

### DevOps & Infrastructure
| Service | Tier | Purpose |
|---------|------|---------|
| **GitHub** | Free | Version control |
| **Docker** | - | Local PostgreSQL |
| **Railway** | Free | Backend hosting (post-launch) |
| **Vercel** | Free | Frontend hosting (post-launch) |

---

## 🏗 Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENT LAYER                              │
│  ┌──────────────────┐         ┌──────────────────┐         │
│  │   Web Browser    │         │   Mobile App     │         │
│  │  (React + Vite)  │         │ (React Native)   │         │
│  └────────┬─────────┘         └────────┬─────────┘         │
└───────────┼──────────────────────────┼────────────────────┘
            │                          │
            │      HTTPS/REST APIs     │
            │                          │
┌───────────┴──────────────────────────┴────────────────────┐
│                    API LAYER                                │
│  ┌──────────────────────────────────────────────────────┐ │
│  │          Spring Boot REST API                        │ │
│  │  - JWT Authentication                                │ │
│  │  - Role-Based Authorization                          │ │
│  │  - Rule-Based Automation Engine                      │ │
│  └──────────┬────────────────────────┬──────────────────┘ │
└─────────────┼────────────────────────┼────────────────────┘
              │                        │
    ┌─────────┴────────┐    ┌─────────┴─────────┐
    │   PostgreSQL     │    │  Caffeine Cache   │
    │   Database       │    │  (In-Memory)      │
    └──────────────────┘    └───────────────────┘
```

### Package Structure

```
com.educator/
├── auth/                 # Authentication logic (JWT, login, register)
├── security/             # Security configuration (SecurityFilterChain, JWT filter)
├── users/                # User entity and repository
├── roles/                # Role entity and management
├── hierarchy/            # Infinite hierarchy system
├── course/               # Course domain (entity, service, controller)
│   └── lesson/           # Lesson domain (nested hierarchy)
├── enrollment/           # Enrollment & progress (Sprint 5) ✅
├── subscription/         # Subscription plans (Sprint 6) 🚧
├── sections/             # Homepage sections (Sprint 6) 🚧
├── exam/                 # Exam system (Sprint 6) 🚧
├── settings/             # Platform settings (Sprint 6) 🚧
├── automation/           # Rule-based automation (Sprint 6) 🚧
└── EducatorApplication.java
```

---

## 🚀 Getting Started

### Prerequisites

Before running the project, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
  ```bash
  java -version  # Should show version 17+
  ```

- **Maven 3.9+**
  ```bash
  mvn -version
  ```

- **Docker** (for PostgreSQL)
  ```bash
  docker --version
  ```

- **Git**
  ```bash
  git --version
  ```

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-org/educator-platform.git
   cd educator-platform
   ```

2. **Start PostgreSQL using Docker**
   ```bash
   docker-compose up -d postgres
   ```
   
   This will start PostgreSQL on `localhost:5432` with:
   - Database: `educator_db`
   - Username: `educator_user`
   - Password: `educator_pass`

3. **Restore Sprint 5 baseline snapshot** (IMPORTANT for Sprint 6)
   ```bash
   psql -U educator_user -d educator_db --disable-triggers < database/snapshots/sprint5_baseline_data.sql
   ```
   
   See [Snapshot Restore Guide](database/migration_guide/SNAPSHOT_RESTORE_GUIDE.md) for detailed instructions.

4. **Configure application properties** (if needed)
   
   Edit `src/main/resources/application.yml` if you need to change database credentials.

5. **Build the project**
   ```bash
   mvn clean install
   ```

### Running the Application

#### Option 1: Using Maven
```bash
mvn spring-boot:run
```

#### Option 2: Using JAR file
```bash
mvn clean package
java -jar target/educator-platform-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**

#### Verify it's running
```bash
curl http://localhost:8080/api/auth/register
```

You should see a response (even if it's an error about missing body - that confirms the server is up).

---

## 📚 API Documentation

### Authentication Endpoints

#### Register New User
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123"
}

Response: 201 Created
{
  "message": "User registered successfully",
  "userId": 1
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "securePassword123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```

### Learner Endpoints (Sprint 5)

#### Enroll in Course
```http
POST /api/learner/enroll/{courseId}
Authorization: Bearer <your-jwt-token>

Response: 201 Created
{
  "message": "Enrolled successfully",
  "enrollmentId": 1,
  "courseId": 1,
  "status": "ACTIVE"
}
```

#### Mark Lesson as Started
```http
POST /api/learner/progress/{lessonId}/start
Authorization: Bearer <your-jwt-token>

Response: 200 OK
{
  "message": "Lesson started",
  "lessonId": 1,
  "startedAt": "2026-02-08T10:30:00Z"
}
```

#### Mark Lesson as Complete
```http
POST /api/learner/progress/{lessonId}/complete
Authorization: Bearer <your-jwt-token>

Response: 200 OK
{
  "message": "Lesson completed",
  "lessonId": 1,
  "completedAt": "2026-02-08T11:00:00Z"
}
```

#### Get Course Progress
```http
GET /api/learner/progress/course/{courseId}
Authorization: Bearer <your-jwt-token>

Response: 200 OK
{
  "courseId": 1,
  "totalLessons": 10,
  "completedLessons": 3,
  "progressPercentage": 30
}
```

### Admin Course Management

#### List All Courses (Admin)
```http
GET /api/admin/courses?page=0&size=10
Authorization: Bearer <your-jwt-token>

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "title": "Introduction to Java",
      "status": "PUBLISHED",
      "difficulty": "BEGINNER",
      "languageCode": "en"
    }
  ],
  "totalElements": 1,
  "totalPages": 1
}
```

#### Create Child Lesson
```http
POST /api/admin/lessons/{parentId}/children
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "title": "Variables and Data Types",
  "lessonType": "TEXT",
  "contentText": "In Java, variables are...",
  "orderIndex": 1
}

Response: 201 Created
```

#### Publish Course
```http
PUT /api/admin/courses/{id}/publish
Authorization: Bearer <your-jwt-token>

Response: 200 OK
{
  "id": 1,
  "status": "PUBLISHED"
}
```

### Public Endpoints

#### Browse Hierarchy
```http
GET /api/public/hierarchy

Response: 200 OK
[
  {
    "id": 1,
    "nameEn": "Programming",
    "slug": "programming",
    "children": [...]
  }
]
```

#### Browse Lesson Tree
```http
GET /api/public/lesson-tree/course/{courseId}

Response: 200 OK
[
  {
    "id": 1,
    "title": "Introduction",
    "lessonType": "VIDEO",
    "orderIndex": 1,
    "depthLevel": 0,
    "children": [
      {
        "id": 2,
        "title": "Getting Started",
        "depthLevel": 1
      }
    ]
  }
]
```

### Complete API Reference
For complete API documentation with all endpoints, see **[Technical Design Document](docs/Educator_TDD_v1.2.0.md) - Section 7**.

---

## 💾 Database Schema

### Current Tables (Sprint 5)

```sql
-- Users and Authentication
users (id, email, password, created_at, updated_at)
roles (id, name)
user_roles (user_id, role_id)

-- Course Organization
hierarchy_nodes (id, parent_id, name_en, slug, sort_order, is_deleted, ...)
courses (id, hierarchy_node_id, title_en, description_en, status, difficulty, ...)
lessons (id, course_id, parent_lesson_id, path, depth_level, text_content, type, ...)

-- Learner Participation (Sprint 5) ✅
enrollments (id, user_id, course_id, status, enrolled_at, completed_at)
lesson_progress (id, enrollment_id, lesson_id, started_at, completed_at)
```

### Relationships
- `user_roles` → many-to-many between `users` and `roles`
- `courses` → belongs to `hierarchy_nodes`
- `lessons` → belongs to `courses`, self-referencing parent-child via `parent_lesson_id`
- `enrollments` → links `users` to `courses`
- `lesson_progress` → tracks progress within `enrollments` for each `lesson`

### Materialized Path Example
```
Lesson 1 (path: "/course/1/lesson/1", depth: 0)
  ├── Lesson 2 (path: "/course/1/lesson/1/lesson/2", depth: 1)
  │   └── Lesson 5 (path: "/course/1/lesson/1/lesson/2/lesson/5", depth: 2)
  └── Lesson 3 (path: "/course/1/lesson/1/lesson/3", depth: 1)
```

For complete schema with all planned tables, see **[Database State Documentation](database/DATABASE_STATE.md)**.

---

## 📁 Project Structure

```
educator-platform/
├── src/
│   ├── main/
│   │   ├── java/com/educator/
│   │   │   ├── auth/                    # Authentication
│   │   │   │   ├── AuthController.java
│   │   │   │   └── AuthService.java
│   │   │   ├── security/                # Security config
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── JwtAuthenticationFilter.java
│   │   │   ├── users/                   # User management
│   │   │   │   ├── User.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── roles/                   # Role management
│   │   │   ├── hierarchy/               # Hierarchy system
│   │   │   ├── course/                  # Course domain
│   │   │   │   ├── Course.java
│   │   │   │   ├── CourseRepository.java
│   │   │   │   ├── CourseService.java
│   │   │   │   └── AdminCourseController.java
│   │   │   ├── lesson/                  # Lesson domain
│   │   │   │   ├── Lesson.java
│   │   │   │   ├── LessonRepository.java
│   │   │   │   ├── LessonService.java
│   │   │   │   ├── AdminLessonController.java
│   │   │   │   └── PublicLessonController.java
│   │   │   └── enrollment/              # Enrollment & progress (Sprint 5)
│   │   │       ├── Enrollment.java
│   │   │       ├── EnrollmentRepository.java
│   │   │       ├── LessonProgress.java
│   │   │       ├── LearnerEnrollmentController.java
│   │   │       └── LearnerProgressController.java
│   │   └── resources/
│   │       ├── application.yml          # Main config
│   │       └── application-prod.yml     # Production config
│   └── test/
│       └── java/com/educator/           # Test classes (Sprint 7)
├── docs/                                # Documentation
│   ├── Educator_TDD_v1.2.0.md          # Technical Design Document
│   ├── PROJECT_HANDOVER.md             # Project Handover
│   ├── BUG_LOG.md                      # Bug tracking
│   ├── SPRINT_5_HANDOVER.md            # Sprint 5 closure
│   ├── SPRINT_4_HANDOVER.md            # Sprint 4 closure
│   └── SPRINT_X_HANDOVER.md            # Other sprint handovers
├── database/                            # Database assets
│   ├── DATABASE_STATE.md               # Database documentation
│   ├── snapshots/
│   │   └── sprint5_baseline_data.sql   # Sprint 5 snapshot
│   └── migration_guide/
│       └── SNAPSHOT_RESTORE_GUIDE.md   # Restore guide
├── config/                              # Configuration
│   └── API_CONTRACT_GUIDELINES.md      # API standards
├── references/                          # Reference materials
│   ├── GOVERNANCE_RULES.md             # Governance
│   └── SPRINT_EXECUTION_RULES.md       # Sprint rules
├── docker-compose.yml                   # Docker services
├── pom.xml                              # Maven dependencies
├── CHANGELOG.md                         # Version history
├── SPRINT_ROAD_MAP.md                  # Master roadmap
├── SPRINT_6_READINESS_CHECKLIST.md     # Pre-flight check
└── README.md                            # This file
```

---

## 📈 Sprint History

| Sprint | Status | Key Deliverables | Date |
|--------|--------|------------------|------|
| **Sprint 1** | ✅ Complete | Authentication, JWT, Roles, Security Foundation | Feb 3, 2026 |
| **Sprint 2** | ✅ Complete | Infinite Hierarchy System, Slug Routing | Feb 4, 2026 |
| **Sprint 3.1** | ✅ Complete | Course & Lesson Domain, Entities, Repositories | Feb 5, 2026 |
| **Sprint 3.2** | ✅ Complete | Lesson APIs (Admin & Public) | Feb 6, 2026 |
| **Sprint 4** | ✅ Complete | Infinite Lesson Hierarchy, Admin Course APIs, Security Stabilization | Feb 7, 2026 |
| **Sprint 5** | ✅ Complete | Enrollment, Progress Tracking, Learner APIs | Feb 8, 2026 |
| **Sprint 6** | 🚧 Next | Foundation, Configuration, UI Skeletons (Backend + Web + Mobile) | Planned |
| **Sprint 7** | ⏳ Planned | Go-Live Sprint (Production Deployment) | Planned |

### Bug Resolution History
- **Total Bugs:** 19 across all sprints (Sprint 1-5)
- **Resolved:** 19 (100% resolution rate)
- **Open:** 0
- **Sprint 5 Issues:** 4 identified and resolved
- **Sprint 4 Bugs:** 4 resolved (JWT filter, path calculation, soft delete, timestamp)
- **Sprint 3 Bugs:** 5 resolved
- **Sprint 2 Bugs:** 3 resolved
- **Sprint 1 Bugs:** 4 resolved

For detailed bug history, see **[BUG_LOG.md](docs/BUG_LOG.md)**.

---

## 🧪 Testing

### Current Testing Approach
- **Manual API Testing:** Postman/curl after each feature
- **Security Testing:** 401/403 response validation
- **Database Constraints:** Validation via Hibernate
- **Regression Testing:** Previous sprint features retested
- **State Verification:** Direct DB inspection for business rules

### Planned (Sprint 7)
```bash
# Backend unit tests
mvn test

# Integration tests
mvn verify

# Test coverage report
mvn jacoco:report
```

**Target Coverage:**
- Backend: 80%
- Frontend: 70%

---

## 🚀 Deployment

### Development
```bash
# Local development with hot reload
mvn spring-boot:run

# Access at http://localhost:8080
```

### Production (Sprint 7)
```bash
# Build production JAR
mvn clean package -DskipTests

# Deploy to Railway
railway up

# Frontend on Vercel
vercel deploy --prod
```

**Planned Infrastructure:**
- **Backend:** Railway (free tier, PostgreSQL included)
- **Frontend:** Vercel (free tier)
- **Database:** Railway PostgreSQL (free tier)
- **Domain:** TBD
- **SSL:** Automatic via Railway/Vercel

---

## 📖 Documentation

### Core Documentation
- **[README.md](README.md)** (this file) - Main project overview
- **[Technical Design Document](docs/Educator_TDD_v1.2.0.md)** - Complete technical specifications, architecture, API reference (1,314 lines)
- **[Project Handover](docs/PROJECT_HANDOVER.md)** - High-level overview, onboarding guide, current state
- **[Bug Log](docs/BUG_LOG.md)** - Complete bug history with resolutions (19 bugs, 100% resolved)
- **[Sprint Roadmap](SPRINT_ROAD_MAP.md)** - Master plan for all 7 sprints
- **[Changelog](CHANGELOG.md)** - Version history and changes
- **[Database State](database/DATABASE_STATE.md)** - Complete database documentation

### Sprint Handovers
- **[Sprint 5 Handover](docs/SPRINT_5_HANDOVER.md)** - Latest sprint closure
- **[Sprint 4 Handover](docs/SPRINT_4_HANDOVER.md)** - Infinite hierarchy completion
- **[Sprint 3.2 Handover](docs/SPRINT_3_2_HANDOVER.md)** - Lesson APIs
- **[Sprint 3.1 Handover](docs/SPRINT_3_1_HANDOVER.md)** - Course domain
- **[Sprint 2 Handover](docs/SPRINT_2_HANDOVER.md)** - Hierarchy system
- **[Sprint 1 Handover](docs/SPRINT_1_HANDOVER.md)** - Authentication

### Operational Guides
- **[Sprint 6 Readiness Checklist](SPRINT_6_READINESS_CHECKLIST.md)** - Pre-flight verification
- **[Sprint 6 Supporting Documentation](docs/SPRINT_6_SUPPORTING_DOCUMENTATION.md)** - Operational guidelines
- **[Snapshot Restore Guide](database/migration_guide/SNAPSHOT_RESTORE_GUIDE.md)** - Database restore procedures
- **[API Contract Guidelines](config/API_CONTRACT_GUIDELINES.md)** - API design standards
- **[Governance Rules](references/GOVERNANCE_RULES.md)** - Project governance
- **[Sprint Execution Rules](references/SPRINT_EXECUTION_RULES.md)** - How to execute sprints

### Documentation Ecosystem
All documents are cross-referenced and follow industry-standard templates:
```
README.md (you are here)
    ↓ references
Educator_TDD_v1.2.0.md (technical specs)
    ↓ defines
SPRINT_X_HANDOVER.md (sprint details)
    ↓ tracks bugs in
BUG_LOG.md (bug archive)
    ↓ all link to
PROJECT_HANDOVER.md (overview)
```

---

## 🤝 Contributing

We follow a strict sprint-based development process:

### Before Starting Work
1. Review the latest sprint handover document
2. Check TDD for architectural constraints
3. Review Bug Log for known issues
4. Ensure you're working on the current sprint scope
5. Read [Sprint Execution Rules](references/SPRINT_EXECUTION_RULES.md)

### Development Process
1. Create a feature branch from `develop`
2. Follow existing code structure and naming conventions
3. Write code adhering to locked architectural decisions
4. Test manually (automated tests in Sprint 7)
5. Update relevant documentation
6. Create Pull Request to `develop`

### After Sprint Completion
All sprints must deliver:
1. Git commit summary and description
2. Updated README (this file)
3. Updated Bug Log (append-only)
4. Complete Sprint Handover document
5. Updated Project Handover
6. Database snapshot (mandatory)
7. Sprint closure confirmation

### Code Style
- Java: Follow Spring Boot conventions
- Naming: camelCase for variables, PascalCase for classes
- Comments: JavaDoc for public methods
- Commit messages: Conventional Commits format

### Locked Architectural Decisions
These decisions are PERMANENT and cannot be changed without explicit approval:
1. **Roles ≠ Subscriptions** - These are separate systems, never mix
2. **URL-based security** - No method-level security annotations
3. **Soft delete only** - `is_deleted` flag, never hard delete
4. **JWT stateless** - No server-side session storage
5. **Materialized path** - For lesson hierarchy (`path + depthLevel`)
6. **React Native only** - No native SDK development (Kotlin/Swift)
7. **No AI/paid APIs** - During development sprints
8. **Sprint 7 go-live** - Platform MUST be live by Sprint 7 end

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 📧 Contact

**Project Lead:** [Your Name]  
**Email:** [your.email@example.com]  
**Project Repository:** [https://github.com/your-org/educator-platform](https://github.com/your-org/educator-platform)

**Issue Tracking:** GitHub Issues  
**Discussions:** GitHub Discussions  
**Documentation:** [Project Wiki](https://github.com/your-org/educator-platform/wiki)

---

## 🙏 Acknowledgments

- Spring Boot Team for the excellent framework
- PostgreSQL Community for the reliable database
- JWT.io for authentication guidance
- Industry-standard templates from [Best-README-Template](https://github.com/othneildrew/Best-README-Template)

---

## 🔗 Quick Links

### Essential (Start Here)
- [Sprint 6 Readiness Checklist](SPRINT_6_READINESS_CHECKLIST.md) - **Start here for Sprint 6!**
- [Sprint Roadmap](SPRINT_ROAD_MAP.md) - Sprint 6 scope details
- [Technical Design Document](docs/Educator_TDD_v1.2.0.md) - Complete technical specifications

### Database
- [Database State](database/DATABASE_STATE.md) - Schema reference
- [Snapshot Restore Guide](database/migration_guide/SNAPSHOT_RESTORE_GUIDE.md) - Database setup

### Project Resources
- [Changelog](CHANGELOG.md) - Version history
- [Bug Log](docs/BUG_LOG.md) - Historical bug tracking
- [Sprint 5 Handover](docs/SPRINT_5_HANDOVER.md) - Latest sprint details
- [Project Handover](docs/PROJECT_HANDOVER.md) - Overview and onboarding

---

## 🎯 Sprint 6 Next Steps

Ready to start Sprint 6? Follow these steps:

1. ✅ Extract Sprint_6_Handover_Package_v1.2.0.zip (if using handover package)
2. ✅ Review this README completely
3. ✅ Read [SPRINT_6_READINESS_CHECKLIST.md](SPRINT_6_READINESS_CHECKLIST.md)
4. ✅ Review [SPRINT_ROAD_MAP.md](SPRINT_ROAD_MAP.md) for Sprint 6 scope
5. ⏭️ Install frontend tools (Node.js, npm, React dev tools)
6. ⏭️ Install mobile tools (React Native CLI, Android Studio)
7. ⏭️ Restore sprint5_baseline_data.sql to database
8. ⏭️ Verify all Sprint 5 APIs are working
9. ⏭️ Create File Impact Checklist for Sprint 6
10. ⏭️ Get File Impact Checklist approved
11. ⏭️ Start new chat thread for Sprint 6
12. ⏭️ Begin Sprint 6 execution following [Sprint Execution Rules](references/SPRINT_EXECUTION_RULES.md)

---

<div align="center">

**[⬆ Back to Top](#educator-platform---edtech-solution)**

Made with ❤️ by the Educator Platform Team

**Version 1.2.0** | **Sprint 5 Completed** | **February 8, 2026**

**Go-Live Target: Sprint 7** | **Next Sprint: Sprint 6 - Foundation, Configuration & UI Skeletons**

</div>

# Educator Platform - EdTech Solution

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com)

> A full-scale, international-level e-learning platform competing with Udemy, Coursera, and Unacademy. Built with automation-first approach and designed for global scale.

---

## 📋 Table of Contents

- [About The Project](#about-the-project)
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
- [Current Status](#current-status)
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

## ✨ Key Features

### ✅ Completed (Sprint 1-4)

#### Authentication & Security
- JWT-based stateless authentication
- Role-based access control (ADMIN, INSTRUCTOR, STUDENT)
- BCrypt password hashing
- URL-based security enforcement

#### Infinite Hierarchy System
- Unlimited category/subcategory nesting
- Slug-based SEO-friendly routing
- Soft delete with full audit trail

#### Course Management
- Complete course lifecycle (DRAFT → PUBLISHED → ARCHIVED)
- Difficulty levels (BEGINNER, INTERMEDIATE, ADVANCED)
- Multi-language support
- Admin CRUD operations

#### Lesson System
- Infinite lesson hierarchy (unlimited nesting depth)
- Materialized path pattern for efficient queries
- Lesson types: TEXT, VIDEO, DOCUMENT
- Subtree operations (create children, delete subtrees)

#### Security Architecture
- JwtAuthenticationFilter integrated
- URL-based admin protection (`/api/admin/**`)
- CSRF disabled for REST APIs
- Stateless session management

### 🚧 Planned (Sprint 5-7)

#### Sprint 5: Enrollment & Progress Tracking
- Student enrollment in courses
- Lesson-by-lesson progress tracking
- Auto-complete enrollment
- Progress percentage calculation

#### Sprint 6: Subscriptions, Exams & Automation
- 6 subscription plan types
- Timed exam system (MCQ, True/False, Essay)
- Platform settings (instructor toggle)
- Admin-configurable homepage sections
- Rule-based automation engine
- User profile management

#### Sprint 7: Frontend & Deployment
- React web application (Vite + React Router)
- Complete UI for students, instructors, admins
- Production deployment (Railway + Vercel)
- End-to-end testing suite

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

### Frontend (Sprint 7)
| Technology | Version | Purpose |
|------------|---------|---------|
| **React** | 18+ | UI framework |
| **Vite** | 5+ | Build tool & dev server |
| **React Router** | 6+ | SPA navigation |
| **Axios** | Latest | HTTP client |
| **Tailwind CSS** | 3.x | Utility-first styling |

### Mobile (Planned)
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
├── enrollment/           # Enrollment & progress (Sprint 5)
├── subscription/         # Subscription plans (Sprint 6)
├── sections/             # Homepage sections (Sprint 6)
├── exam/                 # Exam system (Sprint 6)
├── settings/             # Platform settings (Sprint 6)
├── automation/           # Rule-based automation (Sprint 6)
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

3. **Configure application properties** (if needed)
   
   Edit `src/main/resources/application.yml` if you need to change database credentials.

4. **Build the project**
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

#### List Course Lessons
```http
GET /api/public/courses/{courseId}/lessons

Response: 200 OK
[
  {
    "id": 1,
    "title": "Introduction",
    "lessonType": "VIDEO",
    "orderIndex": 1,
    "depthLevel": 0
  }
]
```

### Complete API Reference
For complete API documentation with all 28 endpoints, see **[Technical Design Document](Educator_TDD_v1_1_2.md) - Section 7**.

---

## 💾 Database Schema

### Current Tables (Sprint 4)

```sql
-- Users and Authentication
users (id, name, email, password, created_at, updated_at)
roles (id, name)
user_roles (user_id, role_id)

-- Course Organization
hierarchy_nodes (id, parent_id, name_en, slug, sort_order, is_deleted, ...)
courses (id, hierarchy_id, title, description, status, difficulty, language_code, ...)
lessons (id, course_id, parent_lesson_id, path, depth_level, title, lesson_type, ...)
```

### Relationships
- `user_roles` → many-to-many between `users` and `roles`
- `courses` → belongs to `hierarchy_nodes`
- `lessons` → belongs to `courses`, self-referencing parent-child via `parent_lesson_id`

### Materialized Path Example
```
Lesson 1 (path: "/1", depth: 0)
  ├── Lesson 2 (path: "/1/2", depth: 1)
  │   └── Lesson 5 (path: "/1/2/5", depth: 2)
  └── Lesson 3 (path: "/1/3", depth: 1)
```

For complete schema with all planned tables, see **[Technical Design Document](Educator_TDD_v1_1_2.md) - Section 6**.

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
│   │   │   └── lesson/                  # Lesson domain
│   │   │       ├── Lesson.java
│   │   │       ├── LessonRepository.java
│   │   │       ├── LessonService.java
│   │   │       ├── AdminLessonController.java
│   │   │       └── PublicLessonController.java
│   │   └── resources/
│   │       ├── application.yml          # Main config
│   │       └── application-prod.yml     # Production config
│   └── test/
│       └── java/com/educator/           # Test classes (Sprint 7)
├── docs/                                # Documentation
│   ├── Educator_TDD_v1_1_2.md          # Technical Design Document
│   ├── PROJECT_HANDOVER.md             # Project Handover
│   ├── BUG_LOG.md                      # Bug tracking
│   ├── SPRINT_1_HANDOVER.md            # Sprint handovers
│   ├── SPRINT_2_HANDOVER.md
│   ├── SPRINT_3_1_HANDOVER.md
│   ├── SPRINT_3_2_HANDOVER.md
│   └── SPRINT_4_HANDOVER.md
├── docker-compose.yml                   # Docker services
├── pom.xml                              # Maven dependencies
└── README.md                            # This file
```

---

## 📊 Current Status

### Version
**v1.1.1** (Sprint 4 Completed - February 7, 2026)

### Progress
- **Sprints Completed:** 4 of 7 (57%)
- **API Endpoints:** 28 implemented
- **Database Tables:** 5 created
- **Test Coverage:** 0% (automated tests planned for Sprint 7)
- **Build Status:** ✅ Stable
- **Known Issues:** 0 (all bugs resolved)

### Capabilities
✅ User registration and authentication  
✅ JWT-based security  
✅ Role-based access control  
✅ Infinite hierarchy system  
✅ Course lifecycle management (DRAFT → PUBLISHED → ARCHIVED)  
✅ Infinite lesson hierarchy with materialized path  
✅ Admin CRUD operations for courses and lessons  
✅ Public browsing APIs  

### What's Next
🚧 Sprint 5: Enrollment & Progress Tracking  
🚧 Sprint 6: Subscriptions, Exams, Automation  
🚧 Sprint 7: React Frontend & Deployment  

---

## 📈 Sprint History

| Sprint | Status | Key Deliverables | Date |
|--------|--------|------------------|------|
| **Sprint 1** | ✅ Complete | Authentication, JWT, Roles, Security Foundation | Completed |
| **Sprint 2** | ✅ Complete | Infinite Hierarchy System, Slug Routing | Completed |
| **Sprint 3.1** | ✅ Complete | Course & Lesson Domain, Entities, Repositories | Completed |
| **Sprint 3.2** | ✅ Complete | Lesson APIs (Admin & Public) | Completed |
| **Sprint 4** | ✅ Complete | Infinite Lesson Hierarchy, Admin Course APIs, Security Stabilization | Feb 7, 2026 |
| **Sprint 5** | 🚧 Next | Enrollment, Progress Tracking | Planned |
| **Sprint 6** | ⏳ Planned | Subscriptions, Exams, Automation, Settings | Planned |
| **Sprint 7** | ⏳ Planned | React Frontend, Deployment | Planned |

### Bug Resolution History
- **Total Bugs:** 15 across all sprints
- **Resolved:** 15 (100% resolution rate)
- **Open:** 0
- **Sprint 4 Bugs:** 4 resolved (JWT filter, path calculation, soft delete, timestamp)

For detailed bug history, see **[BUG_LOG.md](BUG_LOG.md)**.

---

## 🧪 Testing

### Current Testing Approach
- **Manual API Testing:** Postman/curl after each feature
- **Security Testing:** 401/403 response validation
- **Database Constraints:** Validation via Hibernate
- **Regression Testing:** Previous sprint features retested

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
- **[Technical Design Document](Educator_TDD_v1_1_2.md)** - Complete technical specifications, architecture, API reference
- **[Project Handover](PROJECT_HANDOVER.md)** - High-level overview, onboarding guide, current state
- **[Bug Log](BUG_LOG.md)** - Complete bug history with resolutions (15 bugs, 100% resolved)
- **[Sprint Handovers](docs/)** - Detailed sprint closure documentation (Sprints 1-4 complete)

### Documentation Ecosystem
All documents are cross-referenced and follow industry-standard templates:
```
README.md (you are here)
    ↓ references
Educator_TDD_v1_1_2.md (technical specs)
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
6. Sprint closure confirmation

### Code Style
- Java: Follow Spring Boot conventions
- Naming: camelCase for variables, PascalCase for classes
- Comments: JavaDoc for public methods
- Commit messages: Conventional Commits format

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

- [Technical Design Document](Educator_TDD_v1_1_2.md) - Complete technical specifications
- [Project Handover](PROJECT_HANDOVER.md) - Overview and onboarding
- [Bug Log](BUG_LOG.md) - Historical bug tracking
- [Sprint 4 Handover](SPRINT_4_HANDOVER.md) - Latest sprint details
- [API Reference](Educator_TDD_v1_1_2.md#7-api-specification) - All 28 endpoints documented

---

<div align="center">

**[⬆ Back to Top](#educator-platform---edtech-solution)**

Made with ❤️ by the Educator Platform Team

**Version 1.1.1** | **Sprint 4 Completed** | **February 7, 2026**

</div>

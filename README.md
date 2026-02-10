# Educator Platform

A full-stack Learning Management System (LMS) for online course delivery, assessment, enrollment tracking, and certification.

---

## Project Status

| Dimension | State |
|---|---|
| **Backend (Sprint 1-7)** | Complete, stable, running. 120 Java files, 23 DB tables, 30+ REST endpoints. APIs contract-frozen. CertificateRepository added in Sprint 7. |
| **Frontend (Sprint 7)** | Scaffolded. Next.js 15, TypeScript, Tailwind CSS, shadcn/ui, TanStack Query, Zustand installed. Placeholder pages and route groups created. |
| **Current Sprint** | Sprint 8 -- Security Hardening & Backend Fixes |

> **Source of Truth:** All architecture, sprint plans, deployment strategy, and feature tracking live in
> [`MASTER_PLAN.md`](./MASTER_PLAN.md). This README is the quick-start guide only.

---

## Tech Stack

| Layer | Technology |
|---|---|
| **Backend** | Java 17, Spring Boot 4.0.2, Spring Security + JWT, JPA/Hibernate, PostgreSQL 15, Maven, Lombok |
| **Frontend** | Next.js 15 (App Router), TypeScript, Tailwind CSS, shadcn/ui, TanStack Query, Zustand |
| **Infrastructure** | Docker, GitHub Actions (CI/CD planned) |

---

## Quick Start -- Backend

### Prerequisites

- Java 17 (JDK)
- Docker & Docker Compose
- Maven (or use the included wrapper `./mvnw`)

### Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Educator_replannned
   ```

2. **Start PostgreSQL via Docker**
   ```bash
   docker run -d \
     --name educator-db \
     -e POSTGRES_DB=educator \
     -e POSTGRES_USER=educator \
     -e POSTGRES_PASSWORD=educator \
     -p 5432:5432 \
     postgres:15
   ```

3. **Build and run the backend**
   ```bash
   cd backend
   ./mvnw clean compile
   ./mvnw spring-boot:run
   ```

4. **Verify**
   - Application starts on `http://localhost:8080`
   - Roles seeded automatically (STUDENT, INSTRUCTOR, ADMIN)
   - Test: `POST http://localhost:8080/api/auth/register` with `{"email":"test@test.com","password":"Test1234"}`

---

## Quick Start -- Frontend

> Frontend scaffolded in Sprint 7. The dev server runs on localhost:3000.

### Prerequisites

- Node.js 20+ (LTS)
- npm 10+

### Steps

1. **Install dependencies**
   ```bash
   cd frontend
   npm install
   ```

2. **Start the dev server**
   ```bash
   npm run dev
   ```

3. **Verify** -- Opens on `http://localhost:3000`

---

## Project Structure

```
Educator_replannned/
  MASTER_PLAN.md              # Single source of truth (architecture, sprints, deployment)
  README.md                   # This file (quick-start guide)
  TECHNICAL_DESIGN_DOCUMENT.md  # Historical TDD (Sprint 1-6, superseded by MASTER_PLAN)
  HANDOVER_REVIEW.md          # Historical audit (superseded by MASTER_PLAN)
  FULL_PRODUCT_EXECUTION_PLAN.md  # Historical forward plan (superseded by MASTER_PLAN)
  backend/
    pom.xml                   # Maven build
    src/main/java/com/educator/
      auth/                   # Authentication (login, register)
      course/                 # Course + lesson hierarchy
      enrollment/             # Enrollment + lesson progress
      exam/                   # Exams, attempts, auto-grading
      engine/                 # Stateless rule engines
      hierarchy/              # Category taxonomy tree
      homepage/               # Dynamic CMS homepage
      notification/           # Notification persistence
      automation/             # Automation rules framework
      certificate/            # Certificate entity + status
      completion/             # Course completion records
      media/                  # Media reference metadata
      importer/               # Import framework (abstractions)
      security/               # JWT filter, security config
      users/                  # User entity
      roles/                  # Role entity + initializer
    src/main/resources/
      application.yml         # Dev config (port 8080, local PostgreSQL)
      application-prod.yml    # Production profile (env vars)
    db/snapshots/             # Database schema snapshots
  frontend/                   # Next.js 15 app (scaffolded in Sprint 7)
```

---

## API Overview

All endpoints are documented in detail in [MASTER_PLAN.md, Section 6](./MASTER_PLAN.md#6-api-endpoint-reference).

| Group | Base Path | Auth | Endpoints |
|---|---|---|---|
| Auth | `/api/auth` | Public | 2 (register, login) |
| Public | `/api/public`, `/api/hierarchy` | Public | 5 (courses, lessons, homepage, taxonomy) |
| Learner | `/api/learner` | Authenticated | 5 (enrollment, progress) |
| Admin | `/api/admin` | ADMIN role | 18 (courses, lessons, exams, taxonomy, homepage) |
| Instructor | `/api/instructor` | Authenticated | 1 (exam view) |
| Student Exam | `/api/student` | Authenticated | 2 (start, submit exam) |

---

## Documentation

Everything lives in [`MASTER_PLAN.md`](./MASTER_PLAN.md):

| Topic | Section |
|---|---|
| Architecture & tech stack | Part I, Sections 2-3 |
| All entities & database schema | Part I, Section 5 |
| Complete API reference | Part I, Section 6 |
| Business logic engines | Part I, Section 7 |
| Known gaps & security issues | Part II, Section 12 |
| Frontend architecture & design system | Part III, Sections 13-14 |
| Sprint 7-14 task breakdowns | Part III, Sections 16-23 |
| Deployment & production checklist | Part IV, Sections 24-30 |
| Adding new features / amendments | Part V, Sections 31-35 |

---

## Key Constraints

- Sprint 1-7 backend code is **frozen** -- do not modify (except Sprint 8 security fixes per MASTER_PLAN)
- Stateless JWT auth only (no sessions, no @PreAuthorize)
- Soft deletes everywhere (no hard deletes)
- Deterministic logic only (no AI/ML)
- Mixed primary keys: `Long` (Sprint 1-5 entities) vs `UUID` (Sprint 6 entities)

---

## License

*To be determined.*

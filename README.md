# Educator Platform

Full-stack LMS platform with Spring Boot backend and Next.js frontend.

## Current Status (2026-02-14)

| Area | Status |
|---|---|
| Sprint 7 | Completed -- Local setup & foundation |
| Sprint 8 | Completed -- Security hardening & auth UI |
| Sprint 9 | Completed -- Backend completion & API polish |
| Sprint 10 | Completed -- Student experience & stabilization |
| Backend APIs | 50+ endpoints (auth, public, learner, admin, student, instructor) |
| Backend tests | 162 passing |
| Frontend pages | Homepage, catalog, course detail, login, register, dashboard, courses, exam, certificates, notifications, settings, admin, instructor |
| Frontend tests | 75 passing |
| CI/CD | GitHub Actions (backend tests + frontend lint/test/build) -- all green |
| Next execution point | Sprint 11 -- Admin Experience (`MASTER_PLAN_SOURCE OF TRUTH.md`, section 20) |

## Source of Truth and Handoff Docs

- Master plan: `MASTER_PLAN_SOURCE OF TRUTH.md`
- **Sprint 10 completion: `SPRINT_10_COMPLETION_SUMMARY.md`**
- Sprint 10A backend: `Sprint 10A_backend completion summary.md`
- Sprint 9 completion: `SPRINT_9_COMPLETION_SUMMARY.md`
- Sprint 8 completion: `SPRINT_8_COMPLETION_SUMMARY.md`
- Sprint 7 closure: `SPRINT_7_FINAL_HANDOFF.md`
- API response shapes: `docs/B0.6_API_RESPONSE_SHAPES.md`
- Git branching strategy: `docs/D0.2_GIT_BRANCHING_STRATEGY.md`
- Backend-aligned API suite: `postman/Educator_MVP_Backend_Aligned.postman_collection.json`
- Backend-aligned API environment: `postman/Educator_MVP_Backend_Aligned.postman_environment.json`
- Latest API run report: `postman/backend-aligned-run.md`

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 4.0.2, Spring Security + JWT, JPA/Hibernate, PostgreSQL 15, Maven, Flyway |
| Frontend | Next.js 16, TypeScript, Tailwind CSS v4, shadcn/ui, TanStack Query, Zustand, Axios, Framer Motion, GSAP, Lenis |
| Testing | JUnit 5 + Mockito (backend), Vitest + Testing Library + MSW (frontend), Newman/Postman (API) |
| CI/CD | GitHub Actions |
| Local infra | Docker Compose |

### Mobile Compatibility Note

- `framer-motion` is used for web UI animation and spring-based motion.
- `lenis` is DOM/web specific for smooth scrolling and is not reusable in native mobile runtimes.
- For future Android/iOS work, keep shared logic in platform-agnostic modules (`types`, API contracts, auth/state rules), and use native animation/scroll libraries in the mobile app layer.

## Local Setup

### Prerequisites

- Java 17
- Maven (or Maven wrapper)
- Node.js 20+
- Docker Desktop (with `docker compose`)

### Option A: Run backend + frontend on host machine

1. Start database:
```powershell
docker compose up -d postgres
```

2. Start backend:
```powershell
cd backend
mvn spring-boot:run
```

3. Start frontend:
```powershell
cd frontend
npm install
npm run dev
```

4. URLs:
- Backend: `http://localhost:8080`
- Frontend: `http://localhost:3000`
- Login: `http://localhost:3000/login-new`
- Register: `http://localhost:3000/register`

### Default Admin Credentials

The backend automatically creates an admin user on first startup:

```
Email:    jayaramadmin@educate.com
Password: Rama@1994
```

Use these credentials to test the auth flow.

### Option B: Run full stack with Docker Compose

Default ports:
```powershell
docker compose up -d postgres backend frontend
```

If `8080` is already in use on your machine:
```powershell
$env:BACKEND_PORT='18080'
docker compose up -d postgres backend frontend
```

Check status:
```powershell
docker compose ps
```

## Verification Commands

### Backend

```powershell
cd backend
./mvnw test
./mvnw -DskipTests package
```

### Backend-aligned API suite (Newman)

From repo root:
```powershell
npx newman run .\postman\Educator_MVP_Backend_Aligned.postman_collection.json -e .\postman\Educator_MVP_Backend_Aligned.postman_environment.json -r cli --reporter-cli-no-banner
```

### Frontend checks

```powershell
cd frontend
npm run lint
npx tsc --noEmit
npm test
npm run build
```

## Sprint History

### Sprint 10 -- Student Experience & Stabilization (2026-02-14)

**Sprint 10A Backend (5 tasks):** Automated notification system for course completion, exam pass/fail, and certificate generation. DB-level ownership validation for notifications (`findByIdAndUserId`). Idempotent read-state hardening.

**Sprint 10 Frontend (8 tasks):** Learner dashboard with enrollment cards, course learning page with progress, exam taking page with timer, certificates page, notifications page with mark-read, settings page with profile/password management, notification bell component.

**Sprint 10 Stabilization (7 fixes):**
- Login page: integrated with Zustand auth store (was using raw axios), fixed student redirect to `/learner/dashboard`
- Register page: wired to backend API (was simulated with setTimeout)
- Design tokens: full dark theme overhaul for dropdowns, popovers, cards, inputs
- AuthGuard: fixed student fallback redirect from `/learner` to `/learner/dashboard`
- CertificateServiceTest: added missing `NotificationPersistenceService` mock
- LearnerNotificationServiceTest: updated mocks to match refactored `findByIdAndUserId` method

**Validation:** 162 backend tests passing, 75 frontend tests passing, lint clean, build green.

### Sprint 9 -- Backend Completion & API Polish (2026-02-13)

**Backend (19 tasks):** Pagination on all list endpoints, learner profile and password change, password reset flow, exam timer enforcement, question shuffle, answer review API, enrollment auto-completion, exam attempt history, notification list/read/unread-count APIs, certificate service and APIs, course search and filters, admin user management, admin stats dashboard, Flyway baseline migration, Spring Actuator health endpoints, auth refresh token endpoint.

**Frontend (5 tasks):** Dynamic CMS homepage, course catalog with search and filters, course detail page, 404 and error pages, public footer. All public pages use the purple/pink palette from Sprint 8.

**Testing (6 tasks):** Unit tests for new services, exam timer and shuffle tests, integration tests for all new endpoints, engine tests (all 10 engine classes), repository custom-query tests, frontend tests for public pages.

**Validation:** 162 backend tests passing, 30 frontend tests passing, both builds green.

### Sprint 8 -- Security Hardening & Auth UI (2026-02-12)

**Backend (14 tasks):** JWT secret externalized, DB credentials externalized, production profile, CORS configuration, STATELESS session policy, explicit URL patterns, StudentExamController fix, global exception handler, DTO validation, security headers, JWT filter error response, password strength validation, rate limiting on auth endpoints.

**Frontend (8 tasks):** Login page (purple theme, GSAP + Framer Motion), register page (password strength indicator), auth guard, role-based route protection, JWT refresh mechanism, top navigation bar, error boundary, toast notification system.

**Design system:** Unified purple/pink color grading -- background `slate-950 -> purple-950 -> slate-900`, purple + pink gradient orbs, purple gradient buttons with shadow.

### Sprint 7 -- Local Setup & Foundation (2026-02-12)

Backend verification, Docker Compose setup, Postman API collection (40 requests, 111 assertions), CertificateRepository creation, API response shape documentation, Next.js project scaffold, TypeScript types, API client, auth store, route layout groups, shadcn/ui components, Vitest + Testing Library setup, Git branching strategy.

## API Endpoint Summary

| Area | Count | Base Path | Auth |
|---|---|---|---|
| Auth | 4 | `/api/auth` | Public |
| Public | 6+ | `/api/public`, `/api/hierarchy` | Public |
| Learner | 10+ | `/api/learner` | Authenticated |
| Admin | 20+ | `/api/admin` | ADMIN role |
| Instructor | 1 | `/api/instructor` | Authenticated |
| Student | 4+ | `/api/student` | Authenticated |

Sprint 9 added: pagination, profile management, password reset, exam timer/shuffle, answer review, notifications, certificates, course search/filters, user management, admin stats, auth refresh.

## CI/CD Pipeline

GitHub Actions runs on every push and pull request:
- **Backend job:** Java 17 setup, `mvnw -B test`
- **Frontend job:** Node 20 setup, `npm ci`, lint, test, build

## Branching Model

- `main`: production-ready code
- `develop`: integration branch
- `feature/*`: sprint feature work
- `hotfix/*`: urgent production fixes

See `docs/D0.2_GIT_BRANCHING_STRATEGY.md` for workflow details.

## Roadmap (from Master Plan)

| Sprint | Focus | Status |
|---|---|---|
| 7 | Local Setup & Foundation | Completed |
| 8 | Security Hardening & Auth UI | Completed |
| 9 | Backend Completion & API Polish | Completed |
| 10 | Student Experience & Stabilization | Completed |
| 11 | Admin Experience | Planned |
| 12 | Instructor, Polish & Integration | Planned |
| 13 | Testing, Performance & Pre-Deploy | Planned |
| 14 | Production Deployment & Launch | Planned |

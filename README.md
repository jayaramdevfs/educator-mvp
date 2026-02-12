# Educator Platform

Full-stack LMS platform with Spring Boot backend and Next.js frontend.

## Current Status (2026-02-12)

| Area | Status |
|---|---|
| Sprint 7 | ✅ Completed and closed |
| Sprint 8 | ✅ **COMPLETED** - Security hardening & auth UI |
| Backend security | ✅ JWT externalized, CORS, validation, rate limiting, exception handling |
| Frontend auth | ✅ Login, register, auth guards, JWT refresh, navigation, error boundary, toasts |
| Design system | ✅ Unified purple/pink theme across all auth pages |
| Code quality | ✅ 0 lint errors, fully typed, production-ready |
| Next execution point | Sprint 9 (`MASTER_PLAN_SOURCE OF TRUTH.md`, section 18) |

## Source of Truth and Handoff Docs

- Master plan: `MASTER_PLAN_SOURCE OF TRUTH.md`
- **Sprint 8 completion: `SPRINT_8_COMPLETION_SUMMARY.md`**
- Sprint 7 closure: `SPRINT_7_FINAL_HANDOFF.md`
- API response shapes: `docs/B0.6_API_RESPONSE_SHAPES.md`
- Git branching strategy: `docs/D0.2_GIT_BRANCHING_STRATEGY.md`
- Backend-aligned API suite: `postman/Educator_MVP_Backend_Aligned.postman_collection.json`
- Backend-aligned API environment: `postman/Educator_MVP_Backend_Aligned.postman_environment.json`
- Latest API run report: `postman/backend-aligned-run.md`

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 4.0.2, Spring Security + JWT, JPA/Hibernate, PostgreSQL 15, Maven |
| Frontend | Next.js 16, TypeScript, Tailwind CSS v4, shadcn/ui, TanStack Query, Zustand, Axios, Framer Motion, GSAP, Lenis |
| Testing | JUnit (backend), Newman/Postman (API), Vitest + Testing Library + MSW (frontend) |
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
Email:    admin@educator.local
Password: Admin@123
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

### Backend-aligned API suite (Newman)

From repo root:
```powershell
npx newman run .\postman\Educator_MVP_Backend_Aligned.postman_collection.json -e .\postman\Educator_MVP_Backend_Aligned.postman_environment.json -r cli --reporter-cli-no-banner
```

From `postman/` folder:
```powershell
npx newman run .\Educator_MVP_Backend_Aligned.postman_collection.json -e .\Educator_MVP_Backend_Aligned.postman_environment.json -r cli --reporter-cli-no-banner
```

### Frontend checks

```powershell
cd frontend
npm run lint
npx tsc --noEmit
npm test
npm run build
```

## Sprint 8 Highlights

### 🔒 Backend Security (14 tasks)
- JWT secret externalized
- CORS configuration
- Global exception handler
- DTO validation
- Security headers (HSTS, X-Frame-Options)
- Password strength validation
- Rate limiting (10 req/min on auth)

### 🎨 Frontend Auth UI (8 tasks)
- **Login page** (`/login-new`) - Purple theme, GSAP + Framer Motion
- **Register page** (`/register`) - Password strength indicator with real-time checklist
- **Auth Guard** - JWT validation, role-based protection (Admin, Instructor, Student)
- **JWT Refresh** - Automatic token refresh on 401
- **Top Navigation** - Role-aware links, user menu, mobile support
- **Error Boundary** - Global error handling with fallback UI
- **Toast System** - Success, error, warning, info notifications

### 🎨 Design System
- Unified **purple/pink** color grading across all pages
- Background: `slate-950 → purple-950 → slate-900`
- Orbs: Purple + Pink gradients
- Buttons: Purple gradient with shadow
- Consistent animation tokens

### 📁 Files Created (20 new files, ~1,600 lines)
- Auth components (guards, role guards)
- Layout components (top nav)
- Error boundary
- Toast system
- Enhanced API client with JWT refresh

## Branching Model

- `main`: production-ready code
- `develop`: integration branch
- `feature/*`: sprint feature work
- `hotfix/*`: urgent production fixes

See `docs/D0.2_GIT_BRANCHING_STRATEGY.md` for workflow details.

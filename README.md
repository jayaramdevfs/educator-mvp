# Educator Platform

Full-stack LMS platform with Spring Boot backend and Next.js frontend.

## Current Status (2026-02-12)

| Area | Status |
|---|---|
| Sprint 7 | Completed and closed |
| Backend API verification | Passed (`40` requests, `111` assertions, `0` failed) |
| Frontend foundation | Completed (route groups, tokens, API client, auth store, test setup) |
| Next execution point | Sprint 8 (`MASTER_PLAN_SOURCE OF TRUTH.md`, section 17) |

## Source of Truth and Handoff Docs

- Master plan: `MASTER_PLAN_SOURCE OF TRUTH.md`
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
| Frontend | Next.js 16, TypeScript, Tailwind CSS v4, shadcn/ui, TanStack Query, Zustand, Axios |
| Testing | JUnit (backend), Newman/Postman (API), Vitest + Testing Library + MSW (frontend) |
| Local infra | Docker Compose |

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

## Branching Model

- `main`: production-ready code
- `develop`: integration branch
- `feature/*`: sprint feature work
- `hotfix/*`: urgent production fixes

See `docs/D0.2_GIT_BRANCHING_STRATEGY.md` for workflow details.

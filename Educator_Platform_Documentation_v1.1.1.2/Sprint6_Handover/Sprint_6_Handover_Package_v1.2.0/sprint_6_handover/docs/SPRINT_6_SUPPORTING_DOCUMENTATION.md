# 🧩 Sprint 6 Supporting Documentation
*(Non-Blocking, Operational, and UI/Integration-Oriented)*

This document consolidates **all supporting documentation**
identified during the Sprint 6 readiness audit.

These items are **NOT architectural sources of truth**  
but exist to **prevent execution friction** during Sprint 6 & 7.

---

## 1️⃣ API Contract Guidelines (Frontend & Mobile Safety)

### Purpose
Provide a stable reference for frontend (Web + Mobile)
to integrate without causing backend churn.

### API Versioning
- All APIs must follow:
  - `/api/v1/...`
- Version changes require explicit approval

### General Rules
- DTOs are immutable once exposed to frontend
- No breaking changes without:
  - Version bump
  - Documentation update
- IDs are always `Long`
- Pagination must be explicit

### Error Handling
- 400 → Validation errors
- 401 → Authentication required
- 403 → Authorization failure
- 404 → Resource not found
- 409 → Business rule violation
- 500 → Internal server error

### Mobile Safety Rules
- No browser-only assumptions
- No HTML in API responses
- Dates in ISO-8601 format
- Boolean flags explicit (no null meaning)

---

## 2️⃣ Frontend Architecture & Design System (Guidelines)

### Purpose
Ensure consistency across:
- Web application
- Android application
- Future iOS application

### Design Ownership
- Product owner has final UI/UX approval
- Architecture owner proposes patterns and constraints

### Design Tokens (To Be Defined)
- Color palette
- Typography scale
- Spacing system
- Border radius rules
- Animation timing constants

### UI Principles
- Mobile-first layouts
- Progressive disclosure for admin features
- Empty states must be designed (no blank screens)
- Loading states mandatory for all async actions

### Animation Rules
- Animations must:
  - Be subtle
  - Be interruptible
  - Not block user input
- No animation should affect backend behavior

---

## 3️⃣ Frontend Folder Structure (Recommended)

### Web (React)
- `components/`
- `pages/`
- `services/` (API calls)
- `hooks/`
- `styles/`
- `utils/`

### Mobile (React Native)
- `screens/`
- `components/`
- `navigation/`
- `services/`
- `theme/`
- `utils/`

Shared logic must be abstracted where feasible.

---

## 4️⃣ Environment & Configuration Guidelines

### Environments
- `dev`
- `prod`

### Configuration Rules
- No secrets in source code
- Environment variables only
- Separate API base URLs per environment

### Required Variables (Indicative)
- `API_BASE_URL`
- `JWT_STORAGE_KEY`
- `ENVIRONMENT`

---

## 5️⃣ Deployment & Go-Live Checklist (Preview)

*(Formal checklist will be finalized in Sprint 7)*

### Backend
- DB migrations applied
- Snapshot taken
- Security sanity checks passed

### Frontend
- Production build tested
- API endpoints verified
- Error boundaries tested

### Mobile (Android)
- Release build generated
- App icon & splash verified
- Versioning set correctly

---

## 6️⃣ Documentation Governance Rules

- This document is **living**
- Updates do NOT require TDD changes
- No rule here may override:
  - Educator_TDD
  - Project Handover
- Conflicts must be resolved in favor of TDD

---

## 7️⃣ Sprint 6 Usage Rule

- This document may be referenced freely during Sprint 6
- It must NOT block:
  - Entity creation
  - Repository creation
  - Service logic
- Any section becoming critical must be promoted to:
  - TDD or
  - Project Handover (explicitly)

---

End of Sprint 6 Supporting Documentation.

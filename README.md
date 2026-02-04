# AI-Powered E-Learning Platform – Backend

## Sprint 1: Authentication & Security Foundation

This repository contains the backend implementation for Sprint 1
of the AI-powered E-Learning Platform.

---

## Tech Stack
- Java 17
- Spring Boot 3.x
- Maven
- PostgreSQL (Docker)
- Spring Security
- JWT (JSON Web Tokens)

---

## Features Implemented (Sprint 1)

### Authentication
- User registration API
- User login API
- BCrypt password hashing
- JWT-based stateless authentication

### Roles & Governance
- Roles:
  - STUDENT
  - INSTRUCTOR
  - ADMIN
- Roles are seeded automatically at application startup
- Default role on registration: STUDENT

### Security
- Default Spring Security login page disabled
- HTTP Basic authentication disabled
- CSRF disabled for REST APIs
- `/api/auth/**` endpoints are publicly accessible
- All other endpoints are secured

### Database
- PostgreSQL running in Docker
- Tables auto-created via Hibernate:
  - users
  - roles
  - user_roles
- UTC-safe configuration applied at JVM, JDBC, and Hibernate levels

---

## How to Run Locally

1. Start PostgreSQL using Docker
2. Run the Spring Boot application
3. Backend will be available at:
   http://localhost:8080

---

## API Endpoints

### Register
POST /api/auth/register

### Login
POST /api/auth/login

Login returns a JWT token for authenticated access.

---

## Sprint Status
- Sprint 1: Completed Successfully
- Next Sprint: Infinite Hierarchy System (Sprint 2)


## Sprint 2 – Infinite Hierarchy Backend

Sprint 2 delivered the core structural backbone of the platform.

### Key Features
- Infinite, self-referencing hierarchy with unlimited depth
- Slug-based identifiers for stable routing and future SEO
- Localization-ready fields (English base)
- Versioning, visibility flags, and soft delete
- Audit fields for governance

### Business Rules
- Cycle prevention in hierarchy
- Maximum depth enforcement
- Deterministic ordering of nodes

### APIs
- Admin APIs for managing hierarchy nodes
- Public read-only APIs for browsing hierarchy

### Status
✔ Completed  
✔ Tested via Postman  
✔ Stable baseline for course and exam attachment

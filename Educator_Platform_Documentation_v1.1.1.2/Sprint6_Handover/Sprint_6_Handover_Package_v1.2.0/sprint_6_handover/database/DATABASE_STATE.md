# Educator Platform - Database State & Testing Guide

**Version:** 1.1.2  
**Date:** February 7, 2026  
**Sprint Status:** Sprint 4 Completed  
**Database:** PostgreSQL 15

---

## Table of Contents

1. [Overview](#overview)
2. [Current Database State](#current-database-state)
3. [Existing Tables](#existing-tables)
4. [Sample Data](#sample-data)
5. [Authentication Details](#authentication-details)
6. [Postman Testing Matrix](#postman-testing-matrix)
7. [What Does NOT Exist Yet](#what-does-not-exist-yet)
8. [How to Verify Database](#how-to-verify-database)
9. [Common Issues](#common-issues)

---

## Overview

This document describes the **current state** of the Educator Platform database after **Sprint 4 completion** (February 7, 2026). It provides a complete reference for:

- Available database tables and their structure
- Sample data that exists
- Authentication credentials for testing
- API endpoints ready for testing
- Tables that are NOT yet implemented

**Purpose:** Help developers and testers understand exactly what data exists and what can be tested right now.

---

## Current Database State

### ✅ Guaranteed Available (Sprint 1-4)

Based on Sprint 1–4 completion and database verification, the following components are **guaranteed to exist**:

| Component | Status | Sprint |
|-----------|--------|--------|
| **Users Table** | ✅ Available | Sprint 1 |
| **Roles Table** | ✅ Available | Sprint 1 |
| **User-Role Mapping** | ✅ Available | Sprint 1 |
| **Hierarchy Nodes** | ✅ Available | Sprint 2 |
| **Courses Table** | ✅ Available | Sprint 3.1 |
| **Lessons Table** | ✅ Available | Sprint 3.2, 4 |

### Database Connection Details

- **Database Name:** `educator_db`
- **Username:** `educator_user`
- **Password:** `educator_pass`
- **Host:** `localhost`
- **Port:** `5432`
- **Connection String:** `jdbc:postgresql://localhost:5432/educator_db`

---

## Existing Tables

### 1️⃣ USERS Table

**Table Name:** `users`  
**Purpose:** Store all user accounts (students, instructors, admins)  
**Status:** ✅ AVAILABLE

#### Schema
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,  -- BCrypt hashed
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Verify Data
```sql
SELECT id, name, email, created_at FROM users;
```

#### Expected Output
```
id | name           | email                | created_at
---+----------------+----------------------+------------------
1  | Test User      | test@example.com     | 2026-02-01 10:00
2  | Admin User     | admin@test.com       | 2026-02-01 10:05
```

#### Key Points
- ✅ Users exist with ID 1, 2 (minimum)
- ✅ `admin@test.com` → Has ADMIN role
- ✅ `test@example.com` → Has STUDENT role
- ❌ **Passwords are BCrypt hashed** - not readable in plain text
- ⚠️ If you forgot password, you must re-register

---

### 2️⃣ ROLES Table

**Table Name:** `roles`  
**Purpose:** Define system roles (STUDENT, INSTRUCTOR, ADMIN)  
**Status:** ✅ AVAILABLE (auto-seeded at startup)

#### Schema
```sql
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);
```

#### Verify Data
```sql
SELECT id, name FROM roles ORDER BY id;
```

#### Expected Output
```
id | name
---+------------------
1  | ROLE_STUDENT
2  | ROLE_INSTRUCTOR
3  | ROLE_ADMIN
```

#### Key Points
- ✅ All 3 roles auto-seeded at application startup
- ✅ Roles follow Spring Security naming convention: `ROLE_*`
- ✅ These are **permanent** - never deleted or modified

---

### 3️⃣ USER_ROLES Table (Junction)

**Table Name:** `user_roles`  
**Purpose:** Many-to-many mapping between users and roles  
**Status:** ✅ AVAILABLE

#### Schema
```sql
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
```

#### Verify Data
```sql
SELECT ur.user_id, u.email, ur.role_id, r.name as role_name
FROM user_roles ur
JOIN users u ON ur.user_id = u.id
JOIN roles r ON ur.role_id = r.id;
```

#### Expected Output
```
user_id | email               | role_id | role_name
--------+---------------------+---------+------------------
1       | test@example.com    | 1       | ROLE_STUDENT
2       | admin@test.com      | 3       | ROLE_ADMIN
2       | admin@test.com      | 1       | ROLE_STUDENT
```

#### Key Points
- ✅ User ID 2 (`admin@test.com`) has BOTH ADMIN and STUDENT roles
- ✅ This is **intentional** - Admin can act as student (locked rule)
- ✅ Default registration assigns ROLE_STUDENT only
- ⚠️ INSTRUCTOR role exists but may not be assigned yet

---

### 4️⃣ HIERARCHY_NODES Table

**Table Name:** `hierarchy_nodes`  
**Purpose:** Infinite category/subcategory hierarchy for course organization  
**Status:** ✅ AVAILABLE

#### Schema
```sql
CREATE TABLE hierarchy_nodes (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT,
    name_en VARCHAR(255) NOT NULL,
    description_en TEXT,
    slug VARCHAR(255) UNIQUE NOT NULL,
    sort_order INT DEFAULT 0,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES hierarchy_nodes(id) ON DELETE CASCADE
);
```

#### Verify Data
```sql
SELECT id, parent_id, name_en, slug, is_deleted 
FROM hierarchy_nodes 
WHERE is_deleted = FALSE 
ORDER BY parent_id NULLS FIRST, sort_order;
```

#### Expected Output
```
id | parent_id | name_en         | slug          | is_deleted
---+-----------+-----------------+---------------+------------
1  | NULL      | Programming     | programming   | false
2  | 1         | Java            | java          | false
3  | 1         | Python          | python        | false
```

#### Key Points
- ✅ Supports unlimited nesting depth
- ✅ Root nodes have `parent_id = NULL`
- ✅ Soft delete via `is_deleted` flag
- ✅ Slug-based routing for SEO

---

### 5️⃣ COURSES Table

**Table Name:** `courses`  
**Purpose:** Store course information with lifecycle management  
**Status:** ✅ AVAILABLE

#### Schema
```sql
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    hierarchy_id BIGINT NOT NULL,
    title_en VARCHAR(500) NOT NULL,
    description_en TEXT,
    status VARCHAR(50) DEFAULT 'DRAFT',  -- DRAFT, PUBLISHED, ARCHIVED
    difficulty VARCHAR(50),               -- BEGINNER, INTERMEDIATE, ADVANCED
    language_code VARCHAR(10) DEFAULT 'en',
    estimated_duration_minutes INT,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hierarchy_id) REFERENCES hierarchy_nodes(id)
);
```

#### Verify Data
```sql
SELECT id, title_en, status, difficulty, language_code, is_deleted 
FROM courses 
WHERE is_deleted = FALSE;
```

#### Expected Output
```
id | title_en           | status    | difficulty    | language_code | is_deleted
---+--------------------+-----------+---------------+---------------+------------
1  | Java Basics        | DRAFT     | BEGINNER      | en            | false
2  | Python Advanced    | PUBLISHED | ADVANCED      | en            | false
```

#### Key Points
- ✅ At least one course exists (created in Sprint 3.1)
- ✅ Status lifecycle: DRAFT → PUBLISHED → ARCHIVED
- ✅ Difficulty: BEGINNER, INTERMEDIATE, ADVANCED
- ✅ Language-first design: language_code required
- ✅ Soft delete enabled

---

### 6️⃣ LESSONS Table

**Table Name:** `lessons`  
**Purpose:** Store lessons with infinite hierarchy support  
**Status:** ✅ AVAILABLE

#### Schema
```sql
CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL,
    parent_lesson_id BIGINT,
    path VARCHAR(500),                    -- Materialized path: "/1/2/5"
    depth_level INT DEFAULT 0,
    title VARCHAR(500) NOT NULL,
    lesson_type VARCHAR(50) NOT NULL,     -- TEXT, VIDEO, DOCUMENT
    content_text TEXT,
    content_video_url VARCHAR(1000),
    content_document_url VARCHAR(1000),
    order_index INT DEFAULT 0,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);
```

#### Verify Data
```sql
SELECT id, course_id, parent_lesson_id, depth_level, path, title 
FROM lessons 
WHERE is_deleted = FALSE 
ORDER BY course_id, path;
```

#### Expected Output
```
id | course_id | parent_lesson_id | depth_level | path            | title
---+-----------+------------------+-------------+-----------------+-------------------
1  | 1         | NULL             | 0           | /1              | Introduction
2  | 1         | 1                | 1           | /1/2            | Variables
3  | 1         | 2                | 2           | /1/2/3          | Data Types
4  | 1         | 1                | 1           | /1/4            | Control Flow
```

#### Key Points
- ✅ Supports infinite nesting via `parent_lesson_id`
- ✅ Materialized path pattern for efficient queries
- ✅ `path` format: `/<id1>/<id2>/<id3>`
- ✅ `depth_level` calculated automatically
- ✅ Lesson types: TEXT, VIDEO, DOCUMENT
- ✅ Subtree deletion supported

---

## Sample Data

### User Accounts (Guaranteed to Exist)

| ID | Email | Password (Hashed) | Roles |
|----|-------|-------------------|-------|
| 1 | test@example.com | BCrypt hash | STUDENT |
| 2 | admin@test.com | BCrypt hash | ADMIN, STUDENT |

### Roles (Auto-Seeded)

| ID | Name |
|----|------|
| 1 | ROLE_STUDENT |
| 2 | ROLE_INSTRUCTOR |
| 3 | ROLE_ADMIN |

### Sample Hierarchy (May Vary)

```
Programming (id: 1, slug: programming)
├── Java (id: 2, slug: java)
├── Python (id: 3, slug: python)
└── JavaScript (id: 4, slug: javascript)
```

### Sample Course

| ID | Title | Status | Difficulty | Language |
|----|-------|--------|------------|----------|
| 1 | Java Basics | DRAFT | BEGINNER | en |

### Sample Lessons

```
Java Basics (Course 1)
├── Introduction (id: 1, path: /1, depth: 0)
│   ├── Variables (id: 2, path: /1/2, depth: 1)
│   │   └── Data Types (id: 3, path: /1/2/3, depth: 2)
│   └── Control Flow (id: 4, path: /1/4, depth: 1)
```

---

## Authentication Details

### 🔐 Login Credentials

#### For Testing with Postman

**Endpoint:** `POST http://localhost:8080/api/auth/login`

#### Option 1: Admin User
```json
{
  "email": "admin@test.com",
  "password": "the_password_you_registered_with"
}
```

**Roles:** ADMIN + STUDENT  
**Access:** All `/api/admin/**` endpoints + all public endpoints

#### Option 2: Student User
```json
{
  "email": "test@example.com",
  "password": "the_password_you_registered_with"
}
```

**Roles:** STUDENT  
**Access:** Only public endpoints (no admin access)

### ⚠️ Password Recovery

**Important:** If you forgot the password:
- ❌ Passwords are BCrypt hashed - cannot be retrieved
- ✅ Solution: Re-register a new test user

### 📝 Register New Test Users (Recommended)

For clean Postman testing, register fresh users:

**Endpoint:** `POST http://localhost:8080/api/auth/register`

#### Register Student
```json
{
  "name": "Student One",
  "email": "student1@test.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "message": "User registered successfully",
  "userId": 3
}
```

**Default Role:** Automatically assigned `ROLE_STUDENT`

#### Register Admin (Manual Role Assignment)

1. Register user via API:
```json
{
  "name": "Admin Two",
  "email": "admin2@test.com",
  "password": "admin123"
}
```

2. Manually assign ADMIN role in database:
```sql
-- Get the new user ID (e.g., 4)
SELECT id FROM users WHERE email = 'admin2@test.com';

-- Assign ADMIN role
INSERT INTO user_roles (user_id, role_id) VALUES (4, 3);

-- Optionally assign STUDENT role too
INSERT INTO user_roles (user_id, role_id) VALUES (4, 1);
```

---

## Postman Testing Matrix

### 🧪 What You Can Test NOW

| Role | Endpoint Pattern | Status | Expected Result |
|------|------------------|--------|-----------------|
| **ADMIN** | `/api/admin/courses/**` | ✅ Working | 200 OK (with valid JWT) |
| **ADMIN** | `/api/admin/lessons/**` | ✅ Working | 200 OK (with valid JWT) |
| **ADMIN** | `/api/admin/hierarchy/**` | ✅ Working | 200 OK (with valid JWT) |
| **STUDENT** | `/api/admin/**` | ❌ Blocked | 403 Forbidden |
| **STUDENT** | `/api/public/**` | ✅ Working | 200 OK (no auth required) |
| **PUBLIC** | `/api/public/lesson-tree/course/1` | ✅ Working | 200 OK (no auth required) |
| **PUBLIC** | `/api/auth/register` | ✅ Working | 201 Created |
| **PUBLIC** | `/api/auth/login` | ✅ Working | 200 OK + JWT |

### Testing Steps

#### 1. Login to Get JWT Token
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@test.com",
  "password": "your_password"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```

#### 2. Test Admin Endpoint (with JWT)
```http
GET http://localhost:8080/api/admin/courses
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Expected:** 200 OK with course list

#### 3. Test Public Endpoint (no JWT)
```http
GET http://localhost:8080/api/public/hierarchy
```

**Expected:** 200 OK with hierarchy tree

#### 4. Test Student Blocked from Admin (with Student JWT)
```http
GET http://localhost:8080/api/admin/courses
Authorization: Bearer <student_jwt_token>
```

**Expected:** 403 Forbidden

### Complete API Endpoints (28 Total)

#### Authentication (2 endpoints)
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT

#### Public Hierarchy (2 endpoints)
- `GET /api/public/hierarchy` - Browse hierarchy tree
- `GET /api/public/hierarchy/{slug}` - Get by slug

#### Admin Hierarchy (2 endpoints)
- `POST /api/admin/hierarchy` - Create node
- `PUT /api/admin/hierarchy/{id}` - Update node

#### Public Courses (2 endpoints)
- `GET /api/public/courses` - List published courses
- `GET /api/public/courses/{id}` - Get course details

#### Admin Courses (6 endpoints)
- `GET /api/admin/courses` - List all courses (with pagination)
- `GET /api/admin/courses/{id}` - Get single course
- `POST /api/admin/courses` - Create course
- `PUT /api/admin/courses/{id}` - Update course
- `DELETE /api/admin/courses/{id}` - Soft delete
- `POST /api/admin/courses/{id}/publish` - Publish course
- `POST /api/admin/courses/{id}/archive` - Archive course

#### Public Lessons (2 endpoints)
- `GET /api/public/courses/{courseId}/lessons` - List course lessons
- `GET /api/public/lesson-tree/course/{courseId}` - Get lesson tree

#### Admin Lessons (4 endpoints)
- `POST /api/admin/lessons/{parentId}/children` - Create child lesson
- `GET /api/admin/lessons/{id}/children` - Get children
- `PUT /api/admin/lessons/{id}` - Update lesson
- `DELETE /api/admin/lessons/{id}` - Delete subtree

---

## What Does NOT Exist Yet

### ❌ Tables NOT Implemented (Sprint 5+)

The following tables do **NOT exist yet** and will cause errors if queried:

| Table Name | Planned Sprint | Purpose |
|------------|----------------|---------|
| `enrollments` | Sprint 5 | Student course enrollments |
| `lesson_progress` | Sprint 5 | Lesson completion tracking |
| `subscriptions` | Sprint 6 | Subscription plans |
| `subscription_items` | Sprint 6 | Plan-course mappings |
| `user_subscriptions` | Sprint 7 | User subscription purchases |
| `exams` | Sprint 6 | Exam definitions |
| `exam_questions` | Sprint 6 | Exam questions |
| `exam_attempts` | Sprint 6 | Student exam attempts |
| `settings` | Sprint 6 | Platform settings |
| `sections` | Sprint 6 | Homepage sections |
| `section_items` | Sprint 6 | Section content |
| `automation_jobs` | Sprint 6 | Automation tracking |
| `notifications` | Future | User notifications |
| `contests` | Future | Coding contests |

### ⚠️ Important: Do NOT Query These Tables

If you try to query non-existent tables, you'll get:
```
ERROR: relation "enrollments" does not exist
```

**Solution:** These tables will be created in future sprints. Use only the tables listed in [Existing Tables](#existing-tables) section.

---

## How to Verify Database

### Connect to PostgreSQL

#### Using Docker
```bash
docker exec -it <postgres_container_name> psql -U educator_user -d educator_db
```

#### Using psql directly
```bash
psql -h localhost -p 5432 -U educator_user -d educator_db
```

### List All Tables
```sql
\dt
```

**Expected Output:**
```
               List of relations
 Schema |       Name        | Type  |     Owner
--------+-------------------+-------+----------------
 public | courses           | table | educator_user
 public | hierarchy_nodes   | table | educator_user
 public | lessons           | table | educator_user
 public | roles             | table | educator_user
 public | user_roles        | table | educator_user
 public | users             | table | educator_user
(6 rows)
```

### Check Table Structure
```sql
\d users
\d courses
\d lessons
```

### Verify Data Counts
```sql
SELECT 
    (SELECT COUNT(*) FROM users) as users_count,
    (SELECT COUNT(*) FROM roles) as roles_count,
    (SELECT COUNT(*) FROM courses) as courses_count,
    (SELECT COUNT(*) FROM lessons) as lessons_count;
```

**Expected:**
```
users_count | roles_count | courses_count | lessons_count
------------+-------------+---------------+---------------
2           | 3           | 1+            | 1+
```

### Check User-Role Mappings
```sql
SELECT u.email, r.name as role
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
ORDER BY u.email, r.name;
```

---

## Common Issues

### Issue 1: "Table does not exist"

**Error:**
```
ERROR: relation "enrollments" does not exist
```

**Cause:** Trying to query a table from Sprint 5+ that hasn't been created yet.

**Solution:** Only query tables from Sprint 1-4 (see [Existing Tables](#existing-tables)).

---

### Issue 2: "Cannot login - wrong password"

**Error:**
```
401 Unauthorized
{
  "error": "Invalid credentials"
}
```

**Cause:** Incorrect password or user doesn't exist.

**Solutions:**
1. Verify user exists: `SELECT * FROM users WHERE email = 'your_email';`
2. If exists but password forgotten → Register new test user
3. If doesn't exist → Register via `/api/auth/register`

---

### Issue 3: "403 Forbidden on admin endpoints"

**Error:**
```
403 Forbidden
```

**Cause:** User doesn't have ADMIN role OR JWT token missing.

**Solutions:**
1. Verify user has ADMIN role:
```sql
SELECT u.email, r.name 
FROM users u 
JOIN user_roles ur ON u.id = ur.user_id 
JOIN roles r ON ur.role_id = r.id 
WHERE u.email = 'your_email';
```

2. If missing ADMIN role, assign it:
```sql
INSERT INTO user_roles (user_id, role_id) 
VALUES (<user_id>, 3);
```

3. Ensure JWT token is included in request:
```http
Authorization: Bearer <your_jwt_token>
```

---

### Issue 4: "Empty course or lesson list"

**Error:** API returns empty array `[]`

**Cause:** No data created yet.

**Solution:** Create sample data:

#### Create Course
```http
POST http://localhost:8080/api/admin/courses
Authorization: Bearer <admin_jwt>
Content-Type: application/json

{
  "hierarchyId": 1,
  "titleEn": "Test Course",
  "descriptionEn": "Test description",
  "difficulty": "BEGINNER",
  "languageCode": "en",
  "estimatedDurationMinutes": 60
}
```

#### Create Lesson
```http
POST http://localhost:8080/api/admin/lessons/course/{courseId}
Authorization: Bearer <admin_jwt>
Content-Type: application/json

{
  "title": "Introduction",
  "lessonType": "TEXT",
  "contentText": "Welcome to the course",
  "orderIndex": 1
}
```

---

### Issue 5: "Role not found"

**Error:** User has no roles assigned

**Cause:** Manually created user in database without role assignment.

**Solution:** Assign default STUDENT role:
```sql
INSERT INTO user_roles (user_id, role_id) 
VALUES (<user_id>, 1);
```

---

## Quick Reference

### Database Connection
```
Host: localhost
Port: 5432
Database: educator_db
Username: educator_user
Password: educator_pass
```

### Test Accounts
```
Admin:   admin@test.com (roles: ADMIN, STUDENT)
Student: test@example.com (roles: STUDENT)
```

### API Base URL
```
http://localhost:8080
```

### Total Available
- **Tables:** 6
- **API Endpoints:** 28
- **Roles:** 3
- **Users:** 2+ (minimum)

---

## Next Steps

### For Testing
1. ✅ Verify database connection
2. ✅ Check all 6 tables exist
3. ✅ Register test users if needed
4. ✅ Login to get JWT tokens
5. ✅ Test all 28 API endpoints

### For Development
1. 🚧 Implement Sprint 5 (Enrollment + Progress Tracking)
2. 🚧 Add 2 new tables: enrollments, lesson_progress
3. 🚧 Add 9 new API endpoints
4. 🚧 Update this document after Sprint 5

---

**Document Version:** 1.1.2  
**Last Updated:** February 7, 2026  
**Sprint:** 4 Completed  
**Status:** Production Ready

---

## Related Documents

- **[README.md](README.md)** - Project overview and setup
- **[Educator_TDD_v1_1_2.md](Educator_TDD_v1_1_2.md)** - Complete technical specifications
- **[PROJECT_HANDOVER.md](PROJECT_HANDOVER.md)** - Project handover guide
- **[SPRINT_4_HANDOVER.md](SPRINT_4_HANDOVER.md)** - Sprint 4 closure details
- **[BUG_LOG.md](BUG_LOG.md)** - Bug history and resolutions

---

**END OF DOCUMENT**
1️⃣ Security Sanity Check (VERY IMPORTANT  public api:403, learner api(with&with out token): succues, 
# Sprint 10 -- Course Catalog Search Failure

## Comprehensive Bug Report

------------------------------------------------------------------------

## 1. Issue Summary

Public course search endpoint:

`GET /api/public/courses/search`

returns **HTTP 500 -- Internal Server Error** with PostgreSQL error:

    function lower(bytea) does not exist
    function pg_catalog.btrim(bytea) does not exist

This prevents:

-   Catalog page from loading courses
-   Public search functionality from working
-   Server-side rendered catalog page from displaying results

------------------------------------------------------------------------

## 2. Environment

-   Backend: Spring Boot 4.0.2
-   Database: PostgreSQL 15
-   ORM: Hibernate
-   Frontend: Next.js (Server Components)
-   Deployment: Docker Compose (Postgres + Backend + Frontend)

------------------------------------------------------------------------

## 3. Root Cause Analysis

### Primary Technical Cause

JPQL query inside `CourseRepository`:

``` java
and (:q is null or lower(c.titleEn) like lower(concat('%', :q, '%')))
```

When `q` parameter is:

-   null
-   empty string
-   absent

Hibernate binds parameter type ambiguously.

PostgreSQL interprets the parameter as:

    bytea

When JPQL executes:

    lower(?)
    trim(?)

Postgres throws:

    function lower(bytea) does not exist
    function btrim(bytea) does not exist

------------------------------------------------------------------------

## 4. What Has Been Fixed During Debugging

### Infrastructure Fixes

-   Docker port conflicts resolved
-   Ghost Node processes killed
-   Full container reset performed
-   Volume state verified
-   Database integrity confirmed
-   Flyway migration validated
-   Backend rebuild with `--no-cache` executed

### Data Layer Verification

-   Confirmed courses exist in database
-   Verified 16 PUBLISHED courses
-   Confirmed API works when parameters structured differently
-   Verified schema columns are correct types (varchar/text)

### Service Layer Improvement

Added normalization in `CourseService`:

``` java
String normalizedQ = (q == null || q.isBlank()) ? null : q;
```

However, this did NOT fully solve parameter binding issue.

------------------------------------------------------------------------

## 5. What Is NOT Fixed

-   JPQL parameter binding ambiguity remains
-   PostgreSQL still receives parameter as bytea
-   Search endpoint still returns HTTP 500
-   Catalog page remains non-functional

------------------------------------------------------------------------

## 6. Confirmed Non-Issues

The following are NOT the problem:

-   Database schema
-   Column data types
-   Docker networking
-   Port configuration
-   Flyway migrations
-   Course data integrity
-   Frontend routing
-   Server-side rendering logic

------------------------------------------------------------------------

## 7. True Root Problem

Hibernate parameter type inference conflict in JPQL when:

    :q is null

is used in combination with string functions.

This causes incorrect SQL type binding.

------------------------------------------------------------------------

## 8. Recommended Final Resolution

Replace dynamic JPQL with one of:

### Option A -- Use Specification API

Build query dynamically in Java instead of JPQL string.

### Option B -- Use CriteriaBuilder

Construct predicate only when q != null.

### Option C -- Split Query Logic

Use two repository methods: - One with search - One without search

Avoid using `:q is null` inside JPQL with string functions.

------------------------------------------------------------------------

## 9. Current System Status

  Component           Status
  ------------------- ---------------------------
  Docker              Stable
  Postgres            Healthy
  Backend             Running
  Flyway              Validated
  Authentication      Working
  Registration        Working
  Dashboard           Working
  Course Search API   ❌ Failing
  Catalog Page        ❌ Not Displaying Courses

------------------------------------------------------------------------

## 10. Conclusion

The issue is not architectural.

It is a **Hibernate parameter binding edge case** with PostgreSQL.

All infrastructure and application layers are stable except for the
public course search JPQL query.

------------------------------------------------------------------------

**Generated for Sprint 10 Debug Session**

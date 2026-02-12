# SPRINT 7C -- BUG LOG REPORT

Project: Educator Platform\
Sprint: 7C (Reopened Stabilization Sprint)\
Environment: Local (Spring Boot + Docker + Postman)

------------------------------------------------------------------------

## 1. Security & Authorization Bugs

### Bug 7C-01: Public Hierarchy Endpoint Returning 403

**Endpoint:** GET /api/hierarchy/roots\
**Issue:** Public endpoint incorrectly secured\
**Root Cause:** Missing permitAll() for /api/hierarchy/\*\*\
**Fix:** Added matcher in SecurityConfig\
**Status:** RESOLVED

------------------------------------------------------------------------

### Bug 7C-02: Role Prefix Mismatch (ADMIN vs ROLE_ADMIN)

**Issue:** JWT contained ADMIN but security expected ROLE_ADMIN\
**Root Cause:** Inconsistent authority prefixing\
**Fix:** Prefixed roles in JWT filter and standardized hasRole("ADMIN")\
**Status:** RESOLVED

------------------------------------------------------------------------

### Bug 7C-03: Admin Endpoint Accessible by Student (Postman Token Confusion)

**Issue:** Student appeared to access admin endpoints\
**Root Cause:** Postman environment variable using admin token
unintentionally\
**Fix:** Verified JWT decoding and environment correction\
**Status:** VERIFIED -- No backend issue

------------------------------------------------------------------------

## 2. Exception Handling Bugs

### Bug 7C-04: Duplicate Slug Returned 403

**Endpoint:** POST /api/admin/hierarchy\
**Issue:** Business validation mapped to 403 Forbidden\
**Root Cause:** IllegalArgumentException misused\
**Fix:** Implemented enterprise exception architecture\
**New Mapping:** DuplicateResourceException → 409 Conflict\
**Status:** RESOLVED

------------------------------------------------------------------------

### Bug 7C-05: No Centralized Error Handling

**Issue:** Inconsistent error responses across modules\
**Fix Implemented:** - ApiError model created - GlobalExceptionHandler
implemented - Custom exceptions added **Status:** RESOLVED

------------------------------------------------------------------------

## 3. Configuration Gaps

### Bug 7C-06: Missing .anyRequest().authenticated()

**Issue:** Security rule set incomplete\
**Fix:** Added fallback authentication rule\
**Status:** RESOLVED

------------------------------------------------------------------------

## 4. Remaining Pending Validation Items

These are NOT confirmed bugs, but require structured verification:

-   Course Management API full regression test
-   Enrollment module validation pass
-   Exam module verification pass
-   Security boundary re-validation in clean Postman workspace
-   API collection full automated verification pass

------------------------------------------------------------------------

## 5. Current System State

Authentication Layer: Stable\
JWT Filter: Stable\
SecurityConfig: Stable\
Exception Architecture: Enterprise-grade\
Hierarchy Module: Verified\
Other Modules: Pending structured pass

------------------------------------------------------------------------

## Sprint 7C Closure Position

Core architectural bugs resolved.\
Enterprise error handling implemented.\
Security hardened.\
Remaining work is structured verification, not debugging.

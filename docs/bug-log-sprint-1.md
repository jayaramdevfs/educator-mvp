# Bug Log – Sprint 1

This document captures the major bugs encountered during Sprint 1
and their resolutions. Primary tracking is done via GitHub Issues.

---

## Bug 1: PostgreSQL Timezone Error

Category: Backend / Database  
Severity: Critical  

Issue:
Application failed to start due to invalid timezone value.

Error:
FATAL: invalid value for parameter "TimeZone": "Asia/Calcutta"

Root Cause:
PostgreSQL rejected deprecated timezone value passed from JVM/OS.

Fix:
- Forced JVM timezone to UTC
- Added JDBC timezone override
- Updated Hibernate and Jackson timezone configuration

Status: Fixed

---

## Bug 2: Hibernate Dialect Detection Failure

Category: Backend / ORM  
Severity: High  

Issue:
Hibernate failed to auto-detect PostgreSQL dialect.

Fix:
- Explicitly configured PostgreSQL dialect in application.yml

Status: Fixed

---

## Bug 3: Spring Security Default Login Page & HTTP Basic Popup

Category: Security  
Severity: Medium  

Issue:
Unexpected default login page and browser authentication popup appeared.

Fix:
- Disabled form login
- Disabled HTTP Basic authentication explicitly

Status: Fixed

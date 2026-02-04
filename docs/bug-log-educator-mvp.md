This document captures the major bugs encountered during all the Sprints 
and their resolutions. Primary tracking is done via GitHub Issues.

# Bug Log – Sprint 1


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

# Bug Log – Sprint 2

## Bug 4: Encountered 403 Forbidden on admin hierarchy endpoints

🔹 Resolution

Temporarily permitted /api/admin/hierarchy/** in security config

Confirmed security behavior is correct

Role-based enforcement deferred to later sprint (planned)

👉 No unresolved bugs from Sprint 2.

## Sprint 3.1 Bug Log

### Resolved Bugs
- Hibernate ExceptionInInitializerError caused by non-null fields without defaults
- Lombok incompatibility with unsupported JDK versions
- Illegal manual assignment of JPA entity IDs
- Package-path mismatches causing compilation failures
- Missing setters in domain entities
- Lesson service compilation failures due to entity misalignment

### Notes
All identified issues were resolved without compromising domain design.
The backend now builds and starts reliably with Java 17.

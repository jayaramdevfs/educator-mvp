# Bug Log - Educator Platform

**Project:** Educator Platform - EdTech Solution  
**Version:** 1.1.2  
**Last Updated:** February 7, 2026  
**Tracking Method:** GitHub Issues (Primary) + This Document (Archive)

---

## Purpose

This document serves as a comprehensive archive of all bugs encountered during the Educator Platform development. It provides historical context, root cause analysis, and resolution details for future reference and knowledge transfer.

---

## Bug Severity Levels

| Level | Description | Response Time |
|-------|-------------|---------------|
| **Critical** | System down, data loss, security breach | Immediate |
| **High** | Major feature broken, significant user impact | Within 24 hours |
| **Medium** | Feature partially working, workaround available | Within 1 week |
| **Low** | Minor issue, cosmetic problem | Next sprint |

---

## Bug Status Types

- 🔴 **Open** - Bug identified, not yet assigned
- 🟡 **In Progress** - Actively being worked on
- 🟢 **Resolved** - Fix implemented and tested
- 🔵 **Closed** - Verified in production/staging
- ⚫ **Deferred** - Acknowledged but postponed
- 🟣 **Duplicate** - Same as another bug
- ⚪ **Won't Fix** - Decided not to fix

---

## Bug Template

```markdown
### [BUG-ID] - [Short Title]

**Sprint:** [Sprint Number]  
**Date Reported:** [YYYY-MM-DD]  
**Date Resolved:** [YYYY-MM-DD]  
**Status:** [Open/In Progress/Resolved/Closed/Deferred]  
**Severity:** [Critical/High/Medium/Low]  
**Category:** [Backend/Frontend/Database/Security/API/DevOps]  
**Component:** [Specific module/package]  
**Reporter:** [Name/Role]  
**Assignee:** [Name/Role]  
**GitHub Issue:** [#IssueNumber or N/A]

#### Description
[Clear description of the bug and its symptoms]

#### Steps to Reproduce
1. [Step 1]
2. [Step 2]
3. [Step 3]

#### Expected Behavior
[What should happen]

#### Actual Behavior
[What actually happens]

#### Error Message/Stack Trace
```
[Error logs, stack traces, or error messages]
```

#### Root Cause
[Technical explanation of why the bug occurred]

#### Resolution
[How the bug was fixed - technical details]

#### Impact
- **Users Affected:** [Number or percentage]
- **Business Impact:** [Description]
- **Data Impact:** [Any data loss or corruption]

#### Prevention
[What can be done to prevent similar bugs in the future]

#### Related Bugs
[Links to related bugs or issues]

#### Testing Notes
[How the fix was tested]
```

---

# Sprint 1 - Bug Log

### S1-B1 - PostgreSQL Timezone Error

**Sprint:** 1  
**Date Reported:** Sprint 1 Start  
**Date Resolved:** Sprint 1  
**Status:** 🟢 Resolved  
**Severity:** Critical  
**Category:** Backend / Database  
**Component:** Database Configuration  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Application failed to start due to invalid timezone value in PostgreSQL configuration.

#### Steps to Reproduce
1. Start Spring Boot application
2. Application attempts to connect to PostgreSQL
3. PostgreSQL rejects connection with timezone error

#### Expected Behavior
Application should start successfully and connect to PostgreSQL database.

#### Actual Behavior
Application crashes on startup with FATAL error.

#### Error Message/Stack Trace
```
FATAL: invalid value for parameter "TimeZone": "Asia/Calcutta"
```

#### Root Cause
PostgreSQL rejected deprecated timezone value "Asia/Calcutta" passed from JVM/OS. Modern PostgreSQL versions use IANA timezone database which deprecated this identifier in favor of "Asia/Kolkata".

#### Resolution
- Forced JVM timezone to UTC using system property: `-Duser.timezone=UTC`
- Added JDBC timezone override in connection URL: `?serverTimezone=UTC`
- Updated Hibernate timezone configuration: `spring.jpa.properties.hibernate.jdbc.time_zone=UTC`
- Configured Jackson for UTC timezone: `spring.jackson.time-zone=UTC`

#### Impact
- **Users Affected:** All (blocked startup)
- **Business Impact:** Development blocked until resolved
- **Data Impact:** None

#### Prevention
- Use UTC for all server-side timestamps
- Configure timezone explicitly in all layers (JVM, JDBC, ORM, serialization)
- Document timezone standards in project guidelines

#### Related Bugs
None

#### Testing Notes
- Verified application starts successfully
- Confirmed timestamps stored as UTC in database
- Tested with multiple timezone configurations

---

### S1-B2 - Hibernate Dialect Detection Failure

**Sprint:** 1  
**Date Reported:** Sprint 1 Start  
**Date Resolved:** Sprint 1  
**Status:** 🟢 Resolved  
**Severity:** High  
**Category:** Backend / ORM  
**Component:** Hibernate Configuration  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Hibernate failed to auto-detect PostgreSQL dialect, causing application startup failure.

#### Steps to Reproduce
1. Start Spring Boot application
2. Hibernate attempts to initialize
3. Fails to detect database dialect

#### Expected Behavior
Hibernate should auto-detect PostgreSQL dialect from JDBC connection.

#### Actual Behavior
Hibernate fails to initialize with dialect detection error.

#### Error Message/Stack Trace
```
[Error message not provided in original log]
```

#### Root Cause
Hibernate's auto-detection mechanism failed, possibly due to:
- JDBC driver version mismatch
- PostgreSQL version not recognized
- Missing dialect metadata

#### Resolution
Explicitly configured PostgreSQL dialect in `application.yml`:
```yaml
spring:
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

#### Impact
- **Users Affected:** All (blocked startup)
- **Business Impact:** Development blocked until resolved
- **Data Impact:** None

#### Prevention
- Always explicitly configure database dialect in production
- Don't rely on auto-detection for critical configuration
- Add to project setup checklist

#### Related Bugs
None

#### Testing Notes
- Verified application starts successfully
- Confirmed Hibernate uses correct dialect
- Tested entity mapping and queries

---

### S1-B3 - Spring Security Default Login Page & HTTP Basic Popup

**Sprint:** 1  
**Date Reported:** Sprint 1 Mid  
**Date Resolved:** Sprint 1  
**Status:** 🟢 Resolved  
**Severity:** Medium  
**Category:** Security  
**Component:** Spring Security Configuration  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Unexpected default login page and browser HTTP Basic authentication popup appeared when accessing public endpoints.

#### Steps to Reproduce
1. Start application
2. Access any public endpoint (e.g., `/api/auth/register`)
3. Browser shows login form or HTTP Basic popup

#### Expected Behavior
Public endpoints should be accessible without authentication prompts.

#### Actual Behavior
Spring Security's default form login and HTTP Basic authentication were enabled, causing authentication prompts.

#### Error Message/Stack Trace
```
[No error - UI behavior issue]
```

#### Root Cause
Spring Security enables form login and HTTP Basic authentication by default. These weren't explicitly disabled in SecurityFilterChain configuration for REST API usage.

#### Resolution
Explicitly disabled form login and HTTP Basic authentication in SecurityConfig:
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http
        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable())
        .csrf(csrf -> csrf.disable())
        // ... rest of config
    return http.build();
}
```

#### Impact
- **Users Affected:** All users accessing public endpoints
- **Business Impact:** Poor user experience, confusion during testing
- **Data Impact:** None

#### Prevention
- Explicitly configure all Spring Security features for REST APIs
- Add to security configuration checklist
- Include in security testing scenarios

#### Related Bugs
None

#### Testing Notes
- Verified no login prompts on public endpoints
- Confirmed JWT authentication still works
- Tested with multiple browsers

---

# Sprint 2 - Bug Log

### S2-B1 - 403 Forbidden on Admin Hierarchy Endpoints

**Sprint:** 2  
**Date Reported:** Sprint 2 Mid  
**Date Resolved:** Sprint 2  
**Status:** 🟢 Resolved  
**Severity:** High  
**Category:** Security  
**Component:** Spring Security / Authorization  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Admin hierarchy endpoints returned 403 Forbidden even with valid JWT token and ADMIN role.

#### Steps to Reproduce
1. Login as admin user
2. Obtain valid JWT token
3. Call POST `/api/admin/hierarchy` with Authorization header
4. Receive 403 Forbidden response

#### Expected Behavior
Admin user with valid JWT and ADMIN role should access admin hierarchy endpoints.

#### Actual Behavior
All requests to `/api/admin/hierarchy/**` returned 403 Forbidden.

#### Error Message/Stack Trace
```
HTTP 403 Forbidden
Access Denied
```

#### Root Cause
SecurityFilterChain configuration did not explicitly permit `/api/admin/hierarchy/**` pattern. Default deny-all rule was blocking access even with valid authentication.

#### Resolution
Temporarily permitted `/api/admin/hierarchy/**` in SecurityConfig:
```java
.requestMatchers("/api/admin/hierarchy/**").permitAll()
```

**Note:** This is a temporary fix. Role-based enforcement for admin endpoints is planned for Sprint 4.

#### Impact
- **Users Affected:** Admin users (development team)
- **Business Impact:** Blocked hierarchy management feature development
- **Data Impact:** None

#### Prevention
- Plan security configuration upfront for each new endpoint pattern
- Document security requirements in feature specifications
- Test authentication/authorization for all new endpoints

#### Related Bugs
- Related to S4-B1, S4-B2, S4-B3 (Sprint 4 security stabilization)

#### Testing Notes
- Verified admin can access hierarchy endpoints
- Confirmed public endpoints still work
- Noted need for proper role-based authorization in future sprint

---

# Sprint 3.1 - Bug Log

### S3.1-B1 - Hibernate ExceptionInInitializerError

**Sprint:** 3.1  
**Date Reported:** Sprint 3.1 Start  
**Date Resolved:** Sprint 3.1  
**Status:** 🟢 Resolved  
**Severity:** High  
**Category:** Backend / ORM  
**Component:** Course & Lesson Entities  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Hibernate threw ExceptionInInitializerError during entity initialization due to non-null fields without default values.

#### Steps to Reproduce
1. Create Course or Lesson entity with non-null constraints
2. Don't provide default values for required fields
3. Start application
4. Hibernate fails to initialize entities

#### Expected Behavior
Entities should initialize successfully with proper default values or allow null temporarily.

#### Actual Behavior
Application crashes on startup with ExceptionInInitializerError.

#### Error Message/Stack Trace
```
java.lang.ExceptionInInitializerError
Caused by: org.hibernate.HibernateException: Missing column or default value
```

#### Root Cause
JPA entities had fields marked with `@Column(nullable = false)` but no default values provided. Hibernate couldn't initialize these entities without explicit values.

#### Resolution
- Added default values where appropriate (e.g., `status = CourseStatus.DRAFT`)
- Made some fields nullable during entity creation, enforced by service layer
- Added `@Builder.Default` annotations for Lombok builder pattern
- Ensured all required fields have sensible defaults or are set explicitly

#### Impact
- **Users Affected:** None (development phase)
- **Business Impact:** Blocked Course/Lesson feature development
- **Data Impact:** None

#### Prevention
- Review all entity constraints before implementation
- Use builder defaults for non-null fields
- Test entity instantiation in unit tests

#### Related Bugs
- S3.1-B5 (Missing setters)

#### Testing Notes
- Verified entities instantiate successfully
- Confirmed default values applied correctly
- Tested with repository save operations

---

### S3.1-B2 - Lombok Incompatibility with JDK Version

**Sprint:** 3.1  
**Date Reported:** Sprint 3.1 Start  
**Date Resolved:** Sprint 3.1  
**Status:** 🟢 Resolved  
**Severity:** Medium  
**Category:** Backend / Build  
**Component:** Build Configuration  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Lombok annotation processing failed due to incompatibility between Lombok version and JDK version.

#### Steps to Reproduce
1. Build project with specific JDK version
2. Lombok annotations not processed correctly
3. Compilation fails with missing methods

#### Expected Behavior
Lombok should generate getters, setters, builders correctly for Java 17.

#### Actual Behavior
Compilation errors due to missing generated methods.

#### Error Message/Stack Trace
```
[Compilation errors for missing getter/setter methods]
```

#### Root Cause
Lombok version in pom.xml was not fully compatible with Java 17. Newer Lombok versions required for Java 17+ features.

#### Resolution
Updated Lombok dependency to latest compatible version:
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
</dependency>
```

#### Impact
- **Users Affected:** None (development phase)
- **Business Impact:** Blocked entity development
- **Data Impact:** None

#### Prevention
- Document compatible dependency versions
- Use dependency management tools (Spring Boot BOM)
- Test builds with clean Maven cache

#### Related Bugs
None

#### Testing Notes
- Verified all Lombok annotations work correctly
- Confirmed getters/setters generated
- Tested builders and constructors

---

### S3.1-B3 - Illegal Manual Assignment of JPA Entity IDs

**Sprint:** 3.1  
**Date Reported:** Sprint 3.1 Mid  
**Date Resolved:** Sprint 3.1  
**Status:** 🟢 Resolved  
**Severity:** Medium  
**Category:** Backend / ORM  
**Component:** Entity Repositories  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
JPA entities were manually assigned IDs, causing conflicts with database sequence generation.

#### Steps to Reproduce
1. Create entity with `@GeneratedValue(strategy = GenerationType.IDENTITY)`
2. Manually set ID before save: `entity.setId(123L)`
3. Attempt to save entity
4. Get constraint violation or unexpected behavior

#### Expected Behavior
IDs should be auto-generated by database sequences.

#### Actual Behavior
Manual ID assignment caused conflicts with database-generated IDs.

#### Error Message/Stack Trace
```
[Constraint violation or duplicate key errors]
```

#### Root Cause
Developer error - manually setting IDs on entities marked for auto-generation. JPA specification prohibits this for IDENTITY generation strategy.

#### Resolution
- Removed all manual ID assignments from service layer code
- Let JPA generate IDs automatically
- Added code review checkpoint to prevent this pattern
- Documented ID generation strategy in entity comments

#### Impact
- **Users Affected:** None (caught in development)
- **Business Impact:** Minor development delay
- **Data Impact:** None

#### Prevention
- Code review checklist item: No manual ID assignment
- Add linter rule if possible
- Document ID generation strategy in development guidelines

#### Related Bugs
None

#### Testing Notes
- Verified IDs auto-generated correctly
- Confirmed no constraint violations
- Tested with multiple entity saves

---

### S3.1-B4 - Package Path Mismatches

**Sprint:** 3.1  
**Date Reported:** Sprint 3.1 Mid  
**Date Resolved:** Sprint 3.1  
**Status:** 🟢 Resolved  
**Severity:** Low  
**Category:** Backend / Build  
**Component:** Package Structure  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Class files placed in wrong packages or package declarations didn't match directory structure, causing compilation failures.

#### Steps to Reproduce
1. Create class in wrong package directory
2. Or declare wrong package in class file
3. Maven compilation fails

#### Expected Behavior
Package declarations should match directory structure.

#### Actual Behavior
Compilation errors due to package mismatches.

#### Error Message/Stack Trace
```
[Compilation errors: class not found, package mismatch]
```

#### Root Cause
Manual file creation or refactoring led to package declaration mismatches with actual file locations.

#### Resolution
- Corrected all package declarations to match directory structure
- Reorganized files into proper packages
- Used IDE refactoring tools instead of manual file moves

#### Impact
- **Users Affected:** None (development phase)
- **Business Impact:** Minor development delay
- **Data Impact:** None

#### Prevention
- Always use IDE refactoring tools for package/class moves
- Enable IDE warnings for package mismatches
- Code review for package structure

#### Related Bugs
None

#### Testing Notes
- Verified clean Maven build
- Confirmed all classes in correct packages
- Tested package imports

---

### S3.1-B5 - Missing Setters in Domain Entities

**Sprint:** 3.1  
**Date Reported:** Sprint 3.1 Mid  
**Date Resolved:** Sprint 3.1  
**Status:** 🟢 Resolved  
**Severity:** Medium  
**Category:** Backend / ORM  
**Component:** Course & Lesson Entities  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Service layer couldn't update entity fields due to missing setter methods in domain entities.

#### Steps to Reproduce
1. Create entity with Lombok `@Getter` but no `@Setter`
2. Try to update entity fields in service layer
3. Compilation error: no setter method found

#### Expected Behavior
Entities should have setters for mutable fields.

#### Actual Behavior
Compilation failures due to missing setters.

#### Error Message/Stack Trace
```
[Compilation error: cannot find symbol - method setXxx()]
```

#### Root Cause
Lombok configuration used `@Getter` but forgot `@Setter` on some entities, or used `@Data` which should have included both but had conflicts.

#### Resolution
- Added `@Setter` annotation to all mutable entities
- Or used `@Data` which includes both getters and setters
- Verified all required fields have setters

#### Impact
- **Users Affected:** None (development phase)
- **Business Impact:** Blocked service layer implementation
- **Data Impact:** None

#### Prevention
- Use `@Data` for standard mutable entities
- Code review checklist: verify entity mutability requirements
- Test service layer update operations

#### Related Bugs
- S3.1-B1 (Hibernate initialization)

#### Testing Notes
- Verified all setters present
- Tested entity updates in service layer
- Confirmed persistence of changes

---

### S3.1-B6 - Lesson Service Compilation Failures

**Sprint:** 3.1  
**Date Reported:** Sprint 3.1 End  
**Date Resolved:** Sprint 3.1  
**Status:** 🟢 Resolved  
**Severity:** Medium  
**Category:** Backend / Service Layer  
**Component:** LessonService  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
LessonService failed to compile due to entity misalignment and method signature issues.

#### Steps to Reproduce
1. Implement LessonService with Course/Lesson entities
2. Entity changes not reflected in service layer
3. Compilation fails

#### Expected Behavior
Service layer should compile with current entity definitions.

#### Actual Behavior
Compilation errors due to outdated entity references.

#### Error Message/Stack Trace
```
[Compilation errors: incompatible types, method not found]
```

#### Root Cause
Entity refactoring (field additions, type changes) not immediately reflected in service layer code. Service methods used old field names or types.

#### Resolution
- Updated LessonService to match current entity definitions
- Fixed method signatures to use correct entity types
- Added proper null checks and validation

#### Impact
- **Users Affected:** None (development phase)
- **Business Impact:** Delayed lesson feature completion
- **Data Impact:** None

#### Prevention
- Refactor entities and services together
- Use IDE refactoring tools for type-safe changes
- Run compilation after each entity change

#### Related Bugs
- S3.1-B1, S3.1-B5 (Entity issues)

#### Testing Notes
- Verified clean compilation
- Tested all service methods
- Confirmed entity-service alignment

---

# Sprint 3.2 - Bug Log

### S3.2-B1 - Potential Duplication of LessonService Logic

**Sprint:** 3.2  
**Date Reported:** Sprint 3.2 Start  
**Date Resolved:** Sprint 3.2 Start  
**Status:** 🟢 Resolved  
**Severity:** Low  
**Category:** Backend / Service Layer  
**Component:** LessonService  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
During Sprint 3.2 planning, concern raised about potentially duplicating LessonService logic that was already implemented in Sprint 3.1.

#### Steps to Reproduce
1. Review Sprint 3.2 requirements
2. Compare with Sprint 3.1 implementation
3. Identify potential overlap

#### Expected Behavior
New sprint should build on existing implementation without duplication.

#### Actual Behavior
Risk of re-implementing already existing service methods.

#### Error Message/Stack Trace
```
N/A - Planning/Design issue
```

#### Root Cause
Unclear handover documentation from Sprint 3.1. Sprint 3.2 plan didn't fully account for what was already implemented.

#### Resolution
- Reviewed existing LessonService implementation from Sprint 3.1
- Verified all required methods already present
- Avoided unnecessary changes
- Updated Sprint 3.2 scope to focus only on new API endpoints

#### Impact
- **Users Affected:** None
- **Business Impact:** Avoided wasted development effort
- **Data Impact:** None

#### Prevention
- Improve sprint handover documentation
- Review existing implementation before starting new sprint
- Clear scope definition in sprint planning

#### Related Bugs
None

#### Testing Notes
- Verified existing implementation meets requirements
- No code changes needed
- Status: Closed without implementation

---

# Sprint 4 - Bug Log

### S4-B1 - Admin APIs Returned 403 Forbidden

**Sprint:** 4  
**Date Reported:** Sprint 4 Start  
**Date Resolved:** Sprint 4 Mid  
**Status:** 🟢 Resolved  
**Severity:** Critical  
**Category:** Security / Authorization  
**Component:** JWT Authentication Filter  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Admin endpoints returned 403 Forbidden despite valid JWT token with ADMIN role.

#### Steps to Reproduce
1. Login as admin user
2. Obtain valid JWT token with ADMIN role
3. Call any `/api/admin/**` endpoint with Authorization header
4. Receive 403 Forbidden

#### Expected Behavior
Valid JWT with ADMIN role should grant access to admin endpoints.

#### Actual Behavior
All admin endpoints returned 403 Forbidden regardless of valid JWT.

#### Error Message/Stack Trace
```
HTTP 403 Forbidden
Access Denied
```

#### Root Cause
JWT token was validated but not injected into Spring Security's SecurityContext. JwtAuthenticationFilter was missing from SecurityFilterChain, so Spring Security didn't know about the authenticated user.

#### Resolution
Added JwtAuthenticationFilter to SecurityFilterChain:
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    return http
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            // ... rest of config
        )
        .build();
}
```

#### Impact
- **Users Affected:** Admin users (development team)
- **Business Impact:** Blocked all admin feature development
- **Data Impact:** None

#### Prevention
- Test security configuration immediately after implementation
- Add integration tests for authentication flow
- Document filter chain order requirements

#### Related Bugs
- S4-B2, S4-B3 (related security issues)

#### Testing Notes
- Verified JWT authentication works
- Confirmed admin role grants access
- Tested with invalid tokens (properly rejected)

---

### S4-B2 - Lesson Admin APIs Always Returned 403

**Sprint:** 4  
**Date Reported:** Sprint 4 Mid  
**Date Resolved:** Sprint 4 Mid  
**Status:** 🟢 Resolved  
**Severity:** High  
**Category:** Security / Authorization  
**Component:** UserDetailsService  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Method-level security annotations on lesson admin APIs always returned 403, even with valid admin JWT.

#### Steps to Reproduce
1. Add `@PreAuthorize("hasRole('ADMIN')")` to lesson endpoints
2. Call endpoint with valid admin JWT
3. Receive 403 Forbidden

#### Expected Behavior
Method security should evaluate JWT roles and grant access.

#### Actual Behavior
Method security couldn't evaluate roles, always denied access.

#### Error Message/Stack Trace
```
HTTP 403 Forbidden
org.springframework.security.access.AccessDeniedException: Access Denied
```

#### Root Cause
Method security (`@PreAuthorize`) requires Spring Security to load user authorities via UserDetailsService. No UserDetailsService was configured, so Spring Security couldn't evaluate role expressions.

#### Resolution
Introduced CustomUserDetails and CustomUserDetailsService:
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}
```

#### Impact
- **Users Affected:** Admin users (lesson management)
- **Business Impact:** Blocked lesson admin features
- **Data Impact:** None

#### Prevention
- Configure UserDetailsService when using method security
- Or avoid method security, use URL-based authorization only
- Document security architecture decisions

#### Related Bugs
- S4-B1, S4-B3 (related security issues)

#### Testing Notes
- Verified method security evaluates correctly
- Confirmed role-based access control works
- Tested with different roles

---

### S4-B3 - Conflicting Authorization Checks

**Sprint:** 4  
**Date Reported:** Sprint 4 Mid  
**Date Resolved:** Sprint 4 End  
**Status:** 🟢 Resolved  
**Severity:** High  
**Category:** Security / Authorization  
**Component:** SecurityFilterChain + Method Security  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Duplicate and conflicting authorization evaluation between JWT-based roles and method-level security annotations caused inconsistent behavior.

#### Steps to Reproduce
1. Configure both URL-based security (`.hasRole("ADMIN")`) and method security (`@PreAuthorize`)
2. JWT provides roles, method security also evaluates roles
3. Conflicting or redundant checks

#### Expected Behavior
Single, clear authorization mechanism.

#### Actual Behavior
Duplicate authorization checks, potential for conflicts.

#### Error Message/Stack Trace
```
[Various 403 errors depending on which check failed]
```

#### Root Cause
Mixed authorization strategies: URL-based (SecurityFilterChain) and method-based (@PreAuthorize). Both evaluated authorities independently, creating confusion and potential conflicts.

#### Resolution
**Unified security approach using URL-based admin protection:**
- Removed all `@PreAuthorize` annotations from controllers
- Rely exclusively on SecurityFilterChain URL patterns:
```java
.requestMatchers("/api/admin/**").hasRole("ADMIN")
```
- This became the locked architectural decision for Sprint 4+

#### Impact
- **Users Affected:** Admin users (intermittent access issues)
- **Business Impact:** Security inconsistency, confusion during development
- **Data Impact:** None

#### Prevention
- Choose ONE authorization strategy and stick to it
- Document security architecture clearly
- Lock security decisions early in project

#### Related Bugs
- S4-B1, S4-B2 (related security issues)
- Resulted in Rule 4.1: URL-based security for admin endpoints

#### Testing Notes
- Verified consistent authorization behavior
- Confirmed no method security annotations remain
- Tested all admin endpoints with URL-based security

---

### S4-B4 - Lesson Creation Failed with 500 Error

**Sprint:** 4  
**Date Reported:** Sprint 4 End  
**Date Resolved:** Sprint 4 End  
**Status:** 🟢 Resolved  
**Severity:** High  
**Category:** Backend / Database  
**Component:** Lesson Entity  
**Reporter:** Dev Team  
**Assignee:** Dev Team  
**GitHub Issue:** N/A

#### Description
Lesson creation API returned 500 Internal Server Error due to NULL constraint violation on timestamps.

#### Steps to Reproduce
1. Call POST `/api/admin/lessons/course/{courseId}`
2. Provide valid lesson data
3. Receive 500 error

#### Expected Behavior
Lesson should be created successfully with auto-populated timestamps.

#### Actual Behavior
Database rejected insert due to NULL value in created_at field.

#### Error Message/Stack Trace
```
org.postgresql.util.PSQLException: ERROR: null value in column "created_at" violates not-null constraint
```

#### Root Cause
Lesson entity had `@Column(nullable = false)` on `created_at` and `updated_at` but no automatic timestamp generation configured. JPA wasn't setting these values before insert.

#### Resolution
Added Hibernate timestamp annotations to Lesson entity:
```java
@CreationTimestamp
@Column(name = "created_at", nullable = false, updatable = false)
private LocalDateTime createdAt;

@UpdateTimestamp
@Column(name = "updated_at", nullable = false)
private LocalDateTime updatedAt;
```

#### Impact
- **Users Affected:** Admin users (lesson creation blocked)
- **Business Impact:** Blocked lesson management feature
- **Data Impact:** None (no data created)

#### Prevention
- Always configure automatic timestamp generation for audit fields
- Add to entity creation checklist
- Test entity persistence immediately after creation

#### Related Bugs
- Similar to S3.1-B1 (entity initialization issues)

#### Testing Notes
- Verified timestamps auto-populated on insert
- Confirmed updated_at changes on update
- Tested with multiple lesson creations

---

# Bug Statistics

## Overall Bug Summary

| Sprint | Total Bugs | Critical | High | Medium | Low | Resolved |
|--------|-----------|----------|------|--------|-----|----------|
| Sprint 1 | 3 | 1 | 1 | 1 | 0 | 3 |
| Sprint 2 | 1 | 0 | 1 | 0 | 0 | 1 |
| Sprint 3.1 | 6 | 0 | 2 | 3 | 1 | 6 |
| Sprint 3.2 | 1 | 0 | 0 | 0 | 1 | 1 |
| Sprint 4 | 4 | 1 | 3 | 0 | 0 | 4 |
| **Total** | **15** | **2** | **7** | **4** | **2** | **15** |

## Bug Categories

| Category | Count | Percentage |
|----------|-------|------------|
| Security | 5 | 33.3% |
| Backend/ORM | 5 | 33.3% |
| Database | 2 | 13.3% |
| Backend/Service | 2 | 13.3% |
| Backend/Build | 1 | 6.7% |

## Resolution Time

| Sprint | Avg. Resolution Time | Notes |
|--------|---------------------|-------|
| Sprint 1 | Within Sprint | All resolved during Sprint 1 |
| Sprint 2 | Within Sprint | Resolved during Sprint 2 |
| Sprint 3.1 | Within Sprint | All resolved during Sprint 3.1 |
| Sprint 3.2 | Immediate | Caught during planning |
| Sprint 4 | Within Sprint | All resolved during Sprint 4 |

## Key Insights

1. **Security Configuration** was the primary source of bugs (5 total), particularly around JWT and authorization
2. **Sprint 4** had the highest severity bugs but all were security-related and resolved
3. **100% resolution rate** - all bugs resolved within their respective sprint
4. **Entity/ORM issues** dominated Sprint 3.1 due to initial domain modeling
5. **No data loss or corruption** occurred from any bug
6. **Prevention measures** documented for each bug category

---

**END OF BUG LOG**

**Note:** This is an append-only document. New bugs should be added to their respective sprint sections. Never delete or modify historical bug entries.

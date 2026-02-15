# Sprint10_Course_Search_Bug_Reportb

## Date

2026-02-14

------------------------------------------------------------------------

## Summary

During Sprint 10 testing, the following issue was identified:

-   Login initially failed due to incorrect frontend store wiring.
-   Enrollment button redirected to `/register` instead of calling
    backend.
-   Backend endpoints `/api/public/courses` and related routes returned
    HTTP 500.
-   Root cause identified as backend routing/controller mismatch or
    incorrect backend build running.

------------------------------------------------------------------------

## Detailed Problem Breakdown

### 1. Enrollment Redirect Issue

**Observed Behavior:** Clicking "Enroll Now" redirected user to
`/register` page.

**Root Cause:** The button was implemented as:

``` tsx
<Link href="/register">Enroll Now</Link>
```

No backend API call was being made.

**Fix Applied:** Replaced the `<Link>` with a client-side `EnrollButton`
component that:

-   Calls: `POST /api/learner/enrollments/course/{id}`
-   Redirects to dashboard on success
-   Redirects to login on 401

------------------------------------------------------------------------

### 2. Zustand Login Wiring Issue

**Error:** TS2554: Expected 2 arguments, but got 1.

**Root Cause:** `login()` store function expected:

``` ts
login(token: string, user: User)
```

But login page was calling:

``` ts
login(data)
```

**Fix Applied:** Updated login flow to:

1.  Call backend `/api/auth/login`
2.  Parse response `{ token, user }`
3.  Call `login(result.token, result.user)`

------------------------------------------------------------------------

### 3. Backend 500 Error

**Error Response:**

``` json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "No static resource api/public/courses"
}
```

**Meaning:** Spring Boot did not find controller mapping for:

    /api/public/courses

This indicates:

-   Incorrect backend version running
-   Controllers not loaded
-   Wrong project started
-   Build not refreshed

------------------------------------------------------------------------

## Final Root Cause

The backend instance currently running does not expose the expected
public course endpoints.

Frontend is now correctly wired.

The remaining issue is backend routing/build configuration.

------------------------------------------------------------------------

## Recommended Next Steps

1.  Stop backend completely.
2.  Navigate to correct backend root directory.
3.  Run:

```{=html}
<!-- -->
```
    mvn clean install
    mvn spring-boot:run

4.  Confirm in startup logs that mappings exist for:

```{=html}
<!-- -->
```
    /api/public/courses
    /api/auth/login
    /api/learner/enrollments

5.  Re-test enrollment flow.

------------------------------------------------------------------------

## Current System Status

Frontend: Stable and correctly wired\
Authentication Store: Fixed\
Enroll Button: Fixed\
Backend: Requires rebuild / controller verification

------------------------------------------------------------------------

End of Report

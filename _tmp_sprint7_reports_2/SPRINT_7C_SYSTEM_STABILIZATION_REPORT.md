# SPRINT 7C -- SYSTEM STABILIZATION & SECURITY INVESTIGATION REPORT

## Overview

Sprint 7C focused on runtime verification, security correction, and
enterprise-level exception architecture implementation.

## Issues Identified & Resolved

### 1. Public Hierarchy Endpoint Returned 403

Cause: Missing permitAll configuration for `/api/hierarchy/**` Fix:
Added matcher in SecurityConfig Status: RESOLVED

### 2. Role Prefix Mismatch

JWT roles contained ADMIN Security required ROLE_ADMIN Fix: Prefixed
roles in JWT filter and used `.hasRole("ADMIN")` Status: RESOLVED

### 3. Duplicate Slug Returned 403

Root cause: IllegalArgumentException mapped incorrectly Fix: Implemented
enterprise exception architecture with proper 409 mapping Status:
RESOLVED

## Enterprise Exception Architecture Introduced

New structure: - ApiError - DuplicateResourceException -
ResourceNotFoundException - BusinessValidationException -
GlobalExceptionHandler

Correct HTTP Mapping: 400 → Validation\
401 → Unauthorized\
403 → Forbidden\
404 → Not Found\
409 → Conflict\
500 → System Error

System Status: STABLE

# Project Governance Rules

**Purpose:** Define project governance, decision-making authority, and change management  
**Status:** LOCKED  
**Last Updated:** February 8, 2026

---

## Decision Authority

### Technical Decisions
- **Architecture Owner:** Has final authority on all technical architecture decisions
- **Product Owner:** Has final approval on all UI/UX and feature prioritization
- **Collaborative:** Architecture owner proposes, Product owner approves

### Scope Changes
- **No mid-sprint changes** - Scope is locked once sprint begins
- **Emergency only** - Only critical production bugs can break sprint discipline
- **Documented approval** - All scope changes require written approval

---

## Documentation Hierarchy

### Source of Truth Priority (Highest to Lowest)

1. **Educator_TDD (Technical Design Document)** - Architectural source of truth
2. **PROJECT_HANDOVER** - Onboarding and current state reference
3. **SPRINT_ROAD_MAP** - Sprint planning authority
4. **Sprint Handover Documents** - Sprint-specific details
5. **Supporting Documentation** - Operational guidelines only

### Conflict Resolution

If documents contradict each other:
1. Higher priority document wins
2. Update lower priority documents to align
3. Document the resolution

---

## Change Management

### Schema Changes
- Must have explicit migration path
- Must be tested with Sprint N-1 snapshot
- Must not break existing data
- Must be documented in TDD

### API Changes
- Breaking changes are PROHIBITED
- New endpoints/fields are allowed
- Deprecation requires 2 sprints notice
- Version bump required for breaking changes

### Architectural Changes
- Must be documented in TDD first
- Must be approved before implementation
- Must not violate locked decisions
- Must be communicated to all stakeholders

---

## Sprint Discipline

### No-Modification Rule

Once a sprint closes, its artifacts are IMMUTABLE:
- Sprint handover documents: NO edits
- Bug log entries: APPEND only, never modify
- Database snapshot: NEVER change
- Locked decisions: NEVER override

### Sprint Closure Requirements

Every sprint MUST deliver:
1. Git commit with summary and description
2. Updated README
3. Updated or appended BUG_LOG
4. Complete SPRINT_X_HANDOVER document
5. Updated PROJECT_HANDOVER
6. Database snapshot (mandatory)
7. Sprint closure confirmation

---

## Locked Decisions (Never Override)

These decisions are PERMANENT unless explicitly unlocked with written approval:

1. **Roles ≠ Subscriptions** - These are separate systems, never mix
2. **URL-based security** - No method-level security annotations
3. **Soft delete only** - `is_deleted` flag, never hard delete
4. **JWT stateless** - No server-side session storage
5. **Materialized path** - For lesson hierarchy (`path + depthLevel`)
6. **React Native only** - No native SDK development (Kotlin/Swift)
7. **No AI/paid APIs** - During development sprints
8. **Sprint 7 go-live** - Platform MUST be live by Sprint 7 end

---

## Quality Gates

### Before Merging Code
- [ ] Manually tested
- [ ] Security endpoints tested (401/403)
- [ ] Database constraints verified
- [ ] Previous sprint features regression tested
- [ ] Documentation updated

### Before Sprint Closure
- [ ] All acceptance criteria met
- [ ] All bugs resolved or documented
- [ ] Database snapshot created and tested
- [ ] All handover documents complete
- [ ] Cross-references validated

---

## Escalation Path

1. **Technical disagreement** → Architecture owner decides
2. **Product disagreement** → Product owner decides
3. **Process violation** → Team lead enforces governance
4. **Emergency change** → Document first, implement second

---

**Last Updated:** February 8, 2026  
**Authority:** Team Lead / Architecture Owner  
**Next Review:** Sprint 7 Completion

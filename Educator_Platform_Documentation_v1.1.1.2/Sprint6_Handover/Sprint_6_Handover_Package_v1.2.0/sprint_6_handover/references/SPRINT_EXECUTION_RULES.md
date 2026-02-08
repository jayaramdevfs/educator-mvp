# Sprint Execution Rules

**Purpose:** Define HOW sprints are executed  
**Status:** LOCKED  
**Last Updated:** February 8, 2026

---

## Guided Step-by-Step Execution

All sprints MUST follow this execution model:

### Step-by-Step Process

1. **One step at a time** - Never skip ahead
2. **One file at a time** - Complete current file before next
3. **Full file content** - Always provide complete file content
4. **One-line confirmation gate** - Wait for confirmation before proceeding
5. **No silent execution** - Always explain what you're doing

### Example Flow

```
Step 6.1: Create Exam Entity
Assistant: [Provides complete Exam.java file]
User: ✅
[Confirmation gate passed]

Step 6.2: Create ExamRepository
Assistant: [Provides complete ExamRepository.java file]
User: ✅
[Confirmation gate passed]
```

---

## File Impact Checklist

### Mandatory Before Sprint X.1

Before starting any step in a sprint, create and approve a **File Impact Checklist**:

#### Files to be Created
- List ALL files that will be created
- Include full package paths
- Estimate file sizes

#### Files to be Modified
- List ALL files that will be modified
- Include specific changes planned
- Note any risky changes

#### Files to be Deleted
- List any files to be deleted (rare)
- Document reason for deletion

### Checklist Template

```markdown
# Sprint 6 File Impact Checklist

## Files to Create
1. com/educator/exam/Exam.java
2. com/educator/exam/ExamRepository.java
3. com/educator/exam/ExamService.java
...

## Files to Modify
1. application.yml - Add exam configuration
2. SecurityConfig.java - Add exam endpoints
...

## Files to Delete
None

## Approval
- [ ] Architecture owner approved
- [ ] Checklist complete
- [ ] Ready to proceed
```

---

## Sprint Start Protocol

### Before Starting Sprint

1. Review SPRINT_ROAD_MAP for scope
2. Review Sprint Handover from previous sprint
3. Create File Impact Checklist
4. Get checklist approved
5. Start new chat thread
6. Begin execution

### During Sprint

- Follow step-by-step execution
- One file at a time
- Complete file content
- Wait for confirmation gates
- Update documentation as you go

### Sprint Closure

1. Create git commit (summary + description)
2. Update README
3. Append to BUG_LOG
4. Create SPRINT_X_HANDOVER
5. Update PROJECT_HANDOVER
6. Create database snapshot
7. Get sprint closure approval

---

## Error Recovery

### If You Make a Mistake

1. **Stop immediately** - Don't compound the error
2. **Document the mistake** - What went wrong?
3. **Propose fix** - How to correct it?
4. **Get approval** - Don't fix silently
5. **Update BUG_LOG** - Record for future reference

### If Sprint Gets Blocked

1. **Identify blocker** - What's preventing progress?
2. **Document blocker** - Add to handover notes
3. **Propose solution** - How to unblock?
4. **Escalate if needed** - Get team lead involved
5. **Update timeline** - Adjust expectations

---

## Code Quality Standards

### Java Code
- Follow Spring Boot conventions
- Use JavaDoc for public methods
- Meaningful variable names
- No magic numbers
- Handle exceptions properly

### Database
- All tables have audit fields (`created_at`, `updated_at`)
- All tables support soft delete (`is_deleted`)
- All foreign keys have proper constraints
- All indexes are documented

### APIs
- RESTful conventions
- Consistent error responses
- Proper HTTP status codes
- Pagination where applicable

---

## Testing Requirements

### Manual Testing (Required Now)
- Test positive path
- Test negative path (401/403)
- Test edge cases
- Test with real database state
- Regression test previous features

### Automated Testing (Sprint 7)
- Unit tests for service layer
- Integration tests for API layer
- Security tests
- Database constraint tests

---

## Documentation Requirements

### During Development
- Update TDD for new entities/APIs
- Update README for new features
- Append to BUG_LOG for bugs found
- Keep sprint handover notes

### At Sprint End
- Finalize sprint handover document
- Update PROJECT_HANDOVER
- Verify all cross-references
- Create database snapshot documentation

---

**Last Updated:** February 8, 2026  
**Mandatory For:** All Sprints  
**Next Review:** Never (these are permanent rules)

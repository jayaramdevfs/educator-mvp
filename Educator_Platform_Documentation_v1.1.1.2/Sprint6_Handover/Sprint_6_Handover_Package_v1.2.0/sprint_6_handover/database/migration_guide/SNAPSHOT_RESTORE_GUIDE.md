# Database Snapshot Restore Guide

**Purpose:** Step-by-step guide for restoring database snapshots
**Last Updated:** February 8, 2026

---

## Overview

Database snapshots are created at the end of every sprint and serve as the baseline for the next sprint. This guide explains how to restore snapshots safely.

---

## Available Snapshots

| Snapshot | Sprint | Date Created | Status |
|----------|--------|--------------|--------|
| `sprint5_baseline_data.sql` | Sprint 5 | Feb 8, 2026 | ✅ Current Baseline |

---

## Prerequisites

- PostgreSQL 15 installed
- Database `educator_db` exists
- User `educator_user` has permissions
- Docker running (if using containerized PostgreSQL)

---

## Restore Procedure

### Step 1: Stop the Application

```bash
# If running via Maven
Ctrl+C to stop spring-boot:run

# If running as JAR
kill <pid-of-java-process>
```

### Step 2: Backup Current Database (Optional but Recommended)

```bash
pg_dump -U educator_user -d educator_db -F c -f backup_before_restore_$(date +%Y%m%d_%H%M%S).dump
```

### Step 3: Drop Existing Data

```sql
-- Connect to database
psql -U educator_user -d educator_db

-- Drop all tables (BE CAREFUL!)
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO educator_user;
GRANT ALL ON SCHEMA public TO public;

-- Exit psql
\q
```

### Step 4: Restore Snapshot

```bash
# Navigate to snapshot directory
cd database/snapshots/

# Restore using psql with --disable-triggers flag
psql -U educator_user -d educator_db --disable-triggers < sprint5_baseline_data.sql
```

**Important:** The `--disable-triggers` flag is required because the snapshot contains data that may trigger foreign key or constraint violations during restore.

### Step 5: Verify Restore

```sql
-- Connect to database
psql -U educator_user -d educator_db

-- Check tables exist
\dt

-- Verify data counts
SELECT 'users' as table_name, COUNT(*) FROM users
UNION ALL
SELECT 'roles', COUNT(*) FROM roles
UNION ALL
SELECT 'courses', COUNT(*) FROM courses
UNION ALL
SELECT 'lessons', COUNT(*) FROM lessons
UNION ALL
SELECT 'enrollments', COUNT(*) FROM enrollments
UNION ALL
SELECT 'lesson_progress', COUNT(*) FROM lesson_progress;

-- Expected output for Sprint 5 snapshot:
-- users: 2
-- roles: 6
-- courses: 1
-- lessons: 1
-- enrollments: 0
-- lesson_progress: 0

-- Exit psql
\q
```

### Step 6: Restart Application

```bash
mvn spring-boot:run
```

Application should start without errors. Hibernate will NOT drop/create tables because the schema already exists.

---

## Alternative: Docker-Based Restore

If using Docker for PostgreSQL:

```bash
# Stop and remove existing container
docker-compose down -v

# Start fresh PostgreSQL container
docker-compose up -d postgres

# Wait for PostgreSQL to be ready
sleep 5

# Restore snapshot
docker exec -i postgres_container_name psql -U educator_user -d educator_db --disable-triggers < database/snapshots/sprint5_baseline_data.sql
```

---

## Troubleshooting

### Error: "permission denied for schema public"

**Solution:**
```sql
GRANT ALL ON SCHEMA public TO educator_user;
GRANT ALL ON SCHEMA public TO public;
```

### Error: "relation already exists"

**Solution:** You didn't drop the schema properly. Repeat Step 3.

### Error: "violates foreign key constraint"

**Solution:** Use `--disable-triggers` flag in restore command.

### Error: "password authentication failed"

**Solution:** Verify PostgreSQL credentials match `application.yml`:
- Username: `educator_user`
- Password: `educator_pass`
- Database: `educator_db`

---

## Creating New Snapshots

At the end of each sprint, create a new snapshot:

```bash
# Generate snapshot (data only, no schema)
pg_dump -U educator_user -d educator_db \
  --data-only \
  --inserts \
  --column-inserts \
  -f database/snapshots/sprint${SPRINT_NUMBER}_baseline_data.sql

# Example for Sprint 6:
pg_dump -U educator_user -d educator_db \
  --data-only \
  --inserts \
  --column-inserts \
  -f database/snapshots/sprint6_baseline_data.sql
```

---

## Snapshot Best Practices

1. **Always create snapshot at sprint end** - This is mandatory
2. **Test snapshot restore** - Verify it works before sprint closure
3. **Version snapshots** - Use sprint number in filename
4. **Document changes** - Note any schema changes in snapshot
5. **Keep all snapshots** - Never delete historical snapshots

---

## Emergency Rollback

If Sprint 6 breaks the database irreparably:

```bash
# 1. Stop application
# 2. Drop all tables (Step 3 above)
# 3. Restore Sprint 5 snapshot
psql -U educator_user -d educator_db --disable-triggers < database/snapshots/sprint5_baseline_data.sql
# 4. Restart application
mvn spring-boot:run
```

This restores you to the Sprint 5 baseline.

---

**Last Updated:** February 8, 2026  
**Maintained By:** Educator Platform Team  
**Questions?** Contact team lead or refer to DATABASE_STATE.md

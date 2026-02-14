# Local Development Credentials

> **WARNING:** This file is for LOCAL DEVELOPMENT ONLY.
> Do NOT commit real production credentials. Add this file to `.gitignore` if needed.

---

## PostgreSQL Database

| Property | Value |
|----------|-------|
| Host | `localhost` |
| Port | `5432` |
| Database | `educator` |
| Username | `educator` |
| Password | `educator` |
| JDBC URL | `jdbc:postgresql://localhost:5432/educator?options=-c%20TimeZone=UTC` |
| Driver | `org.postgresql.Driver` |
| Dialect | `PostgreSQLDialect` |
| DDL Mode | `validate` (schema managed by Flyway) |

---

## Backend (Spring Boot) - Port 8080

### JWT Configuration

| Property | Value |
|----------|-------|
| Secret | `educator-secret-key-change-later` |
| Expiry | `3600000` ms (1 hour) |
| Algorithm | HMAC-SHA256 |

### Bootstrap Admin Account

| Property | Value |
|----------|-------|
| Email | `jayaramadmin@educate.com` |
| Password | `Rama@1994` |
| Role | `ADMIN` |

### CORS

| Property | Value |
|----------|-------|
| Allowed Origins | `http://localhost:3000`, `http://127.0.0.1:3000` |

### Actuator Endpoints

| Endpoint | URL |
|----------|-----|
| Health | `http://localhost:8080/actuator/health` |
| Info | `http://localhost:8080/actuator/info` |

---

## Frontend (Next.js) - Port 3000

| Property | Value |
|----------|-------|
| API Base URL | `http://localhost:8080` |
| Env Variable | `NEXT_PUBLIC_API_BASE_URL` |

---

## Test Database (H2 In-Memory)

| Property | Value |
|----------|-------|
| URL | `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL` |
| Username | `sa` |
| Password | *(empty)* |
| DDL Mode | `create-drop` |
| Flyway | Disabled |

---

## Environment Variables (Production Override)

These env vars override defaults when set:

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=educator
DB_USERNAME=educator
DB_PASSWORD=educator

# Connection Pool (Prod profile)
DB_POOL_MAX_SIZE=20
DB_POOL_MIN_IDLE=5
DB_POOL_CONNECTION_TIMEOUT_MS=30000
DB_POOL_IDLE_TIMEOUT_MS=600000
DB_POOL_MAX_LIFETIME_MS=1800000

# JWT
JWT_SECRET=educator-secret-key-change-later
JWT_EXPIRATION_MS=3600000

# Admin Bootstrap
BOOTSTRAP_ADMIN_EMAIL=jayaramadmin@educate.com
BOOTSTRAP_ADMIN_PASSWORD=Rama@1994

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:3000

# Server
SERVER_PORT=8080

# Frontend
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

---

## Quick Start Commands

```bash
# 1. Start PostgreSQL (ensure it's running on port 5432)

# 2. Start Backend
cd backend
./mvnw spring-boot:run

# 3. Start Frontend
cd frontend
npm run dev
```

## Connection Verification

```bash
# Test DB connection
psql -h localhost -p 5432 -U educator -d educator

# Test Backend health
curl http://localhost:8080/actuator/health

# Test Frontend
curl http://localhost:3000
```

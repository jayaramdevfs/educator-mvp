# API Contract Guidelines

**Purpose:** Ensure stable, consistent APIs for frontend/mobile integration  
**Last Updated:** February 8, 2026

---

## API Versioning

### URL Structure
```
/api/v1/{resource}
```

- **v1** - Current version
- Future breaking changes require v2, v3, etc.
- Version in URL, not header

### Version Change Policy
- New endpoints: Add to current version
- New optional fields: Add to current version
- Breaking changes: Create new version
- Deprecate old version after 2 sprints

---

## General API Rules

### Request/Response Format
- Content-Type: `application/json`
- Character encoding: UTF-8
- Date format: ISO-8601 (`2026-02-08T10:30:00Z`)
- Boolean: true/false (not 1/0 or "true"/"false")

### ID Types
- All IDs are `Long` (not Integer, not String)
- IDs are always positive numbers
- ID `null` means "not set"

### Pagination
```json
{
  "content": [...],
  "page": 0,
  "size": 10,
  "totalElements": 100,
  "totalPages": 10
}
```

- Default page size: 10
- Max page size: 100
- Page numbers start at 0

---

## Error Responses

### Standard Error Format
```json
{
  "timestamp": "2026-02-08T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Email already exists",
  "path": "/api/auth/register"
}
```

### HTTP Status Codes
- `200 OK` - Success
- `201 Created` - Resource created
- `400 Bad Request` - Validation error
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - Not authorized
- `404 Not Found` - Resource doesn't exist
- `409 Conflict` - Business rule violation
- `500 Internal Server Error` - Server error

---

## Security

### Authentication
```http
Authorization: Bearer <jwt-token>
```

- Token expires in 24 hours
- Include in ALL protected endpoints
- Public endpoints don't need token

### CORS
- Allow credentials: true
- Allow origins: localhost:5173 (dev), production-domain (prod)
- Allow methods: GET, POST, PUT, DELETE
- Allow headers: Authorization, Content-Type

---

## Mobile Safety Rules

### No Browser-Only Assumptions
- Don't assume cookies work
- Don't assume localStorage exists
- Don't send HTML in responses
- Don't rely on referrer header

### Mobile-Friendly Responses
- Keep response sizes small
- Support resumable downloads
- Handle slow connections gracefully
- Provide clear error messages

---

**Last Updated:** February 8, 2026  
**Applies To:** All API endpoints  
**Next Review:** Sprint 6 Implementation

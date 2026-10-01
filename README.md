# HMS Backend

A REST API for a small Hospital Management System, built with **Spring Boot** and **PostgreSQL**. It manages doctors, patients and appointments.

I built this as a learning project to practice Spring Boot fundamentals: layered architecture, validation, centralized error handling, pagination, transactions and concurrency control. The interesting parts are the design decisions, described below.

## Tech Stack

- Java 17+
- Spring Boot (Web MVC, Data JPA, Validation)
- Hibernate
- PostgreSQL

## Quick Start

**1. Create the database**

```bash
createdb hms
psql -d hms -f db/schema.sql
```

**2. Configure the connection** in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hms
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# The schema is managed in SQL, so Hibernate only checks that it matches the entities
spring.jpa.hibernate.ddl-auto=validate
```

**3. Run the app**

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

## API

### Doctors

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/doctors` | Create a doctor |
| `GET` | `/doctors?size=10&cursor=...` | List doctors (cursor pagination) |
| `GET` | `/doctors/{id}` | Get a doctor |
| `PATCH` | `/doctors/{id}` | Update a doctor (partial) |
| `DELETE` | `/doctors/{id}` | Delete a doctor |

### Patients

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/patients` | Create a patient |
| `GET` | `/patients?size=10&cursor=...` | List patients (cursor pagination) |
| `GET` | `/patients/{id}` | Get a patient |
| `PATCH` | `/patients/{id}` | Update a patient (partial) |
| `DELETE` | `/patients/{id}` | Delete a patient |

### Appointments

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/appointments` | Book an appointment |
| `GET` | `/appointments` | List appointments |
| `GET` | `/appointments/{id}` | Get an appointment |
| `PATCH` | `/appointments/{id}/status` | Complete or cancel an appointment |

### Example: book an appointment

```http
POST /appointments
Content-Type: application/json

{
  "doctorId": "0192f3a4-...",
  "patientId": "0192f3a5-...",
  "appointmentDate": "2026-10-05",
  "startTime": "10:30"
}
```

Slot rules:

- Appointments are 30 minutes long. The server computes the end time.
- The start time must be on the hour or half hour (`10:00`, `10:30`), with no seconds.
- Slots in the past are rejected.

### Pagination

List endpoints use cursor pagination. Pass `size` (1–20, default 10) and, for the next page, the `nextCursor` from the previous response:

```http
GET /doctors?size=3
```

```json
{
  "content": [ { "id": "...", "name": "..." }, ... ],
  "nextCursor": "aWQ6MDE5MmYz...",
  "hasNext": true
}
```

```http
GET /doctors?size=3&cursor=aWQ6MDE5MmYz...
```

When `hasNext` is `false`, `nextCursor` is `null` and there are no more results.

### Updates and versions

`PATCH` requests change only the fields that are sent. Fields that are omitted (or `null`) are left unchanged.

Updates include the `version` the client last read. If the record was changed by someone else in the meantime, the API returns `409 Conflict`, and the client should reload and retry:

```json
{
  "version": 3,
  "phone": "9876543210"
}
```

### Errors

All errors use the standard [Problem Details](https://www.rfc-editor.org/rfc/rfc9457) format (`application/problem+json`):

```json
{
  "type": "about:blank",
  "title": "Validation Error",
  "status": 400,
  "detail": "Request validation failed",
  "instance": "/patients",
  "errors": {
    "phone": "Invalid phone number"
  }
}
```

| Status | When |
|---|---|
| `400` | Validation failed, malformed JSON, invalid cursor, invalid slot |
| `404` | Resource not found |
| `405` / `415` | Wrong HTTP method or content type |
| `409` | Duplicate phone/email, slot already booked, concurrent modification |
| `500` | Unexpected error (details are logged, never returned) |

## Design Decisions

### Cursor pagination instead of offset

`OFFSET` gets slower the deeper you page, because PostgreSQL reads and discards every skipped row, and inserts between requests shift the pages. Instead, each page asks for rows **after the last id seen**:

```sql
SELECT ... FROM doctors WHERE id > ? ORDER BY id FETCH FIRST ? ROWS ONLY
```

- The query fetches `size + 1` rows. If the extra row comes back, there is a next page, so no `COUNT(*)` query is needed.
- The cursor is the last id, Base64-encoded so clients treat it as opaque.
- Primary keys are **UUIDv7**, which are time-ordered. Sorting by id therefore follows creation order, and new rows always land on the last page. Random (v4) UUIDs would give an arbitrary order and fragment the index.

### Optimistic locking for concurrent updates

Two users editing the same record at once could silently overwrite each other's changes (a *lost update*). Database transactions don't prevent this at PostgreSQL's default `READ COMMITTED` isolation level.

Entities have a `@Version` column. Hibernate adds it to every update:

```sql
UPDATE patients SET ..., version = 4 WHERE id = ? AND version = 3
```

If another transaction got there first, the update matches zero rows and Hibernate throws, which the API maps to `409 Conflict`. The service also compares the client's `version` up front, to reject edits made from stale data.

### Double booking prevented by the database

Checking "is this slot free?" in Java and then inserting has a race: two requests can both see the slot as free and both insert. Optimistic locking can't help, because a booking is a new row with no version yet.

Instead, PostgreSQL enforces it with **partial unique indexes**:

```sql
CREATE UNIQUE INDEX uk_doctor_active_slot
    ON appointments (doctor_id, appointment_date, start_time)
    WHERE status <> 'CANCELLED';

CREATE UNIQUE INDEX uk_patient_active_slot
    ON appointments (patient_id, appointment_date, start_time)
    WHERE status <> 'CANCELLED';
```

- Fixed 30-minute slots aligned to `:00` and `:30` make overlaps impossible, so a unique start time is enough.
- The `WHERE` clause frees a slot again when its appointment is cancelled.
- The second index stops a patient from being booked with two doctors at the same time.

The second concurrent insert fails with a constraint violation, which the API maps to `409` with a message based on the constraint name.

### Centralized error handling

A single `@RestControllerAdvice` extends `ResponseEntityExceptionHandler`, so Spring's built-in errors (malformed JSON, wrong method, validation failures) and the application's own exceptions all return the same Problem Details format. Customizations for built-in errors override the parent's `handleXxx` methods rather than declaring duplicate `@ExceptionHandler`s.

### Partial updates with dirty checking

Update DTOs are records where `null` means "leave unchanged." The service loads the entity inside a `@Transactional` method and changes only the provided fields. Hibernate's dirty checking writes the changes on commit, without an explicit `save()`.

### The database as the last line of defense

Request DTOs are validated with Bean Validation, but the schema also enforces the rules (`NOT NULL`, `UNIQUE` on phone and email, `CHECK` constraints), so data stays valid even if application code has a bug. Enums such as gender are stored as `VARCHAR` with a `CHECK` constraint rather than a native PostgreSQL enum, which maps cleanly with `@Enumerated(EnumType.STRING)`.

## Project Structure

The code follows a standard layered layout under `com.hms.backend`:

- **Controllers** handle HTTP and request validation.
- **Services** hold business rules and transactions.
- **Repositories** are Spring Data JPA interfaces.
- **DTOs** (Java records) define the API's request and response shapes, so entities are never exposed directly.
- **Exceptions** contain custom exceptions and the global exception handler.

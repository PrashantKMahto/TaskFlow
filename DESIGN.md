# TaskFlow Lite — Design

## Purpose

TaskFlow Lite is a web task-management product for a small team. It demonstrates a complete, approachable IT application: a browser UI, a Java REST API, persisted data, input validation, error handling, and tests.

## Main capabilities

- Create, view, edit, delete, and filter tasks.
- Track title, description, priority, due date, creation date, and workflow status.
- Move work through `TODO`, `IN_PROGRESS`, and `DONE`.

## Architecture

```text
Browser (HTML, CSS, JavaScript)
  → Spring REST Controller
    → Service (business rules and mapping)
      → JPA Repository
        → H2 file database
```

Spring Boot serves the dashboard and API together, so the application runs with one command.

## Technical choices

| Layer | Technology |
| --- | --- |
| Back end | Java 21, Spring Boot, Spring Web |
| Persistence | Spring Data JPA, H2 file database |
| Validation | Jakarta Bean Validation |
| Front end | HTML, CSS, vanilla JavaScript |
| Tests | JUnit 5, Spring Boot Test, MockMvc |
| Build | Maven |

## API contract

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/tasks` | Lists tasks; accepts optional `status` filter |
| `GET` | `/api/tasks/{id}` | Gets a task |
| `POST` | `/api/tasks` | Creates a task |
| `PUT` | `/api/tasks/{id}` | Updates a task |
| `DELETE` | `/api/tasks/{id}` | Deletes a task |

`TaskRequest` validates a required 3–100 character title and a description of no more than 500 characters. A global exception handler returns consistent JSON for validation failures and missing tasks.

## Implementation prompt

```prompt
Build a complete, beginner-friendly Java web application named "TaskFlow Lite". Use Java 21, Maven, Spring Boot, Spring Web, Spring Data JPA, Bean Validation, and an H2 file database. It must run with `mvn spring-boot:run`.

Create a task-management dashboard served by Spring Boot. The vanilla HTML/CSS/JavaScript front end must list, create, edit, delete, and filter tasks. A task has id, title, description, status (TODO, IN_PROGRESS, DONE), priority (LOW, MEDIUM, HIGH), dueDate, and createdAt. Implement REST endpoints for listing (including optional status filtering), reading, creating, updating, and deleting tasks.

Use controller, service, repository, model, dto, and exception packages. Use request/response DTOs, validation, clear JSON error responses, startup sample data, focused explanatory comments, a responsive UI, README documentation, and integration tests for create, filter, update, and missing-task behavior.
```

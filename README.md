# TaskFlow Lite

TaskFlow Lite is a small, complete task-management web product. It demonstrates a standard Java IT-project structure: a browser UI, REST API, service layer, database persistence, input validation, error responses, and automated integration tests.

## Prerequisites

- Java 21 or later
- Maven 3.9 or later

## Run the application

```bash
cd taskflow-lite
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080). The first launch creates a local H2 database in `data/` and adds three sample tasks. The H2 development console is available at [http://localhost:8080/h2-console](http://localhost:8080/h2-console); use JDBC URL `jdbc:h2:file:./data/taskflow`.

## Test

```bash
mvn test
```

The integration tests cover creating and updating tasks, status filtering, and the JSON response for a missing task.

## Architecture

```text
Browser (HTML/CSS/JavaScript)
        ↓ JSON over HTTP
TaskController → TaskService → TaskRepository → H2 database
```

- `controller`: REST endpoints and HTTP response statuses.
- `service`: use-case/business logic and DTO mapping.
- `repository`: Spring Data JPA persistence queries.
- `model`: database entity and enums.
- `dto`: public API request and response shapes.
- `exception`: consistent JSON errors for clients.

## API examples

List every task:

```bash
curl http://localhost:8080/api/tasks
```

Filter tasks:

```bash
curl "http://localhost:8080/api/tasks?status=TODO"
```

Create a task:

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Review release plan","description":"Confirm owners and milestones","status":"TODO","priority":"HIGH","dueDate":"2026-11-10"}'
```

## Notes

This intentionally focuses on a single local demonstration user. Authentication, authorization, CI/CD, and production database settings would be added before a real deployment.

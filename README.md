# 🌟 Spring Boot - Task Manager API

A **Spring Boot REST API** to manage tasks, demonstrating clean architecture, interface-driven design, and proper unit testing.  
This project is designed as a portfolio-quality demonstration of backend development skills.

---

## 🛠️ Features

- Create, list, and complete tasks
- Filter tasks by status
- Pagination and sorting support
- Data validation for task creation
- Exception handling for missing tasks
- Fully tested with **unit tests** and **controller tests**
- Postman collection included for manual API testing

---

## 💻 Tech Stack

- Java 17
- Spring Boot
    - Spring Web
    - Spring Data JPA
    - H2 in-memory database (for testing and development)
- Lombok
- JUnit 5 + Mockito
- Maven
- Postman (for API testing)

---

## ⚡ Getting Started

### Prerequisites

- Java 17+
- Maven
- Postman (optional, for API testing)

### Run the application

```bash
git clone <repo-url>
cd spring-task-manager
mvn clean install
mvn spring-boot:run
```

The API will start at: `http://localhost:8080` and you can access the H2 console at `http://localhost:8080/h2-console`

- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: leave empty

You can now create tasks via the API and see them in the H2 console immediately. No external database setup is required.

---

## 📝 API Endpoints

### 1️⃣ Create Task

**POST** `/v1/api/tasks`

**Request Body:**

```json
{
  "title": "Spring Boot Project",
  "description": "Practice REST APIs",
  "status": "IN_PROGRESS"
}
```

**Response (201 Created):**

```json
{
  "id": 1,
  "title": "Spring Boot Project",
  "description": "Practice REST APIs",
  "status": "IN_PROGRESS",
  "createdAt": "2026-02-16T00:00:00"
}
```

---

### 2️⃣ Get All Tasks

**GET** `/v1/api/tasks`

**Optional Query Parameters:**

- `status` – Filter by task status (`PENDING`, `COMPLETED`, `IN_PROGRESS`)
- `sortBy` – Sort field (default: `createdAt`)
- `limit` – Maximum number of results

**Example:** `/v1/api/tasks?status=PENDING&sortBy=createdAt&limit=5`

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "title": "Spring Boot Project",
    "description": "Practice REST APIs",
    "status": "PENDING",
    "createdAt": "2026-02-16T00:00:00"
  }
]
```

---

### 3️⃣ Get Tasks by Status

**GET** `/v1/api/tasks/status?status=PENDING`

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "title": "Spring Boot Project",
    "description": "Practice REST APIs",
    "status": "PENDING",
    "createdAt": "2026-02-16T00:00:00"
  }
]
```

---

### 4️⃣ Complete Task

**POST** `/v1/api/tasks/{id}/complete`

**Response (201 Created):**

```json
{
  "id": 1,
  "title": "Spring Boot Project",
  "description": "Practice REST APIs",
  "status": "COMPLETED",
  "createdAt": "2026-02-16T00:00:00"
}
```

**Error Response (Task not found, 500):**

```json
{
  "error": "Task not found for id 99"
}
```

---

## 🧪 Testing

### Run Tests

Unit and Controller Tests:

```bash
mvn clean verify
```

✅ All tests currently pass

### Postman Collection

A Postman collection is included at /postman:

- `task-manager.postman_collection.json`
- `task-manager.postman_environment.json`

Steps to Use:

1. Open Postman and import both files.
2. Select the environment local and set baseUrl to: `http://localhost:8080`
3. Run requests manually or with the collection runner
4. All requests use Basic Auth (username = sa, password = sa).

---

## 📦 Project Structure

- **Controller:** Handles HTTP requests
- **Service:** Business logic (interface-driven)
- **Repository:** Data access layer (JPA)
- **DTO:** Data transfer objects
- **Mapper:** Maps entities to responses
- **Exceptions:** Custom exceptions
- **Utilities:** Helper methods for tests

---



# Spring Boot Task Manager

A simple Spring Boot application to manage tasks.  
It supports creating tasks, completing them, and listing tasks with filters and sorting.

## Features

- Create a task with title and description
- Complete a task by ID
- List tasks filtered by status or sorted
- Input validation for task creation
- Unit tests using MockMvc

## Technologies

- Java 17
- Spring Boot
- Spring Web
- Spring Security
- JUnit 5
- Mockito
- Maven

## API Endpoints

- POST /v1/api/tasks → create a new task
- POST /v1/api/tasks/{id}/complete → mark a task as completed
- GET /v1/api/tasks → list tasks with optional status and sort parameters
- GET /v1/api/tasks/status/{status} → list tasks filtered by status

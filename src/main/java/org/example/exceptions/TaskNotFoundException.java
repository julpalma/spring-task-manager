package org.example.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(int id) {
        super("Task not found for id " + id);
    }
}

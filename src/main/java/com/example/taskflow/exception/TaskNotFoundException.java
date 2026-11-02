package com.example.taskflow.exception;

/** Thrown when a requested task ID does not exist. */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Task with id " + id + " was not found");
    }
}

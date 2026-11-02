package com.example.taskflow.dto;

import com.example.taskflow.model.Priority;
import com.example.taskflow.model.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Stable JSON representation returned to the dashboard. */
public record TaskResponse(Long id, String title, String description, TaskStatus status,
                           Priority priority, LocalDate dueDate, LocalDateTime createdAt) {
}

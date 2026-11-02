package com.example.taskflow.dto;

import com.example.taskflow.model.Priority;
import com.example.taskflow.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** Input accepted when a client creates or updates a task. */
public record TaskRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,
        @NotNull(message = "Status is required") TaskStatus status,
        @NotNull(message = "Priority is required") Priority priority,
        LocalDate dueDate) {
}

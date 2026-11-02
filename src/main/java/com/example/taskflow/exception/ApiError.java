package com.example.taskflow.exception;

import java.time.LocalDateTime;
import java.util.Map;

/** Consistent error format that is easy for the UI or another API consumer to parse. */
public record ApiError(LocalDateTime timestamp, int status, String message, Map<String, String> fieldErrors) {
}

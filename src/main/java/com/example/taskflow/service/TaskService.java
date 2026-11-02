package com.example.taskflow.service;

import com.example.taskflow.dto.TaskRequest;
import com.example.taskflow.dto.TaskResponse;
import com.example.taskflow.exception.TaskNotFoundException;
import com.example.taskflow.model.Task;
import com.example.taskflow.model.TaskStatus;
import com.example.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** Contains use-case logic and maps between HTTP DTOs and the database entity. */
@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<TaskResponse> findAll(TaskStatus status) {
        List<Task> tasks = status == null
                ? repository.findAllByOrderByCreatedAtDesc()
                : repository.findByStatusOrderByCreatedAtDesc(status);
        return tasks.stream().map(this::toResponse).toList();
    }

    public TaskResponse findById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public TaskResponse create(TaskRequest request) {
        Task task = new Task(cleanTitle(request.title()), request.description(), request.status(),
                request.priority(), request.dueDate());
        return toResponse(repository.save(task));
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = findEntity(id);
        task.update(cleanTitle(request.title()), request.description(), request.status(),
                request.priority(), request.dueDate());
        return toResponse(task);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findEntity(id));
    }

    private Task findEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    private String cleanTitle(String title) {
        return title.trim();
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(),
                task.getPriority(), task.getDueDate(), task.getCreatedAt());
    }
}

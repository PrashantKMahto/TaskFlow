package com.example.taskflow;

import com.example.taskflow.model.Priority;
import com.example.taskflow.model.Task;
import com.example.taskflow.model.TaskStatus;
import com.example.taskflow.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Exercises the API with Spring's HTTP layer, validation, and a real test database. */
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private TaskRepository repository;

    @BeforeEach
    void cleanDatabase() { repository.deleteAll(); }

    @Test
    void createsTask() throws Exception {
        mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Prepare demo\",\"description\":\"Slides\",\"status\":\"TODO\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Prepare demo"));
    }

    @Test
    void filtersByStatus() throws Exception {
        repository.save(new Task("Ready task", null, TaskStatus.DONE, Priority.LOW, null));
        repository.save(new Task("Next task", null, TaskStatus.TODO, Priority.MEDIUM, null));
        mockMvc.perform(get("/api/tasks?status=DONE"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("DONE"));
    }

    @Test
    void updatesTask() throws Exception {
        Task task = repository.save(new Task("Old title", null, TaskStatus.TODO, Priority.LOW, null));
        mockMvc.perform(put("/api/tasks/{id}", task.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New title\",\"description\":null,\"status\":\"IN_PROGRESS\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.title").value("New title"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void returnsJson404ForMissingTask() throws Exception {
        mockMvc.perform(get("/api/tasks/99999")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task with id 99999 was not found"));
    }
}

package com.example.taskflow.config;

import com.example.taskflow.model.Priority;
import com.example.taskflow.model.Task;
import com.example.taskflow.model.TaskStatus;
import com.example.taskflow.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;

/** Adds example records only to an empty database so first-time users see a useful dashboard. */
@Configuration
public class SampleDataConfig {
    @Bean
    CommandLineRunner seedTasks(TaskRepository repository) {
        return arguments -> {
            if (repository.count() == 0) {
                repository.save(new Task("Plan product demo", "Prepare the agenda and sample data.",
                        TaskStatus.IN_PROGRESS, Priority.HIGH, LocalDate.now().plusDays(2)));
                repository.save(new Task("Review feedback", "Summarise feedback from the pilot team.",
                        TaskStatus.TODO, Priority.MEDIUM, LocalDate.now().plusDays(5)));
                repository.save(new Task("Create project workspace", "Repository and baseline documentation are ready.",
                        TaskStatus.DONE, Priority.LOW, LocalDate.now().minusDays(1)));
            }
        };
    }
}

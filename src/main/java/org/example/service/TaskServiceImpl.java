package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TaskRequest;
import org.example.model.Task;
import org.example.repository.TaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl {

    private final TaskRepository taskRepository;

    public Task createTask(TaskRequest taskRequest) {
        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(taskRequest.getStatus())
                .createdAt(taskRequest.getCreatedAt() != null ? taskRequest.getCreatedAt() : LocalDateTime.now())
                .build();

        return taskRepository.save(task);
    }

    public List<Task> getTaskByStatus(String status) {
        return taskRepository.findTaskByStatus(status);
    }

    public Task completeTask(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found for id " + id));

        task.setStatus("completed");
        return taskRepository.save(task);
    }

    public List<Task> getAndSortTasks(String status, String sort) {
        Sort sortBy = Sort.unsorted();

        if ("created_at".equalsIgnoreCase(sort)) {
            sortBy = Sort.by("createdAt").ascending();
        }

        if (status != null && !status.isBlank()) {
            return taskRepository.findByStatusIgnoreCase(status, sortBy);
        }

        // No status, just sort them all
        return taskRepository.findAll(sortBy);
    }

}

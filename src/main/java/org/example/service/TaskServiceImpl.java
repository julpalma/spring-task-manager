package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TaskRequest;
import org.example.entities.Task;
import org.example.enums.TaskStatus;
import org.example.exceptions.TaskNotFoundException;
import org.example.repository.TaskRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService  {

    private final TaskRepository taskRepository;

    public Task createTask(TaskRequest taskRequest) {
        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(taskRequest.getStatus() != null ? taskRequest.getStatus() : TaskStatus.PENDING)
                .createdAt(taskRequest.getCreatedAt() != null ? taskRequest.getCreatedAt() : LocalDateTime.now())
                .build();

        return taskRepository.save(task);
    }

    public List<Task> getTaskByStatus(TaskStatus status) {
        return taskRepository.findTaskByStatus(status);
    }

    public List<Task> getTasks(TaskStatus status, String sortBy, Integer limit) {
        Sort sort = Sort.by(sortBy != null ? sortBy : "createdAt").ascending();
        if (status != null && limit != null) {
            return taskRepository.findByStatus(status, PageRequest.of(0, limit, sort)).getContent();
        } else if (status != null) {
            return taskRepository.findByStatus(status, PageRequest.of(0, 50, sort)).getContent(); // default limit
        } else if (limit != null) {
            return taskRepository.findAll(PageRequest.of(0, limit, sort)).getContent();
        } else {
            return taskRepository.findAll(sort);
        }
    }

    public Task completeTask(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setStatus(TaskStatus.COMPLETED);
        return taskRepository.save(task);
    }

}

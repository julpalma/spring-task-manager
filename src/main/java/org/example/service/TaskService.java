package org.example.service;

import org.example.dto.TaskRequest;
import org.example.entities.Task;
import org.example.enums.TaskStatus;

import java.util.List;

public interface TaskService {

    Task createTask(TaskRequest taskRequest);

    List<Task> getTaskByStatus(TaskStatus status);

    List<Task> getTasks(TaskStatus status, String sortBy, Integer limit);

    Task completeTask(int id);
}

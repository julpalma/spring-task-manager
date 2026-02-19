package org.example.mapper;

import org.example.dto.TaskResponse;
import org.example.entities.Task;

public class TaskMapper {

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus() != null ? task.getStatus().name(): null,
                task.getCreatedAt());
    }

}

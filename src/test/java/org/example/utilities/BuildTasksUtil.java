package org.example.utilities;

import org.example.dto.TaskRequest;
import org.example.entities.Task;
import org.example.enums.TaskStatus;

import java.time.LocalDateTime;

public class BuildTasksUtil {

    public static TaskRequest buildTaskRequest() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 2, 16, 0, 0);
        TaskRequest request = new TaskRequest();
        request.setTitle("Test title");
        request.setDescription("Test description");
        request.setCreatedAt(dateTime);
        return request;
    }

    public static Task buildTask() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 2, 16, 0, 0);
        return Task.builder()
                .id(1)
                .title("Test title")
                .description("Test description")
                .status(TaskStatus.PENDING)
                .createdAt(dateTime)
                .build();
    }

    public static Task buildInvalidTask() {
        return Task.builder()
                .id(1)
                .title(" ")
                .description("Test description")
                .build();
    }
}

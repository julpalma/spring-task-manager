package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TaskResponse {
    private int id;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;

}

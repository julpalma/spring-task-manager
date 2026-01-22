package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//DTO (Data Transfer Object) for task creation.
//The API accepts JSON from the client, maps it to the DTO.

@Getter
@Setter
public class TaskRequest {

    private String title;
    private String description;
    private String status = "pending";
    private LocalDateTime createdAt;

}
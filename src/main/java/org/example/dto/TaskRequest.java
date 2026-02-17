package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// DTO (Data Transfer Object) is a simple Java object used
// to transfer data between different layers of an application.
// The API accepts JSON from the client, maps it to the DTO.

@Getter
@Setter
public class TaskRequest {

    private String title;
    private String description;
    private String status = "pending";
    private LocalDateTime createdAt;

}
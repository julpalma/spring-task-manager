package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TaskRequest;
import org.example.dto.TaskResponse;
import org.example.enums.TaskStatus;
import org.example.mapper.TaskMapper;
import org.example.service.TaskService;
import org.example.entities.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/v1/api")
@Validated
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/tasks")
    ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        if (taskRequest != null && !taskRequest.getTitle().isBlank()) {
            Task taskCreated = taskService.createTask(taskRequest);
            TaskResponse taskResponse = TaskMapper.toResponse(taskCreated);
            return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/tasks/status")
    ResponseEntity<List<TaskResponse>> getTaskByStatus(@RequestParam(required = true) TaskStatus status) {
        if (status == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Task> listTasks = taskService.getTaskByStatus(status);
        if (listTasks.isEmpty()) {
            System.out.println("No tasks found for status " + status);
            //Return empty list and log for debugging purposes
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        } else {
            List<TaskResponse> taskResponseList = listTasks.stream().map(TaskMapper::toResponse).toList();
            return new ResponseEntity<>(taskResponseList, HttpStatus.OK);
        }
    }

    @GetMapping("/tasks")
    ResponseEntity<List<TaskResponse>> listTasks(@RequestParam(required = false) TaskStatus status,
                                                 @RequestParam(required = false) String sortBy,
                                                 @RequestParam(required = false) Integer limit) {

        List<Task> listTasks = taskService.getTasks(status, sortBy, limit);
        List<TaskResponse> taskResponse = listTasks.stream().map(TaskMapper::toResponse).toList();
        return ResponseEntity.ok(taskResponse);
    }

    @PostMapping("/tasks/{id}/complete")
    ResponseEntity<TaskResponse> completeTask(@PathVariable int id) {
        try {
            Task taskCompleted = taskService.completeTask(id);
            TaskResponse taskResponse = TaskMapper.toResponse(taskCompleted);
            return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

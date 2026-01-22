package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TaskRequest;
import org.example.service.TaskServiceImpl;
import org.example.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/v1/api")
@RequiredArgsConstructor
public class Api {

    private final TaskServiceImpl taskService;

    @PostMapping("/tasks")
    ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        if (taskRequest != null && !taskRequest.getTitle().isBlank()) {
            Task taskCreated = taskService.createTask(taskRequest);
            return new ResponseEntity<>(taskCreated, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/tasks/status/{status}")
    ResponseEntity<List<Task>> getTaskByStatus(@PathVariable String status) {
        if (status == null || status.isBlank()) {
            System.out.println("Status not valid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Task> listTasks = taskService.getTaskByStatus(status);
        if (listTasks.isEmpty()) {
            System.out.println("No tasks found for status " + status);
            //Return empty list and log for debugging purposes
            return new ResponseEntity<>(listTasks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(listTasks, HttpStatus.OK);
        }
    }

    @PostMapping("/tasks/{id}/complete")
    ResponseEntity<Task> completeTask(@PathVariable int id) {
        try {
            Task taskCompleted = taskService.completeTask(id);
            return new ResponseEntity<>(taskCompleted, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> getAndSortTasks(@RequestParam(required = false) String status,
                                               @RequestParam(required = false) String sort) {
        List<Task> tasksSorted = taskService.getAndSortTasks(status, sort);
        return ResponseEntity.ok(tasksSorted);
    }

}

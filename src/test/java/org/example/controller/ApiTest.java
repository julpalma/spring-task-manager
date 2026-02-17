package org.example.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TaskRequest;
import org.example.entities.Task;
import org.example.service.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.utilities.BuildTasksUtil.buildInvalidTask;
import static org.example.utilities.BuildTasksUtil.buildTask;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

//Tests the controller layer.
//HTTP requests are simulated using MockMvc

@WebMvcTest(Api.class)
@AutoConfigureMockMvc
public class ApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskServiceImpl taskService;

    @Test
    @WithMockUser
    void createTask_validInput_shouldReturnCreated() throws Exception {

        Task task = buildTask();

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle("Test title");
        taskRequest.setDescription("Task description");

        when(taskService.createTask(any(TaskRequest.class)))
                .thenReturn(task);

        MvcResult result = mockMvc.perform(post("/v1/api/tasks")
                        .with(csrf())  //CSRF token is required.
                        .content(objectMapper.writeValueAsString(taskRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        Task taskResult = objectMapper.readValue(result.getResponse().getContentAsString(), Task.class);
        assertThat(taskResult.getTitle()).isEqualTo("Test title");
    }

    @Test
    @WithMockUser
    void createTask_invalidInput_shouldReturnBadRequest() throws Exception {

        Task invalidTask = buildInvalidTask();

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle(" ");
        taskRequest.setDescription("Task description");

        when(taskService.createTask(any(TaskRequest.class)))
                .thenReturn(invalidTask);

        mockMvc.perform(post("/v1/api/tasks")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(taskRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithMockUser
    void createTask_nullRequest_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/v1/api/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }

    @Test
    @WithMockUser
    void getTaskByStatus_validInput_shouldReturnTaskList() throws Exception {
        Task task = buildTask();

        when(taskService.getTaskByStatus("completed"))
                .thenReturn(List.of(task));

        MvcResult result = mockMvc.perform(get("/v1/api/tasks/status")
                        .param("status", "completed")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        List<Task> taskResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Task>>() {});
        assertThat(taskResult).hasSize(1);
        assertThat(taskResult.get(0).getTitle()).isEqualTo("Test title");
    }

    @Test
    @WithMockUser
    void getTaskByStatus_noTasks_shouldReturnEmptyList() throws Exception {
        when(taskService.getTaskByStatus(anyString()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v1/api/tasks/status")
                        .param("status","pending")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithMockUser
    void getTaskByStatus_invalidStatus_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/v1/api/tasks/status")
                        .param("status", " ")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).getTaskByStatus(any());
    }

    @Test
    @WithMockUser
    void getTaskByStatus_nullStatus_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/v1/api/tasks/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).getTaskByStatus(any());
    }

    @Test
    @WithMockUser
    void completeTask_validId_shouldReturnCreated() throws Exception {
        Task completedTask = buildTask();

        when(taskService.completeTask(completedTask.getId())).thenReturn(completedTask);

        MvcResult result = mockMvc.perform(post("/v1/api/tasks/{id}/complete", completedTask.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        Task taskResult = objectMapper.readValue(result.getResponse().getContentAsString(), Task.class);
        assertThat(taskResult.getStatus()).isEqualTo("completed");
    }

    @Test
    @WithMockUser
    void completeTask_invalidId_shouldReturn500() throws Exception {
        int invalidId = 99;

        when(taskService.completeTask(invalidId)).thenThrow(new RuntimeException("Task not found"));

        mockMvc.perform(post("/v1/api/tasks/{id}/complete", invalidId)
                        .with(csrf())  //CSRF token is required.
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()).andReturn();
    }

    @Test
    @WithMockUser
    void listTasks_noFilter_shouldReturnAllTasks() throws Exception {
        Task task1 = buildTask();
        Task task2 = buildTask();
        task2.setId(2);

        when(taskService.getAndSortTasks(null, null)).thenReturn(List.of(task1, task2));

        MvcResult result = mockMvc.perform(get("/v1/api/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Task[] tasks = objectMapper.readValue(result.getResponse().getContentAsString(), Task[].class);
        assertThat(tasks).hasSize(2);

    }

}

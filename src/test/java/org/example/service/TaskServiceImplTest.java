package org.example.service;

import org.example.dto.TaskRequest;
import org.example.entities.Task;
import org.example.enums.TaskStatus;
import org.example.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.example.utilities.BuildTasksUtil.buildTask;
import static org.example.utilities.BuildTasksUtil.buildTaskRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepositoryMock;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask_validInput_shouldSaveAndReturnTask() {
        TaskRequest request = buildTaskRequest();

        Task savedTask = buildTask();

        when(taskRepositoryMock.save(any(Task.class))).thenReturn(savedTask);

        Task result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals("Test title", result.getTitle());
        assertEquals(TaskStatus.PENDING, result.getStatus());
        assertEquals("2026-02-16T00:00", result.getCreatedAt().toString());
        verify(taskRepositoryMock, times(1)).save(any());
    }

    @Test
    void createTask_withNullCreatedAt_shouldSetCurrentTime() {
        TaskRequest taskRequest = buildTaskRequest();
        taskRequest.setCreatedAt(null);

        when(taskRepositoryMock.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        taskService.createTask(taskRequest);

        // Verify repository save was called
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepositoryMock).save(captor.capture());

        Task savedTask = captor.getValue();

        assertThat(savedTask.getTitle()).isEqualTo("Test title");
        assertThat(savedTask.getDescription()).isEqualTo("Test description");
        assertThat(savedTask.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(savedTask.getCreatedAt()).isNotNull();
        assertThat(savedTask.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }

    @Test
    void getTaskByStatus_shouldReturnList() {
        List<Task> tasks = List.of(buildTask());

        when(taskRepositoryMock.findTaskByStatus(TaskStatus.COMPLETED)).thenReturn(tasks);

        List<Task> result = taskService.getTaskByStatus(TaskStatus.COMPLETED);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepositoryMock, times(1)).findTaskByStatus(TaskStatus.COMPLETED);
    }

    @Test
    void completeTask_existingTask_shouldUpdateStatus() {
        Task task = buildTask();
        task.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepositoryMock.findById(1)).thenReturn(Optional.of(task));
        when(taskRepositoryMock.save(any(Task.class))).thenReturn(task);

        Task result = taskService.completeTask(1);

        assertNotNull(result);
        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        verify(taskRepositoryMock, times(1)).save(task);
    }

    @Test
    void completeTask_taskNotFound_shouldReturnException() {
        when(taskRepositoryMock.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class, () -> taskService.completeTask(1));

        assertTrue(ex.getMessage().contains("Task not found"));
        verify(taskRepositoryMock, never()).save(any());
    }

    @Test
    void listTasks_withStatusAndLimit_shouldCallFindByStatus() {
        Task task = buildTask();
        Page<Task> page = new PageImpl<>(List.of(task));

        when(taskRepositoryMock.findByStatus(eq(TaskStatus.PENDING), any(PageRequest.class))).thenReturn(page);

        List<Task> result = taskService.getTasks(TaskStatus.PENDING, null,10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepositoryMock, times(1)).findByStatus(eq(TaskStatus.PENDING), any());
        verify(taskRepositoryMock, never()).findAll(any(Sort.class));
    }

    @Test
    void listTasks_withStatusOnly_shouldCallFindByStatus() {
        Task task = buildTask();
        Page<Task> page = new PageImpl<>(List.of(task));

        when(taskRepositoryMock.findByStatus(eq(TaskStatus.PENDING), any(PageRequest.class))).thenReturn(page);

        List<Task> result = taskService.getTasks(TaskStatus.PENDING, null,null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepositoryMock, times(1)).findByStatus(eq(TaskStatus.PENDING), any());
        verify(taskRepositoryMock, never()).findAll(any(Sort.class));
    }

    @Test
    void listTasks_onlyLimit_shouldCallFindAll() {
        Task task = buildTask();
        Page<Task> page = new PageImpl<>(List.of(task));
        when(taskRepositoryMock.findAll(any(PageRequest.class))).thenReturn(page);

        List<Task> result = taskService.getTasks(null,null, 10);

        assertThat(result).hasSize(1);
        verify(taskRepositoryMock, times(1)).findAll(any(PageRequest.class));
        verify(taskRepositoryMock, never()).findByStatus(any(), any());
    }
}

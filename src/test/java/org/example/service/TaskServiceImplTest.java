package org.example.service;

import org.example.dto.TaskRequest;
import org.example.entities.Task;
import org.example.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
        assertEquals("completed", result.getStatus());
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
        assertThat(savedTask.getStatus()).isEqualTo("pending");
        assertThat(savedTask.getCreatedAt()).isNotNull();
        assertThat(savedTask.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }

    @Test
    void getTaskByStatus_shouldReturnList() {
        List<Task> tasks = List.of(buildTask());

        when(taskRepositoryMock.findTaskByStatus("completed")).thenReturn(tasks);

        List<Task> result = taskService.getTaskByStatus("completed");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepositoryMock, times(1)).findTaskByStatus("completed");
    }

    @Test
    void completeTask_existingTask_shouldUpdateStatus() {
        Task task = buildTask();
        task.setStatus("pending");

        when(taskRepositoryMock.findById(1)).thenReturn(Optional.of(task));
        when(taskRepositoryMock.save(any(Task.class))).thenReturn(task);

        Task result = taskService.completeTask(1);

        assertNotNull(result);
        assertEquals("completed", result.getStatus());
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
    void getAndSortTask_withStatusAndCreatedAtSort_shouldCallFindByStatusIgnoreCase() {
        List<Task> tasks = List.of(buildTask());

        when(taskRepositoryMock.findByStatusIgnoreCase(eq("completed"), any())).thenReturn(tasks);

        List<Task> result = taskService.getAndSortTasks("completed", "created_at");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepositoryMock, times(1)).findByStatusIgnoreCase(eq("completed"), any());
        verify(taskRepositoryMock, never()).findAll(any(Sort.class));
    }

    @Test
    void getAndSortTasks_withStatusOnly_shouldUseUnsorted() {

        List<Task> tasks = List.of(Task.builder().title("Test").build());

        when(taskRepositoryMock.findAll(any(Sort.class))).thenReturn(tasks);

        List<Task> result = taskService.getAndSortTasks(null, "created_at");

        assertEquals(1, result.size());
        verify(taskRepositoryMock, times(1)).findAll(any(Sort.class));
        verify(taskRepositoryMock, never()).findByStatusIgnoreCase(any(), any());
    }

    @Test
    void getAndSortTasks_noStatus_shouldCallFindAll() {
        List<Task> tasks = List.of(buildTask());
        when(taskRepositoryMock.findAll(any(Sort.class))).thenReturn(tasks);

        List<Task> result = taskService.getAndSortTasks(null, null);

        assertThat(result).hasSize(1);
        verify(taskRepositoryMock, times(1)).findAll(any(Sort.class));
        verify(taskRepositoryMock, never()).findByStatusIgnoreCase(any(), any());

    }
}

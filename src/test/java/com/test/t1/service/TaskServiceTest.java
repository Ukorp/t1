package com.test.t1.service;

import com.test.t1.dto.TaskRequest;
import com.test.t1.dto.TaskResponse;
import com.test.t1.dto.mapper.TaskMapper;
import com.test.t1.exception.TaskNotFoundException;
import com.test.t1.kafka.KafkaClientProducer;
import com.test.t1.model.Status;
import com.test.t1.model.Task;
import com.test.t1.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private KafkaClientProducer kafkaClientProducer;

    private final TaskMapper taskMapper = TaskMapper.INSTANCE;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskMapper, taskRepository, kafkaClientProducer);
    }

    @Test
    @DisplayName("Тест сохранения задачи")
    void testSaveTask() {
        Task taskResponse = new Task(1L, 55L, "titler", "desct", Status.RECEIVED);

        when(taskRepository.save(any())).thenReturn(taskResponse);

        var response = taskService.save(new TaskRequest("titler", "desct", 55, Status.RECEIVED));

        assertNotNull(response);
        assertEquals(taskMapper.toTaskResponse(taskResponse), response);
    }

    @Test
    @DisplayName("Тест успешного поиска задачи")
    void testFindByIdSuccess() {
        Task task = new Task(1L, 55L, "titler", "desct", null);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        var response = taskService.findById(1L);

        assertNotNull(response);
        assertEquals(taskMapper.toTaskResponse(task), response);
    }

    @Test
    @DisplayName("Тест успешного поиска задачи")
    void testFindByIdFail() {
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.findById(0L));
    }

    @Test
    @DisplayName("Тест успешного поиска всех задач")
    void testFindAllList() {
        Task task1 = new Task(1L, 55L, "titler", "desct", null);
        Task task2 = new Task(2L, 55L, "titler", "desct", null);
        List<Task> list = List.of(task1, task2);

        when(taskRepository.findAll()).thenReturn(list);

        List<TaskResponse> response = taskService.findAll();

        assertNotNull(response);
        assertEquals(list.stream().map(taskMapper::toTaskResponse).toList(), response);
    }

    @Test
    @DisplayName("Тест поиска где нет значений")
    void testFindAllEmpty() {
        when(taskRepository.findAll()).thenReturn(List.of());

        List<TaskResponse> response = taskService.findAll();

        assertNotNull(response);
        assertEquals(List.of(), response);
    }

    @Test
    @DisplayName("Тест успешного обновления задачи")
    void testUpdateSuccess() {
        Task task1 = new Task(1L, 55L, "titler", "desct", Status.RECEIVED);
        TaskRequest task2 = new TaskRequest("titler", "desct", 55, Status.DONE);
        Task ans = new Task(1L, 55L, "titler", "desct", Status.DONE);

        when(taskRepository.findById(task1.getId())).thenReturn(Optional.of(task1));
        when(taskRepository.save(any())).thenReturn(ans);
        doNothing().when(kafkaClientProducer).send(any(), any());

        TaskResponse response = taskService.update(task1.getId(), task2);

        assertNotNull(response);
        assertEquals(response, taskMapper.toTaskResponse(ans));
    }

    @Test
    @DisplayName("Тест неуспешного обновления задачи")
    void testUpdateFail() {
        Task task1 = new Task(1L, 55L, "titler", "desct", Status.RECEIVED);
        TaskRequest task2 = new TaskRequest("titler", "desct", 55, Status.DONE);

        when(taskRepository.findById(task1.getId())).thenReturn(Optional.empty());

        long id = task1.getId();
        assertThrows(TaskNotFoundException.class, () -> taskService.update(id, task2));
    }

    @Test
    @DisplayName("Тест успешного удаления задачи")
    void testDeleteSuccess() {
        Task task1 = new Task(1L, 55L, "titler", "desct", Status.RECEIVED);

        doNothing().when(taskRepository).deleteById(any());
        assertDoesNotThrow(() -> taskService.delete(task1.getId()));
    }
}
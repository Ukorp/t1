package com.test.t1.dto.mapper;

import com.test.t1.dto.TaskRequest;
import com.test.t1.dto.TaskResponse;
import com.test.t1.model.Status;
import com.test.t1.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskMapperTest {

    TaskMapper taskMapper = TaskMapper.INSTANCE;

    @Test
    @DisplayName("Тест на преобразование TaskRequest без enum в Task")
    void testToTaskDefaultEnumSuccess() {
        TaskRequest taskRequest = new TaskRequest("title", "descr", 1L, null);
        Task task = new Task(null, 1L, "title", "descr", Status.RECEIVED);
        assertEquals(task, taskMapper.toTask(taskRequest));
    }

    @Test
    @DisplayName("Тест на преобразование TaskRequest с enum в Task")
    void testToTaskSuccess() {
        TaskRequest taskRequest = new TaskRequest("title", "descr", 1L, Status.ARCHIVED);
        Task task = new Task(null, 1L, "title", "descr", Status.ARCHIVED);
        assertEquals(task, taskMapper.toTask(taskRequest));
    }

    @Test
    @DisplayName("Тест на преобразование Task в TaskResponse")
    void testToTaskResponseSuccess() {
        Task task = new Task(1L, 1L, "title", "descr", Status.ARCHIVED);
        TaskResponse taskResponse = new TaskResponse(1L, "title", "descr", 1, Status.ARCHIVED);
        assertEquals(taskResponse, taskMapper.toTaskResponse(task));
    }

}
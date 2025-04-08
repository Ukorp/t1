package com.test.t1.service;

import com.test.t1.annotation.CustomLogging;
import com.test.t1.annotation.TimeMonitoring;
import com.test.t1.dto.TaskRequest;
import com.test.t1.dto.TaskResponse;
import com.test.t1.dto.mapper.TaskMapper;
import com.test.t1.exception.TaskNotFoundException;
import com.test.t1.kafka.KafkaClientProducer;
import com.test.t1.model.Task;
import com.test.t1.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;

    private final KafkaClientProducer kafkaClientProducer;

    @CustomLogging
    @TimeMonitoring
    public TaskResponse save(TaskRequest task) {
        Task entity = taskMapper.toTask(task);
        return taskMapper.toTaskResponse(taskRepository.save(entity));
    }

    @CustomLogging
    public TaskResponse findById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));
        return taskMapper.toTaskResponse(task);
    }

    @CustomLogging
    public List<TaskResponse> findAll() {
        List<Task> taskList = taskRepository.findAll();
        return taskList.stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @CustomLogging
    public TaskResponse update(Long id, TaskRequest task) {
        Task entity = taskMapper.toTask(task);
        entity.setId(id);
        Task updatedTask = taskRepository.save(entity);
        log.info("Новый статус задания: {}", task.status());
        if (task.status() != null) {

            kafkaClientProducer.send(id, task.status().name());
        }
        return taskMapper.toTaskResponse(updatedTask);
    }

    @CustomLogging
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}

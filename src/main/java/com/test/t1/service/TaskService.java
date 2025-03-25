package com.test.t1.service;

import com.test.t1.annotation.CustomLog;
import com.test.t1.annotation.TimeMonitoring;
import com.test.t1.exception.TaskNotFoundException;
import com.test.t1.model.Task;
import com.test.t1.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @CustomLog
    @TimeMonitoring
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @CustomLog
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));
    }

    @CustomLog
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @CustomLog
    public Task update(Long id, Task task) {
        task.setId(id);
        return taskRepository.save(task);
    }

    @CustomLog
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}

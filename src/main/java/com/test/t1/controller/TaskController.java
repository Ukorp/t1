package com.test.t1.controller;

import com.test.t1.dto.TaskRequest;
import com.test.t1.dto.TaskResponse;
import com.test.t1.dto.mapper.TaskMapper;
import com.test.t1.model.Task;
import com.test.t1.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @PostMapping("/")
    public TaskResponse saveTask(@RequestBody TaskRequest task) {
        Task response = taskService.save(taskMapper.toTaskRequest(task));
        return taskMapper.toTaskResponse(response);
    }

    @GetMapping("/")
    public List<TaskResponse> findAll() {
        return taskService.findAll()
                .stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public TaskResponse findById(@PathVariable Long id) {
        return taskMapper.toTaskResponse(taskService.findById(id));
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody TaskRequest task) {
        Task response = taskService.update(id, taskMapper.toTaskRequest(task));
        return taskMapper.toTaskResponse(response);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }



}

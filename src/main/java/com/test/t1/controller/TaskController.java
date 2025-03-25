package com.test.t1.controller;

import com.test.t1.dto.TaskRequest;
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
    public Task saveTask(@RequestBody TaskRequest task) {
        return taskService.save(taskMapper.toTaskRequest(task));
    }

    @GetMapping("/")
    public List<Task> findAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody TaskRequest task) {
        return taskService.update(id, taskMapper.toTaskRequest(task));
    }

    @DeleteMapping("/{id}")
    public void findAll(@PathVariable Long id) {
        taskService.delete(id);
    }



}

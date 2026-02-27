package com.cbielaszczuk.crm.controller;

import com.cbielaszczuk.crm.dto.ApiResponse;
import com.cbielaszczuk.crm.dto.TaskDTO;
import com.cbielaszczuk.crm.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createTask(@RequestBody TaskDTO dto) {
        service.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Task created successfully.", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getAllTasks() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAllTasks()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> getTaskById(@PathVariable Long id) {
        TaskDTO task = service.getTaskById(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Task not found."));
        }
        return ResponseEntity.ok(ApiResponse.ok(task));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getTasksByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getTasksByProjectId(projectId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateTask(@PathVariable Long id, @RequestBody TaskDTO dto) {
        dto.setId(id);
        service.updateTask(dto);
        return ResponseEntity.ok(ApiResponse.ok("Task updated successfully.", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.ok("Task deleted successfully.", null));
    }
}

package com.cbielaszczuk.crm.controller;

import com.cbielaszczuk.crm.dto.ApiResponse;
import com.cbielaszczuk.crm.dto.ProjectDTO;
import com.cbielaszczuk.crm.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createProject(@RequestBody ProjectDTO dto) {
        service.createProject(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Project created successfully.", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getAllProjects() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAllProjects()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDTO>> getProjectById(@PathVariable Long id) {
        ProjectDTO project = service.getProjectById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Project not found."));
        }
        return ResponseEntity.ok(ApiResponse.ok(project));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getProjectsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getProjectsByClientId(clientId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateProject(@PathVariable Long id, @RequestBody ProjectDTO dto) {
        dto.setId(id);
        service.updateProject(dto);
        return ResponseEntity.ok(ApiResponse.ok("Project updated successfully.", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        service.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.ok("Project deleted successfully.", null));
    }
}

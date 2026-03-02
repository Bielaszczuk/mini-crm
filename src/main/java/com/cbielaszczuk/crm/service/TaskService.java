package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.TaskDTO;
import com.cbielaszczuk.crm.mapper.TaskMapper;
import com.cbielaszczuk.crm.model.ProjectModel;
import com.cbielaszczuk.crm.model.TaskModel;
import com.cbielaszczuk.crm.repository.ProjectRepository;
import com.cbielaszczuk.crm.repository.TaskRepository;
import com.cbielaszczuk.crm.repository.UserRepository;
import com.cbielaszczuk.crm.validation.TaskValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles business logic for managing tasks.
 */
@Service
@Transactional
public class TaskService {

    private final TaskRepository repository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository repository, ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    /**
     * Gets the current authenticated user's ID.
     */
    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"))
                .getId();
    }

    /**
     * Creates a new task from a DTO after validation.
     *
     * @param dto task data
     */
    public void createTask(TaskDTO dto) {
        TaskValidator.validateForCreate(dto);
        // Verify project belongs to current user
        ProjectModel project = projectRepository.findByIdAndUserId(dto.getProjectId(), getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found or access denied"));

        TaskModel model = TaskMapper.toModel(dto);
        model.setProject(project);
        repository.save(model);
    }

    /**
     * Updates an existing task from a DTO after validation.
     *
     * @param dto updated task data
     */
    public void updateTask(TaskDTO dto) {
        TaskValidator.validateForUpdate(dto);
        TaskModel existing = repository.findByIdAndUserId(dto.getId(), getCurrentUserId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found or access denied"));

        if (dto.getProjectId() != null) {
            ProjectModel project = projectRepository.findByIdAndUserId(dto.getProjectId(), getCurrentUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Project not found or access denied"));
            existing.setProject(project);
        }

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setStartDate(dto.getStartDate());
        existing.setDueDate(dto.getDueDate());
        repository.save(existing);
    }

    /**
     * Retrieves all non-deleted tasks for current user.
     *
     * @return list of task DTOs
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        return repository.findAllActiveByUserId(getCurrentUserId()).stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a task by its ID (only if belongs to current user).
     *
     * @param id the task ID
     * @return task DTO if found, otherwise null
     */
    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        return repository.findByIdAndUserId(id, getCurrentUserId())
                .map(TaskMapper::toDTO)
                .orElse(null);
    }

    /**
     * Gets tasks by project ID (only if project belongs to current user).
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByProjectId(Long projectId) {
        // Verify project belongs to current user
        projectRepository.findByIdAndUserId(projectId, getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found or access denied"));

        return repository.findByProjectId(projectId).stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a task (only if belongs to current user).
     */
    public void deleteTask(Long id) {
        TaskValidator.validateForDelete(id);
        TaskModel task = repository.findByIdAndUserId(id, getCurrentUserId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found or access denied"));
        repository.delete(task);
    }
}

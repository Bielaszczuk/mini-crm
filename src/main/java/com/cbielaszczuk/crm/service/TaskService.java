package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.TaskDTO;
import com.cbielaszczuk.crm.mapper.TaskMapper;
import com.cbielaszczuk.crm.model.ProjectModel;
import com.cbielaszczuk.crm.model.TaskModel;
import com.cbielaszczuk.crm.repository.ProjectRepository;
import com.cbielaszczuk.crm.repository.TaskRepository;
import com.cbielaszczuk.crm.validation.TaskValidator;
import jakarta.persistence.EntityNotFoundException;
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

    public TaskService(TaskRepository repository, ProjectRepository projectRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
    }

    /**
     * Creates a new task from a DTO after validation.
     *
     * @param dto task data
     */
    public void createTask(TaskDTO dto) {
        TaskValidator.validateForCreate(dto);
        TaskModel model = TaskMapper.toModel(dto);
        ProjectModel project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + dto.getProjectId()));
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
        TaskModel model = TaskMapper.toModel(dto);
        if (dto.getProjectId() != null) {
            ProjectModel project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + dto.getProjectId()));
            model.setProject(project);
        }
        repository.save(model);
    }

    /**
     * Retrieves all non-deleted tasks.
     *
     * @return list of task DTOs
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        return repository.findAllActive().stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a task by its ID.
     *
     * @param id the task ID
     * @return task DTO if found, otherwise null
     */
    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        return repository.findById(id)
                .map(TaskMapper::toDTO)
                .orElse(null);
    }

    /**
     * Soft deletes a task by setting deletedAt.
     *
     * @param id ID of the task
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByProjectId(Long projectId) {
        return repository.findByProjectId(projectId).stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteTask(Long id) {
        TaskValidator.validateForDelete(id);
        repository.deleteById(id);
    }
}

package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.ClientDTO;
import com.cbielaszczuk.crm.mapper.ClientMapper;
import com.cbielaszczuk.crm.model.ClientModel;
import com.cbielaszczuk.crm.model.ProjectModel;
import com.cbielaszczuk.crm.model.TaskModel;
import com.cbielaszczuk.crm.repository.ClientRepository;
import com.cbielaszczuk.crm.repository.ProjectRepository;
import com.cbielaszczuk.crm.repository.TaskRepository;
import com.cbielaszczuk.crm.validation.ClientValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles business logic for managing clients.
 */
@Service
@Transactional
public class ClientService {

    private final ClientRepository repository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ClientService(ClientRepository repository, ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Converts a DTO to model and saves it.
     *
     * @param dto client data from UI
     */
    public void createClient(ClientDTO dto) {
        ClientValidator.validateForCreate(dto);
        ClientModel model = ClientMapper.toModel(dto);
        repository.save(model);
    }

    /**
     * Gets all non-deleted clients and maps to DTOs.
     *
     * @return list of client DTOs
     */
    @Transactional(readOnly = true)
    public List<ClientDTO> getAllClients() {
        return repository.findAllActive()
                .stream()
                .map(ClientMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a client by ID (only if not soft-deleted).
     *
     * @param id client ID
     * @return DTO if found and not deleted, null otherwise
     */
    @Transactional(readOnly = true)
    public ClientDTO getClientById(Long id) {
        return repository.findById(id)
                .filter(client -> client.getDeletedAt() == null)
                .map(ClientMapper::toDTO)
                .orElse(null);
    }

    /**
     * Updates an existing client.
     *
     * @param dto updated data
     */
    public void updateClient(ClientDTO dto) {
        ClientValidator.validateForUpdate(dto);
        ClientModel model = ClientMapper.toModel(dto);
        repository.save(model);
    }

    /**
     * Soft deletes a client by setting deletedAt timestamp.
     * Also soft deletes all associated projects and their tasks.
     *
     * @param id ID of the client to delete
     */
    public void deleteClient(Long id) {
        ClientValidator.validateForDelete(id);
        ClientModel client = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + id));

        LocalDateTime now = LocalDateTime.now();

        List<ProjectModel> projects = projectRepository.findAllByClientId(id);
        for (ProjectModel project : projects) {
            List<TaskModel> tasks = taskRepository.findAllByProjectId(project.getId());
            for (TaskModel task : tasks) {
                if (task.getDeletedAt() == null) {
                    task.setDeletedAt(now);
                    taskRepository.save(task);
                }
            }
            if (project.getDeletedAt() == null) {
                project.setDeletedAt(now);
                projectRepository.save(project);
            }
        }

        client.setDeletedAt(now);
        repository.save(client);
    }
}

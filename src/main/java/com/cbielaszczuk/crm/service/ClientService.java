package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.ClientDTO;
import com.cbielaszczuk.crm.mapper.ClientMapper;
import com.cbielaszczuk.crm.model.ClientModel;
import com.cbielaszczuk.crm.model.ProjectModel;
import com.cbielaszczuk.crm.model.TaskModel;
import com.cbielaszczuk.crm.model.UserModel;
import com.cbielaszczuk.crm.repository.ClientRepository;
import com.cbielaszczuk.crm.repository.ProjectRepository;
import com.cbielaszczuk.crm.repository.TaskRepository;
import com.cbielaszczuk.crm.repository.UserRepository;
import com.cbielaszczuk.crm.validation.ClientValidator;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    public ClientService(ClientRepository repository, ProjectRepository projectRepository,
                         TaskRepository taskRepository, UserRepository userRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
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
     * Gets the current authenticated user.
     */
    private UserModel getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * Converts a DTO to model and saves it, associating with current user.
     *
     * @param dto client data from UI
     */
    public void createClient(ClientDTO dto) {
        ClientValidator.validateForCreate(dto);
        ClientModel model = ClientMapper.toModel(dto);
        model.setUser(getCurrentUser());
        repository.save(model);
    }

    /**
     * Gets all non-deleted clients for the current user.
     *
     * @return list of client DTOs
     */
    @Transactional(readOnly = true)
    public List<ClientDTO> getAllClients() {
        return repository.findAllActiveByUserId(getCurrentUserId())
                .stream()
                .map(ClientMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a client by ID (only if not soft-deleted and belongs to current user).
     *
     * @param id client ID
     * @return DTO if found and not deleted, null otherwise
     */
    @Transactional(readOnly = true)
    public ClientDTO getClientById(Long id) {
        return repository.findByIdAndUserId(id, getCurrentUserId())
                .map(ClientMapper::toDTO)
                .orElse(null);
    }

    /**
     * Updates an existing client (only if belongs to current user).
     *
     * @param dto updated data
     */
    public void updateClient(ClientDTO dto) {
        ClientValidator.validateForUpdate(dto);
        ClientModel existing = repository.findByIdAndUserId(dto.getId(), getCurrentUserId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found or access denied"));

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setCompany(dto.getCompany());
        existing.setNotes(dto.getNotes());
        repository.save(existing);
    }

    /**
     * Soft deletes a client by setting deletedAt timestamp.
     * Also soft deletes all associated projects and their tasks.
     * Only works if client belongs to current user.
     *
     * @param id ID of the client to delete
     */
    public void deleteClient(Long id) {
        ClientValidator.validateForDelete(id);
        ClientModel client = repository.findByIdAndUserId(id, getCurrentUserId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found or access denied"));

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

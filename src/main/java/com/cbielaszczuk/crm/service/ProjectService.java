package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.ProjectDTO;
import com.cbielaszczuk.crm.mapper.ProjectMapper;
import com.cbielaszczuk.crm.model.ClientModel;
import com.cbielaszczuk.crm.model.ProjectModel;
import com.cbielaszczuk.crm.repository.ClientRepository;
import com.cbielaszczuk.crm.repository.ProjectRepository;
import com.cbielaszczuk.crm.repository.UserRepository;
import com.cbielaszczuk.crm.validation.ProjectValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository repository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository repository, ClientRepository clientRepository,
                          UserRepository userRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
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

    public void createProject(ProjectDTO dto) {
        ProjectValidator.validateForCreate(dto);
        // Verify client belongs to current user
        ClientModel client = clientRepository.findByIdAndUserId(dto.getClientId(), getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found or access denied"));

        ProjectModel model = ProjectMapper.toModel(dto);
        model.setClient(client);
        repository.save(model);
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getAllProjects() {
        return repository.findAllActiveByUserId(getCurrentUserId()).stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long id) {
        return repository.findByIdAndUserId(id, getCurrentUserId())
                .map(ProjectMapper::toDTO)
                .orElse(null);
    }

    public void updateProject(ProjectDTO dto) {
        ProjectValidator.validateForUpdate(dto);
        ProjectModel existing = repository.findByIdAndUserId(dto.getId(), getCurrentUserId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found or access denied"));

        if (dto.getClientId() != null) {
            ClientModel client = clientRepository.findByIdAndUserId(dto.getClientId(), getCurrentUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Client not found or access denied"));
            existing.setClient(client);
        }

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setStartDate(dto.getStartDate());
        existing.setDueDate(dto.getDueDate());
        repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByClientId(Long clientId) {
        // Verify client belongs to current user
        clientRepository.findByIdAndUserId(clientId, getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found or access denied"));

        return repository.findByClientId(clientId).stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteProject(Long id) {
        ProjectValidator.validateForDelete(id);
        ProjectModel project = repository.findByIdAndUserId(id, getCurrentUserId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found or access denied"));
        repository.delete(project);
    }
}

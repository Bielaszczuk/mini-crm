package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.ProjectDTO;
import com.cbielaszczuk.crm.mapper.ProjectMapper;
import com.cbielaszczuk.crm.model.ClientModel;
import com.cbielaszczuk.crm.model.ProjectModel;
import com.cbielaszczuk.crm.repository.ClientRepository;
import com.cbielaszczuk.crm.repository.ProjectRepository;
import com.cbielaszczuk.crm.validation.ProjectValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository repository;
    private final ClientRepository clientRepository;

    public ProjectService(ProjectRepository repository, ClientRepository clientRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
    }

    public void createProject(ProjectDTO dto) {
        ProjectValidator.validateForCreate(dto);
        ProjectModel model = ProjectMapper.toModel(dto);
        ClientModel client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + dto.getClientId()));
        model.setClient(client);
        repository.save(model);
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getAllProjects() {
        return repository.findAllActive().stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long id) {
        return repository.findById(id)
                .map(ProjectMapper::toDTO)
                .orElse(null);
    }

    public void updateProject(ProjectDTO dto) {
        ProjectValidator.validateForUpdate(dto);
        ProjectModel model = ProjectMapper.toModel(dto);
        if (dto.getClientId() != null) {
            ClientModel client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + dto.getClientId()));
            model.setClient(client);
        }
        repository.save(model);
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByClientId(Long clientId) {
        return repository.findByClientId(clientId).stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteProject(Long id) {
        ProjectValidator.validateForDelete(id);
        repository.deleteById(id);
    }
}

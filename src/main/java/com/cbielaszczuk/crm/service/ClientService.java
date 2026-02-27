package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.ClientDTO;
import com.cbielaszczuk.crm.mapper.ClientMapper;
import com.cbielaszczuk.crm.model.ClientModel;
import com.cbielaszczuk.crm.repository.ClientRepository;
import com.cbielaszczuk.crm.validation.ClientValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles business logic for managing clients.
 */
@Service
@Transactional
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
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
     * Finds a client by ID.
     *
     * @param id client ID
     * @return DTO if found, null otherwise
     */
    @Transactional(readOnly = true)
    public ClientDTO getClientById(Long id) {
        return repository.findById(id)
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
     * Soft deletes a client.
     *
     * @param id ID of the client to delete
     */
    public void deleteClient(Long id) {
        ClientValidator.validateForDelete(id);
        repository.deleteById(id);
    }
}

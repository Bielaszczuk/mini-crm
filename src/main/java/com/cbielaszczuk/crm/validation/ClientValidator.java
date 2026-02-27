package com.cbielaszczuk.crm.validation;

import com.cbielaszczuk.crm.dto.ClientDTO;

/**
 * Validates client data for creation and update operations.
 */
public class ClientValidator {

    /**
     * Validates required fields for creating a client.
     *
     * @param dto the client data
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateForCreate(ClientDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Client data must not be null.");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Client name is required.");
        }

        if (dto.getEmail() == null || !dto.getEmail().matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("A valid email is required.");
        }

        if (dto.getPhone() == null || dto.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone number is required.");
        }

        if (dto.getNotes() != null && dto.getNotes().length() > 1000) {
            throw new IllegalArgumentException("Notes must not exceed 1000 characters.");
        }
    }

    /**
     * Validates required fields for updating a client.
     *
     * @param dto the client data
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateForUpdate(ClientDTO dto) {
        validateForCreate(dto);
    }

    public static void validateForDelete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Valid client ID is required for deletion.");
        }
    }
}

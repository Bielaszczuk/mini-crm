package com.cbielaszczuk.crm.validation;

import com.cbielaszczuk.crm.dto.ClientDTO;

/**
 * Validates client data for creation and update operations.
 */
public class ClientValidator {

    private static final String EMAIL_REGEX = "^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
    private static final String PHONE_REGEX = "^[+]?[0-9\\s\\-().]{6,20}$";

    /**
     * Validates required fields for creating a client.
     *
     * @param dto the client data
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateForCreate(ClientDTO dto) {
        if (dto == null)
            throw new IllegalArgumentException("Client data must not be null.");
        if (isBlank(dto.getName()))
            throw new IllegalArgumentException("Client name is required.");
        if (dto.getName().length() > 100)
            throw new IllegalArgumentException("Name must not exceed 100 characters.");
        if (isBlank(dto.getEmail()) || !dto.getEmail().matches(EMAIL_REGEX))
            throw new IllegalArgumentException("A valid email is required.");
        if (isBlank(dto.getPhone()))
            throw new IllegalArgumentException("Phone number is required.");
        if (!dto.getPhone().matches(PHONE_REGEX))
            throw new IllegalArgumentException("Phone must contain only digits, spaces, +, -, ( ) and be 6-20 characters.");
        if (dto.getCompany() != null && dto.getCompany().length() > 100)
            throw new IllegalArgumentException("Company name must not exceed 100 characters.");
        if (dto.getNotes() != null && dto.getNotes().length() > 1000)
            throw new IllegalArgumentException("Notes must not exceed 1000 characters.");
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
        if (id == null || id <= 0)
            throw new IllegalArgumentException("Valid client ID is required for deletion.");
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}

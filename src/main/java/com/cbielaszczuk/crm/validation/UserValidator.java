package com.cbielaszczuk.crm.validation;

import com.cbielaszczuk.crm.dto.UserDTO;
import com.cbielaszczuk.crm.dto.UserLoginDTO;
import com.cbielaszczuk.crm.dto.UserRegistrationDTO;

/**
 * Provides validation logic for user-related operations.
 */
public class UserValidator {

    /**
     * Validates user data for creation or update.
     *
     * @param dto user data
     */
    public static void validateForCreateOrUpdate(UserDTO dto) {
        if (dto == null)
            throw new IllegalArgumentException("User data must not be null.");
        if (isBlank(dto.getName()))
            throw new IllegalArgumentException("User name is required.");
        if (isBlank(dto.getEmail()) || !dto.getEmail().contains("@"))
            throw new IllegalArgumentException("A valid email is required.");
        if (isBlank(dto.getPhone()))
            throw new IllegalArgumentException("Phone number is required.");
        if (isBlank(dto.getUsername()))
            throw new IllegalArgumentException("Username is required.");
        if (isBlank(dto.getPassword()))
            throw new IllegalArgumentException("Password is required.");
    }

    /**
     * Validates user data for deletion.
     *
     * @param id user ID
     */
    public static void validateForDelete(Long id) {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("Valid user ID is required for deletion.");
    }

    /**
     * Validates login credentials.
     *
     * @param loginDTO login data
     */
    public static void validateLogin(UserLoginDTO loginDTO) {
        if (loginDTO == null)
            throw new IllegalArgumentException("Login data must not be null.");
        if (isBlank(loginDTO.getUsername()))
            throw new IllegalArgumentException("Username is required.");
        if (isBlank(loginDTO.getPassword()))
            throw new IllegalArgumentException("Password is required.");
    }

    /**
     * Validates registration data.
     *
     * @param regDTO registration data
     */
    public static void validateRegistration(UserRegistrationDTO regDTO) {
        if (regDTO == null)
            throw new IllegalArgumentException("Registration data must not be null.");
        if (isBlank(regDTO.getName()))
            throw new IllegalArgumentException("Name is required.");
        if (isBlank(regDTO.getEmail()) || !regDTO.getEmail().contains("@"))
            throw new IllegalArgumentException("A valid email is required.");
        if (isBlank(regDTO.getPhone()))
            throw new IllegalArgumentException("Phone number is required.");
        if (isBlank(regDTO.getUsername()))
            throw new IllegalArgumentException("Username is required.");
        if (isBlank(regDTO.getPassword()))
            throw new IllegalArgumentException("Password is required.");
        if (isBlank(regDTO.getConfirmPassword()) || !regDTO.getPassword().equals(regDTO.getConfirmPassword()))
            throw new IllegalArgumentException("Passwords do not match.");
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}

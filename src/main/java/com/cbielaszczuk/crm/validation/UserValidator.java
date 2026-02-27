package com.cbielaszczuk.crm.validation;

import com.cbielaszczuk.crm.dto.UserDTO;
import com.cbielaszczuk.crm.dto.UserLoginDTO;
import com.cbielaszczuk.crm.dto.UserRegistrationDTO;

/**
 * Provides validation logic for user-related operations.
 */
public class UserValidator {

    private static final String EMAIL_REGEX = "^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,30}$";
    private static final String PHONE_REGEX = "^[+]?[0-9\\s\\-().]{6,20}$";

    /**
     * Validates user data for creation or update.
     *
     * @param dto user data
     */
    public static void validateForCreateOrUpdate(UserDTO dto) {
        if (dto == null)
            throw new IllegalArgumentException("User data must not be null.");
        if (isBlank(dto.getName()))
            throw new IllegalArgumentException("Name is required.");
        if (dto.getName().length() > 100)
            throw new IllegalArgumentException("Name must not exceed 100 characters.");
        if (isBlank(dto.getEmail()) || !dto.getEmail().matches(EMAIL_REGEX))
            throw new IllegalArgumentException("A valid email is required.");
        if (!isBlank(dto.getPhone()) && !dto.getPhone().matches(PHONE_REGEX))
            throw new IllegalArgumentException("Phone must contain only digits, spaces, +, -, ( ) and be 6-20 characters.");
        if (isBlank(dto.getUsername()) || !dto.getUsername().matches(USERNAME_REGEX))
            throw new IllegalArgumentException("Username must be 3-30 characters and contain only letters, numbers or underscores.");
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
        if (regDTO.getName().length() > 100)
            throw new IllegalArgumentException("Name must not exceed 100 characters.");
        if (isBlank(regDTO.getEmail()) || !regDTO.getEmail().matches(EMAIL_REGEX))
            throw new IllegalArgumentException("A valid email is required.");
        if (!isBlank(regDTO.getPhone()) && !regDTO.getPhone().matches(PHONE_REGEX))
            throw new IllegalArgumentException("Phone must contain only digits, spaces, +, -, ( ) and be 6-20 characters.");
        if (isBlank(regDTO.getUsername()) || !regDTO.getUsername().matches(USERNAME_REGEX))
            throw new IllegalArgumentException("Username must be 3-30 characters and contain only letters, numbers or underscores.");
        if (isBlank(regDTO.getPassword()) || regDTO.getPassword().length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        if (regDTO.getPassword().length() > 72)
            throw new IllegalArgumentException("Password must not exceed 72 characters.");
        if (!regDTO.getPassword().equals(regDTO.getConfirmPassword()))
            throw new IllegalArgumentException("Passwords do not match.");
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}

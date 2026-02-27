package com.cbielaszczuk.crm.validation;

import com.cbielaszczuk.crm.dto.UserDTO;
import com.cbielaszczuk.crm.dto.UserLoginDTO;
import com.cbielaszczuk.crm.dto.UserRegistrationDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    void validateForCreateOrUpdate_valid_shouldPass() {
        UserDTO dto = new UserDTO(1L, "Test", "test@email.com", "+54 11 1234-5678", "validuser", "pass123");
        assertDoesNotThrow(() -> UserValidator.validateForCreateOrUpdate(dto));
    }

    @Test
    void validateForCreateOrUpdate_blankEmail_shouldThrow() {
        UserDTO dto = new UserDTO(1L, "Test", "", "+54 11 1234-5678", "validuser", "pass123");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForCreateOrUpdate(dto));
        assertEquals("A valid email is required.", ex.getMessage());
    }

    @Test
    void validateForCreateOrUpdate_invalidPhone_shouldThrow() {
        UserDTO dto = new UserDTO(1L, "Test", "test@email.com", "abc", "validuser", "pass123");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForCreateOrUpdate(dto));
        assertTrue(ex.getMessage().contains("Phone must contain only digits"));
    }

    @Test
    void validateForCreateOrUpdate_invalidUsername_shouldThrow() {
        UserDTO dto = new UserDTO(1L, "Test", "test@email.com", "+54 11 1234-5678", "u s e r!", "pass123");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForCreateOrUpdate(dto));
        assertTrue(ex.getMessage().contains("Username must be"));
    }

    @Test
    void validateForDelete_invalidId_shouldThrow() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForDelete(0L));
        assertEquals("Valid user ID is required for deletion.", ex.getMessage());
    }

    @Test
    void validateLogin_valid_shouldPass() {
        UserLoginDTO dto = new UserLoginDTO("user", "pass");
        assertDoesNotThrow(() -> UserValidator.validateLogin(dto));
    }

    @Test
    void validateLogin_blankPassword_shouldThrow() {
        UserLoginDTO dto = new UserLoginDTO("user", " ");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> UserValidator.validateLogin(dto));
        assertEquals("Password is required.", ex.getMessage());
    }

    @Test
    void validateRegistration_valid_shouldPass() {
        UserRegistrationDTO dto = new UserRegistrationDTO("Name", "mail@mail.com", "+54 11 1234-5678", "validuser", "pass123", "pass123");
        assertDoesNotThrow(() -> UserValidator.validateRegistration(dto));
    }

    @Test
    void validateRegistration_shortPassword_shouldThrow() {
        UserRegistrationDTO dto = new UserRegistrationDTO("Name", "mail@mail.com", "+54 11 1234-5678", "validuser", "abc", "abc");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> UserValidator.validateRegistration(dto));
        assertEquals("Password must be at least 6 characters.", ex.getMessage());
    }

    @Test
    void validateRegistration_mismatchedPasswords_shouldThrow() {
        UserRegistrationDTO dto = new UserRegistrationDTO("Name", "mail@mail.com", "+54 11 1234-5678", "validuser", "pass123", "pass456");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> UserValidator.validateRegistration(dto));
        assertEquals("Passwords do not match.", ex.getMessage());
    }
}

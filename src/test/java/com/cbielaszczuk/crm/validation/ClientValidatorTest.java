package com.cbielaszczuk.crm.validation;

import com.cbielaszczuk.crm.dto.ClientDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ClientValidatorTest {

    @Test
    void testValidateForCreate_validData_shouldPass() {
        ClientDTO dto = new ClientDTO(1L, "Juan", "juan@test.com", "123456789", "Empresa", "Notas");
        assertDoesNotThrow(() -> ClientValidator.validateForCreate(dto));
    }

    @Test
    void testValidateForCreate_nullDto_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ClientValidator.validateForCreate(null));
        assertEquals("Client data must not be null.", exception.getMessage());
    }

    @Test
    void testValidateForCreate_blankName_shouldThrowException() {
        ClientDTO dto = new ClientDTO(1L, " ", "juan@test.com", "123456789", "Empresa", "Notas");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ClientValidator.validateForCreate(dto));
        assertEquals("Client name is required.", exception.getMessage());
    }

    @Test
    void testValidateForCreate_invalidEmail_shouldThrowException() {
        ClientDTO dto = new ClientDTO(1L, "Juan", "noemail", "123456789", "Empresa", "Notas");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ClientValidator.validateForCreate(dto));
        assertEquals("A valid email is required.", exception.getMessage());
    }

    @Test
    void testValidateForCreate_blankPhone_shouldThrowException() {
        ClientDTO dto = new ClientDTO(1L, "Juan", "juan@test.com", " ", "Empresa", "Notas");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ClientValidator.validateForCreate(dto));
        assertEquals("Phone number is required.", exception.getMessage());
    }

    @Test
    void testValidateForCreate_notesTooLong_shouldThrowException() {
        String longNotes = "a".repeat(1001);
        ClientDTO dto = new ClientDTO(1L, "Juan", "juan@test.com", "123456789", "Empresa", longNotes);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ClientValidator.validateForCreate(dto));
        assertEquals("Notes must not exceed 1000 characters.", exception.getMessage());
    }

    @Test
    void testValidateForDelete_invalidId_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ClientValidator.validateForDelete(0L));
        assertEquals("Valid client ID is required for deletion.", exception.getMessage());
    }

    @Test
    void testValidateForDelete_validId_shouldPass() {
        assertDoesNotThrow(() -> ClientValidator.validateForDelete(3L));
    }
}

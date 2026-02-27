package com.cbielaszczuk.crm.validation;

import com.cbielaszczuk.crm.dto.TaskDTO;
import com.cbielaszczuk.crm.model.TaskStatusEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TaskValidatorTest {

    @Test
    void validateForCreate_nullDTO_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> TaskValidator.validateForCreate(null));
    }

    @Test
    void validateForCreate_blankTitle_shouldThrow() {
        TaskDTO dto = new TaskDTO(null, " ", "desc", TaskStatusEnum.NOT_STARTED, 1L,
                LocalDate.now(), LocalDate.now().plusDays(1), null);
        assertThrows(IllegalArgumentException.class, () -> TaskValidator.validateForCreate(dto));
    }

    @Test
    void validateForCreate_nullStatus_shouldThrow() {
        TaskDTO dto = new TaskDTO(null, "Task", "desc", null, 1L,
                LocalDate.now(), LocalDate.now().plusDays(1), null);
        assertThrows(IllegalArgumentException.class, () -> TaskValidator.validateForCreate(dto));
    }

    @Test
    void validateForCreate_invalidProjectId_shouldThrow() {
        TaskDTO dto = new TaskDTO(null, "Task", "desc", TaskStatusEnum.IN_PROGRESS, -5L,
                LocalDate.now(), LocalDate.now().plusDays(1), null);
        assertThrows(IllegalArgumentException.class, () -> TaskValidator.validateForCreate(dto));
    }

    @Test
    void validateForCreate_valid_shouldPass() {
        TaskDTO dto = new TaskDTO(null, "New Task", "desc", TaskStatusEnum.ON_HOLD, 2L,
                LocalDate.now(), LocalDate.now().plusDays(2), null);
        assertDoesNotThrow(() -> TaskValidator.validateForCreate(dto));
    }

    @Test
    void validateForUpdate_missingId_shouldThrow() {
        TaskDTO dto = new TaskDTO(null, "Update", "desc", TaskStatusEnum.FINISHED, 1L,
                LocalDate.now(), LocalDate.now().plusDays(2), null);
        assertThrows(IllegalArgumentException.class, () -> TaskValidator.validateForUpdate(dto));
    }

    @Test
    void validateForUpdate_valid_shouldPass() {
        TaskDTO dto = new TaskDTO(99L, "Task X", "details", TaskStatusEnum.IN_PROGRESS, 3L,
                LocalDate.now(), LocalDate.now().plusDays(3), null);
        assertDoesNotThrow(() -> TaskValidator.validateForUpdate(dto));
    }

    @Test
    void validateForDelete_invalidId_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> TaskValidator.validateForDelete(0L));
    }
}

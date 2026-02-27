package com.cbielaszczuk.crm.validation;

import com.cbielaszczuk.crm.dto.ProjectDTO;

public class ProjectValidator {

    public static void validateForCreate(ProjectDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Project data must not be null.");
        }

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Project title is required.");
        }

        if (dto.getClientId() == null || dto.getClientId() <= 0) {
            throw new IllegalArgumentException("Valid client ID is required.");
        }
    }

    public static void validateForUpdate(ProjectDTO dto) {
        validateForCreate(dto);
    }

    public static void validateForDelete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Valid project ID is required for deletion.");
        }
    }
}

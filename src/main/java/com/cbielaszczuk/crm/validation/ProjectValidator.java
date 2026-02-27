package com.cbielaszczuk.crm.validation;

import com.cbielaszczuk.crm.dto.ProjectDTO;

public class ProjectValidator {

    public static void validateForCreate(ProjectDTO dto) {
        if (dto == null)
            throw new IllegalArgumentException("Project data must not be null.");
        if (isBlank(dto.getTitle()))
            throw new IllegalArgumentException("Project title is required.");
        if (dto.getTitle().length() > 150)
            throw new IllegalArgumentException("Title must not exceed 150 characters.");
        if (dto.getDescription() != null && dto.getDescription().length() > 2000)
            throw new IllegalArgumentException("Description must not exceed 2000 characters.");
        if (dto.getClientId() == null || dto.getClientId() <= 0)
            throw new IllegalArgumentException("Valid client ID is required.");
        if (dto.getStartDate() != null && dto.getDueDate() != null
                && dto.getDueDate().isBefore(dto.getStartDate()))
            throw new IllegalArgumentException("Due date cannot be before start date.");
    }

    public static void validateForUpdate(ProjectDTO dto) {
        if (dto == null || dto.getId() == null || dto.getId() <= 0)
            throw new IllegalArgumentException("Valid project ID is required for update.");
        validateForCreate(dto);
    }

    public static void validateForDelete(Long id) {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("Valid project ID is required for deletion.");
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}

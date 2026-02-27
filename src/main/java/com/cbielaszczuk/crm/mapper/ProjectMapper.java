package com.cbielaszczuk.crm.mapper;

import com.cbielaszczuk.crm.dto.ProjectDTO;
import com.cbielaszczuk.crm.model.ProjectModel;

public class ProjectMapper {

    /**
     * Converts a ProjectDTO to ProjectModel.
     *
     * @param dto the DTO object
     * @return the corresponding ProjectModel
     */
    public static ProjectModel toModel(ProjectDTO dto) {
        if (dto == null) return null;

        ProjectModel model = new ProjectModel();
        model.setId(dto.getId());
        model.setTitle(dto.getTitle());
        model.setDescription(dto.getDescription());
        model.setStatus(dto.getStatus());
        model.setStartDate(dto.getStartDate());
        model.setDueDate(dto.getDueDate());

        return model;
    }

    /**
     * Converts a ProjectModel to ProjectDTO.
     *
     * @param model the entity object
     * @return the corresponding ProjectDTO
     */
    public static ProjectDTO toDTO(ProjectModel model) {
        if (model == null) return null;

        return new ProjectDTO(
                model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getStartDate(),
                model.getDueDate(),
                model.getStatus(),
                model.getClientId()
        );
    }
}

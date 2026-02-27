package com.cbielaszczuk.crm.mapper;

import com.cbielaszczuk.crm.dto.TaskDTO;
import com.cbielaszczuk.crm.model.TaskModel;

public class TaskMapper {

    /**
     * Convierte un TaskDTO a TaskModel.
     *
     * @param dto el DTO
     * @return el modelo correspondiente
     */
    public static TaskModel toModel(TaskDTO dto) {
        if (dto == null) return null;

        TaskModel model = new TaskModel();
        model.setId(dto.getId());
        model.setTitle(dto.getTitle());
        model.setDescription(dto.getDescription());
        model.setStatus(dto.getStatus());
        model.setStartDate(dto.getStartDate());
        model.setDueDate(dto.getDueDate());
        model.setDeletedAt(dto.getDeletedAt());

        return model;
    }

    /**
     * Convierte un TaskModel a TaskDTO.
     *
     * @param model el modelo
     * @return el DTO correspondiente
     */
    public static TaskDTO toDTO(TaskModel model) {
        if (model == null) return null;

        return new TaskDTO(
                model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getStatus(),
                model.getProjectId(),
                model.getStartDate(),
                model.getDueDate(),
                model.getDeletedAt()
        );
    }
}

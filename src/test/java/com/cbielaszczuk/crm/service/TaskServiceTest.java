package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.ClientDTO;
import com.cbielaszczuk.crm.dto.ProjectDTO;
import com.cbielaszczuk.crm.dto.TaskDTO;
import com.cbielaszczuk.crm.model.ProjectStatusEnum;
import com.cbielaszczuk.crm.model.TaskStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ClientService clientService;

    private Long createTestProject() {
        ClientDTO clientDTO = new ClientDTO(null, "Client", "client" + System.nanoTime() + "@test.com", "000", "Corp", "");
        clientService.createClient(clientDTO);
        Long clientId = clientService.getAllClients().stream()
                .reduce((first, second) -> second).orElseThrow().getId();

        ProjectDTO projectDTO = new ProjectDTO(
                null, "Project", "Desc",
                LocalDate.now(), LocalDate.now().plusDays(30),
                ProjectStatusEnum.IN_PROGRESS, clientId
        );
        projectService.createProject(projectDTO);
        return projectService.getProjectsByClientId(clientId).get(0).getId();
    }

    private TaskDTO createTestTask(Long projectId) {
        TaskDTO dto = new TaskDTO("Tarea 1", "Descripción", TaskStatusEnum.NOT_STARTED,
                projectId, LocalDate.now(), LocalDate.now().plusDays(5));
        taskService.createTask(dto);
        return taskService.getTasksByProjectId(projectId).get(0);
    }

    @Test
    void createTask_shouldPersistData() {
        Long projectId = createTestProject();
        createTestTask(projectId);
        List<TaskDTO> tasks = taskService.getTasksByProjectId(projectId);
        assertEquals(1, tasks.size());
        assertEquals("Tarea 1", tasks.get(0).getTitle());
    }

    @Test
    void getTaskById_existing_shouldReturn() {
        Long projectId = createTestProject();
        TaskDTO created = createTestTask(projectId);
        TaskDTO found = taskService.getTaskById(created.getId());
        assertNotNull(found);
        assertEquals("Tarea 1", found.getTitle());
    }

    @Test
    void updateTask_shouldModifyStatus() {
        Long projectId = createTestProject();
        TaskDTO created = createTestTask(projectId);

        created.setStatus(TaskStatusEnum.FINISHED);
        taskService.updateTask(created);

        TaskDTO updated = taskService.getTaskById(created.getId());
        assertEquals(TaskStatusEnum.FINISHED, updated.getStatus());
    }

    @Test
    void deleteTask_shouldRemove() {
        Long projectId = createTestProject();
        TaskDTO created = createTestTask(projectId);
        taskService.deleteTask(created.getId());
        assertNull(taskService.getTaskById(created.getId()));
    }
}

package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.ClientDTO;
import com.cbielaszczuk.crm.dto.ProjectDTO;
import com.cbielaszczuk.crm.model.ProjectStatusEnum;
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
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ClientService clientService;

    private Long createTestClient() {
        ClientDTO dto = new ClientDTO(null, "Test Client", "testclient" + System.nanoTime() + "@test.com", "+54 11 1234-5678", "Corp", "");
        clientService.createClient(dto);
        return clientService.getAllClients().stream()
                .filter(c -> c.getEmail().contains("testclient"))
                .reduce((first, second) -> second)
                .orElseThrow().getId();
    }

    private ProjectDTO createTestProject(Long clientId) {
        ProjectDTO dto = new ProjectDTO(
                null, "Test Project", "Description",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                ProjectStatusEnum.IN_PROGRESS,
                clientId
        );
        projectService.createProject(dto);
        return projectService.getProjectsByClientId(clientId).get(0);
    }

    @Test
    void getAllProjects_shouldReturnAtLeastOne() {
        Long clientId = createTestClient();
        createTestProject(clientId);
        assertFalse(projectService.getAllProjects().isEmpty());
    }

    @Test
    void getProjectById_existing_shouldReturn() {
        Long clientId = createTestClient();
        ProjectDTO created = createTestProject(clientId);
        ProjectDTO found = projectService.getProjectById(created.getId());
        assertNotNull(found);
        assertEquals("Test Project", found.getTitle());
    }

    @Test
    void updateProject_shouldModifyTitle() {
        Long clientId = createTestClient();
        ProjectDTO created = createTestProject(clientId);

        created.setTitle("Updated Title");
        projectService.updateProject(created);

        ProjectDTO after = projectService.getProjectById(created.getId());
        assertEquals("Updated Title", after.getTitle());
    }

    @Test
    void deleteProject_shouldRemove() {
        Long clientId = createTestClient();
        ProjectDTO created = createTestProject(clientId);
        projectService.deleteProject(created.getId());
        assertNull(projectService.getProjectById(created.getId()));
    }
}

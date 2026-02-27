package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.ClientDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClientServiceTest {

    @Autowired
    private ClientService service;

    private ClientDTO createTestClient(String name, String email) {
        ClientDTO dto = new ClientDTO(null, name, email, "1234567890", "TestCorp", "Notes");
        service.createClient(dto);
        return service.getAllClients().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow();
    }

    @Test
    void getAllClients_shouldReturnAtLeastOne() {
        createTestClient("Juan Test", "juan@test.com");
        List<ClientDTO> all = service.getAllClients();
        assertFalse(all.isEmpty());
    }

    @Test
    void getClientById_existing_shouldReturn() {
        ClientDTO created = createTestClient("Juan Test", "juan2@test.com");
        ClientDTO fetched = service.getClientById(created.getId());
        assertNotNull(fetched);
        assertEquals(created.getId(), fetched.getId());
    }

    @Test
    void updateClient_shouldModifyData() {
        ClientDTO created = createTestClient("Original Name", "original@test.com");

        ClientDTO updated = new ClientDTO(created.getId(), "Carlos Bielaszczuk", "carlos@test.com", "0987654321", "TestCorp", "Updated notes");
        service.updateClient(updated);

        ClientDTO after = service.getClientById(created.getId());
        assertEquals("Carlos Bielaszczuk", after.getName());
    }

    @Test
    void deleteClient_shouldRemoveFromList() {
        ClientDTO created = createTestClient("To Delete", "delete@test.com");
        service.deleteClient(created.getId());
        assertNull(service.getClientById(created.getId()));
    }
}

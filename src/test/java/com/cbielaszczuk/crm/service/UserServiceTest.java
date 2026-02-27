package com.cbielaszczuk.crm.service;

import com.cbielaszczuk.crm.dto.UserDTO;
import com.cbielaszczuk.crm.dto.UserLoginDTO;
import com.cbielaszczuk.crm.dto.UserRegistrationDTO;
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
class UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    void register_shouldPersistUser() {
        UserRegistrationDTO dto = new UserRegistrationDTO(
                "Juan Tester", "juan@test.com", "999", "juantest", "secret", "secret"
        );

        assertDoesNotThrow(() -> service.register(dto));

        List<UserDTO> all = service.getAllUsers();
        assertTrue(all.stream().anyMatch(u -> u.getUsername().equals("juantest")));
    }

    @Test
    void login_shouldReturnUserOnValidCredentials() {
        UserRegistrationDTO reg = new UserRegistrationDTO(
                "Login User", "loginuser@test.com", "000", "loginuser", "pass123", "pass123"
        );
        service.register(reg);

        UserLoginDTO login = new UserLoginDTO("loginuser", "pass123");
        UserDTO user = service.login(login);

        assertNotNull(user);
        assertEquals("loginuser", user.getUsername());
    }

    @Test
    void login_shouldThrowOnInvalidPassword() {
        UserRegistrationDTO reg = new UserRegistrationDTO(
                "Login User2", "loginuser2@test.com", "000", "loginuser2", "pass123", "pass123"
        );
        service.register(reg);

        UserLoginDTO login = new UserLoginDTO("loginuser2", "wrongpassword");
        assertThrows(IllegalArgumentException.class, () -> service.login(login));
    }

    @Test
    void updateUser_shouldChangeData() {
        UserRegistrationDTO reg = new UserRegistrationDTO(
                "Original Name", "original@test.com", "111", "usertoUpdate", "pass", "pass"
        );
        service.register(reg);

        List<UserDTO> all = service.getAllUsers();
        UserDTO user = all.stream().filter(u -> u.getUsername().equals("usertoUpdate")).findFirst().orElseThrow();

        user.setName("Updated Name");
        user.setEmail("updated@test.com");
        service.updateUser(user);

        UserDTO updated = service.getUserById(user.getId());
        assertEquals("Updated Name", updated.getName());
        assertEquals("updated@test.com", updated.getEmail());
    }

    @Test
    void deleteUser_shouldRemoveUser() {
        UserRegistrationDTO reg = new UserRegistrationDTO(
                "To Delete", "delete@test.com", "222", "userToDelete", "pass", "pass"
        );
        service.register(reg);

        List<UserDTO> before = service.getAllUsers();
        UserDTO user = before.stream().filter(u -> u.getUsername().equals("userToDelete")).findFirst().orElseThrow();

        service.deleteUser(user.getId());

        assertNull(service.getUserById(user.getId()));
    }
}

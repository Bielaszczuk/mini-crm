package com.cbielaszczuk.crm.controller;

import com.cbielaszczuk.crm.dto.ApiResponse;
import com.cbielaszczuk.crm.dto.UserDTO;
import com.cbielaszczuk.crm.dto.UserLoginDTO;
import com.cbielaszczuk.crm.dto.UserRegistrationDTO;
import com.cbielaszczuk.crm.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody UserRegistrationDTO dto) {
        service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("User registered successfully.", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> login(@RequestBody UserLoginDTO dto) {
        UserDTO user = service.login(dto);
        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = service.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found."));
        }
        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        dto.setId(id);
        service.updateUser(dto);
        return ResponseEntity.ok(ApiResponse.ok("User updated successfully.", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok("User deleted successfully.", null));
    }
}

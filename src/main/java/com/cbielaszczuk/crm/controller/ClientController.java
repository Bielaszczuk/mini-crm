package com.cbielaszczuk.crm.controller;

import com.cbielaszczuk.crm.dto.ApiResponse;
import com.cbielaszczuk.crm.dto.ClientDTO;
import com.cbielaszczuk.crm.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller that exposes client-related operations to UI or test layer.
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createClient(@RequestBody ClientDTO dto) {
        service.createClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Client created successfully.", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientDTO>>> getAllClients() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAllClients()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientDTO>> getClientById(@PathVariable Long id) {
        ClientDTO client = service.getClientById(id);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Client not found."));
        }
        return ResponseEntity.ok(ApiResponse.ok(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateClient(@PathVariable Long id, @RequestBody ClientDTO dto) {
        dto.setId(id);
        service.updateClient(dto);
        return ResponseEntity.ok(ApiResponse.ok("Client updated successfully.", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable Long id) {
        service.deleteClient(id);
        return ResponseEntity.ok(ApiResponse.ok("Client deleted successfully.", null));
    }
}

package com.test.vaudoise.infrastructure.web.controller;

import com.test.vaudoise.application.clientusecases.CreateClientUseCase;
import com.test.vaudoise.application.clientusecases.DeleteClientUseCase;
import com.test.vaudoise.application.clientusecases.ReadClientUseCase;
import com.test.vaudoise.application.clientusecases.UpdateClientUseCase;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.infrastructure.web.dto.client.ClientResponse;
import com.test.vaudoise.infrastructure.web.dto.client.CreateCompanyRequest;
import com.test.vaudoise.infrastructure.web.dto.client.CreatePersonRequest;
import com.test.vaudoise.infrastructure.web.dto.client.UpdateClientRequest;
import com.test.vaudoise.infrastructure.web.mapper.client.ClientMapper;
import com.test.vaudoise.infrastructure.web.mapper.client.ClientRequestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Clients", description = "Endpoints for managing clients (persons and companies)")
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final ReadClientUseCase readClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final DeleteClientUseCase deleteClientUseCase;

    public ClientController(CreateClientUseCase createClientUseCase, ReadClientUseCase readClientUseCase, UpdateClientUseCase updateClientUseCase, DeleteClientUseCase deleteClientUseCase) {
        this.createClientUseCase = createClientUseCase;
        this.readClientUseCase = readClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.deleteClientUseCase = deleteClientUseCase;
    }

    @Operation(
            summary = "Create a new person client",
            description = "Registers a new person in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Client created successfully"),
                    @ApiResponse(responseCode = "400", description = "Validation failed")
            }
    )
    @PostMapping("/person")
    public ResponseEntity<ClientResponse> createPerson(@Valid @RequestBody CreatePersonRequest req) {
        var saved = createClientUseCase.execute(ClientRequestMapper.toDomain(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponse(saved));
    }

    @Operation(
            summary = "Create a new company client",
            description = "Registers a new company in the system",
            responses = {
                @ApiResponse(responseCode = "201", description = "Client created successfully"),
                @ApiResponse(responseCode = "400", description = "Validation failed")
            }
    )
    @PostMapping("/company")
    public ResponseEntity<ClientResponse> createCompany(@Valid @RequestBody CreateCompanyRequest req) {
        var saved = createClientUseCase.execute(ClientRequestMapper.toDomain(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponse(saved));
    }

    @Operation(
            summary = "Retrieve a client by ID",
            description = "Fetches client details using UUID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client by ID retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Validation failed")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable UUID id) {
        var found = readClientUseCase.execute(new ClientId(id));
        return ResponseEntity.ok(ClientMapper.toResponse(found));
    }

    @Operation(
            summary = "Update client information",
            description = "Updates an existing client's contact information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client Updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Validation failed")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable UUID id, @Valid @RequestBody UpdateClientRequest req) {
        var updated = updateClientUseCase.execute(new ClientId(id), req.name(), req.email(), req.phone());
        return ResponseEntity.ok(ClientMapper.toResponse(updated));
    }

    @Operation(
            summary = "Delete a client by ID",
            description = "Removes a client and ends all active contracts",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Client removed successfully"),
                    @ApiResponse(responseCode = "400", description = "Validation failed")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        deleteClientUseCase.execute(new ClientId(id));
        return ResponseEntity.noContent().build();
    }

}

package com.test.vaudoise.infrastructure.web.controller;

import com.test.vaudoise.application.clientusecases.CreateClientUseCase;
import com.test.vaudoise.application.clientusecases.ReadClientUseCase;
import com.test.vaudoise.application.clientusecases.UpdateClientUseCase;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.infrastructure.web.dto.client.ClientResponse;
import com.test.vaudoise.infrastructure.web.dto.client.CreateCompanyRequest;
import com.test.vaudoise.infrastructure.web.dto.client.CreatePersonRequest;
import com.test.vaudoise.infrastructure.web.dto.client.UpdateClientRequest;
import com.test.vaudoise.infrastructure.web.mapper.client.ClientMapper;
import com.test.vaudoise.infrastructure.web.mapper.client.ClientRequestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final ReadClientUseCase readClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;

    public ClientController(CreateClientUseCase createClientUseCase, ReadClientUseCase readClientUseCase, UpdateClientUseCase updateClientUseCase) {
        this.createClientUseCase = createClientUseCase;
        this.readClientUseCase = readClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
    }

    @PostMapping("/person")
    public ResponseEntity<ClientResponse> createPerson(@Valid @RequestBody CreatePersonRequest req) {
        var id = new ClientId(UUID.randomUUID());
        var saved = createClientUseCase.execute(ClientRequestMapper.toDomain(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponse(saved));
    }

    @PostMapping("/company")
    public ResponseEntity<ClientResponse> createCompany(@Valid @RequestBody CreateCompanyRequest req) {
        var id = new ClientId(UUID.randomUUID());
        var saved = createClientUseCase.execute(ClientRequestMapper.toDomain(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable UUID id) {
        var found = readClientUseCase.execute(new ClientId(id));
        return ResponseEntity.ok(ClientMapper.toResponse(found));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable UUID id, @Valid @RequestBody UpdateClientRequest req) {
        var updated = updateClientUseCase.execute(new ClientId(id), req.name(), req.email(), req.phone());
        return ResponseEntity.ok(ClientMapper.toResponse(updated));
    }

}

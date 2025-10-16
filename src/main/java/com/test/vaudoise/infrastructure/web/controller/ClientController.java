package com.test.vaudoise.infrastructure.web.controller;

import com.test.vaudoise.application.usecase.CreateClientUseCase;
import com.test.vaudoise.application.usecase.ReadClientUseCase;
import com.test.vaudoise.application.usecase.UpdateClientUseCase;
import com.test.vaudoise.domain.model.*;
import com.test.vaudoise.infrastructure.web.dto.ClientResponse;
import com.test.vaudoise.infrastructure.web.dto.CreateCompanyRequest;
import com.test.vaudoise.infrastructure.web.dto.CreatePersonRequest;
import com.test.vaudoise.infrastructure.web.dto.UpdateClientRequest;
import com.test.vaudoise.infrastructure.web.mapper.ClientMapper;
import com.test.vaudoise.infrastructure.web.mapper.ClientRequestMapper;
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

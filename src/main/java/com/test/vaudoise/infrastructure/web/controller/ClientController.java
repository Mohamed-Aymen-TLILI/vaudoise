package com.test.vaudoise.infrastructure.web.controller;

import com.test.vaudoise.application.usecase.CreateClientUseCase;
import com.test.vaudoise.application.usecase.ReadClientUseCase;
import com.test.vaudoise.domain.model.*;
import com.test.vaudoise.infrastructure.web.dto.ClientResponse;
import com.test.vaudoise.infrastructure.web.dto.CreateCompanyRequest;
import com.test.vaudoise.infrastructure.web.dto.CreatePersonRequest;
import com.test.vaudoise.infrastructure.web.mapper.ClientMapper;
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

    public ClientController(CreateClientUseCase createClientUseCase, ReadClientUseCase readClientUseCase) {
        this.createClientUseCase = createClientUseCase;
        this.readClientUseCase = readClientUseCase;
    }

    @PostMapping("/person")
    public ResponseEntity<ClientResponse> createPerson(@Valid @RequestBody CreatePersonRequest req) {
        var id = new ClientId(UUID.randomUUID());
        var saved = createClientUseCase.execute(
                new Person(id, new Name(req.name()), new Email(req.email()), new Phone(req.phone()), req.birthdate())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponse(saved));
    }

    @PostMapping("/company")
    public ResponseEntity<ClientResponse> createCompany(@Valid @RequestBody CreateCompanyRequest req) {
        var id = new ClientId(UUID.randomUUID());
        var saved = createClientUseCase.execute(
                new Company(id, new Name(req.name()), new Email(req.email()), new Phone(req.phone()),
                        new CompanyIdentifier(req.companyIdentifier()))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable UUID id) {
        var found = readClientUseCase.execute(new ClientId(id));
        return ResponseEntity.ok(ClientMapper.toResponse(found));
    }

}

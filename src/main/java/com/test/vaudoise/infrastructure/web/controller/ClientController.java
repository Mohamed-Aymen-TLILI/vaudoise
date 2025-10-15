package com.test.vaudoise.infrastructure.web.controller;

import com.test.vaudoise.application.usecase.CreateClientUseCase;
import com.test.vaudoise.domain.model.*;
import com.test.vaudoise.infrastructure.web.dto.ClientResponse;
import com.test.vaudoise.infrastructure.web.dto.CreateCompanyRequest;
import com.test.vaudoise.infrastructure.web.dto.CreatePersonRequest;
import com.test.vaudoise.infrastructure.web.mapper.ClientMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final CreateClientUseCase create;
    public ClientController(CreateClientUseCase create) {
        this.create=create;
    }

    @PostMapping("/person")
    public ResponseEntity<ClientResponse> createPerson(@Valid @RequestBody CreatePersonRequest req) {
        var id = new ClientId(UUID.randomUUID());
        var saved = create.execute(
                new Person(id, new Name(req.name()), new Email(req.email()), new Phone(req.phone()), req.birthdate())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponse(saved));
    }

    @PostMapping("/company")
    public ResponseEntity<ClientResponse> createCompany(@Valid @RequestBody CreateCompanyRequest req) {
        var id = new ClientId(UUID.randomUUID());
        var saved = create.execute(
                new Company(id, new Name(req.name()), new Email(req.email()), new Phone(req.phone()),
                        new CompanyIdentifier(req.companyIdentifier()))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponse(saved));
    }

}

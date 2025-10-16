package com.test.vaudoise.infrastructure.web.mapper;

import com.test.vaudoise.domain.model.*;
import com.test.vaudoise.infrastructure.web.dto.CreateCompanyRequest;
import com.test.vaudoise.infrastructure.web.dto.CreatePersonRequest;
import com.test.vaudoise.infrastructure.web.dto.UpdateClientRequest;

import java.util.UUID;

public class ClientRequestMapper {

    private ClientRequestMapper() {}

    public static Client toDomain(CreatePersonRequest req) {
        var id = new ClientId(UUID.randomUUID());
        return new Person(id, new Name(req.name()), new Email(req.email()), new Phone(req.phone()), req.birthdate());
    }

    public static Client toDomain(CreateCompanyRequest req) {
        var id = new ClientId(UUID.randomUUID());
        return new Company(id, new Name(req.name()), new Email(req.email()), new Phone(req.phone()), new CompanyIdentifier(req.companyIdentifier()));
    }
}

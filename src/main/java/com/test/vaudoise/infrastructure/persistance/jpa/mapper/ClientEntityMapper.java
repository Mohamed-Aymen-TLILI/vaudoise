package com.test.vaudoise.infrastructure.persistance.jpa.mapper;

import com.test.vaudoise.domain.model.client.*;
import com.test.vaudoise.infrastructure.persistance.jpa.entity.ClientEntity;

public class ClientEntityMapper {

    public static ClientEntity toEntity(Client client) {
        var e = new ClientEntity();
        e.setId(client.id().value());
        e.setType(client.type().name());
        if (client instanceof Person person) {
            e.setName(person.name().value());
            e.setEmail(person.email().value());
            e.setPhone(person.phone().value());
            e.setBirthDate(person.birthdate());
        } else if (client instanceof Company company) {
            e.setName(company.name().value());
            e.setEmail(company.email().value());
            e.setPhone(company.phone().value());
            e.setCompanyIdentifier(company.companyIdentifier().value());
        }

        return e;
    }

    public static Client toDomain(ClientEntity e) {
        if ("PERSON".equalsIgnoreCase(e.getType())) {
            return new Person(
                    new ClientId(e.getId()),
                    new Name(e.getName()),
                    new Email(e.getEmail()),
                    new Phone(e.getPhone()),
                    e.getBirthDate()
            );
        } else {
            return new Company(
                    new ClientId(e.getId()),
                    new Name(e.getName()),
                    new Email(e.getEmail()),
                    new Phone(e.getPhone()),
                    new CompanyIdentifier(e.getCompanyIdentifier())
            );
        }
    }
}

package com.test.vaudoise.infrastructure.web.mapper.client;

import com.test.vaudoise.domain.model.client.Client;
import com.test.vaudoise.domain.model.client.Company;
import com.test.vaudoise.domain.model.client.Person;
import com.test.vaudoise.infrastructure.web.dto.client.ClientType;
import com.test.vaudoise.infrastructure.web.dto.client.ClientResponse;

public class ClientMapper {

    public static ClientResponse toResponse(Client client) {
        if (client instanceof Person p) {
            return new ClientResponse(
                    p.id().value(),
                    ClientType.PERSON,
                    p.name().value(),
                    p.email().value(),
                    p.phone().value(),
                    p.birthdate(),
                    null
            );
        } else if (client instanceof Company c) {
            return new ClientResponse(
                    c.id().value(),
                    ClientType.COMPANY,
                    c.name().value(),
                    c.email().value(),
                    c.phone().value(),
                    null,
                    c.companyIdentifier().value()
            );
        }
        throw new IllegalArgumentException("Unknown client type");
    }
}

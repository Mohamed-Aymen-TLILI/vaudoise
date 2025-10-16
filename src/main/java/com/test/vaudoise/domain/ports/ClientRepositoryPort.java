package com.test.vaudoise.domain.ports;

import com.test.vaudoise.domain.model.client.Client;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.client.CompanyIdentifier;

import java.util.Optional;

public interface ClientRepositoryPort {
    Client save(Client c);
    boolean existsCompanyIdentifier(CompanyIdentifier companyId);
    Optional<Client> findById(ClientId id);
}

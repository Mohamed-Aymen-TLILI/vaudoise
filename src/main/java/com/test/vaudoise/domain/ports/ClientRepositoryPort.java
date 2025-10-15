package com.test.vaudoise.domain.ports;

import com.test.vaudoise.domain.model.Client;
import com.test.vaudoise.domain.model.ClientId;
import com.test.vaudoise.domain.model.CompanyIdentifier;

import java.util.Optional;

public interface ClientRepositoryPort {
    Client save(Client c);
    boolean existsCompanyIdentifier(CompanyIdentifier companyId);
    Optional<Client> findById(ClientId id);
}

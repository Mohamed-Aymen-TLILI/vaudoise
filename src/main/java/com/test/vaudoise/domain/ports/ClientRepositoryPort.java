package com.test.vaudoise.domain.ports;

import com.test.vaudoise.domain.model.Client;
import com.test.vaudoise.domain.model.CompanyIdentifier;

public interface ClientRepositoryPort {
    Client save(Client c);
    boolean existsCompanyIdentifier(CompanyIdentifier companyId);
}

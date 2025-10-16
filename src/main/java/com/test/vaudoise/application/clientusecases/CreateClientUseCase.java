package com.test.vaudoise.application.clientusecases;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.client.Client;
import com.test.vaudoise.domain.model.client.Company;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;

public class CreateClientUseCase {

    private final ClientRepositoryPort repo;

    public CreateClientUseCase(ClientRepositoryPort repo) {
        this.repo = repo;
    }

    public Client execute(Client client) {
        if (client instanceof Company co) {
            if (repo.existsCompanyIdentifier(co.companyIdentifier()))
                throw new ValidationException("companyIdentifier already exists");
        }
        return repo.save(client);
    }
}

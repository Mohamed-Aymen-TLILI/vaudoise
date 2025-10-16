package com.test.vaudoise.application.contractusecases;

import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;
import com.test.vaudoise.infrastructure.web.mapper.contract.ContractRequestMapper;

public class CreateContractUseCase {

    private final ContractRepositoryPort contractRepo;
    private final ClientRepositoryPort clientRepo;

    public CreateContractUseCase(ContractRepositoryPort contractRepo, ClientRepositoryPort clientRepo) {
        this.contractRepo = contractRepo;
        this.clientRepo = clientRepo;
    }

    public Contract execute(Contract contract) {
        var client = clientRepo.findById(contract.getClientId());
        if (client.isEmpty()) {
            throw new NotFoundException("Client not found for ID: " + contract.getClientId());
        }

        return contractRepo.save(contract);
    }
}

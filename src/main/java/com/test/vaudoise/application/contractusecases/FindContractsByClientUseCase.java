package com.test.vaudoise.application.contractusecases;

import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

public class FindContractsByClientUseCase {

    private final ClientRepositoryPort clientRepo;
    private final ContractRepositoryPort contractRepo;

    public FindContractsByClientUseCase(ClientRepositoryPort clientRepo, ContractRepositoryPort contractRepo) {
        this.clientRepo = clientRepo;
        this.contractRepo = contractRepo;
    }

    public List<Contract> execute(ClientId clientId, LocalDateTime updatedAfter) {
        clientRepo.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        if (updatedAfter == null) {
            return contractRepo.findActiveByClientId(clientId);
        }
        return contractRepo.findByClientIdAndUpdatedAfter(clientId, updatedAfter);
    }
}

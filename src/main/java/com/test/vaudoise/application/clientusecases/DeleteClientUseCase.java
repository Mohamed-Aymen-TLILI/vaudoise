package com.test.vaudoise.application.clientusecases;

import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;

import java.util.List;

public class DeleteClientUseCase {

    private final ClientRepositoryPort clientRepo;
    private final ContractRepositoryPort contractRepo;

    public DeleteClientUseCase(ClientRepositoryPort clientRepo, ContractRepositoryPort contractRepo) {
        this.clientRepo = clientRepo;
        this.contractRepo = contractRepo;
    }

    public void execute(ClientId clientId) {
        clientRepo.findById(clientId).orElseThrow(() -> new NotFoundException("Client not found"));
        List<Contract> activeContracts = contractRepo.findActiveByClientId(clientId);
        activeContracts.forEach(contract -> {
            contract.endNow();
            contractRepo.save(contract);
        });
        clientRepo.delete(clientId);
    }
}

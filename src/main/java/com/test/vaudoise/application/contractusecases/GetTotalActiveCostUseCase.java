package com.test.vaudoise.application.contractusecases;

import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;

import java.math.BigDecimal;
import java.util.List;

public class GetTotalActiveCostUseCase {

    private final ContractRepositoryPort repo;

    public GetTotalActiveCostUseCase(ContractRepositoryPort repo) {
        this.repo = repo;
    }

    public BigDecimal execute(ClientId clientId) {
        List<Contract> activeContracts = repo.findActiveByClientId(clientId);
        return activeContracts.stream()
                .map(Contract::getCostAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

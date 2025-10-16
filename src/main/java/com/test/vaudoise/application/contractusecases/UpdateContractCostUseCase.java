package com.test.vaudoise.application.contractusecases;

import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;

import java.math.BigDecimal;

public class UpdateContractCostUseCase {

    private final ContractRepositoryPort repo;

    public UpdateContractCostUseCase(ContractRepositoryPort repo) {
        this.repo = repo;
    }

    public Contract execute(ContractId id, BigDecimal newCost) {
        Contract contract = repo.findById(id).orElseThrow(() -> new NotFoundException("Contract not found"));
        contract.updateCost(newCost);
        return repo.save(contract);
    }
}
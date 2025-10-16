package com.test.vaudoise.infrastructure.persistance.memory;

import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryContractRepo  implements ContractRepositoryPort {

    private final Map<UUID, Contract> storage = new ConcurrentHashMap<>();

    @Override
    public Contract save(Contract contract) {
        storage.put(contract.id().value(), contract);
        return contract;
    }
}

package com.test.vaudoise.infrastructure.persistance.memory;

import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryContractRepo  implements ContractRepositoryPort {

    private final Map<UUID, Contract> storage = new ConcurrentHashMap<>();

    @Override
    public Contract save(Contract contract) {
        storage.put(contract.id().value(), contract);
        return contract;
    }


    @Override
    public List<Contract> findActiveByClientId(ClientId clientId) {
        LocalDate today = LocalDate.now();
        return storage.values().stream()
                .filter(c -> c.getClientId().equals(clientId))
                .filter(c -> c.endDate() == null || c.endDate().isAfter(today))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Contract> findById(ContractId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }
}

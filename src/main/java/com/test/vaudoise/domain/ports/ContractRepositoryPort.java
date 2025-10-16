package com.test.vaudoise.domain.ports;

import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ContractRepositoryPort {

    Contract save(Contract contract);
    List<Contract> findActiveByClientId(ClientId clientId);
    Optional<Contract> findById(ContractId id);
    List<Contract> findByClientIdAndUpdatedAfter(ClientId clientId, LocalDateTime updatedAfter);

}

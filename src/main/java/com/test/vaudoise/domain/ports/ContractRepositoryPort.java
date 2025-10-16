package com.test.vaudoise.domain.ports;

import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;

import java.util.List;

public interface ContractRepositoryPort {

    Contract save(Contract contract);
    List<Contract> findActiveByClientId(ClientId clientId);
}

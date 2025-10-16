package com.test.vaudoise.domain.ports;

import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContractRepositoryPort {

    Contract save(Contract contract);
}

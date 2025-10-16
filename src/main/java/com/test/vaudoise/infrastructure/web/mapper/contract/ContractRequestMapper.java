package com.test.vaudoise.infrastructure.web.mapper.contract;

import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.infrastructure.web.dto.contract.CreateContractRequest;

import java.time.LocalDate;
import java.util.UUID;

public class ContractRequestMapper {

    public static Contract toDomain(CreateContractRequest req) {
        return new Contract(
                new ContractId(UUID.randomUUID()),
                new ClientId(req.clientId()),
                req.startDate() != null ? req.startDate() : LocalDate.now(),
                req.endDate(),
                req.costAmount()
        );
    }
}

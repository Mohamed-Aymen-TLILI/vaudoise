package com.test.vaudoise.infrastructure.persistance.jpa.mapper;

import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.infrastructure.persistance.jpa.entity.ContractEntity;

public class ContractEntityMapper {

    public static ContractEntity toEntity(Contract contract) {
        var e = new ContractEntity();
        e.setId(contract.id().value());
        e.setClientId(contract.getClientId().value());
        e.setStartDate(contract.startDate());
        e.setEndDate(contract.endDate());
        e.setCostAmount(contract.getCostAmount());
        e.setLastUpdateDate(contract.getLastUpdateDate());
        return e;
    }

    public static Contract toDomain(ContractEntity e) {
        return new Contract(
                new ContractId(e.getId()),
                new ClientId(e.getClientId()),
                e.getStartDate(),
                e.getEndDate(),
                e.getCostAmount()
        );
    }
}

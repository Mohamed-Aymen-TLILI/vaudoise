package com.test.vaudoise.infrastructure.web.mapper.contract;

import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.infrastructure.web.dto.contract.ContractResponse;

public class ContractMapper {

    public static ContractResponse toResponse(Contract contract) {
        return new ContractResponse(
                contract.id().value(),
                contract.getClientId().value(),
                contract.startDate(),
                contract.endDate(),
                contract.getCostAmount()
        );
    }
}

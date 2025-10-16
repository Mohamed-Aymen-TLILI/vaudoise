package com.test.vaudoise.domain.model.contrat;

import java.util.UUID;

public record ContractId(UUID value) {
    public ContractId {
        if (value == null) throw new IllegalArgumentException("ContractId cannot be null");
    }
}

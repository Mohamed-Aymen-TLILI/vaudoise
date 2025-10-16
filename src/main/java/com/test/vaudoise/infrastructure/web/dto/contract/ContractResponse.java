package com.test.vaudoise.infrastructure.web.dto.contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContractResponse (UUID id,
                                UUID clientId,
                                LocalDate startDate,
                                LocalDate endDate,
                                BigDecimal costAmount) {
}

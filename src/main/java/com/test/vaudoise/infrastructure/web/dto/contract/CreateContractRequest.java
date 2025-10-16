package com.test.vaudoise.infrastructure.web.dto.contract;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateContractRequest(@NotNull UUID clientId,
                                    LocalDate startDate,
                                    LocalDate endDate,
                                    @NotNull BigDecimal costAmount) {
}

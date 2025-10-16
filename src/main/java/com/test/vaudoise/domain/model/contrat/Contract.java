package com.test.vaudoise.domain.model.contrat;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.client.ClientId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Contract {

    private final ContractId id;
    private final ClientId clientId;
    private final LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal costAmount;
    private LocalDateTime lastUpdateDate;

    public Contract(ContractId id, ClientId clientId, LocalDate startDate, LocalDate endDate, BigDecimal costAmount) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.clientId = Objects.requireNonNull(clientId, "clientId cannot be null");
        if (startDate != null && startDate.isBefore(LocalDate.now())) {
            throw new ValidationException("Contract start date cannot be in the past");
        }
        if (costAmount == null || costAmount.signum() < 0)
            throw new ValidationException("Cost amount must be positive");

        this.startDate = startDate != null ? startDate : LocalDate.now();
        this.endDate = endDate;
        this.costAmount = costAmount;
    }

    public boolean isActive() {
        return endDate == null || LocalDate.now().isBefore(endDate);
    }

    public void endNow() {
        this.endDate = LocalDate.now();
    }

    public ContractId id() { return id; }
    public LocalDate startDate() { return startDate; }
    public LocalDate endDate() { return endDate; }
    public ClientId getClientId() {return clientId;}
    public BigDecimal getCostAmount() {return costAmount;}
}
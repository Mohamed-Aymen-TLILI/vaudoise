package com.test.vaudoise.contract.model;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ContractTest {

    @Test
    void should_set_start_date_to_today_if_null() {
        var contract = new Contract(
                new ContractId(UUID.randomUUID()),
                new ClientId(UUID.randomUUID()),
                null,
                null,
                BigDecimal.TEN
        );
        assertEquals(LocalDate.now(), contract.startDate());
    }

    @Test
    void should_allow_null_end_date() {
        var contract = new Contract(
                new ContractId(UUID.randomUUID()),
                new ClientId(UUID.randomUUID()),
                LocalDate.now(),
                null,
                BigDecimal.TEN
        );
        assertNull(contract.endDate());
        assertTrue(contract.isActive());
    }

    @Test
    void should_throw_when_cost_is_negative() {
        assertThrows(ValidationException.class, () ->
                new Contract(
                        new ContractId(UUID.randomUUID()),
                        new ClientId(UUID.randomUUID()),
                        LocalDate.now(),
                        null,
                        BigDecimal.valueOf(-5)
                )
        );
    }

    @Test
    void should_end_now_and_set_end_date() {
        var contract = new Contract(
                new ContractId(UUID.randomUUID()),
                new ClientId(UUID.randomUUID()),
                LocalDate.now(),
                null,
                BigDecimal.TEN
        );

        contract.endNow();
        assertEquals(LocalDate.now(), contract.endDate());
        assertFalse(contract.isActive());
    }

    @Test
    void should_throw_when_start_date_in_the_past() {
        var yesterday = LocalDate.now().minusDays(1);
        assertThrows(ValidationException.class, () ->
                new Contract(
                        new ContractId(UUID.randomUUID()),
                        new ClientId(UUID.randomUUID()),
                        yesterday,
                        null,
                        BigDecimal.TEN
                )
        );

    }
}

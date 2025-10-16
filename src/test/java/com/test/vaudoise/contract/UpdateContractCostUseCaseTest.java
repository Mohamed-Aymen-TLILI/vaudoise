package com.test.vaudoise.contract;

import com.test.vaudoise.application.contractusecases.UpdateContractCostUseCase;
import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateContractCostUseCaseTest {

    private ContractRepositoryPort repo;
    private UpdateContractCostUseCase useCase;

    @BeforeEach
    void setup() {
        repo = mock(ContractRepositoryPort.class);
        useCase = new UpdateContractCostUseCase(repo);
    }

    @Test
    void should_update_cost_and_save_contract() {
        var id = new ContractId(UUID.randomUUID());
        var clientId = new ClientId(UUID.randomUUID());
        var contract = new Contract(id, clientId, LocalDate.now(), null, BigDecimal.valueOf(100));

        when(repo.findById(id)).thenReturn(Optional.of(contract));
        when(repo.save(contract)).thenReturn(contract);

        var result = useCase.execute(id, BigDecimal.valueOf(300));

        assertEquals(BigDecimal.valueOf(300), result.getCostAmount());
        verify(repo).save(contract);
    }

    @Test
    void should_throw_not_found_when_contract_does_not_exist() {
        var id = new ContractId(UUID.randomUUID());
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(id, BigDecimal.valueOf(200)));
        verify(repo, never()).save(any());
    }

    @Test
    void should_not_update_when_cost_is_same() {
        var id = new ContractId(UUID.randomUUID());
        var clientId = new ClientId(UUID.randomUUID());
        var contract = spy(new Contract(id, clientId, LocalDate.now(), null, BigDecimal.valueOf(100)));

        when(repo.findById(id)).thenReturn(Optional.of(contract));
        when(repo.save(contract)).thenReturn(contract);

        var result = useCase.execute(id, BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100), result.getCostAmount());
        verify(repo).save(contract);
    }


    @Test
    void should_update_last_update_date_when_cost_changes() {
        var id = new ContractId(UUID.randomUUID());
        var clientId = new ClientId(UUID.randomUUID());
        var contract = new Contract(id, clientId, LocalDate.now(), null, BigDecimal.valueOf(100));

        when(repo.findById(id)).thenReturn(Optional.of(contract));
        when(repo.save(contract)).thenReturn(contract);

        var beforeUpdate = contract.getLastUpdateDate();

        useCase.execute(id, BigDecimal.valueOf(200));

        var afterUpdate = contract.getLastUpdateDate();
        assertNotNull(afterUpdate, "lastUpdateDate should be set after cost change");
        assertTrue(afterUpdate.isAfter(beforeUpdate == null ? LocalDateTime.MIN : beforeUpdate),
                "lastUpdateDate must be more recent after update");

        verify(repo).save(contract);
    }
}

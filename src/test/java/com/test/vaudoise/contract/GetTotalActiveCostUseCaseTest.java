package com.test.vaudoise.contract;

import com.test.vaudoise.application.contractusecases.GetTotalActiveCostUseCase;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class GetTotalActiveCostUseCaseTest {

    private ContractRepositoryPort repo;
    private GetTotalActiveCostUseCase useCase;

    @BeforeEach
    void setup() {
        repo = mock(ContractRepositoryPort.class);
        useCase = new GetTotalActiveCostUseCase(repo);
    }

    @Test
    void should_sum_all_active_contracts() {
        var clientId = new ClientId(UUID.randomUUID());
        var c1 = new Contract(new ContractId(UUID.randomUUID()), clientId, LocalDate.now(), null, BigDecimal.valueOf(100));
        var c2 = new Contract(new ContractId(UUID.randomUUID()), clientId, LocalDate.now(), null, BigDecimal.valueOf(200));

        when(repo.findActiveByClientId(clientId)).thenReturn(List.of(c1, c2));

        var total = useCase.execute(clientId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(300));
        verify(repo).findActiveByClientId(clientId);
    }

    @Test
    void should_return_zero_when_no_active_contracts() {
        var clientId = new ClientId(UUID.randomUUID());
        when(repo.findActiveByClientId(clientId)).thenReturn(List.of());

        var total = useCase.execute(clientId);

        assertThat(total).isEqualTo(BigDecimal.ZERO);
    }
}
